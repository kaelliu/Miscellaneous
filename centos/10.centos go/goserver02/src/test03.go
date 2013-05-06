package main

import (
	"fmt"
	"net"
	"time"
	//	"lib.kael/utils"
	//	"database/sql"
	"lib.kael/detail/crypto"
	"lib.kael/server"
	"lib.kael/utils"
	//	"encoding/base64"
	"encoding/json"
	"reflect"
)

const JsonProtocolName = "json"

type Encoder interface {
	DoEnc(b []byte) []byte
}

type Decoder interface {
	DoDec(b []byte) []byte
}

type ByteBuffer interface {
	ReadInt(b []byte) int
	ReadShort(b []byte) int16
	WriteInt(b []byte, data int) error
}

type BufferedChannel struct {
	Buffer   []byte
	ReadChan chan []byte
}

type protocol interface {
	ContentType() string
	DoEncode(data interface{}) ([]byte, error)
	DoDecode(b []byte, data_in interface{}) error
}

type MyCipher struct {
	key string
}

func (this *MyCipher) XORKeyStream(dst, src []byte) {

}

type JsonProtocol struct {
}

type XmlProtocol struct {
}

func (this *JsonProtocol) ContentType() string {
	return JsonProtocolName
}

func (this *JsonProtocol) DoEncode(data interface{}) ([]byte, error) {
	return nil, nil
}

func (this *JsonProtocol) DoDecode(b []byte, data_in interface{}) error {
	return nil
}

type Client struct {
	Id        int
	ReadChan  chan []byte
	WriteChan chan []byte
	Conn      net.Conn
	Quit      chan bool
	HeartBeat *time.Ticker
}

type myCallBack func(c *GameClient) error

// when read / write do Cipher.Decode -> Proto.DoDecode / Proto.DoEncode -> Cipher.Encrypt
type GameClient struct {
	Client
	LogicChan               chan interface{}
	CacheHandleChan         chan []byte
	DbHandlerChan           chan string
	Cipher                  *MyCipher
	Proto                   *protocol
	OnDisconnectionCallback myCallBack
	OnConnctionCallback     myCallBack
}

type ClientInterface interface {
	OnConnection()
	OnDisconnection()
	//	MessageReceived(c* Client)
	ExceptionGot(c *Client)
	//	Close()
}

func (c *Client) Close() error {
	var err error
	if c.Conn != nil {
		err = c.Conn.Close()
	}
	return err
}

func (c *Client) OnDisconnection() {
	c.Close()
}

func (c *GameClient) OnDisconnection() {
	c.Client.OnDisconnection()
	// do own logic
	if c.OnDisconnectionCallback != nil {
		c.OnConnctionCallback(c)
	}
}

type CacheHandler interface {
	Call(s string) interface{}
} // redis/memcached

type Detail struct {
	Tp reflect.Type
	Da interface{}
}

type ServerCodec interface {
	Close() error
}
type jsonServerCodec struct {
	dec int
}

func NewServerCodec() ServerCodec {
	return &jsonServerCodec{dec: 3}
}

func (c *jsonServerCodec) Close() error {
	return nil
}

type abc struct {
	A string `json:"a"`
	B string `json:"b"`
	C int    `json:"c"`
}

type Message struct {
	Name string
	Body string
	Time int64
}

func main() {
	aaa := abc{"1", "2", 3}
	//	var bca interface{}
	//	bca = aaa
	//	m := Message{"Alice", "Hello", 1294706395881547000}
	msg, _ := utils.EncodeJson(aaa)
	fmt.Println(string(msg))
	c28, _ := utils.DecodeJson(msg)
	fmt.Println("c28:", c28)
	var c30 abc
	fmt.Println("c28.A:", c28["a"])
	fmt.Println("c28.C:", c28["c"])
	c30.A = c28["a"].(string)
	c30.C = int(c28["c"].(float64))
	fmt.Println("c30.A:", c30.A)
	fmt.Println("c30.C:", c30.C)
	err := json.Unmarshal(msg, &c30)

	fmt.Println(reflect.TypeOf(c28))

	for _, val := range c28 {
		val2 := reflect.ValueOf(val) //.FieldByName(val)
		fmt.Println("suck", val2)
	}

	for i := 0; i < reflect.ValueOf(c30).NumField(); i++ {
		//		val2 := reflect.ValueOf(val)//.FieldByName(val)
		fmt.Println("suck2", reflect.ValueOf(c30).Field(i))
	}

	//	fmt.Println("s")
	//	go server.ServerBs.InitWithConfigFile("config.xml")

	//	abcd := server.ServerBs
	//	abc := server.DefaultServerCommon()
	//	abc.Start()
	//	abcd.ServerParam = abc
	var c1 interface{}
	var c2 protocol
	var c3 *protocol
	//	var c4 protocol
	c1 = 1
	c2 = &JsonProtocol{}
	c3 = &c2
	fmt.Println(reflect.TypeOf(c1))
	fmt.Println(reflect.TypeOf(c2))
	fmt.Println(reflect.TypeOf(c3))

	fmt.Println(reflect.ValueOf(c1))
	fmt.Println(reflect.ValueOf(c2))
	fmt.Println(reflect.ValueOf(c3))

	db := []byte("kael@ptahstudio.com")
	//	key := "sfe023f_"
	var des server.Crypt
	des = &crypto.MiscCipher{Key: 0xA55AA55A}
	fmt.Println(reflect.TypeOf(reflect.TypeOf(des)))

	//	var chiper *server.Crypt
	//	chiper = &des

	result, err := des.DoEncrypt(db)
	fmt.Println("current:", string(result))
	if err != nil {
		panic(err)
	}
	//	fmt.Println(base64.StdEncoding.EncodeToString(result))
	origData, err := des.DoDecrypt(result)
	if err != nil {
		panic(err)
	}
	fmt.Println(string(origData))
	fmt.Println(utils.BytesToUint16([]byte{1, 255}))
	fmt.Println(utils.Uint16ToBytes(511))
	server.ServerBs.InitWithConfigFile("config.xml")
	//	server.ServerBs.Start()
	//	select{}
}
