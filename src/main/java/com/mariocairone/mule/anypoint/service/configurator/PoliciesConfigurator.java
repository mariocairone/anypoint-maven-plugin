package com.mariocairone.mule.anypoint.service.configurator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jsoniter.ValueType;
import com.jsoniter.any.Any;
import static com.mariocairone.mule.anypoint.service.utils.AnyUtils.*;
import static com.mariocairone.mule.anypoint.service.utils.Console.*;

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
	public Any delete(Integer apiId, Any config) {
		Any result = client.removePolicy(orgName, envName, apiId, config.toInt("id"));

		print("INFO",String.format("\tDeleted Policy: %s", getKey(config).toString()));
		
		return result;
	}

	@Override
	public Any create(Integer apiId, Any config) {
		
		Any result = client.applyPolicy(orgName, envName, apiId, config);
		
		print("INFO",String.format("\tApplied Policy: %s", getKey(config).toString()));

		return result;
	}

	@Override
	public Any update(Integer apiId, Any resource, Any config) {
		Integer policyId = resource.toInt("id");
		config.asMap().put("id", Any.wrap(policyId));
		
		Any result = client.editPolicy(orgName, envName, apiId, policyId,config);
		
		print("INFO",String.format("\tUpdated Policy: %s", getKey(config).toString()));
		
		return result;
	}

	@Override
	public String getKeyName(Any config) {	
		Any assetId = config.get('*', "assetId"); 
		String name = assetId.valueType().equals(ValueType.INVALID) ? "policyTemplateId" : "assetId" ;
		return name;
	}
	
	@Override	
	protected void afterHook(Integer apiId, Any configuredPolicies) {
		
	
		Any assetId = configuredPolicies.get('*', "assetId"); 
		final String name = assetId.valueType().equals(ValueType.INVALID) ? "policyTemplateId" : "assetId" ;
	
		Any appliedPolicies = findAll(apiId);
		
		List<Any> orderedPolicies = new ArrayList<>();
		
		boolean isWrongOrder = false;
		int index = 1;
		for(Any policy : configuredPolicies) {
		
			
			Any appliedPolicy = appliedPolicies
										.asList()
											.stream()
												.filter(appliedpolicy -> appliedpolicy.toString(name).equals(policy.toString(name)))
												 .findFirst().get();
			
			Integer id = appliedPolicy.toInt("id");
			Any anyOrder = appliedPolicy.get("order");
			
			if(!isPresent(anyOrder) && index == 1) {
				index +=1;
				continue;
			}
			
			Integer order = anyOrder.toInt();
			
			isWrongOrder = isWrongOrder || !order.equals(index);
			
			Map<String,Object> newOrder  = new HashMap<String, Object>();
			newOrder.put("id", id);
			newOrder.put("order",index);
			
			orderedPolicies.add(Any.wrap(newOrder));
			
			index++;
			
		}
		if(isWrongOrder) {
			
			client.patchPolicies(orgName, envName, apiId, orderedPolicies);
			print("INFO","\tReordered policies");
			
		}	
		
		print("INFO","Policies are Updated");

	}

}
