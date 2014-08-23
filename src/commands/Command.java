package commands;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public abstract class Command {
	
	public static ArrayList<Command> Commands;
	static
	{
		Commands = new ArrayList<Command>();
		Commands.add(new HelpCommand());
		Commands.add(new VolumeDownCommand());
		Commands.add(new VolumeSetCommand());
		Commands.add(new VolumeUpCommand());
		Commands.add(new PlayCommand());
		Commands.add(new PauseCommand());
		Commands.add(new NextCommand());
		Commands.add(new PrevCommand());
		Commands.add(new TrackCommand());
		Commands.add(new QuitCommand());
	}
	
	public abstract String[] commandStrings();
	public abstract void execute(String cmd) throws IOException;
	public abstract String help();
	
	private static String ip;
	public static void SetIP(String ip)
	{
		Command.ip = ip;
	}
	
	
	private static String soap(String action, String service_type, String version, String arguments)
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
	
	private static String soapAction(String action, String service_type, String version)
	{
		return "urn:schemas-upnp-org:service:"+service_type+":"+version+"#"+action;
	}
	
	protected String get(String action, String service_type, String version, String argument)
	{
		String resp = communicate(action, service_type, version, argument);
		return resp;
	}
	
	protected String extract(String raw, String tag)
	{
		try{
			return raw.split("<"+tag+">")[1].split("</"+tag+">")[0];
		}
		catch (Exception e)
		{
			return "";
		}
	}
	
	protected void send(String action, String service_type, String version, String argument)
	{
		communicate(action, service_type, version, argument);
	}
	
	private String communicate(String action, String service_type, String version,String argument)
	{
		String soap = soap(action, service_type, version, argument);
		String soapAction = soapAction(action, service_type, version);
		
		
		String url = "http://"+ip+":1400/MediaRenderer/"+service_type+"/Control";
		
		//------------- SEND
		try {
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
	 
			//int responseCode = con.getResponseCode();
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
			//resp = resp.replace("<", "\r\n<");
			return resp;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
