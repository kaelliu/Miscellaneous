package main

import (
	"fmt"
	"time"
	"net"
	"runtime"
)

type MyStructs struct{
	c1 int
	c2 string
	c3 map[int]string
	c4 byte
	c5 bool
	c6 [10]int16
}

func recvFunc(ch chan *MyStructs,chout chan int){
	ms := <-ch
//	fmt.Println(ms.c1)
	fmt.Println("when i use the data,c1 is",ms.c1,time.Now())
	chout<-ms.c1
}

func changeFunc(ms *MyStructs){
	ms.c1 = 3
	fmt.Println("after i call method change in main process c1 is:",ms.c1,time.Now())
}

func memoryTest(ch chan **MyStructs){
	defer func(){
		fmt.Println("thing is:")
	}()
	for{
		va := <-ch
//		fmt.Println("now the thing is:",&(*va),va.c1)
		time.Sleep(1 * 1e9)
		fmt.Println("now the thing is:",va,(*va).c2)
		time.Sleep(2 * 1e9)
		fmt.Println("final now the thing is:",va)
	 }
}

type client struct{
	id int
	conn *net.TCPConn
}

func main(){
	maxProcs := runtime.GOMAXPROCS(0)
    numCPU := runtime.NumCPU()
    var param int
    if maxProcs < numCPU {
        param = maxProcs
    }else{
     param = numCPU
    }
	fmt.Println("cpu info",param * 2 + 1)
	fmt.Println("hell kael",time.Now())
	chMemory := make(chan **MyStructs,10)
	go memoryTest(chMemory)
	for i:=1;i<2;i++{
		ms := &MyStructs{i,"2",make(map[int]string),3,false,[10]int16{1,3,4,5,6,7,8,9,0,2}}
		mss := &ms
		fmt.Println("well the thing is:",mss)
		chMemory<-mss
		time.Sleep(2 * 1e9)
		ms.c2 = "3"
	}
	
	// test code start
	ms := &MyStructs{1,"2",make(map[int]string),3,false,[10]int16{1,3,4,5,6,7,8,9,0,2}}
	
	// 
	chrecv := make(chan *MyStructs,1)
	chchg  := make(chan int,1)
	fmt.Println("start c1 is:",ms.c1,time.Now())
	// do recv wait
	go recvFunc(chrecv,chchg)
	// i send it first
	chrecv <- ms
	// i change pointer in other goroutine
	go changeFunc(ms)
	// change it here
//	changeFunc(ms)
		
	// get the final value
	final := <-chchg
	fmt.Println("value i get send back",final,time.Now())
	time.Sleep(2 * 1e9)
}





//const APP_VERSION = "0.1"
//
//
//func main() {
//	ch := make(chan int, 10)
//	go testChan(ch)
//	
//	for i := 1; i<=11; i++ {
//		ch <- i
//		fmt.Print("send:", i, "\n")
//	}
//    fmt.Println("Version:", APP_VERSION)
//    time.Sleep(15 * 1e9)
//}
//
//
//func testChan(ch chan int){
//	 for{
//	 	i := <-ch
//	 	time.Sleep(1 * 1e9)
//	 	fmt.Print("receive:", i, "\n")
//	 }
//}

