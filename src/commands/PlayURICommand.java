package commands;

import java.io.IOException;

public class PlayURICommand extends Command {

	@Override
	public String[] commandStrings() {
		return new String[]{"url", "uri"};
	}

	@Override
	public void execute(String url) throws IOException {
		url = "http://www.dr.dk/mu/MediaRedirector/WithFileExtension/mads-monopolet-uge-15-mungo-park-kolding.mp3?highestBitrate=True&amp;podcastDownload=True";
		
		String title = "Custom URL";
		String meta = "<DIDL-Lite xmlns:dc=\"http://purl.org/dc/elements"+
		        "/1.1/\" xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\" "+
		        "xmlns:r=\"urn:schemas-rinconnetworks-com:metadata-1-0/\" "+
		        "xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\">"+
		        "<item id=\"R:0/0/0\" parentID=\"R:0/0\" restricted=\"true\">"+
		        "<dc:title>"+title+"</dc:title><upnp:class>"+
		        "object.item.audioItem.audioBroadcast</upnp:class><desc "+
		        "id=\"cdudn\" nameSpace=\"urn:schemas-rinconnetworks-com:"+
		        "metadata-1-0/\">"+"SA_RINCON65031_"+"</desc></item></DIDL-Lite>";
				
		
		String action = "SetAVTransportURI";
		String service_type = "AVTransport";
		String version = "1";
		String arguments = "<InstanceID>0</InstanceID><CurrentURI>"+url+"</CurrentURI><CurrentURIMetaData>"+meta+"</CurrentURIMetaData>";
		
		send(action, service_type, version, arguments);	
	}

	@Override
	public String help() {
		return "Start playing a given url (provide the url to play)";
	}

}


/*

"""
if meta == '' and title != '':
    meta_template = '<DIDL-Lite xmlns:dc="http://purl.org/dc/elements'\
        '/1.1/" xmlns:upnp="urn:schemas-upnp-org:metadata-1-0/upnp/" '\
        'xmlns:r="urn:schemas-rinconnetworks-com:metadata-1-0/" '\
        'xmlns="urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/">'\
        '<item id="R:0/0/0" parentID="R:0/0" restricted="true">'\
        '<dc:title>{title}</dc:title><upnp:class>'\
        'object.item.audioItem.audioBroadcast</upnp:class><desc '\
        'id="cdudn" nameSpace="urn:schemas-rinconnetworks-com:'\
        'metadata-1-0/">{service}</desc></item></DIDL-Lite>'
    tunein_service = 'SA_RINCON65031_'
    # Radio stations need to have at least a title to play
    meta = meta_template.format(title=title, service=tunein_service)

self.avTransport.SetAVTransportURI([
    ('InstanceID', 0),
    ('CurrentURI', uri),
    ('CurrentURIMetaData', meta)
])
# The track is enqueued, now play it if needed
if start:
    return self.play()
return False

*/