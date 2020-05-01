package com.mariocairone.mule.anypoint.service.configurator;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.jsoniter.any.Any;
import com.mariocairone.mule.anypoint.client.AnypointClient;
import com.mariocairone.mule.anypoint.client.AnypointClientImpl;
import com.mariocairone.mule.anypoint.service.utils.AnyUtils;

public abstract class AbstractConfigurator implements Repository,Configurator {

	protected String orgName;
	protected String envName;
		
	protected AnypointClient client;
	
	
	public AbstractConfigurator(String organizationName,String environment,String username, String password) {
		super();
		this.client = new AnypointClientImpl(username,password);
		this.orgName = organizationName;
		this.envName =environment;				
	}
	
	public void configure(Integer apiId, Any config) {
		
		beforeHook(apiId,config);
		
		Any resources = findAll(apiId);
		
		
		final Set<String> configurationKeys = getKeys(config)
													.asList()
														.stream()
															.map(key -> key.toString())
																.collect(Collectors.toSet());
		
		Collection<Any> toCreate = Collections.emptySet();
		Collection<Any> toDelete = Collections.emptySet();
		Collection<Any> toUpdate = Collections.emptySet();
		
			if(resources.size() > 0 ) {
			
			toDelete = ! AnyUtils.isPresent(config) ? resources.asList() : resources.asList().stream()
											.filter( resource ->  !configurationKeys.contains(getKey(resource).toString()))
												.collect(Collectors.toList());
		
			Set<String> resourcesKeys = getKeys(resources)
													.asList()
														.stream()
															.map(key -> key.toString())
																.collect(Collectors.toSet());
			if(AnyUtils.isPresent(config)) {
				Map<Boolean, List<Any>> partitions = config
														.asList()
															.stream()
																.collect(Collectors.partitioningBy(resourceConfig -> resourcesKeys.contains(getKey(resourceConfig).toString())));
				
				toCreate = partitions.get(Boolean.FALSE);
				toUpdate = partitions.get(Boolean.TRUE);
			}
		} else {
			if(AnyUtils.isPresent(config))
				toCreate = config.asList();
		}		
		
		
		//1 - delete all the resource that exist in the API Manager but not in the configuration defined in the configuration
		toDelete.forEach( resource -> {
			delete(apiId, resource);
		});
		
		// 2 - create all the resource that exist in the configuration but not in the API Manager
		toCreate.forEach( resourceConfig -> {
			create(apiId, resourceConfig);
		});
		
		// 3 - update all the resources that exists in both configuration and API Manager
		toUpdate.forEach( resourceConfig -> {
			
			Any currentResource = resources
									.asList()
										.stream()
											.filter(resource ->  getKey(resourceConfig).toString().equals(getKey(resource).toString()))
												.findFirst()
													.get();
			
			if(!isUpdated(resourceConfig,currentResource)) {
				
				update(apiId, currentResource,resourceConfig);				
			}
		});			

		afterHook(apiId,config);
		
	}
	
	public abstract String getKeyName(Any config);
	
	protected  Any getKeys(Any config) {
		Any any = config.get('*', getKeyName(config));
		return AnyUtils.isPresent(any) ? any : Any.wrap(new HashSet<>());
	};
	
	protected Any getKey(Any config) {
		Any any = config.get(getKeyName(config));
		return AnyUtils.isPresent(any) ? any : Any.wrapNull();
	} 
	
	
	protected void afterHook(Integer apiId, Any config) {}
	protected void beforeHook(Integer apiId, Any config) {}
	
}
