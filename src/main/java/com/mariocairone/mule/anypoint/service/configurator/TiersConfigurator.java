package com.mariocairone.mule.anypoint.service.configurator;

import static com.mariocairone.mule.anypoint.service.utils.Console.print;

import com.jsoniter.any.Any;

public class TiersConfigurator extends AbstractConfigurator {

	public TiersConfigurator(String organizationName, String environment, String username, String password) {
		super(organizationName, environment, username, password);
	}

	@Override
	public Any findAll(Integer apiId) {
		return client.getTiers(orgName, envName, apiId, null).get("tiers");
	}

	@Override
	public Any delete(Integer apiId, Any config) {
		Any result = client.removeTier(orgName, envName, apiId, config.toInt("id"));

		print("INFO",String.format("\tDeleted Tier: %s", getKey(config).toString()));
		
		return result;
	}

	@Override
	public Any create(Integer apiId, Any config) {
		Any result = client.createTier(orgName, envName, apiId, config);
		
		print("INFO",String.format("\tCreated Tier: %s", getKey(config).toString()));
		
		return result;
	}

	@Override
	public Any update(Integer apiId, Any resource, Any config) {
		
		Any result = client.editTier(orgName, envName, apiId, resource.toInt("id"),config);

		print("INFO",String.format("\tUpdated Tier: %s", getKey(config).toString()));
		
		return result;
		
	}

	@Override
	public String getKeyName(Any config) {
		return "name";
	}

	@Override
	protected void afterHook(Integer apiId, Any config) {
		print("INFO","Tiers are Updated");
	}
	
}
