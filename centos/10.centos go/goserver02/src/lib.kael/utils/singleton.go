package utils

import (

)

type single struct {
        O *interface{};
}

var instance *single = nil

func GetInstance() *single{
	 if instance == nil {
                instance = new(single);
     }
     return instance;
}
