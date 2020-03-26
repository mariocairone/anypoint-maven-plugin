package com.mariocairone.mule.maven.service;

import java.io.File;

import org.junit.Test;

import com.mariocairone.mule.anypoint.service.AnypointServiceImpl;

public class ApplyTest {
	
	String USERNAME = System.getProperty("anypoint.username");
	String PASSWORD = System.getProperty("anypoint.password");
	String ORG = System.getProperty("anypoint.org");
	

		
	
	@Test
	public void applyTest() throws Exception {
		AnypointServiceImpl service = new AnypointServiceImpl(ORG,"Development",USERNAME, PASSWORD);
		service.apply(new File("src/test/resources/apimanager.yml"));
	}
	

}
