package tcp

import (
	"reflect"
	"unicode"
	"unicode/utf8"
//	"log"
)

type methodType struct {
    method           reflect.Method
    ServeConnType    reflect.Type
    ArgType          reflect.Type
}

type service struct {
    name   string                 // name of service
    rcvr   reflect.Value          // receiver of methods for the service
    typ    reflect.Type           // type of the receiver
    method map[string]*methodType // registered methods
}

func (s *service) call(c *ServeConn, mtype *methodType, req *Request, argv reflect.Value, codec ServerCodec) {
	function := mtype.method.Func
	
	returnValues := function.Call([]reflect.Value{s.rcvr, reflect.ValueOf(c), argv})
	
	errInter := returnValues[0].Interface()
	errmsg := ""
	if errInter != nil {
		errmsg = errInter.(error).Error()
		c.sendResponse(invalidReturnResponse, errmsg)
	}
	
//	if resp, ok :=  replyv.Interface().(*Response); ok {
//		c.sendResponse(req, resp, codec, errmsg)
//	}else{
//		log.Println("resp", replyv, "must (*Response).reflect.ValueOf()")
//	}
}

func isExported(name string) bool {
	rune, _ := utf8.DecodeRuneInString(name)
	return unicode.IsUpper(rune)
}

func isExportedOrBuiltinType(t reflect.Type) bool {
	for t.Kind() == reflect.Ptr {
		t = t.Elem()
	}
	return isExported(t.Name()) || t.PkgPath() == ""
}
