package commands;

public class PlayCommand extends Command<Void>{

	public PlayCommand(String ip) {
		super(ip);
	}
	
	@Override
	protected Void sendCommand() {
		String action = "Play";
		String service_type = "AVTransport";
		String version = "1";
		String arguments = "<InstanceID>0</InstanceID><Speed>1</Speed>";
		
		send(action, service_type, version, arguments);
		
		return null;
	}


}
