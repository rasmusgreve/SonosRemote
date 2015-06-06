package old_commands;

import java.io.IOException;


public class TrackCommand extends Command {

	@Override
	public String[] commandStrings() {
		return new String[]{"track"};
	}

	@Override
	public void execute(String cmd) throws IOException {
		String action = "GetPositionInfo";
		String service_type = "AVTransport";
		String version = "1";
		String arguments = "<InstanceID>0</InstanceID>";
		
		String raw = get(action, service_type, version, arguments);
		String meta = extract(raw, "TrackMetaData");
		meta = meta.replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"").replace("&amp;", "&").replace("&amp;", "&");
		//System.out.println(meta);
		String title = extract(meta, "dc:title");
		String creator = extract(meta, "dc:creator");
		String album = extract(meta, "upnp:album");
		String content = extract(meta, "r:streamContent");
		
		System.out.println(title);
		System.out.println(creator);
		System.out.println(album);
		System.out.println(content);
		
	}

	@Override
	public String help() {
		return "Get info about the currently playing track";
	}

}
