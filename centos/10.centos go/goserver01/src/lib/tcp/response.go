package tcp

import (

)

type Response struct{
	Code    int            `json:"code"`
	Method	string         `json:"method"`
	Data    interface{}    `json:"data"`
}
