package com.mvu.lottery.customserializer;

public interface IFieldSerializerValueApprover {
	public String filterName();
	public boolean approve(Object pojo, String fieldName);
}
