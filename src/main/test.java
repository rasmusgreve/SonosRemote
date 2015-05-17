package main;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class test {

	
	public static String soap(String action, String service_type, String version, String arguments)
	{
		return soap(action, service_type, version, arguments, false);
	}
	
	public static String soap(String action, String service_type, String version, String arguments, boolean sonosScheme)
	{
		if (sonosScheme)
		{
			return "<?xml version=\"1.0\"?>"
			        + "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\""
			        + " s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"
			          +   "<s:Body>"
			            +    "<u:"+action+" xmlns:u=\"urn:schemas-sonos-com:service:"
			             +       service_type+":"+version+"\">"
			              +      arguments
			              +  "</u:"+action+">"
			           + "</s:Body>"
			        +"</s:Envelope>";
		}
		else
		{
			return "<?xml version=\"1.0\"?>"
			        + "<s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\""
			        + " s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"
			          +   "<s:Body>"
			            +    "<u:"+action+" xmlns:u=\"urn:schemas-upnp-org:service:"
			             +       service_type+":"+version+"\">"
			              +      arguments
			              +  "</u:"+action+">"
			           + "</s:Body>"
			        +"</s:Envelope>";
		}
	}
	
	public static String soapAction(String action, String service_type, String version)
	{
		return "urn:schemas-upnp-org:service:"+service_type+":"+version+"#"+action;
	}
	
	
	public static void main(String[] args) throws IOException
	{
		/*String action = "SetVolume";
		String service_type = "RenderingControl";
		String version = "1";
		String arguments = "<InstanceID>0</InstanceID><Channel>Master</Channel><DesiredVolume>10</DesiredVolume>";
		*/
		
		/*String action = "Play";
		String service_type = "AVTransport";
		String version = "1";
		String arguments = "<InstanceID>0</InstanceID><Speed>1</Speed>";*/
		
		/*String action = "Pause";
		String service_type = "AVTransport";
		String version = "1";
		String arguments = "<InstanceID>0</InstanceID>";*/
		
		/*String action = "Next";
		String service_type = "AVTransport";
		String version = "1";
		String arguments = "<InstanceID>0</InstanceID>";*/
		
		/*String action = "Previous";
		String service_type = "AVTransport";
		String version = "1";
		String arguments = "<InstanceID>0</InstanceID>";*/
		
		String action = "GetPositionInfo";
		String service_type = "AVTransport";
		String version = "1";
		String arguments = "<InstanceID>0</InstanceID>";
		
		/*String action = "GetVolume";
		String service_type = "RenderingControl";
		String version = "1";
		String arguments = "<InstanceID>0</InstanceID><Channel>Master</Channel>";*/
		
		/*String action = "RemoveAllTracks";
		String service_type = "Queue";
		String version = "1";
		String arguments = "<QueueID>0</QueueID><UpdateID>0</UpdateID>";
		//sonos scheme = true!
		*/
		
		String soap = soap(action, service_type, version, arguments);
		String soapAction = soapAction(action, service_type, version);
		
		
		String url = "http://192.168.1.8:1400/MediaRenderer/"+service_type+"/Control";
		
		//------------- SEND
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		//
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Linux UPnP/1.0 Sonos/26.1-77080 (WDCR:Microsoft Windows NT 6.2.9200.0)");
		con.setRequestProperty("Content-Type", "text/xml; charset=\"utf-8\"");
		con.setRequestProperty("Content-Length", soap.getBytes().length + "");
		con.setRequestProperty("Connection", "Close");
		con.setRequestProperty("SOAPACTION", soapAction);
 

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(soap);
		wr.flush();
		wr.close();
 
		int responseCode = con.getResponseCode();
		//System.out.println("\nSending 'POST' request to URL : " + url);
		//System.out.println("Post parameters : " + soap);
		//System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
		String resp = response.toString();
		resp = resp.split("<s:Body>")[1].split("</s:Body>")[0];
		resp = resp.replace("<", "\r\n<");
		System.out.println(resp);
		
	}
}
