package room

import (
	"lib/tcp"
	"fmt"
)

type roomConfig struct{
	roomId uint16
	tableNum uint16
	perTableNum uint8
	gameType string
}

type roomUser struct{
	uid        uint
	token      string
	serveConn  *tcp.ServeConn
	sign       bool
}

type Room struct{
	config          *roomConfig
	closeChan       chan bool 
	roomUsers       map[uint] *roomUser
	roomUserChan    chan *roomUser
	roomDelUserChan chan uint 
	broadcastChan   chan *broadcact 
}

type broadcact struct{
	uid  uint
	resp *tcp.Response 
}

var defaultRoom *Room

func InitRoom(gameType string, tableNum uint16, perTableNum uint8) {
	defaultRoom = newRoom(gameType, tableNum, perTableNum)
	go defaultRoom.run()
}

func JoinRoom(uid uint, token string, serveConn *tcp.ServeConn) {
	fmt.Println(&roomUser{uid:uid, serveConn:serveConn, token:token, sign:true})
	defaultRoom.roomUserChan <- &roomUser{uid:uid, serveConn:serveConn, token:token, sign:true}
}

func LeaveRoom(uid uint){
	defaultRoom.roomDelUserChan <- uid
}

func SendMessage(uid uint, message string){
	var resp = &tcp.Response{0, "Room.SendMessage", &map[string]interface{}{
		"Uid"     : uid,
		"Message" : message,
	}}
	defaultRoom.broadcastChan <- &broadcact{uid:uid, resp:resp}
}

func newRoom(gameType string, tableNum uint16, perTableNum uint8) *Room{
	//@TODO 之后需要先到游戏大厅注册roomId
	var roomId uint16
	roomId = 1
	
	config := &roomConfig{
		roomId       : roomId,
		tableNum     : tableNum,
		perTableNum  : perTableNum,
		gameType     : gameType,
	}
	
	return &Room{
		config           : config,
		closeChan        : make(chan bool),
		roomUsers        : make(map[uint] *roomUser),
		broadcastChan    : make(chan *broadcact, 100), 
		roomUserChan     : make(chan *roomUser), 
		roomDelUserChan  : make(chan uint), 
	}
}

func (r *Room) join(roomUser *roomUser){
	r.roomUsers[roomUser.uid] = roomUser
	var resp = &tcp.Response{0, "Room.JoinRoom", &map[string]interface{}{
		"Uid"     : roomUser.uid,
	}}
	defaultRoom.broadcastChan <- &broadcact{uid:0, resp:resp}
//	fmt.Println("joinroom :", roomUser.uid)
//	fmt.Println("users :", r.roomUsers, "num:",len(r.roomUsers))
}

func (r *Room) leave(uid uint){
	delete(r.roomUsers, uid)
	var resp = &tcp.Response{0, "Room.LeaveRoom", &map[string]interface{}{
		"Uid"     : uid,
	}}
	defaultRoom.broadcastChan <- &broadcact{uid:0, resp:resp}
//	fmt.Println("leaveroom :", uid)
//	fmt.Println("users :", r.roomUsers, "num:",len(r.roomUsers))
}

func (r *Room) close(){
	if r.broadcastChan != nil {
		close(r.broadcastChan)
	}
	
	if r.closeChan != nil {
		close(r.closeChan)
	}
	
	if r.roomUserChan != nil {
		close(r.roomUserChan)
	}
	
	if r.roomDelUserChan != nil {
		close(r.roomDelUserChan)
	}
}

func (r *Room) run(){
	defer r.close()
	
	//用于监听RoomUsers
	go func(){
		for {
			select {
				case roomUser := <- r.roomUserChan:
					r.join(roomUser)
				case uid := <- r.roomDelUserChan:
					r.leave(uid)
			}
		}
	}()
	
	for {		
		select {
			case broadcact := <- r.broadcastChan:	
				for _, roomUser := range r.roomUsers {
					if roomUser.uid != broadcact.uid{
						roomUser.serveConn.SendResponse(broadcact.resp)
					}
				}
			case c := <- r.closeChan:			
				if c == true {
					close(r.broadcastChan)				
					close(r.closeChan)				
					return
				}		
		}	
	}
}