package old_commands;

public class VolumeUpCommand extends Command {
	
	public String[] commandStrings()
	{
		return new String[]{"+", "volup", "volumeup"};
	}
	
	public String help()
	{
		return "Turn up the volume";
	}
	
	public void execute(String cmd)
	{
		int volume;
		String action = "GetVolume";
		String service_type = "RenderingControl";
		String version = "1";
		String arguments = "<InstanceID>0</InstanceID><Channel>Master</Channel>";
		
		volume = Integer.parseInt(extract(get(action, service_type, version, arguments), "CurrentVolume"));
		volume = Math.min(100, volume + 5);
		
		action = "SetVolume";
		service_type = "RenderingControl";
		version = "1";
		arguments = "<InstanceID>0</InstanceID><Channel>Master</Channel><DesiredVolume>"+volume+"</DesiredVolume>";
		
		send(action, service_type, version, arguments);	
		System.out.println(volume);
	}
}
