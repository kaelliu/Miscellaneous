package utils

import (
	"encoding/json"
	"net"
	"io"
//	"reflect"
)

func DecodeJson(byteArr []byte) (map[string]interface{}, error) {
	var msg interface{}
	err := json.Unmarshal(byteArr, &msg)
	if err != nil {
		return nil, err
	}
	return msg.(map[string]interface{}), nil
}

func DecodeJsonEx(byteArr []byte,typ interface{}) (interface{}, error) {
	err := json.Unmarshal(byteArr, &typ)
	if err != nil {
		return nil, err
	}
	return typ, nil
}

func EncodeJson(jsonData interface{}) ([]byte, error) {
	msg, err := json.Marshal(jsonData)
	if err != nil {
		return nil, err
	}
	return msg, err
}

// Encode uint32 to [4]byte
func Uint32ToBytes(i uint32) []byte {
	return []byte{byte((i >> 24) & 0xff), byte((i >> 16) & 0xff),
		byte((i >> 8) & 0xff), byte(i & 0xff)}
}

func BytesToUint32(buf []byte) uint32 {
	return uint32(buf[0])<<24 + uint32(buf[1])<<16 + uint32(buf[2])<<8 +
		uint32(buf[3])
}

func Uint16ToBytes(i uint16) []byte {
	return []byte{byte((i >> 8) & 0xff), byte(i & 0xff)}
}

func BytesToUint16(buf []byte) uint16 {
	return (uint16(buf[0])<<8) | (uint16(buf[1]))
}

/**
     read certain length from net.conn
**/
func ReadFromConn(length int,conn net.Conn) ([]byte,error){
	dataBuf := make([]byte,length)
	var dataLenTag int
	for {
		tmpNum,err := conn.Read(dataBuf[dataLenTag:length])
		if err != nil{
			if err == io.EOF{
//				println("read eof  num=%d\n", tmpNum)
				return dataBuf,nil
			}
			return dataBuf,err
		}
		dataLenTag += tmpNum
		if dataLenTag >= length{
			break
		}
	}
	return dataBuf,nil
}