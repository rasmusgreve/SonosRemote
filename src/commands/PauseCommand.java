package commands;

public class PauseCommand extends Command<Void> {

	public PauseCommand(String destinationAddress) {
		super(destinationAddress);
	}

	@Override
	protected Void sendCommand() {
		String action = "Pause";
		String service_type = "AVTransport";
		String version = "1";
		String arguments = "<InstanceID>0</InstanceID><Speed>1</Speed>";
		
		send(action, service_type, version, arguments);
		
		return null;
	}

}
