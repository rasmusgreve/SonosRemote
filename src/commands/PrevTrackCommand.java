package commands;

public class PrevTrackCommand extends Command<Void> {

	public PrevTrackCommand(String destinationAddress) {
		super(destinationAddress);
	}

	@Override
	protected Void sendCommand() {
		String action = "Previous";
		String service_type = "AVTransport";
		String version = "1";
		String arguments = "<InstanceID>0</InstanceID>";
		
		send(action, service_type, version, arguments);
		
		return null;
	}

}
