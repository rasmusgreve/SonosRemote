package commands;

import model.PlayPosition;

public class SeekCommand extends Command<Void> {
	
	final PlayPosition position;
	public SeekCommand(String destinationAddress, PlayPosition position) {
		super(destinationAddress);
		this.position = position;
	}

	@Override
	protected Void sendCommand() {
        String action = "Seek";
		String service_type = "AVTransport";
		String version = "1";
		String arguments = "<InstanceID>0</InstanceID><Unit>REL_TIME</Unit><Target>"+position+"</Target>";

		send(action, service_type, version, arguments);
		return null;
	}

}
