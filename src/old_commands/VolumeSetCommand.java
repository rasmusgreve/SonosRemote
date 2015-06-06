package old_commands;


public class VolumeSetCommand extends Command {
	
	
	@Override
	public String[] commandStrings() {
		return new String[]{"v", "vol", "volume"};
	}

	@Override
	public void execute(String cmd) {
		int newVolume = -1;
		
		try{
			newVolume = Integer.parseInt(cmd.split(" ")[1]);
		}catch (Exception e){}
		
		if (newVolume < 0 || newVolume > 100)
		{
			
			String action = "GetVolume";
			String service_type = "RenderingControl";
			String version = "1";
			String arguments = "<InstanceID>0</InstanceID><Channel>Master</Channel>";
			
			System.out.println(extract(get(action, service_type, version, arguments), "CurrentVolume"));
		}
		else
		{
			String action = "SetVolume";
			String service_type = "RenderingControl";
			String version = "1";
			String arguments = "<InstanceID>0</InstanceID><Channel>Master</Channel><DesiredVolume>"+newVolume+"</DesiredVolume>";
			
			send(action, service_type, version, arguments);	
		}
	}

	@Override
	public String help() {
		return "Get or set the volume (provide a number from 0-100 to set)";
	}

}
