package commands;

public class VolumeGetCommand extends Command<Integer> {

	public VolumeGetCommand(String destinationAddress) {
		super(destinationAddress);
	}

	@Override
	protected Integer sendCommand() {
		String action = "GetVolume";
		String service_type = "RenderingControl";
		String version = "1";
		String arguments = "<InstanceID>0</InstanceID><Channel>Master</Channel>";
		
		try{
			String raw = extract(get(action, service_type, version, arguments), "CurrentVolume");
			return new Integer(raw);
		}
		catch (Exception e){
			throw new RuntimeException(e);
		}
	}

}
