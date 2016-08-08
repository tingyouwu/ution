package com.wty.ution.data.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DatabaseField {
	
	FieldType Type() default FieldType.VARCHAR;
	
	boolean primaryKey() default false;
	
	String fieldName() default "";
	
	enum FieldType{
		VARCHAR,INT,REAL
	}
}