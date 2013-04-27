package crypto

import (
	"crypto/cipher"
	"crypto/des"
)

type TripleDesCipher struct{
	Key []byte
}

func (this* TripleDesCipher) DoDecrypt(src []byte) ([]byte,error){
	dest,err := tripleDesDecrypt(src,this.Key)
	return dest,err
}

func (this* TripleDesCipher)DoEncrypt(src []byte) ([]byte,error){
	dest,err := tripleDesEncrypt(src,this.Key)
	return dest,err
}

// 3DES加密
func tripleDesEncrypt(origData, key []byte) ([]byte, error) {
	block, err := des.NewTripleDESCipher(key)
	if err != nil {
		return nil, err
	}
	origData = pKCS5Padding(origData, block.BlockSize())
	// origData = ZeroPadding(origData, block.BlockSize())
	blockMode := cipher.NewCBCEncrypter(block, key[:8])
	crypted := make([]byte, len(origData))
	blockMode.CryptBlocks(crypted, origData)
	return crypted, nil
}

// 3DES解密
func tripleDesDecrypt(crypted, key []byte) ([]byte, error) {
	block, err := des.NewTripleDESCipher(key)
	if err != nil {
		return nil, err
	}
	blockMode := cipher.NewCBCDecrypter(block, key[:8])
	origData := make([]byte, len(crypted))
	// origData := crypted
	blockMode.CryptBlocks(origData, crypted)
	origData = pKCS5UnPadding(origData)
	// origData = ZeroUnPadding(origData)
	return origData, nil
}
