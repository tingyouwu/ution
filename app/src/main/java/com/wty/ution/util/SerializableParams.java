package com.wty.ution.util;

import java.io.Serializable;

public class SerializableParams implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Object object;
	
	public SerializableParams(Object object) {
		this.object = object;
	}
	
    public Object get()  
    {  
        return object;  
    }  
} 