package main

import (
	"log"
	//	"encoding/json"
	//	"lib/util"
	"flag"
	"fmt"
	"lib/tcp"
	"module/room"
	"service"

//	"reflect"
)

var ip *string = flag.String("i", ":8001", "server listen ip \"127.0.0.1:8001\"")
var gameType *string = flag.String("g", "blackjack", "game type \"blackjack\"")
var tableNum *int = flag.Int("tn", 100, "table num  per room. default 100  in 1~65535")
var perTableNum *int = flag.Int("ptn", 3, "user num  per table. default 2 in 1~255")

func main() {
	//parse flag
	flagParse()

	//初始化room
	room.InitRoom(*gameType, uint16(*tableNum), uint8(*perTableNum))

	//add service to serve
	tcp.Register(new(service.Table))
	tcp.Register(new(service.Room))

	//listen and serve
	err := tcp.ListenAndServe(*ip)
	if err != nil {
		log.Fatal(err)
	}
}

func flagParse() {
	flag.Parse()
	fmt.Println("flag set ip：", *ip)
	fmt.Println("flag set game type：", *gameType)
	fmt.Println("flag set table num：", *tableNum)
	if *tableNum < 1 || *tableNum > 65535 {
		log.Fatal("table num must uint16 2~65535 now:", *tableNum)
	}
	fmt.Println("flag set perTableNum：", *perTableNum)
	if *perTableNum < 1 || *perTableNum > 255 {
		log.Fatal("per table num must uint8 1~255 now:", *tableNum)
	}
}
