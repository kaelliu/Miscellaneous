package tcp

import (

)

type Request struct{
	Method     string       `json:"method"`
	Params  interface{}  `json:"params"`
}