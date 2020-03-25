package com.mariocairone.mule.anypoint.service.configurator;

import com.jsoniter.any.Any;

public class AlertsConfigurator extends AbstractConfigurator {

	public AlertsConfigurator(String organizationName, String environment, String username, String password) {
		super(organizationName, environment, username, password);
	}

	@Override
	public Any findAll(Integer apiId) {
		return client.getAlerts(orgName, envName, apiId);
	}

	@Override
	public Any delete(Integer apiId, Any resource) {
		return client.deleteAlert(orgName, envName, apiId, resource.toString("id"));
	}

	@Override
	public Any create(Integer apiId, Any config) {
		return client.createAlert(orgName, envName, apiId, config);
	}

	@Override
	public Any update(Integer apiId, Any resource, Any config) {
		return 	client.editAlert(orgName, envName, apiId, resource.toString("id"),config);
	}

	@Override
	public String getKeyName(Any config) {
		return "name";
	}

}
