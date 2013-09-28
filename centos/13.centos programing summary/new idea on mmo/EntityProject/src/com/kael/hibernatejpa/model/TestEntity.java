package com.kael.hibernatejpa.model;

import javax.persistence.Column;  
import javax.persistence.Entity;  
import javax.persistence.GeneratedValue;  
import javax.persistence.GenerationType;  
import javax.persistence.Id;  
 
@Entity  
public class TestEntity {  
    private Integer no1;  
    private String str1;  
 
    public TestEntity() {  
    }  
 
    @Id  
	@GeneratedValue(strategy = GenerationType.AUTO)  
    @Column(name = "id", unique = true, nullable = false)  
    public Integer getNo1() {  
        return no1;  
    }  
 
    public void setNo1(Integer no1) {  
        this.no1 = no1;  
    }  
 
    @Column(nullable = false, length = 12)  
    public String getStr1() {  
        return str1;  
    }  
 
    public void setStr1(String str1) {  
        this.str1 = str1;  
    }  
 
}  
