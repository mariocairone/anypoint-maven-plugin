package com.mariocairone.mule.anypoint.service.configurator;

import static com.mariocairone.mule.anypoint.service.utils.Console.*;

import com.jsoniter.any.Any;
import com.mariocairone.mule.anypoint.client.AnypointClient;
import com.mariocairone.mule.anypoint.client.AnypointClientImpl;

public class PropertiesConfigurator implements Configurator {

	private String orgName;
	private String envName;

	
	private AnypointClient client;
	
	public PropertiesConfigurator(String organizationName, String environment, String username, String password) {
		this.client = new AnypointClientImpl(username,password);
		this.orgName = organizationName;
		this.envName =environment;
	}

	@Override
	public void configure(Integer apiId, Any config) {
		
		Any api = client.getApi(orgName, envName, apiId, null);
		
		if(!isUpdated(config, api)) {
			client.editApi(orgName, envName, apiId, config);
			print("INFO","\tUPDATED: properties");
		}
	}

}
