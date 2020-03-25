package com.mariocairone.mule.anypoint.service.configurator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import com.jsoniter.any.Any;
import com.mariocairone.mule.anypoint.service.utils.AnyUtils;

public class ContractsConfigurator extends AbstractConfigurator {

	public ContractsConfigurator(String organizationName, String environment, String username, String password) {
		super(organizationName, environment, username, password);
	}

	@Override
	public Any findAll(Integer apiId) {
		
		return client.getContracts(orgName, envName, apiId, null).get("contracts");
	}

	@Override
	public Any delete(Integer apiId, Any resource) {
		Integer contractId = resource.toInt("id");
		String status = resource.toString("status");
		if(status.equals("APPROVED"))
			client.revokeContract(orgName, envName, apiId, contractId);
				
		return client.deleteContract(orgName, envName, apiId, contractId);
		
	}

	@Override
	public Any create(Integer apiId, Any config) {
		
		String applicationName = config.toString("application","name");
		
		Map<String,Object> queryParams = new HashMap<>();
		
		queryParams.put("limit", 100);
		queryParams.put("targetAdminSite",true);
		
		Optional<Any> application = client.getClientApplications(orgName, queryParams).get("applications")
				.asList().stream().filter(app -> app.toString("name").equals(applicationName)).findFirst();
		
		if(application.isPresent()) {
			Any clientApplication = application.get();
			Integer applicationId = clientApplication.toInt("id");
			Integer tierId = null;
			

			
			Any tierName = config.get("tier","name");

			if(AnyUtils.isPresent(tierName)) {
				Any applicationTier = getTier(apiId, tierName.toString());
				
				if(AnyUtils.isPresent(applicationTier))
					tierId = applicationTier.toInt("id");			
				
			}
			
			Map<String,Object> body = new HashMap<String, Object>();
			body.put("applicationId", applicationId);
			body.put("partyId", "");
			body.put("partyName", "");
			body.put("acceptedTerms", true);
			body.put("requestedTierId", tierId);	
			
			Any contract = client.createContract(orgName, envName, apiId, body);
			
			if(config.toString("status").equals("APPROVED"))
				client.approveContract(orgName, envName, apiId, contract.toInt("id"));
			
			return contract;
		}
		
		return Any.wrapNull();
	}

	@Override
	public Any update(Integer apiId, Any resource, Any config) {
		String status = config.toString("status");
		Integer contractId = resource.toInt("id");
		if(!status.equals(resource.toString("status"))) {
			if(status.equals("APPROVED"))
				client.approveContract(orgName, envName, apiId, contractId);
			else if( status.equals("REVOKED")) 
				client.revokeContract(orgName, envName, apiId, contractId);
			
		}	
		
		Any tierName = config.get("tier","name");
		if(AnyUtils.isPresent(tierName)){
			
			Any existingTier = resource.get("tier","name");
			
				 
			if(AnyUtils.isPresent(existingTier) || !tierName.toString().equals(existingTier.toString())) {
				
				Any requestedTier = getTier(apiId, tierName.toString());
				
				
				Map<String,Object> body = new HashMap<>();
				body.put("tierId", requestedTier.toInt("id"));

				
				client.editContract(orgName, envName, apiId, contractId, body);
				
			}

			
		}
		
		
		return Any.wrapNull();
		
	}

	@Override
	public String getKeyName(Any config) {	
		return "name";
	}
	
	@Override
	public  Any getKeys(Any config) {
		
		Any any = config.get('*',"application", getKeyName(config));
		return AnyUtils.isPresent(any) ? any : Any.wrap(new HashSet<>());
		 
	};
	
	@Override
	public Any getKey(Any config) {
		Any any = config.get("application",getKeyName(config));
		return AnyUtils.isPresent(any)? any : Any.wrapNull();

	} 
	
	private Any getTier(Integer apiId,String name) {
		Any tiers = client.getTiers(orgName, envName, apiId, null).get("tiers");
		Any tier = getTier(tiers, name);
		
		if(AnyUtils.isPresent(tier)) {
			return tier;			
		}
		
		return Any.wrapNull();
	} 
	private Any getTier(Any tiers,String name) {
		Optional<Any> applicationTier = tiers
				.asList().stream().filter(tier -> tier.toString("name").equals(name)).findFirst();
		
		if(applicationTier.isPresent()) {
			return applicationTier.get();			
		}
		
		return Any.wrapNull();
	}
}
