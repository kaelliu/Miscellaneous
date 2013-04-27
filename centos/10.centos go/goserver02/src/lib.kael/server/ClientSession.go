package server

import (
	"net"
	"bytes"
	"fmt"
	"runtime/debug"
	"log"
	"lib.kael/utils"
//	"reflect"
	"bufio"
)

type ClientSession struct{
	conn 	  net.Conn
	writeChan chan []byte
//	readChan  chan []byte
	quitChan  chan bool
}

func (c *ClientSession) DoLogic(){
	go ClientReader(c)
	go ClientWriter(c)
}

func (c* ClientSession) ExceptionOccured(){
	err := recover()
	if err == nil {
		return
	}
	var buf bytes.Buffer
	fmt.Fprintf(&buf, "tcp: panic serving %v: %v\n", c.conn.RemoteAddr(), err)
	buf.Write(debug.Stack())
	log.Print(buf.String())

	if c.conn != nil {
		c.conn.Close()
	}
}

func ClientReader(c* ClientSession){
	defer c.ExceptionOccured()
	var dataBuf *bytes.Buffer
	for {
		// read 2 bytes package's length 
		buf,err := utils.ReadFromConn(2,c.conn)
		if err != nil{
			// maybe connection broken
			// should call logic layer,onDisconection
			break
		}
		// then read the package
		packageLen := utils.BytesToUint16(buf)
		if packageLen == 0{
			continue
		}
		
		data,err := utils.ReadFromConn(int(packageLen),c.conn)
		if err!=nil{
			break
		}
		
		if ServerBs.SPipelineConfig.Cipher != nil{
			newData,err := ServerBs.SPipelineConfig.Cipher.DoDecrypt(data)
			if err != nil{
				fmt.Println("invalid data,stopped")
				break
			}
			dataBuf = bytes.NewBuffer(newData)
		}else{
			dataBuf = bytes.NewBuffer(data)
		}
		
		// get the command
		command := utils.BytesToUint32(dataBuf.Next(4))
		module  := command / 10000
		funcs   := command % 10000
		service,exist := ServerBs.SPipelineConfig.Router[module]
		var argv interface{}
		if exist{
			// get the arguments
			if ServerBs.SPipelineConfig.Proto != nil{
				err := ServerBs.SPipelineConfig.Proto.DoDecode(dataBuf.Bytes()[0:],argv)
				if err != nil{
					fmt.Println("invalid data,stopped")
					break
				}
			}
			
			go service.call(funcs,argv,c)
		}else{
			fmt.Println("invalid command,stopped")
			break
		}
	}
}

func ClientWriter(c* ClientSession){
	for {
		select{
			case buffer := <-c.writeChan:
				fmt.Println("ClientSender sending")
				bufk := bufio.NewWriter(c.conn)
				bufk.Write([]byte(buffer)[0:])
				err := bufk.Flush()
//				_,err := c.conn.Write([]byte(buffer)[0:])
				if err!=nil{
					fmt.Println("write error,connection may down")
					c.quitChan<-true
					break
				}
			case <-c.quitChan:
				c.Close()
				break
		}
	}
}

func (c *ClientSession) Close() error {
	if c.conn != nil {
		c.conn.Close()
	}
	return nil
}

func (c *ClientSession) SendResponse(command uint32,da interface{}) {
	var data []byte
	var err error
	var newdata []byte
	var final []byte
	var buffer *bytes.Buffer
	buffer = bytes.NewBuffer(data)
	// write length first
	buffer.Write(utils.Uint16ToBytes(uint16(len(newdata))))
	if ServerBs.SPipelineConfig.Proto != nil{
		newdata,err = ServerBs.SPipelineConfig.Proto.DoEncode(da)
		if err != nil{
		}
	}
	// write command and data
	buffer.Write(utils.Uint32ToBytes(uint32(command)))
	buffer.Write(newdata)
	if ServerBs.SPipelineConfig.Cipher != nil{
		// encrypt some bytes
		final,err = ServerBs.SPipelineConfig.Cipher.DoEncrypt(buffer.Bytes()[2:])
		if err != nil{
		}
	}else{
		final = buffer.Bytes()[0:]
	}
	
	// to bufio
	c.writeChan <- final
}

