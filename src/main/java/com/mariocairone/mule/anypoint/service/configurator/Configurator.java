package com.mariocairone.mule.anypoint.service.configurator;

import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;

import com.jsoniter.any.Any;

public interface Configurator {

	public void configure(Integer apiId, Any config);
	
	
	public default boolean isUpdated(Any config, Any resource) {

		try {
			JSONCompareResult result = JSONCompare.compareJSON(config.toString(), resource.toString(), JSONCompareMode.LENIENT);
			return result.passed();
		} catch (JSONException e) {
			return false;
		}		
	}
}
