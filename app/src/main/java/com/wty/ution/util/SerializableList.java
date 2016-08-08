package com.wty.ution.util;

import java.io.Serializable;
import java.util.List;

public class SerializableList implements Serializable {

	private static final long serialVersionUID = 1L;
	private List list;

	public SerializableList(List list) {
		this.list= list;
	}
	
    public List getList()
    {  
        return list;
    }  
} 