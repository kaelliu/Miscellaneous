package game

import (
	"fmt"
)

func Hello(){
	fmt.Printf("testing")
}

type Game interface {
	Begin() error
	Step() error
	Over() error
}