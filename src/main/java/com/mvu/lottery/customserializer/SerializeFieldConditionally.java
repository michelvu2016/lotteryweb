package com.mvu.lottery.customserializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@Component
public class SerializeFieldConditionally {

	private PropertyFilter propFilter;
	private IFieldSerializerValueApprover approver;
	
	@Autowired
	public SerializeFieldConditionally(IFieldSerializerValueApprover approver) {
		this.approver = approver;
		SerializeFieldConditionally thisObject = this;
		this.propFilter =  new SimpleBeanPropertyFilter() {
			@Override
			public void serializeAsField(Object pojo, JsonGenerator gen, SerializerProvider prov, PropertyWriter writer)
					throws Exception {
				
				if (include(writer)) {
					if(thisObject.approver.approve(pojo, writer.getName())) {
				     	writer.serializeAsField(pojo, gen, prov);
						return;
					}
				} else if(!gen.canOmitFields()) {
					writer.serializeAsField(pojo, gen, prov);
				}
				
			}

			@Override
			protected boolean include(BeanPropertyWriter writer) {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			protected boolean include(PropertyWriter writer) {
				// TODO Auto-generated method stub
				return true;
			}
		};
		
		
		
	}
	
	
	public String serializePojo(Object pojo, String filterName) throws RuntimeException, JsonProcessingException {
		FilterProvider filters = new SimpleFilterProvider().addFilter(filterName, this.propFilter);
		
		ObjectMapper mapper = new ObjectMapper();
		
		String pojoAsString = mapper.writer(filters).writeValueAsString(pojo);
		
		System.out.println(">>>Serialized:"+pojoAsString);
		
		return pojoAsString;
		
	}
	
	

	

}
