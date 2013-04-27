package server

import (
	"reflect"
	"log"
	"unicode"
	"unicode/utf8"
	"errors"
	"fmt"
)

type methodType struct{
	method			reflect.Method			// method
	ServeConnType   reflect.Type			// connection type
	ArgType     	reflect.Type			// arguments type
}

var typeOfError = reflect.TypeOf((*error)(nil)).Elem()

// lowercase make other package is not allow to access member
type service struct{
	name       string	       				// service name
	receiver   reflect.Value   				// receiver of methods for the service
	typ		   reflect.Type    				// type of the receiver(module as)
	methods    map[uint32]*methodType 		// this need function sequeced to be change to string?
}

type routeTable map[uint32]*service

func (s *service) call(function uint32,argv interface{},c *ClientSession){
	functionTodo := s.methods[function]
	returnValues := functionTodo.Call([]reflect.Value{s.receiver, reflect.ValueOf(c), argv})
	
	errInter := returnValues[0].Interface()
	errmsg := ""
	if errInter != nil {
//		errmsg = errInter.(error).Error()
	}
}

func (s *service) register(module uint32,rcvr interface{}) error{
	if ServerBs.SPipelineConfig.Router == nil{
		ServerBs.SPipelineConfig.Router = make(map[uint32]*service)
	}
	svc := &service{}
	svc.typ = reflect.TypeOf(rcvr)
	svc.receiver = reflect.ValueOf(rcvr)
	
	sname := reflect.Indirect(svc.receiver).Type().Name()
	
	if module == 0{
		log.Fatal("tcp: no service name for type",svc.typ.String())
	}
	if !isExported(sname){
		se := "tcp Register: type " + sname + " is not exported"
		log.Print(se)
		return errors.New(se)
	}
	if _, present := ServerBs.SPipelineConfig.Router[module]; present {
		return errors.New("tcp: service already defined: " + sname)
	}
	svc.name = sname
	svc.methods = make(map[uint32]*methodType)
	fmt.Println("Service Register service :", svc.name)
	
	// Install the methods
	for m := 0; m < svc.typ.NumMethod(); m++ {
		method := svc.typ.Method(m)
		mtype  := method.Type
		mname  := method.Name
		
		// Method must be exported.
		if method.PkgPath != "" {
			continue
		}
		
		// Method needs three ins: receiver, *args.
		if mtype.NumIn() < 2 {
			log.Println("method", mname, "has wrong number of ins:", mtype.NumIn())
			continue
		}
		
		// First arg need not be a pointer.
		// connection
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
		
		svc.methods[uint32(m)] = &methodType{method: method, ServeConnType: serveConnType, ArgType: argType}
		fmt.Println("    Method :", mname, " Type :", svc.methods[uint32(m)])
	}
	
	if len(svc.methods) == 0 {
		str := "router Register: type " + sname + " has no exported methods of suitable type"
		log.Print(str)
		return errors.New(str)
	}
	ServerBs.SPipelineConfig.Router[module] = svc
	return nil
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