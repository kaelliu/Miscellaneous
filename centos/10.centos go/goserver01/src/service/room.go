package service

import (
	"module/room"
	"lib/tcp"
	"fmt"
)

type Room int

type RoomArgs struct {
	Uid   uint    
	Token string ""
	Message string ""
}

func (r *Room) JoinRoom(sc *tcp.ServeConn, Args RoomArgs) error{
	fmt.Println("Args :",Args)
	room.JoinRoom(Args.Uid, Args.Token, sc)
	return nil
}

func (r *Room) LeaveRoom(sc *tcp.ServeConn, Args RoomArgs) error{
	fmt.Println("Args :",Args)
	room.LeaveRoom(Args.Uid)
	return nil
}

func (r *Room) SendMessage(sc *tcp.ServeConn, Args RoomArgs) error{
	fmt.Println("Args :",Args)
	room.SendMessage(Args.Uid, Args.Message)
	return nil
}