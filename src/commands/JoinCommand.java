package commands;


public class JoinCommand extends Command<Void> {

	private final String masterUID;
	
	public JoinCommand(String destinationAddress, String masterUID) {
		super(destinationAddress);
		this.masterUID = masterUID;
	}

	@Override
	protected Void sendCommand() {
		String action = "SetAVTransportURI";
		String service_type = "AVTransport";
		String version = "1";
		String arguments = "<InstanceID>0</InstanceID><CurrentURI>x-rincon:"+masterUID+"</CurrentURI><CurrentURIMetaData></CurrentURIMetaData>";
		
		send(action, service_type, version, arguments);
		
		return null;
	}

}
