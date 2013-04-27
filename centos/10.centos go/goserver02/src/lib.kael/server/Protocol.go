package server

import (

)

type Protocol interface{
	ContentType() string
	DoEncode(data interface{}) ([]byte,error)
	DoDecode(b []byte,data_in interface{}) error
}
