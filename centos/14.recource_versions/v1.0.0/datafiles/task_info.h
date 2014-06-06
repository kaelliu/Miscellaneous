#ifndef __task_info__h__
#define __task_info__h__
#include<string>
#include<stdlib.h>
using namespace std;
struct task_info{
	int id;
	std::string taskname;
	float taskneed1;
	bool taskneed2;
	void parse_self_generated(const char* data){
		std::string value=data;
		size_t offset = 0;
		size_t end = 0;
		end = value.find(';', offset);
		string vals0(value,offset,end-offset);
		id=atoi(vals0.c_str());
		offset = end + 1;
		end = value.find(';', offset);
		string vals1(value,offset,end-offset);
		taskname.assign(value,offset,end-offset);
		offset = end + 1;
		end = value.find(';', offset);
		string vals2(value,offset,end-offset);
		taskneed1=atof(vals2.c_str());
		offset = end + 1;
		end = value.find(';', offset);
		string vals3(value,offset,end-offset);
		taskneed2=atoi(vals3.c_str());
		offset = end + 1;
	}
	void parse_self_generated_ex(std::string& value){
		size_t offset = 0;
		size_t end = 0;
		end = value.find(';', offset);
		string vals0(value,offset,end-offset);
		id=atoi(vals0.c_str());
		offset = end + 1;
		end = value.find(';', offset);
		string vals1(value,offset,end-offset);
		taskname.assign(value,offset,end-offset);
		offset = end + 1;
		end = value.find(';', offset);
		string vals2(value,offset,end-offset);
		taskneed1=atof(vals2.c_str());
		offset = end + 1;
		end = value.find(';', offset);
		string vals3(value,offset,end-offset);
		taskneed2=atoi(vals3.c_str());
		offset = end + 1;
	}
};
#endif