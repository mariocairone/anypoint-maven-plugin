package com.mariocairone.mule.anypoint.service.configurator;

import com.jsoniter.any.Any;

public interface Repository {
		
	public Any findAll(Integer apiId);	
	public Any delete(Integer apiId, Any resource);
	public Any create(Integer apiId, Any config);
	public Any update(Integer apiId,Any resource, Any config);
	
}
