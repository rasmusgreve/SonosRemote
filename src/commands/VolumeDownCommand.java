package commands;

public class VolumeDownCommand extends Command {
	
	public String[] commandStrings()
	{
		return new String[]{"-", "voldown", "volumedown"};
	}
	
	public String help()
	{
		return "Turn down the volume";
	}
	
	public void execute(String cmd)
	{
		int volume;
		String action = "GetVolume";
		String service_type = "RenderingControl";
		String version = "1";
		String arguments = "<InstanceID>0</InstanceID><Channel>Master</Channel>";
		
		volume = Integer.parseInt(get(action, service_type, version, arguments, "CurrentVolume"));
		volume = Math.max(0, volume - 5);
		
		action = "SetVolume";
		service_type = "RenderingControl";
		version = "1";
		arguments = "<InstanceID>0</InstanceID><Channel>Master</Channel><DesiredVolume>"+volume+"</DesiredVolume>";
		
		send(action, service_type, version, arguments);	
		System.out.println(volume);
	}
}
