package main;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;

import old_commands.*;

public class CommandInterface {
	
	public static HashMap<String,Command> Commands = new HashMap<String, Command>();
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	public static int instanceId = 0;
	
	public static void main(String[] args) throws IOException
	{
		if (args.length == 0)
		{
			CommandInterface ci = new CommandInterface();
			ci.start();
		}
	}
	
	private boolean validIP(String ip)
	{
		String[] parts = ip.split("\\.");
		if (parts.length != 4) return false;
		try
		{
			for (String s : parts)
			{
				if (Integer.parseInt(s) < 0 || Integer.parseInt(s) > 255) return false;
			}	
			
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}
	
	private String discover() throws IOException
	{
		DatagramSocket ds = new DatagramSocket();
		ds.setBroadcast(true);
		
		String data = "M-SEARCH * HTTP/1.1\r\n"
        				+ "HOST: 239.255.255.250:1900\r\n"
        				+ "MAN: \"ssdp:discover\"\r\n"
        				+ "MX: 1\r\n"
        				+ "ST: urn:schemas-upnp-org:device:ZonePlayer:1\r\n";
		
		
		DatagramPacket dp = new DatagramPacket(data.getBytes(), data.length());
		dp.setAddress(InetAddress.getByName("255.255.255.255"));
		dp.setPort(1900);
		
		ds.send(dp);
		
		DatagramPacket inp = null;
		boolean a = true;
		
		//while (a){
			inp = new DatagramPacket(new byte[2048], 2048);
			ds.receive(inp);
			ds.close();
	
			String d = new String(inp.getData());
			System.out.println(d);
			System.out.println("------------------------------------");
		//}
		
		System.out.println("Connected to: " + inp.getAddress().toString().replace("/", ""));
		
		return inp.getAddress().toString().replace("/","");
	}
	
	
	public void start() throws IOException
	{
		System.out.println("Sonos controller v. 0.1");
		//Todo fetch ip etc.		
		String ip = discover();
		while(!validIP(ip))
		{
			System.out.println("Enter the IP of your Sonos device:");
			ip = br.readLine();
		}
		
		Command.SetIP(ip);
		
		registerCommands();
		System.out.println("For a list of possible commands type ?");
		while (true)
		{
			handle(br.readLine());
		}
	}
	
	private static void registerCommands()
	{
		for (Command cmd : Command.Commands)
			for (String cmdStr : cmd.commandStrings())
				Commands.put(cmdStr, cmd);
	}
	
	public void handle(String cmd)
	{
		Command command = Commands.get(cmd.split(" ")[0]);
		if (command == null){
			System.out.println("Unknown command: \"" + cmd + "\"");
		}
		else
		{
			try{
				command.execute(cmd);
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		}
	}
}
