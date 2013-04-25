package tcp

import (
	"errors"
	"fmt"
	"log"
	"net"
	"reflect"
)

var DefaultServer = NewServer()
var invalidRequestResponse = &Response{Code: -1}
var invalidReturnResponse = &Response{Code: -2}

func ListenAndServe(addr string) error {
	DefaultServer.Addr = addr
	return DefaultServer.ListenAndServe()
}

func NewServer() *Server {
	return &Server{serviceMap: make(map[string]*service)}
}

type Server struct {
	Addr       string ":8001"
	serviceMap map[string]*service
}

func Register(rcvr interface{}) error {
	return DefaultServer.register(rcvr, "", false)
}

var typeOfError = reflect.TypeOf((*error)(nil)).Elem()

func (srv *Server) ListenAndServe() error {
	serverAddr, err := net.ResolveTCPAddr("tcp", srv.Addr)
	if err != nil {
		return err
	}

	listener, err := net.ListenTCP("tcp", serverAddr)
	if err != nil {
		return err
	}

	println("Listening to: ", listener.Addr().String())

	return srv.Serve(listener)
}

func (srv *Server) Serve(listener *net.TCPListener) error {
	defer listener.Close()

	for {
		conn, err := listener.Accept()
		fmt.Println("accept :", conn.RemoteAddr())
		if err != nil {
			return err
		}

		serveConn, err := srv.newConn(conn)
		if err != nil {
			continue
		}
		go serveConn.ServeConn()
	}

	panic("not reached")
}

func (srv *Server) newConn(conn net.Conn) (*ServeConn, error) {
	serveConn := &ServeConn{
		remoteAddr: conn.RemoteAddr().String(),
		server:     srv,
		conn:       conn,
	}

	return serveConn, nil
}

//注册路由方法
func (srv *Server) register(rcvr interface{}, name string, useName bool) error {
	if srv.serviceMap == nil {
		srv.serviceMap = make(map[string]*service)
	}
	s := new(service)
	s.typ = reflect.TypeOf(rcvr)
	s.rcvr = reflect.ValueOf(rcvr)

	var sname string
	if useName {
		sname = name
	} else {
		sname = reflect.Indirect(s.rcvr).Type().Name()
	}

	if sname == "" {
		log.Fatal("tcp: no service name for type", s.typ.String())
	}

	if !isExported(sname) && !useName {
		s := "tcp Register: type " + sname + " is not exported"
		log.Print(s)
		return errors.New(s)
	}

	if _, present := srv.serviceMap[sname]; present {
		return errors.New("tcp: service already defined: " + sname)
	}
	s.name = sname
	s.method = make(map[string]*methodType)
	fmt.Println("Service Register service :", s.name)
	// Install the methods
	for m := 0; m < s.typ.NumMethod(); m++ {
		method := s.typ.Method(m)
		mtype := method.Type
		mname := method.Name

		// Method must be exported.
		if method.PkgPath != "" {
			continue
		}
		// Method needs three ins: receiver, *args, *reply.
		if mtype.NumIn() != 3 {
			log.Println("method", mname, "has wrong number of ins:", mtype.NumIn())
			continue
		}
		// First arg need not be a pointer.
		serveConnType := mtype.In(1)
		if serveConnType.Kind() != reflect.Ptr {
			log.Println("method", mname, "serveConnType not a pointer:", serveConnType)
			continue
		}

		// Second arg must be a pointer.
		argType := mtype.In(2)
		if !isExportedOrBuiltinType(argType) {
			log.Println(mname, "argument type not exported:", argType)
			continue
		}

		// Reply type must be exported.
		if !isExportedOrBuiltinType(serveConnType) {
			log.Println("method", mname, "reply type not exported:", serveConnType)
			continue
		}
		// Method needs one out.
		if mtype.NumOut() != 1 {
			log.Println("method", mname, "has wrong number of outs:", mtype.NumOut())
			continue
		}
		// The return type of the method must be error.
		if returnType := mtype.Out(0); returnType != typeOfError {
			log.Println("method", mname, "returns", returnType.String(), "not error")
			continue
		}
		s.method[mname] = &methodType{method: method, ServeConnType: serveConnType, ArgType: argType}
		fmt.Println("    Method :", mname, " Type :", s.method[mname])
	}

	if len(s.method) == 0 {
		s := "rpc Register: type " + sname + " has no exported methods of suitable type"
		log.Print(s)
		return errors.New(s)
	}
	srv.serviceMap[s.name] = s
	return nil
}
