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
	  private static final String USERNAME_TAG = "username";
	  
	  public static void sendNotification(String content, String[] users, String data) {
		    String method = "createMessage";
	        URL url = null;
			try {
				url = new URL(PUSHWOOSH_SERVICE_BASE_URL + method);
			} catch (MalformedURLException e) {
				System.out.println("malformed url exception for push woosh request");
			}
	 
	        ObjectMapper mapper = new ObjectMapper();
    	    
	        
	        ArrayNode conditions =  mapper.createArrayNode();
	       
	        for(String d: users) {
	        	ArrayNode usernameCondition =  mapper.createArrayNode().add(USERNAME_TAG).add("EQ").add(d);
	        	conditions.add(usernameCondition);
	        }
	        
	        ObjectNode dataNode =  mapper.createObjectNode();
	        dataNode.put("about", data);
	        
	        
	        ObjectNode notifications =  mapper.createObjectNode();
	        
	        notifications.put("send_date", "now");
	        notifications.put("content", content);
	        notifications.put("conditions", conditions);
	        notifications.put("data", dataNode);
	        
	        ArrayNode notificationsArray = mapper.createArrayNode().add(notifications);
	        
	        ObjectNode reqObject = mapper.createObjectNode();
	        reqObject.put("application", APPLICATION_CODE);
	        reqObject.put("auth", AUTH_TOKEN);
	        reqObject.put("notifications", notificationsArray);
	 
	        
	        ObjectNode mainRequest = mapper.createObjectNode();
	        mainRequest.put("request", reqObject);
	        
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