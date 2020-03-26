package com.mariocairone.mule.maven.service;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.jsoniter.ValueType;
import com.jsoniter.any.Any;
import com.mariocairone.mule.anypoint.service.AnypointServiceImpl;
import com.mariocairone.mule.anypoint.service.exception.ConfigurationException;

public class ConfigurationTest {
	
	AnypointServiceImpl service = new AnypointServiceImpl("Org","ENV","USERNAME", "PASSWORD");
		
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void parseNull() throws Exception {
						
		Any config = service.parseConfiguration(null);
		
		assertTrue(config.valueType().equals(ValueType.NULL));
	}
	
	@Test
	public void parseEmpty() throws Exception {
					
		Any config = service.parseConfiguration("");
		
		assertTrue(config.valueType().equals(ValueType.NULL));
	}	
	
	@Test
	public void apimanager_is_mandatory() throws Exception {
		
		String errorMessage = "required key [apimanager] not found";
		
		expectedEx.expect(ConfigurationException.class);

		expectedEx.expectMessage(containsString(errorMessage));
		
		StringBuilder builder = new StringBuilder();
		builder.append("---\n")
			   .append("akey:\n");

		service.parseConfiguration(builder.toString());
				
		
	}

	@Test
	public void environment_is_mandatory() throws Exception {
		
		expectedEx.expect(ConfigurationException.class);
		String errorMessage = "required key [environment] not found";
		expectedEx.expectMessage(containsString(errorMessage));
		
		StringBuilder builder = new StringBuilder();
		builder.append("---\n")
			   .append("apimanager:\n")
			   .append(" akey:\n")
			   ;

		service.parseConfiguration(builder.toString());
		
	}	

	@Test
	public void environment_name_is_mandatory() throws Exception {
		
		expectedEx.expect(ConfigurationException.class);
		String errorMessage = "required key [name] not found";
		expectedEx.expectMessage(containsString(errorMessage));
		
		StringBuilder builder = new StringBuilder();
		builder.append("---\n")
			   .append("apimanager:\n")
		   	   .append(" environment:\n")
		   	   .append("  - api:\n")
		   	   .append("    id: 123456 \n");

		service.parseConfiguration(builder.toString());
		
	}

	@Test
	public void environment_api_is_mandatory() throws Exception {
		
		expectedEx.expect(ConfigurationException.class);
		String errorMessage = "required key [api] not found";
		expectedEx.expectMessage(containsString(errorMessage));
		
		StringBuilder builder = new StringBuilder();
		builder.append("---\n")
			   .append("apimanager:\n")
		   	   .append(" environment:\n")
		   	   .append("  - name: Development\n")	   	   
		   	   ;

		service.parseConfiguration(builder.toString());
		
	}
	
	@Test
	public void environment_api_id_is_mandatory() throws Exception {
		
		expectedEx.expect(ConfigurationException.class);
		String errorMessage = "required key [id] not found";
		expectedEx.expectMessage(containsString(errorMessage));
		
		StringBuilder builder = new StringBuilder();
		builder.append("---\n")
			   .append("apimanager:\n")
		   	   .append(" environment:\n")
		   	   .append("  - name: Development\n")
		   	   .append("    api:\n")
		   	   .append("     properties:\n")	
		   	   .append("      asserVersion: 1.0.4\n")			   	   
		   	   ;

		service.parseConfiguration(builder.toString());
		
	}	

	@Test
	public void environment_api_alerts_name_is_mandatory() throws Exception {
		
		expectedEx.expect(ConfigurationException.class);
		String errorMessage = "required key [name] not found";
		expectedEx.expectMessage(containsString(errorMessage));
		
		StringBuilder builder = new StringBuilder();
		builder.append("---\n")
			   .append("apimanager:\n")
		   	   .append(" environment:\n")
		   	   .append("  - name: Development\n")
		   	   .append("    api:\n")
		   	   .append("     id: 12345\n")
		   	   .append("     alerts:\n")	
		   	   .append("      - type: api-policy-violation\n")			   	   
		   	   ;

		service.parseConfiguration(builder.toString());
		
	}	

	@Test
	public void environment_api_tiers_name_is_mandatory() throws Exception {
		
		expectedEx.expect(ConfigurationException.class);
		String errorMessage = "required key [name] not found";
		expectedEx.expectMessage(containsString(errorMessage));
		
		StringBuilder builder = new StringBuilder();
		builder.append("---\n")
			   .append("apimanager:\n")
		   	   .append(" environment:\n")
		   	   .append("  - name: Development\n")
		   	   .append("    api:\n")
		   	   .append("     id: 12345\n")
		   	   .append("     tiers:\n")	
		   	   .append("      - description: Standard\n")			   	   
		   	   ;

		service.parseConfiguration(builder.toString());
		
	}	

	@Test
	public void environment_api_policies_configurationData_is_mandatory() throws Exception {
		
		expectedEx.expect(ConfigurationException.class);
		String errorMessage = "required key [configurationData] not found";
		expectedEx.expectMessage(containsString(errorMessage));
		
		StringBuilder builder = new StringBuilder();
		builder.append("---\n")
			   .append("apimanager:\n")
		   	   .append(" environment:\n")
		   	   .append("  - name: Development\n")
		   	   .append("    api:\n")
		   	   .append("     id: 12345\n")
		   	   .append("     policies:\n")	
		   	   .append("      - assetId: djdjuehiefhweu\n")			   	   
		   	   ;

		service.parseConfiguration(builder.toString());
		
	}

	@Test
	public void environment_api_contracts_application_is_mandatory() throws Exception {
		
		expectedEx.expect(ConfigurationException.class);
		String errorMessage = "required key [application] not found";
		expectedEx.expectMessage(containsString(errorMessage));
		
		StringBuilder builder = new StringBuilder();
		builder.append("---\n")
			   .append("apimanager:\n")
		   	   .append(" environment:\n")
		   	   .append("  - name: Development\n")
		   	   .append("    api:\n")
		   	   .append("     id: 12345\n")
		   	   .append("     contracts:\n")	
		   	   .append("      - status: APPROVED\n")			   	   
		   	   ;

		service.parseConfiguration(builder.toString());
		
	}	

	@Test
	public void environment_api_contracts_application_name_is_mandatory() throws Exception {
		
		expectedEx.expect(ConfigurationException.class);
		String errorMessage = "required key [name] not found";
		expectedEx.expectMessage(containsString(errorMessage));
		
		StringBuilder builder = new StringBuilder();
		builder.append("---\n")
			   .append("apimanager:\n")
		   	   .append(" environment:\n")
		   	   .append("  - name: Development\n")
		   	   .append("    api:\n")
		   	   .append("     id: 12345\n")
		   	   .append("     contracts:\n")	
		   	   .append("      - application: \n")
		   	   .append("         akey: a\n")
		   	   .append("        status: APPROVED\n")			   	   
		   	   ;

		service.parseConfiguration(builder.toString());
		
	}		
	@Test
	public void environment_api_contracts_status_is_mandatory() throws Exception {
		
		expectedEx.expect(ConfigurationException.class);
		String errorMessage = "required key [status] not found";
		expectedEx.expectMessage(containsString(errorMessage));
		
		StringBuilder builder = new StringBuilder();
		builder.append("---\n")
			   .append("apimanager:\n")
		   	   .append(" environment:\n")
		   	   .append("  - name: Development\n")
		   	   .append("    api:\n")
		   	   .append("     id: 12345\n")
		   	   .append("     contracts:\n")	
		   	   .append("      - application: \n")
		   	   .append("         name: Hello World\n")
		   	   ;

		service.parseConfiguration(builder.toString());
		
	}			
}
