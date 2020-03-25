package com.mariocairone.mule.anypoint.service.utils;

import com.jsoniter.ValueType;
import com.jsoniter.any.Any;

public class AnyUtils {

	
	public static boolean isPresent(Any any) {
		
		if(any.valueType().equals(ValueType.NULL))
			return false;
		
		return !any.valueType().equals(ValueType.INVALID) ;
	}
	
	public static boolean isDefined(Any any) {
		
		return !any.valueType().equals(ValueType.INVALID);
	}
}
