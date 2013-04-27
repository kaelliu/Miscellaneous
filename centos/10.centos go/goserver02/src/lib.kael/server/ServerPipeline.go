package server

import (
	"net"
)
// u can consider interface as a base pointer
// so we do not need to do this Cipher *Crypt,
// actually this is useless
type ServerPipeline struct{
	Proto  			   Protocol
	Cipher 			   Crypt 
	HeartBeatInterval  int
	TcpNodelay		   bool
	KeepAlive          bool
	Router 			   routeTable
}

func (s *ServerPipeline) NewClientConnection(c net.Conn) (*ClientSession,error){
	session := &ClientSession{conn:c,
							  writeChan:make(chan []byte),
							  quitChan:make(chan bool)}
	// todo here
	return session, nil
}