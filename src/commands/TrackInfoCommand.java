package commands;

public class TrackInfoCommand extends Command<TrackInfo> {

	public TrackInfoCommand(String destinationAddress) {
		super(destinationAddress);
	}

	@Override
	protected TrackInfo sendCommand() {
		String action = "GetPositionInfo";
		String service_type = "AVTransport";
		String version = "1";
		String arguments = "<InstanceID>0</InstanceID>";
		
		String raw = get(action, service_type, version, arguments);
		String meta = extract(raw, "TrackMetaData");
		meta = meta.replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"").replace("&amp;", "&").replace("&amp;", "&");
		
		String title = extract(meta, "dc:title");
		String creator = extract(meta, "dc:creator");
		String album = extract(meta, "upnp:album");
		String content = extract(meta, "r:streamContent");
		
		return new TrackInfo(title, creator, album, content);
	}

}
