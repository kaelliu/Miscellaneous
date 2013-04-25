package main 

import (
//	"service"
//	"reflect"
//	"unicode/utf8"
//	"unicode"
//	"log"
//	"fmt"
//	"lib/tcp"
)
//var serviceMap map[string]*service

func main() {
	
	
//	reg(new(service.Table),"table")
//	
//	service := serviceMap["table"]
//	
//	mtype := service.method["TestResponse"]
//	
//	function := mtype.method.Func
//	
//	argv   := reflect.ValueOf(100)
//	replyv :=  reflect.ValueOf(&tcp.Response{})
//	
//	returnValues := function.Call([]reflect.Value{serviceMap["table"].rcvr,  argv, replyv})[0].Int()
//	fmt.Println(returnValues);
//	
//	if resp, ok :=  replyv.Interface().(*tcp.Response); ok {
//		fmt.Println(resp)
//	}
//	fmt.Println(&tcp.Response{Code:1000, Method: "hello"})
}



//func reg(rcvr interface{}, name string){
//	if serviceMap == nil {
//		serviceMap = make(map[string]*service)
//	}
//	s := new(service)
//	s.typ = reflect.TypeOf(rcvr)
//	s.rcvr = reflect.ValueOf(rcvr)
//	if name != "" {
//		s.name = name
//	}else{
//		s.name = reflect.Indirect(s.rcvr).Type().Name()
//		if !isExported(s.name) {
//	        s := "rpc Register: type " + s.name + " is not exported"
//	        log.Print(s)
//	        return 
//	    }
//	}
//	
//    s.method = make(map[string]*methodType)
//    for m := 0; m < s.typ.NumMethod(); m++{
//    	method := s.typ.Method(m)
//    	mtype := method.Type
//		mname := method.Name
//		argType := mtype.In(1)
//		replyType := mtype.In(2)
//    	s.method[mname] = &methodType{method: method, ArgType: argType, ReplyType: replyType}
//    }
//    serviceMap[s.name] = s
//}
//
//func isExported(name string) bool {
//    rune, _ := utf8.DecodeRuneInString(name)
//    return unicode.IsUpper(rune)
//}
//
//type methodType struct {
//    method     reflect.Method
//    ArgType    reflect.Type
//    ReplyType  reflect.Type
//}
//
//type service struct {
//    name   string                 // name of service
//    rcvr   reflect.Value          // receiver of methods for the service
//    typ    reflect.Type           // type of the receiver
//    method map[string]*methodType // registered methods
//}