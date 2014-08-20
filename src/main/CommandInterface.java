package main;
import java.io.*;
import java.util.HashMap;

import commands.*;

public class CommandInterface {
	
	public static HashMap<String,Command> Commands = new HashMap<String, Command>();
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	public static int instanceId = 0;
	
	public static void main(String[] args) throws IOException
	{
		CommandInterface ci = new CommandInterface();
		ci.start();
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
	
	public void start() throws IOException
	{
		System.out.println("Sonos controller v. 0.1");
		//Todo fetch ip etc.		
		String ip = "";
		do
		{
			System.out.println("Enter the IP of your Sonos device:");
			ip = br.readLine();
		}
		while(!validIP(ip));
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
