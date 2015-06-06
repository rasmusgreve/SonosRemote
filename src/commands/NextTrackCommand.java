package commands;

public class NextTrackCommand extends Command<Void> {

	public NextTrackCommand(String destinationAddress) {
		super(destinationAddress);
	}

	@Override
	protected Void sendCommand() {
		String action = "Next";
		String service_type = "AVTransport";
		String version = "1";
		String arguments = "<InstanceID>0</InstanceID>";

		send(action, service_type, version, arguments);
		return null;
	}
	
}
