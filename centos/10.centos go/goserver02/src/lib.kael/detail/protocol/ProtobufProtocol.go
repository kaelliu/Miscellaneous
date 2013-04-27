package protocol

import (

)

const ProtoBufProtocolName = "protobuf"

type ProtoBufProtocol struct{
}

func (this *ProtoBufProtocol) ContentType()string{
	return ProtoBufProtocolName
}

func (this *ProtoBufProtocol) DoEncode(data interface{}) ([]byte,error){
	return nil,nil
}

func (this *ProtoBufProtocol) DoDecode(b []byte,data_in interface{}) error{
	return nil
}

