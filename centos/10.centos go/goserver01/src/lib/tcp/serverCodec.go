package tcp

import (
	"encoding/json"
	"bufio"
//	"time"
//	"fmt"
)

type ServerCodec interface {
	ReadRequestHeader(*Request) error
	ReadRequestBody(interface{}) error
	WriteResponse(*Response) error

	Close() error
}


type jsonServerCodec struct {
	dec    *json.Decoder // for reading JSON values
	enc    *json.Encoder // for writing JSON values
	encBuf *bufio.Writer // for writing JSON values buf
	conn   *ServeConn

	// temporary work space
	req  serverRequest
}


func NewServerCodec(serveConn *ServeConn) ServerCodec {
	encBuf := bufio.NewWriter(serveConn.conn)
	return &jsonServerCodec{
		dec    :     json.NewDecoder(serveConn.conn),
		enc    :     json.NewEncoder(encBuf),
		encBuf :     encBuf,
		conn   :     serveConn,
	}
}

type serverRequest struct {
	Method string           `json:"method"`
	Params *json.RawMessage `json:"params"`
}

func (r *serverRequest) reset() {
	r.Method = ""
	if r.Params != nil {
		*r.Params = nil
	}
}


func (c *jsonServerCodec) ReadRequestHeader(r *Request) error {
	c.req.reset()
	if err := c.dec.Decode(&c.req); err != nil {
		return err
	}
	r.Method = c.req.Method
	return nil
}

func (c *jsonServerCodec) ReadRequestBody(params interface{}) error {
	if params == nil {
		return nil
	}
	
	if c.req.Params == nil{
		return nil
	}
	return json.Unmarshal(*c.req.Params, &params)
}

var null = json.RawMessage([]byte("null"))

func (c *jsonServerCodec) WriteResponse(resp *Response)  (err error) {
	if err = c.enc.Encode(resp); err != nil {
		return
	}
	
	return c.encBuf.Flush()
}

func (c *jsonServerCodec) Close() error {
	return c.conn.Close()
}

