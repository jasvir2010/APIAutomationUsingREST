package com.qa.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.base.TestBase;
import com.qa.client.RestClient;
import com.qa.data.Users;
import com.qa.util.TestUtil;

public class PostAPITest extends TestBase
{
	public PostAPITest() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	TestBase testBase;
	String apiUrl;
	String serviceURL;
	String url;
	RestClient restClient;
	CloseableHttpResponse httpResponse;

	@BeforeMethod
	public void setUp() throws IOException
	{
		testBase = new TestBase(); 
		apiUrl = prop.getProperty("URL");
		serviceURL = prop.getProperty("serviceURL");
		//https://reqres.in/api/users
		url = apiUrl + serviceURL;
	}

	@Test
	public void postAPITest() throws ClientProtocolException, IOException
	{
		restClient = new RestClient();
		HashMap<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Content-Type", "application/json");
//		headerMap.put("username", "test@amazon.com");
//		headerMap.put("password", "test123");
//		headerMap.put("Auth Token", "12345");
		
		//jackson API (for Marshelling(Object to JSON) and UnMarshelling(JSON to Object):
		ObjectMapper mapper = new ObjectMapper();
		Users users = new Users("morpheus","leader"); //expected users object
		
		//object to json file
		mapper.writeValue(new File("D:/WorkSpace/restapi/src/main/java/com/qa/data/users.json"), users);
		
		//object to json in String
		String usersJsonString = mapper.writeValueAsString(users);
		System.out.println(usersJsonString);
		
		httpResponse = restClient.post(url, usersJsonString, headerMap); //call the API
		
		//validate response from API:
		//1. status code:
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		System.out.println("Status Code: " + statusCode);
		Assert.assertEquals(statusCode, testBase.RESPONSE_STATUS_CODE_201);
		
		//2. JsonString:
		String responseString = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
		JSONObject responseJson = new JSONObject(responseString);
		System.out.println("The response from API is: " + responseJson);
		
		//JSON to java object
		Users usersResObj = mapper.readValue(responseString, Users.class); //actual users object
		System.out.println(usersResObj);
		
		Assert.assertTrue(users.getName().equals(usersResObj.getName()));
		Assert.assertTrue(users.getJob().equals(usersResObj.getJob()));
		
		System.out.println(usersResObj.getId());
		System.out.println(usersResObj.getCreatedAt());
		
	}

}
