package com.kael.hibernatejpa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
//@Table( name="actor")
public class Actor extends ModelBase{
	// Fields

	private Long id;
	private String name;
	private Byte level;
	private Integer exp;

	// Constructors

	/** default constructor */
	public Actor() {
	}

	// Property accessors
	@Id  
	@GeneratedValue(strategy = GenerationType.IDENTITY)  
    @Column(name = "id", unique = true, nullable = false)  
	public Long getId() {
		return this.id;
		// id should not set to unsigned
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(unique = false, length=32)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Integer getExp() {
		return this.exp;
	}

	public void setExp(Integer exp) {
		this.exp = exp;
	}

	public Byte getLevel() {
		return level;
	}

	public void setLevel(Byte level) {
		this.level = level;
	}
}
