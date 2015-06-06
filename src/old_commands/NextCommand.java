package old_commands;

import java.io.IOException;

public class NextCommand extends Command {

	@Override
	public String[] commandStrings() {
		return new String[]{"next", "forward"};
	}

	@Override
	public void execute(String cmd) throws IOException {
		String action = "Next";
		String service_type = "AVTransport";
		String version = "1";
		String arguments = "<InstanceID>0</InstanceID>";

		send(action, service_type, version, arguments);
	}

	@Override
	public String help() {
		return "Go to the next item in the current playlist";
	}

}
