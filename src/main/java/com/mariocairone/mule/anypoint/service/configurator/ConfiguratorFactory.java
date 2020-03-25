package com.mariocairone.mule.anypoint.service.configurator;

public class ConfiguratorFactory {

	
	private String username;
	private String password;
	private String orgName;
	private String envName;
	
	
	public ConfiguratorFactory(String orgName, String envName,String username, String password) {
		super();
		this.username = username;
		this.password = password;
		this.orgName = orgName;
		this.envName = envName;
	}


	public Configurator createConfigurator(String resource) {
		
		if(resource.equals("alerts"))
			return new AlertsConfigurator(orgName, envName, username, password);
		
		if(resource.equals("tiers"))
			return new TiersConfigurator(orgName, envName, username, password);
		
		if(resource.equals("policies"))
			return new PoliciesConfigurator(orgName, envName, username, password);

		if(resource.equals("properties"))
			return new PropertiesConfigurator(orgName, envName, username, password);		

		if(resource.equals("contracts"))
			return new ContractsConfigurator(orgName, envName, username, password);			
		
		throw new IllegalArgumentException(String.format("Configurator not supported for resource: %s", resource));

	}
}
