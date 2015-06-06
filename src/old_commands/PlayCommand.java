package old_commands;

import java.io.IOException;

public class PlayCommand extends Command {

	@Override
	public String[] commandStrings() {
		return new String[]{"play","start"};
	}

	@Override
	public void execute(String cmd) throws IOException {
		String action = "Play";
		String service_type = "AVTransport";
		String version = "1";
		String arguments = "<InstanceID>0</InstanceID><Speed>1</Speed>";
		
		send(action, service_type, version, arguments);
	}

	@Override
	public String help() {
		return "Start playing";
	}

}
