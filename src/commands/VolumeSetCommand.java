package commands;

public class VolumeSetCommand extends Command<Void> {

	private final int newVolume;
	
	public VolumeSetCommand(String destinationAddress, int newVolume) {
		super(destinationAddress);
		this.newVolume = newVolume;
	}

	@Override
	protected Void sendCommand() {
		if (newVolume < 0 || newVolume > 100)
			throw new RuntimeException("Desired volume ("+newVolume+") outside valid range (0-100)");
		String action = "SetVolume";
		String service_type = "RenderingControl";
		String version = "1";
		String arguments = "<InstanceID>0</InstanceID><Channel>Master</Channel><DesiredVolume>"+newVolume+"</DesiredVolume>";
		
		send(action, service_type, version, arguments);	
		
		return null;
	}

}
