package com.mariocairone.mule.anypoint.service.configurator;

import java.util.HashMap;
import java.util.Map;

import com.jsoniter.ValueType;
import com.jsoniter.any.Any;

public class PoliciesConfigurator extends AbstractConfigurator {

	public PoliciesConfigurator(String organizationName, String environment, String username, String password) {
		super(organizationName, environment, username, password);
	}

	@Override
	public Any findAll(Integer apiId) {
		Map<String,Object> params = new HashMap<>();
		params.put("fullInfo", false);
		return client.getPolicies(orgName, envName, apiId, params);
	}

	@Override
	public Any delete(Integer apiId, Any resource) {
		return client.removePolicy(orgName, envName, apiId, resource.toInt("id"));
	}

	@Override
	public Any create(Integer apiId, Any config) {
		return client.applyPolicy(orgName, envName, apiId, config);
	}

	@Override
	public Any update(Integer apiId, Any resource, Any config) {
		Integer policyId = resource.toInt("id");
		config.asMap().put("id", Any.wrap(policyId));
		return client.editPolicy(orgName, envName, apiId, policyId,config);
	}

	@Override
	public String getKeyName(Any config) {	
		Any assetId = config.get('*', "assetId"); 
		String name = assetId.valueType().equals(ValueType.INVALID) ? "policyTemplateId" : "assetId" ;
		return name;
	}

}
