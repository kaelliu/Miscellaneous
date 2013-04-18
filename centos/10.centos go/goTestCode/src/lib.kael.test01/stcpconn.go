package kael

import (
	"net"
	"errors"
)

type sTCPConn struct{
	net.TCPConn // contains net.TCPConn's all member
	sc chan void_signal
	se string
}

func newsTCPConn(conn *net.TCPConn) *sTCPConn{
	return &sTCPConn{TCPConn:*conn,sc:make(chan void_signal,1),se:"interruption signal received"}
}

func (this *sTCPConn) Read(in []byte) (n int,e error){
	select{
		case <-this.sc:return -1,errors.New(this.se)
		default:
	}
	
	n,e = this.TCPConn.Read(in)
	return
}

