package service

import (
	"lib/tcp"
	"fmt"
)

type Table int
type Args struct {
	A,B int
}

func (t *Table) TestAdd(sc *tcp.ServeConn, Args Args) error{
	fmt.Println("Args :",Args)
//	sc.SendChan
//	roomUsersChan[uid] =sc.SendChan
//	tableUsersChan[uid] =sc.SendChan
//	Talbe.send()
//	room.send
	sum := Args.A + Args.B
	fmt.Println("Response :", &tcp.Response{0,"Table.TestAdd", sum});
	sc.SendResponse(&tcp.Response{0,"Table.TestAdd", sum})
	return nil
}

func (t *Table) testAdd(sc *tcp.ServeConn, Args Args) error{
	fmt.Println("Args :",Args)
//	sc.SendChan
//	roomUsersChan[uid] =sc.SendChan
//	tableUsersChan[uid] =sc.SendChan
//	Talbe.send()
//	room.send
	sum := Args.A + Args.B
	fmt.Println("Response :", &tcp.Response{0,"Table.TestAdd", sum});
	sc.SendResponse(&tcp.Response{0,"Table.TestAdd", sum})
	return nil
}

//func (t *Table) TestRev(a, b int)(int, int){
//	return b, a
//}
//
//func (t *Table) TestResponse(Args *Args, resp *tcp.Response)error{
//	resp.Code = a
//	return a
//}