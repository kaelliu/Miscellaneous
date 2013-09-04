// ======================================================================================
// File         : something.c
// Author       : Liu ZiTao 
// Last Change  : 4/10/2012 | 9:30:24 AM | Tuesday,April
// Description  : to encrypt the flash from alchemy
// ======================================================================================

///////////////////////////////////////////////////////////////////////////////
// includings
///////////////////////////////////////////////////////////////////////////////
#include"AS3.h"

///////////////////////////////////////////////////////////////////////////////
// global varibles
///////////////////////////////////////////////////////////////////////////////
int r=0;

///////////////////////////////////////////////////////////////////////////////
// functions
///////////////////////////////////////////////////////////////////////////////
AS3_Val initialize(void * self,AS3_Val args)
{
	AS3_Val spNS = AS3_String("flash.display");
	AS3_Val spClass = AS3_NSGetS(spNS,"Sprite");
	AS3_Val emptyParams = AS3_Array("");
	AS3_Val sp = AS3_New(spClass,emptyParams);
	AS3_Val gpc = AS3_GetS(sp,"graphics");
	AS3_CallTS("beginFill",gpc,"IntType",0xffffff*rand());
	AS3_CallTS("drawRect",gpc,"IntType,IntType,IntType,IntType",-100,-100,200,200);
	r+=5;
	AS3_Val ro=AS3_Int(r);
	AS3_SetS(sp,"rotation",ro);
	AS3_Release(ro);

	AS3_Val xy=AS3_Int(150);
	AS3_SetS(sp,"x",xy);
	AS3_SetS(sp,"y",xy);
	AS3_Release(xy);
	return sp;
}

int main()
{
	AS3_Val initializeMethod = AS3_Function(NULL,initialize);
	AS3_Val result = AS3_Object("initialize:AS3ValType",initializeMethod);
	AS3_Release(initializeMethod);
	AS3_LibInit(result);
	return 0;
}
