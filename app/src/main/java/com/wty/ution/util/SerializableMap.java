package com.wty.ution.util;

import java.io.Serializable;
import java.util.Map;

public class SerializableMap implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Map<String, ? extends Object> map;
	
	public SerializableMap(Map<String,? extends Object> map) {
		this.map = map;
	}
	
    public Map<String,? extends Object> getMap()  
    {  
        return map;  
    }  
} 