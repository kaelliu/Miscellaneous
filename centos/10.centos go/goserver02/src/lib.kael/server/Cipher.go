package server

type Crypt interface{
	DoDecrypt(src []byte) ([]byte,error)
	DoEncrypt(src []byte) ([]byte,error)
}