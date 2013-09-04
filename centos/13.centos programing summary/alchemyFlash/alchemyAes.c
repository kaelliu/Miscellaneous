// ======================================================================================
// File         : alchemyAes.c
// Author       : Liu ZiTao 
// Last Change  : 4/10/2012 | 9:30:24 AM | Tuesday,April
// Description  : to encrypt the flash from alchemy
// =======================================================================================

///////////////////////////////////////////////////////////////////////////////
// includings
///////////////////////////////////////////////////////////////////////////////
#include "AS3.h"
#include "alchemyAes.h"

///////////////////////////////////////////////////////////////////////////////
// functions
///////////////////////////////////////////////////////////////////////////////
AS3_Val doAes(void *data, AS3_Val args)
{
	AS3_Val input = NULL;
    unsigned int in_len;
	char * ar;
	AS3_ArrayValue(args, "AS3ValType, IntType", &input, &in_len);
	ar = (char *)malloc(in_len);
	
	AS3_ByteArray_readBytes((void*)ar, input, in_len);
	
	char * aesData;
	int out_len = 0;
	char * keyStr = "kael";
	
	//use AES to Encrypt the bytes
	aesData = DES_Encrypt(ar, keyStr, in_len, &out_len );

	//make a new as3 byteArray var
	AS3_Val baNS = AS3_String("flash.utils");
	AS3_Val baClass = AS3_NSGetS(baNS, "ByteArray");
	AS3_Val emptyParams = AS3_Array("");
	AS3_Val byteArray2 = AS3_New(baClass, emptyParams);
	
	AS3_ByteArray_writeBytes(byteArray2, aesData, out_len);
	return byteArray2;
}

AS3_Val doUnAes(void *data, AS3_Val args)
{
	AS3_Val input = NULL;
    unsigned int in_len;
	char * ar;
	AS3_ArrayValue(args, "AS3ValType, IntType", &input, &in_len);
	ar = (char *)malloc(in_len);
	
	AS3_ByteArray_readBytes((void*)ar, input, in_len);
		
	char * aesData;
	int out_len = 0;
	char * keyStr = "kael";
	
	//use AES to Decrypt the bytes	
	aesData = DES_Decrypt(ar, keyStr, in_len, &out_len );

	//make a new as3 byteArray var
	AS3_Val baNS = AS3_String("flash.utils");
	AS3_Val baClass = AS3_NSGetS(baNS, "ByteArray");
	AS3_Val emptyParams = AS3_Array("");
	AS3_Val byteArray2 = AS3_New(baClass, emptyParams);
	
	AS3_ByteArray_writeBytes(byteArray2, aesData, out_len);
	return byteArray2;
}

char* dofileEx(const char *in_fname, const char *pwd,int fileLen)
{
    register char ch;
    int j  = 0;
    int j0 = 0;

    while ( pwd[ ++j0 ] );
    ch = in_fname[0];
	char *result ;
	result = (char*)malloc(fileLen);
    /*加密算法开始*/
    int countc=0;
    while ( countc < fileLen ) {
        //if(countc >= fileLen)
        //    break;
		result[countc] = ((ch) ^ pwd[j]);
		++j;
		if(j >= j0)
            j=0;
        /*异或后写入*/
		++countc;
		ch = in_fname[countc];
    }
    return result;
}

AS3_Val doDecode(void* data,AS3_Val args)
{
	AS3_Val input = NULL;
	int in_len;
	char * ar;
	AS3_ArrayValue(args,"AS3ValType,IntType",&input,&in_len);
	ar = (char*)malloc(in_len);

	AS3_ByteArray_readBytes((void*)ar,input,in_len);

	char * decryptData;
	char * key="kael";
	decryptData = dofileEx(ar, key, in_len);
	//make a new as3 byteArray var
	AS3_Val baNS = AS3_String("flash.utils");
	AS3_Val baClass = AS3_NSGetS(baNS, "ByteArray");
	AS3_Val emptyParams = AS3_Array("");
	AS3_Val byteArray2 = AS3_New(baClass, emptyParams);
	
	AS3_ByteArray_writeBytes(byteArray2, decryptData, in_len);
	return byteArray2;
}

int main(int argc, char *argv[])
{
     // regular function
	 AS3_Val doAesVal = AS3_Function(NULL, doAes);
	 
	 // async function
	 AS3_Val doUnAesVal = AS3_Function(NULL, doUnAes);
	 
	 AS3_Val doDecodeVal = AS3_Function(NULL,doDecode);

	 // construct an object that holds refereces to the 2 functions
	 AS3_Val result = AS3_Object("doAes: AS3ValType, doUnAes: AS3ValType,doDecode:AS3ValType",
	 doAesVal, doUnAesVal,doDecodeVal);

	 AS3_Release(doAesVal);
	 AS3_Release(doUnAesVal);
	 AS3_Release(doDecodeVal);

	 // notify that we initialized -- THIS DOES NOT RETURN!
	 AS3_LibInit(result);

	 return 0;
}

