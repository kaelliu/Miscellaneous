package crypto

import (
	"crypto/cipher"
	"crypto/aes"
)

type AesCipher struct{
	Key []byte
}

func (this* AesCipher) DoDecrypt(src []byte) ([]byte,error){
	dest,err := aesDecrypt(src,this.Key)
	return dest,err
}

func (this* AesCipher)DoEncrypt(src []byte) ([]byte,error){
	dest,err := aesEncrypt(src,this.Key)
	return dest,err
}

func aesEncrypt(origData, key []byte) ([]byte, error) {
	block, err := aes.NewCipher(key)
	if err != nil {
		return nil, err
	}
	blockSize := block.BlockSize()
	origData = pKCS5Padding(origData, blockSize)
	blockMode := cipher.NewCBCEncrypter(block, key[:blockSize])
	crypted := make([]byte, len(origData))
	// 根据CryptBlocks方法的说明，如下方式初始化crypted也可以
	blockMode.CryptBlocks(crypted, origData)
	return crypted, nil
}

func aesDecrypt(crypted, key []byte) ([]byte, error) {
	block, err := aes.NewCipher(key)
	if err != nil {
		return nil, err
	}
	blockSize := block.BlockSize()
	blockMode := cipher.NewCBCDecrypter(block, key[:blockSize])
	origData := make([]byte, len(crypted))
	blockMode.CryptBlocks(origData, crypted)
	origData = pKCS5UnPadding(origData)
	return origData, nil
}