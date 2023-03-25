package com.example.demo.controller;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.net.URI;
import java.net.http.HttpClient;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.net.http.HttpRequest;
@RestController
@RequestMapping("/RapidAPI")
@ComponentScan
@CrossOrigin(origins ="*")
public class RapidApiController {
	
	@Value("${rapid.forest}")
	String forest;
	
	@Value("${rapid.summary}")
	String summary;
	
	@Value("${rapid.hourly}")
	String hourly;
	
	@Value("${rapid.host}")
	String host;
	
	@Value("${rapid.host.value}")
	String hostValue;
	
	@Value("${rapid.key}")
	String keyhost;
	
	@Value("${rapid.key.value}")
	String keyValue;
	
	@Value("${rapid.username}")
	String username;
	
	@Value("${rapid.password}")
	String password;
	
	@Autowired
	Environment env;
	
	@GetMapping("/summary/{location}")
	public ResponseEntity<String> getSummaryByLocationName(@RequestHeader Map<String , String> auth ,   @PathVariable("location") String location ) throws IOException {

		String key = "" ;
		String pwd ="";
		for(Map.Entry m : auth.entrySet()){    
		     key = (String) m.getKey();
		     pwd = (String) m.getValue();    
		    break;
		   }  
		if(!username.equalsIgnoreCase(key)||!password.equalsIgnoreCase(pwd)) {
			return new ResponseEntity<String>("username or password is wrong",HttpStatus.UNAUTHORIZED);
		}
		
		String url=forest+location+summary;
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.header(host, hostValue)
				.header(keyhost, keyValue)
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
		HttpResponse<String> response = null;
		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<String>(response.body(),statusCodeReturn(response.statusCode()));
	}

	@GetMapping("/Forest/{location}")
	public ResponseEntity<String> getForestByLocationName(@RequestHeader Map<String , String> auth ,@PathVariable("location") String location) throws IOException {
		String key = "" ;
		String pwd ="";
		for(Map.Entry m : auth.entrySet()){    
		     key = (String) m.getKey();
		     pwd = (String) m.getValue();    
		    break;
		   }  
		if(!username.equalsIgnoreCase(key)||!password.equalsIgnoreCase(pwd)) {
			return new ResponseEntity<String>("username or password is wrong",HttpStatus.UNAUTHORIZED);
		}
		
		String url=forest+location+hourly;
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.header(host, hostValue)
				.header(keyhost, keyValue)
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
		HttpResponse<String> response = null;
		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<String>(response.body(),statusCodeReturn(response.statusCode()));
	}

	private HttpStatusCode statusCodeReturn(int statusCode) {
		switch(statusCode) {
		case 200 : return HttpStatus.OK;
		case 401 : return HttpStatus.UNAUTHORIZED;
		case 403 : return HttpStatus.FORBIDDEN;
		case 404 : return HttpStatus.NOT_FOUND;
		case 400 : return HttpStatus.BAD_REQUEST;
		case 500 : return HttpStatus.INTERNAL_SERVER_ERROR;
		default : return HttpStatus.BAD_GATEWAY;
		}
	}
}
