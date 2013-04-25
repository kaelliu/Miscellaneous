package tcp

import (
	"bufio"
	"bytes"
	"encoding/json"
	"errors"
	"fmt"
	"io"
	"log"
	"net"
	"reflect"
	"runtime/debug"
	"strings"
)

type ServeConn struct {
	remoteAddr string
	server     *Server
	conn       net.Conn
	sendChan   chan *Response
	codec      ServerCodec
}

func (c *ServeConn) Close() error {
	if c.conn != nil {
		c.conn.Close()
	}
	return nil
}

func (c *ServeConn) ServeCodec(codec ServerCodec) {
	defer codec.Close()
	c.codec = codec

	//make chan for  wait response
	c.sendChan = make(chan *Response, 100)
	go c.waitForRendResponse()

	for {
		service, mtype, req, argv, err := c.readRequest()
		if err != nil {
			if err != io.EOF {
				log.Println("tcp serveCode:", err)
			} else {
				//				log.Println("tcp disconnect:", err)
			}

			// send a response if we actually managed to read a header.
			if req != nil {
				invalidRequestResponse.Method = req.Method
				c.sendResponse(invalidRequestResponse, err.Error())
			}
			break
		}
		go service.call(c, mtype, req, argv, codec)
	}
}

func (c *ServeConn) ServeConn() {
	c.ServeCodec(NewServerCodec(c))
}

func (c *ServeConn) readRequest() (service *service, mtype *methodType, req *Request, argv reflect.Value, err error) {
	service, mtype, req, err = c.readRequestHeader()
	if err != nil {
		c.codec.ReadRequestBody(nil)
		return
	}

	// Decode the argument value.
	argIsValue := false // if true, need to indirect before calling.
	if mtype.ArgType.Kind() == reflect.Ptr {
		argv = reflect.New(mtype.ArgType.Elem())
	} else {
		argv = reflect.New(mtype.ArgType)
		argIsValue = true
	}
	// argv guaranteed to be a pointer now.
	if err = c.codec.ReadRequestBody(argv.Interface()); err != nil {
		return
	}
	if argIsValue {
		argv = argv.Elem()
	}

	return
}

func (c *ServeConn) waitForRendResponse() {
	for {
		resp := <-c.sendChan
//		fmt.Print(resp)
		c.sendResponse(resp, "")
	}
}

func (c *ServeConn) SendResponse(resp *Response) {
//	fmt.Println(resp)
	c.sendChan <- resp
}

func (c *ServeConn) sendResponse(resp *Response, errmsg string) {
	log.Print(resp)

	if errmsg != "" {
		resp.Data = errmsg
	}

	err := c.codec.WriteResponse(resp)
	if err != nil {
		log.Println("tcp sendResponse: writing response:", err)
	}
}

func (c *ServeConn) readRequestHeader() (service *service, mtype *methodType, req *Request, err error) {
	req = &Request{}
	err = c.codec.ReadRequestHeader(req)

	if err != nil {
		req = nil
		if err == io.EOF || err == io.ErrUnexpectedEOF {
			return
		}
		err = errors.New("tcp readRequestHeader: server cannot decode request: " + err.Error())
		return
	}

	Method := strings.Split(req.Method, ".")
	if len(Method) != 2 {
		err = errors.New("tcp readRequestHeader: service/method request ill-formed: " + req.Method)
		return
	}

	service = c.server.serviceMap[Method[0]]
	if service == nil {
		err = errors.New("tcp readRequestHeader: can't find service " + req.Method)
		return
	}
	mtype = service.method[Method[1]]
	if mtype == nil {
		err = errors.New("tcp readRequestHeader: can't find method " + req.Method)
	}
	return
}

func (c *ServeConn) Serve() {
	defer func() {
		err := recover()
		if err == nil {
			return
		}
		var buf bytes.Buffer
		fmt.Fprintf(&buf, "tcp: panic serving %v: %v\n", c.remoteAddr, err)
		buf.Write(debug.Stack())
		log.Print(buf.String())

		if c.conn != nil {
			c.conn.Close()
		}
	}()

	fmt.Print(c.remoteAddr, "\n")

	encBuf := bufio.NewWriter(c.conn)
	dnc := json.NewDecoder(c.conn)
	enc := json.NewEncoder(encBuf)
	for {
		var req interface{}

		err := dnc.Decode(&req)
		if err != nil {
			fmt.Println(req)
			if err == io.EOF {
				fmt.Println("disconnect eof:", err.Error())
				break
			}

			fmt.Println("disconnect exception:", err.Error())
			break
		}
		fmt.Print("receive : ", req, "\n")
		//        replyMsg := "hehehehehe"
		//		response := &Response{}
		//		c.bw.WriteString(req)
		//		c.bw.Flush()
		enc.Encode(&req)
		encBuf.Flush()
		//		response := &Response{}
		//		
		//		userChan <- response
	}

	c.Close()
}
