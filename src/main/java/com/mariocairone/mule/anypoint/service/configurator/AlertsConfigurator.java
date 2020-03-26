package com.mariocairone.mule.anypoint.service.configurator;

import static com.mariocairone.mule.anypoint.service.utils.Console.print;

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
	public Any delete(Integer apiId, Any config) {
		
		Any result = client.deleteAlert(orgName, envName, apiId, config.toString("id"));
		
		print("INFO",String.format("\tDeleted Alert: %s", getKey(config).toString()));

		return result;
	}

	@Override
	public Any create(Integer apiId, Any config) {
		
		Any result = client.createAlert(orgName, envName, apiId, config);
		
		print("INFO",String.format("\tCreated Alert: %s", getKey(config).toString()));
		
		return result;
	}

	@Override
	public Any update(Integer apiId, Any resource, Any config) {
		Any result = client.editAlert(orgName, envName, apiId, resource.toString("id"),config);
		
		print("INFO",String.format("\tUpdated Alert: %s", getKey(config).toString()));

		return result;
	}

	@Override
	public String getKeyName(Any config) {
		return "name";
	}
	
	@Override
	protected void afterHook(Integer apiId, Any config) {
		print("INFO","Alerts are Updated");
	}

}
