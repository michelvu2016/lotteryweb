package com.mvu.lottery.data.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mvu.lottery.constant.LotteryConstants;

@Entity
@Table(name="authority")
public class Authority implements LotteryConstants{

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Authority other = (Authority) obj;
		
		if (name != other.name)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Authority [id=" + id + ", name=" + name + ", getId()=" + getId() + ", getName()=" + getName()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}



	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	private AuthorityType name;
	
	
	
	public Authority() {
		// TODO Auto-generated constructor stub
	}

	public Authority(AuthorityType authType) {
		this.name = authType;
	}

	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public AuthorityType getName() {
		return name;
	}



	public void setName(AuthorityType name) {
		this.name = name;
	}

	
	
}
