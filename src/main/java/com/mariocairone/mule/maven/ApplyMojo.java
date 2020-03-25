package com.mariocairone.mule.maven;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.crypto.DefaultSettingsDecryptionRequest;
import org.apache.maven.settings.crypto.SettingsDecrypter;

import com.mariocairone.mule.anypoint.service.AnypointService;
import com.mariocairone.mule.anypoint.service.AnypointServiceImpl;
import com.mariocairone.mule.anypoint.service.exception.AnypointServiceException;

@Mojo(name = "apply" )
public class ApplyMojo extends AbstractMojo  {
	
	@Component
	protected Settings settings;

	@Component
	protected SettingsDecrypter decrypter;
	
	@Parameter(required = false, property = "maven.server")
	protected String server;

	@Parameter(property = "anypoint.username")
	protected String username;

	@Parameter(property = "anypoint.password")
	protected String password;
	
	@Parameter(property = "anypoint.org", required = true)
	protected String organization;

	@Parameter(property = "anypoint.env", required = true)
	protected String environment;

	@Parameter(property = "anypoint.config", required = true)
	private File configuration;

	@Parameter(property = "anypoint.skip", defaultValue="false")
	protected Boolean skip;	

	private Log log = getLog();
	
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {

		if(skip) {
		 log.info("Skipping execution: skip=" + skip);
		 return;
	   } 
	   
	   configureCredentials();
	   
	   AnypointService service = new AnypointServiceImpl(organization, environment,username,password);
	   service.apply(configuration);

	}

	public void run() throws MojoExecutionException {
		
		if(configuration == null || !configuration.exists()) {
			log.info("Configuration File "+ configuration.toString() +" Not found! Skipping execution");
			return;
		}
		
		AnypointService service = new AnypointServiceImpl(organization, environment,username,password);
		
		try {
			
			service.apply(configuration);
			
		} catch (AnypointServiceException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		} 
		
		
	}
	
	
	private void configureCredentials() throws MojoExecutionException {
		
	    if (server != null) {
		      Server serverObject = this.settings.getServer(server);
		      if (serverObject == null) {
		    	  getLog().error("Server [" + server + "] not found in settings file.");
		        throw new MojoExecutionException("Server [" + server + "] not found in settings file.");
		      }
		      // Decrypting Maven server, in case of plain text passwords returns the same
		      serverObject = decrypter.decrypt(new DefaultSettingsDecryptionRequest(serverObject)).getServer();
		      if (isNotEmpty(username) || isNotEmpty(password)) {
		    	  getLog().warn("Both server and credentials are configured. Using plugin configuration credentials.");
		      } else {
		    	  username = serverObject.getUsername();
		    	  password = serverObject.getPassword();
		      }
		    }
	}
	
	protected boolean isNotEmpty(String string ) {
		
		if(string == null)
			return false;
		
		return !string.isEmpty();
		
	}
}
