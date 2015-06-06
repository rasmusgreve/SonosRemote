package commands;

public class PlayURICommand extends Command<Void> {

	final String uri, title;
	
	public PlayURICommand(String destinationAddress, String uri, String title) {
		super(destinationAddress);
		this.uri = uri;
		this.title = title;
	}

	@Override
	protected Void sendCommand() {
		//String url = "http://www.dr.dk/mu/MediaRedirector/WithFileExtension/mads-monopolet-uge-15-mungo-park-kolding.mp3?highestBitrate=True&amp;podcastDownload=True";
		//String url = "http://www.tonycuffe.com/mp3/saewill.mp3";
		
		//String title = "CustomURL";

		String meta = "&lt;DIDL-Lite xmlns:dc=&quot;http://purl.org/dc/elements/1.1/&quot; xmlns:upnp=&quot;urn:schemas-upnp-org:metadata-1-0/upnp/&quot; xmlns:r=&quot;urn:schemas-rinconnetworks-com:metadata-1-0/&quot; xmlns=&quot;urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/&quot;&gt;&lt;item id=&quot;R:0/0/0&quot; parentID=&quot;R:0/0&quot; restricted=&quot;true&quot;&gt;&lt;dc:title&gt;"+title+"&lt;/dc:title&gt;&lt;upnp:class&gt;object.item.audioItem.audioBroadcast&lt;/upnp:class&gt;&lt;desc id=&quot;cdudn&quot; nameSpace=&quot;urn:schemas-rinconnetworks-com:metadata-1-0/&quot;&gt;SA_RINCON65031_&lt;/desc&gt;&lt;/item&gt;&lt;/DIDL-Lite&gt;";
		
		//meta = "";
		String action = "SetAVTransportURI";
		String service_type = "AVTransport";
		String version = "1";
		String arguments = "<InstanceID>0</InstanceID><CurrentURI>"+uri+"</CurrentURI><CurrentURIMetaData>"+meta+"</CurrentURIMetaData>";
		
		send(action, service_type, version, arguments);
		
		return null;
	}

}
