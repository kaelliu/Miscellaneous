package server
import (
	"net"
	"fmt"
	"runtime"
)
type ServerCommon struct{
	Addr string ":8888" // used ip and port
	MaxConn uint16 // how many client socket can be accept in
	Af byte // Address_family AF_UNIX/AF_INET(ipv4)/AF_INET6(ipv6)
	Tp byte // SOCK_DGRAM/SOCK_STREAM/SOCK_RAW
	// todo max connection exceed check
	CurrConn uint16
}

func DefaultServerCommon() *ServerCommon{
	return &ServerCommon{}
}

func (s *ServerCommon) Start() error{
	// start listen
	serverAddr, err := net.ResolveTCPAddr("tcp", s.Addr)
	if err != nil {
		return err
	}

	listener, err := net.ListenTCP("tcp", serverAddr)
	if err != nil {
		return err
	}

	println("Listening to: ", listener.Addr().String())
	runtime.GOMAXPROCS(runtime.NumCPU())
	cpuCores := runtime.NumCPU()
	for i:=0;i<cpuCores;i++{
		go s.DoServer(listener)
	}
	return nil
}

func (s *ServerCommon) DoServer(listener *net.TCPListener) error{
	defer listener.Close()
	for {
		conn,err := listener.AcceptTCP()
		fmt.Println("accept :", conn.RemoteAddr())
		if err != nil {
			return err
		}
		
		conn.SetNoDelay(ServerBs.SPipelineConfig.TcpNodelay)
		conn.SetKeepAlive(ServerBs.SPipelineConfig.KeepAlive)
		
		newConnection, err := ServerBs.SPipelineConfig.NewClientConnection(conn)
		if err != nil {
			continue
		}
		go newConnection.DoLogic()
	}
	panic("not reached")
}

func (s *ServerCommon) Stop() {
	ServerBs.MainChannel<-0
}
