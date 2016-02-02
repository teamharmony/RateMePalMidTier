package prj.resources.mgmt.services;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class NotificationsUtil {
	
	  public static final String PUSHWOOSH_SERVICE_BASE_URL = "https://cp.pushwoosh.com/json/1.3/";
	  private static final String AUTH_TOKEN = "YOUR_AUTH_TOKEN";
	  private static final String APPLICATION_CODE = "PW_APPLICATION_CODE";
	 
	  
	  public static void sendNotification(String content, List<String> deviceIds) {
		  if(deviceIds != null && deviceIds.size() <= 0) {
			  return;
		  }
		  
		  String method = "createMessage";
	        URL url = null;
			try {
				url = new URL(PUSHWOOSH_SERVICE_BASE_URL + method);
			} catch (MalformedURLException e) {
				System.out.println("malformed url exception for push woosh request");
			}
	 
	        ObjectMapper mapper = new ObjectMapper();
    	              
	        ArrayNode devices = mapper.createArrayNode();
	        for(String d : deviceIds) {
	        	devices.add(d);
	        }
	        	        
	        JsonNode node =  mapper.createObjectNode()
	        		.put("send_date", "now")
	        		.put("content", content)
	        		.put("devices", devices);
	        
	        ArrayNode notificationsArray = mapper.createArrayNode().add(node);
	        
	        JsonNode reqObject = mapper.createObjectNode()
	        		.put("application", APPLICATION_CODE)
	        		.put("auth", AUTH_TOKEN)
	        		.put("notifications", notificationsArray);
	 
	        
	        JsonNode mainRequest = mapper.createObjectNode().put("request", reqObject);
	        String response = SendServerRequest.sendJSONRequest(url, mainRequest.toString());
	 
	        System.out.println("Response is: " + response);		  
	  }

}


class SendServerRequest
{
    static String sendJSONRequest(URL url, String request)
    {
        HttpURLConnection connection = null;
        try
        {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoInput(true);
            connection.setDoOutput(true);
 
            DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
            writer.write(request.getBytes("UTF-8"));
            writer.flush();
            writer.close();
 
            return parseResponse(connection);
        }
        catch (Exception e)
        {
            System.out.println("An error occurred: " + e.getMessage());
            return null;
        }
        finally
        {
            if (connection != null)
            {
                connection.disconnect();
            }
        }
    }
 
    static String parseResponse(HttpURLConnection connection) throws IOException
    {
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
 
        ObjectMapper mapper = new ObjectMapper();
    	
        
        while ((line = reader.readLine()) != null)
        {
            response.append(line).append('\r');
        }
        reader.close();
 
        return response.toString();
    }
}