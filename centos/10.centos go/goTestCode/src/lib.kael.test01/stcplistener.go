package kael

import (
	"net"
	"errors"
)

type void_signal struct {}

type sTCPListener struct {
	net.TCPListener
	sc chan void_signal
	se string			// string error
}

func newsTCPListener(link *net.TCPListener) *sTCPListener{
	return &sTCPListener{TCPListener:*link,sc:make(chan void_signal,1),se:"interruption signal received"}
}

func (this* sTCPListener) AcceptTCP() (conn *net.TCPConn,err error){
	select{
		case <-this.sc:return nil,errors.New(this.se)
		default:
	}
	conn,err = this.TCPListener.AcceptTCP()
	return
}
