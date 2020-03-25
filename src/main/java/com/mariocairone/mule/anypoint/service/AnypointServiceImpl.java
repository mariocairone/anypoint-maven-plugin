package com.mariocairone.mule.anypoint.service;

import static com.mariocairone.mule.anypoint.service.utils.AnyUtils.*;
import static com.mariocairone.mule.anypoint.service.utils.Console.print;
import static com.mariocairone.mule.anypoint.service.utils.FileUtils.readFile;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.yaml.snakeyaml.Yaml;

import com.jsoniter.any.Any;
import com.mariocairone.mule.anypoint.service.configurator.Configurator;
import com.mariocairone.mule.anypoint.service.configurator.ConfiguratorFactory;
import com.mariocairone.mule.anypoint.service.exception.ConfigurationException;


public class AnypointServiceImpl implements AnypointService {

	private static final String SCHEMA_FILE = "apimanager-schema.json";
	
	
	protected String orgName;
	protected String envName;

	private ConfiguratorFactory factory;
	
	public AnypointServiceImpl(String organizationName,String environment,String username, String password) {
		super();
		this.orgName = organizationName;
		this.envName =environment;
		this.factory = new ConfiguratorFactory(orgName, envName,username, password);
	}


	@Override
	public void apply(File configFile)  {
		

		String configuration = readFile(configFile);
		
		Any config = parseConfiguration(configuration);
		
		
		Optional<Any> envConfig = config.get("environment")
							.asList()
								.stream()
									.filter(env -> env.get("name").toString().equals(envName))
										.findFirst();
		
		if(!envConfig.isPresent()) {
			print("INFO",String.format("Configuration not found for environment %s", envName));
			return;
		}
		
		Any environment = config.get("environment")
							.asList()
								.stream()
									.filter(env -> env.get("name").toString().equals(envName))
										.findFirst()
											.get();
		
		Integer apiId = environment
							.get("api","id")
								.toInt();

	
		Any apiProperties = environment.get("api","properties");
		if(isDefined(apiProperties)) {
			print("INFO","Processing Properties");
			Configurator configurator = factory.createConfigurator("properties");
			configurator.configure(apiId,apiProperties);
		} 
		
		Any apiAlerts = environment.get("api","alerts");
		if(isDefined(apiAlerts)) {
			print("INFO","Processing Alerts");
			Configurator configurator = factory.createConfigurator("alerts");
			configurator.configure(apiId,apiAlerts);
		} else {
			print("INFO","Alerts not defined - Skip");
		}
		
		Any apiTiers = environment.get("api","tiers");
		if(isDefined(apiTiers)) {
			print("INFO","Processing Tiers");
			Configurator configurator = factory.createConfigurator("tiers");
			configurator.configure(apiId,apiTiers);
		} else {
			print("INFO","Tiers not defined - Skip");
		}
		
		Any apiPolicies = environment.get("api","policies");
		if(isDefined(apiPolicies)) {
			print("INFO","Processing Policies");
			Configurator configurator = factory.createConfigurator("policies");
			configurator.configure(apiId,apiPolicies);
		} else {
			print("INFO","Policies not defined - Skip");
		}
		
		Any apiContracts = environment.get("api","contracts");
		if(isDefined(apiContracts)) {
			print("INFO","Processing Contracts");
			Configurator configurator = factory.createConfigurator("contracts");
			configurator.configure(apiId,apiContracts);
		} else {
			print("INFO","Contracts not defined - Skip");
		}
		
	}
	
	
	public Any parseConfiguration(String file)  {

		if(file == null)
			return Any.wrapNull();
		
		Yaml yaml = new Yaml();

		Map<String, Object> obj = yaml.load(file);
	
		Any config = Any.wrap(obj);
		
		if(!isPresent(config))
			return Any.wrapNull();
			
		JSONObject jsonSchema = new JSONObject(
			      new JSONTokener(getClass()
			  			.getClassLoader().getResourceAsStream(SCHEMA_FILE)));
		
		JSONObject jsonSubject = new JSONObject(new JSONTokener(config.toString()));	     
	    Schema schema = SchemaLoader.load(jsonSchema);
	    
	    try {
	    schema.validate(jsonSubject);
	    } catch (ValidationException e) {
	    	
	    	final StringBuilder builder = new StringBuilder();
	    	
	    	builder.append("The configuration file is invalid or malformed.").append("\n\t");
	    	
	    	List<ValidationException> errors = e.getCausingExceptions();
	    	if(errors.size() > 0 )
		    	e.getCausingExceptions().stream()
		          .map(ValidationException::getMessage)
		          .forEach(message -> builder.append(message).append("\n\t"));
	    	else
	    		builder.append(e.getMessage()).append("\n\t");
	    	
	    	
	    	print("ERROR",  builder.toString());
	    	
	    	throw new ConfigurationException(builder.toString());
		}
		
		return config.get("apimanager");
	}


	
}
