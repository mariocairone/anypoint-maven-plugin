package com.mariocairone.mule.anypoint.service.configurator;

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
	public Any delete(Integer apiId, Any resource) {
		return client.removeTier(orgName, envName, apiId, resource.toInt("id"));
	}

	@Override
	public Any create(Integer apiId, Any config) {
		return client.createTier(orgName, envName, apiId, config);
	}

	@Override
	public Any update(Integer apiId, Any resource, Any config) {
		return 	client.editTier(orgName, envName, apiId, resource.toInt("id"),config);
	}

	@Override
	public String getKeyName(Any config) {
		return "name";
	}

}
