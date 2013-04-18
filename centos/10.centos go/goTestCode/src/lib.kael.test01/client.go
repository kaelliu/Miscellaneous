package kael

import (
	"net"
	"bytes"
)

type Client interface {
	GetId() int

	GetLocalAddress() net.Addr
	GetRemoteAddress() net.Addr

	GetData() interface{}

	SetData(interface{})

	BufferedWrite([]byte) (int, error)
}

type client struct {
	id int
	nconn *net.TCPConn
	conn *sTCPConn

	last_in bytes.Buffer

	data interface{}
}

func (this *client) GetId() int {
	return this.id
}

func (this *client) GetLocalAddress() net.Addr {
	return this.conn.LocalAddr()
}

func (this *client) GetRemoteAddress() net.Addr {
	return this.conn.RemoteAddr()
}

func (this *client) GetData() interface{} {
	return this.data
}

func (this *client) SetData(data interface{}) {
	this.data = data
}

func (this *client) BufferedWrite(bs []byte) (n int, err error) {
	var b bytes.Buffer

	b.Write(bs)
	b.WriteString(CRLF)

	bb := split_by_n(b.Bytes(), MAX_PACKET_LENGTH)
	for _, ab := range(bb) {
		n, err = this.conn.Write(ab)
	}

	return
}
