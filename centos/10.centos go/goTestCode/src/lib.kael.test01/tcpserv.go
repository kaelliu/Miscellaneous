package kael

import (
	"fmt"
	"net"
	"sync"
	"time"
	"bytes"
	"errors"
	"strings"
)


const (
       DISCONNECT_CLIENT = 1 << iota
       KEEP_CLIENT = DISCONNECT_CLIENT << iota
)

var (
       CRLF = "\r\n"
       MAX_PACKET_LENGTH = 512
)

type ConnectionAction int

type TCPServ interface {
	Init(uint) error
	Run() error
	Stop(bool) error
	Free() error

	GetPort() uint

	GetNClients() uint
	GetClients() []Client

	IsRunning() bool

	SetOnConnection(func(Client) ConnectionAction)
	SetOnReadError(func(Client, error) ConnectionAction)
	SetOnWriteError(func(Client, error) ConnectionAction)
	SetOnPacketReceived(func(Client, []byte, uint) ConnectionAction)
	SetOnDisconnection(func(Client))
}

type tcpserv struct {
	init bool
	running bool

	port uint
	m sync.Mutex
	nln *net.TCPListener
	ln *sTCPListener

	clients []*client

	crlf string

	on_connection func(Client) ConnectionAction
	on_read_error func(Client, error) ConnectionAction
	on_write_error func(Client, error) ConnectionAction
	on_packet_received func(Client, []byte, uint) ConnectionAction
	on_disconnection func(Client)
}

func NewTCPServ() TCPServ {
	s := new(tcpserv)

	s.init = false
	s.running = false

	return s
}

func (this* tcpserv) remove_client(cl* client){
	this.m.Lock()
	defer this.m.Unlock()
	
	cls := []*client{}
	for _,c := range(this.clients){
		if c.id != cl.id{
			cls = append(cls,c)
		}
	}
	
	this.clients = []*client{}
	this.clients = cls
}

func (this *tcpserv) GetPort() uint {
	this.m.Lock()
	defer this.m.Unlock()

	p := this.port

	return p
}

func (this *tcpserv) GetNClients() uint {
	this.m.Lock()
	defer this.m.Unlock()

	n := len(this.clients)

	return (uint)(n)
}

func (this *tcpserv) GetClients() []Client {
	this.m.Lock()
	defer this.m.Unlock()

	cls := make([]Client, this.GetNClients())
	for i, _ := range(cls) {
		cls[i] = this.clients[i]
	}

	return cls
}

func (this *tcpserv) IsRunning() bool {
	this.m.Lock()
	defer this.m.Unlock()

	b := this.running

	return b
}

func (this *tcpserv) SetOnConnection(on_connection func(Client) ConnectionAction) {
	this.m.Lock()
	defer this.m.Unlock()

	this.on_connection = on_connection
}

func (this *tcpserv) SetOnReadError(on_read_error func(Client, error) ConnectionAction) {
	this.m.Lock()
	defer this.m.Unlock()

	this.on_read_error = on_read_error
}

func (this *tcpserv) SetOnWriteError(on_write_error func(Client, error) ConnectionAction) {
	this.m.Lock()
	defer this.m.Unlock()

	this.on_write_error = on_write_error
}

func (this *tcpserv) SetOnPacketReceived(on_packet_received func(Client, []byte, uint) ConnectionAction) {
	this.m.Lock()
	defer this.m.Unlock()

	this.on_packet_received = on_packet_received
}

func (this *tcpserv) SetOnDisconnection(on_disconnection func(Client)) {
	this.m.Lock()
	defer this.m.Unlock()

	this.on_disconnection = on_disconnection
}

func (this *tcpserv) Init(port uint) error {
	if this.init == true {
		return errors.New("server already initialized")
	}
	this.on_connection = func(Client) ConnectionAction { return KEEP_CLIENT }
	this.on_read_error = func(Client, error) ConnectionAction { return DISCONNECT_CLIENT }
	this.on_write_error = func(Client, error) ConnectionAction { return DISCONNECT_CLIENT }
	this.on_packet_received = func(Client, []byte, uint) ConnectionAction { return KEEP_CLIENT }
	this.on_disconnection = func(Client) {}
	
	var err error
	var addr *net.TCPAddr
	addr, err = net.ResolveTCPAddr("tcp", fmt.Sprintf(":%d", port))
	this.nln, err = net.ListenTCP("tcp", addr)
	
	if err != nil {
		return errors.New(err.Error())
	}
	this.ln = newsTCPListener(this.nln)
	
	return nil
}

func (this *tcpserv) Run() (err error) {
	if this.running == true {
		return errors.New("server already running")
	}
	this.running = true

	id_gen := 0
	
	var wg sync.WaitGroup
	for {
		this.m.Lock()
		if this.running == false {
			break
		}
		this.m.Unlock()
		
		t := time.Now().Add(time.Second)
		this.ln.SetDeadline(t)
		var conn *net.TCPConn
		conn, err = this.ln.AcceptTCP()
		
		if err != nil {
			if err.Error() == this.ln.se {
				err = nil
			} else if strings.HasSuffix(err.Error(), "timeout") == true {
				continue
			}

			return
		}
		cl := new(client)
		cl.nconn = conn
		cl.nconn.SetReadBuffer(MAX_PACKET_LENGTH)
		cl.nconn.SetKeepAlive(true)
		cl.conn = newsTCPConn(cl.nconn)
		
		cl.id = id_gen

		id_gen++
		
		this.clients = append(this.clients, cl)
		wg.Add(1)
		go func(cl *client) {
			defer func() {
				// FIXME: NULL dereferencement upon forced stop
				this.remove_client(cl)
				this.on_disconnection(cl)
				cl.conn.Close()
				wg.Done()
			}()
			
			if this.on_connection(cl) == DISCONNECT_CLIENT {
				return
			}
			
			for {
				in := make([]byte, MAX_PACKET_LENGTH)
				ct := time.Now().Add(time.Second)
				cl.conn.SetDeadline(ct)
				n, e := cl.conn.Read(in)
				if e != nil {
					if e.Error() == cl.conn.se {
						return
					} else if strings.HasSuffix(e.Error(), "timeout") == true {
							continue
					} else if this.on_read_error(cl, e) == DISCONNECT_CLIENT {
						return
					}
					continue
				}
				
				cl.last_in.Write(in[:n])
				if bytes.HasSuffix(cl.last_in.Bytes(), ([]byte)(this.crlf)) == true {
					if this.on_packet_received(cl,
								   cl.last_in.Bytes(),
								   (uint)(cl.last_in.Len())) == DISCONNECT_CLIENT {
						return
					}
					cl.last_in.Reset()
				}
			}
		}(cl)
	}
	
	wg.Wait()

	return nil
}

func (this *tcpserv) Stop(kill_connections bool) error {
	this.m.Lock()
	defer this.m.Unlock()

	if this.running == false {
		return errors.New("server already stopped")
	}

	this.running = false
	if kill_connections == true {
		for _, c := range(this.clients) {
			c.conn.sc <- void_signal{}
		}
	}

	this.ln.sc <- void_signal{}

	return nil
}

func (this *tcpserv) Free() error {
	if this.init == false {
		return errors.New("server not initialized")
	}

	return nil
}