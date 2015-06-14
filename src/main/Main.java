package main;

import java.util.List;

import main.DeviceDiscoverer.DiscoveryListener;
import model.PlayPosition;
import model.SonosDevice;

public class Main {

	
	public static void main(String[] args) {
		System.out.println("Testing new device discoverer");
		DeviceDiscoverer.discover(new DiscoveryListener() {
			
			@Override
			public void newDevice(SonosDevice device) {
				System.out.println("Found " + device.getPlayerType() + " @ " + device.getIp() + " - " + device.getRoomName());
			}
			
			@Override
			public void discoveryComplete(List<SonosDevice> devices) {
				System.out.println("Discovery done. Found " + devices.size() + " devices");

				SonosDevice stue = devices.stream().filter(d -> d.getRoomName().equals("Stue")).findFirst().get();
				SonosDevice altan = devices.stream().filter(d -> d.getRoomName().equals("Altan")).findFirst().get();
				SonosDevice soveværelse = devices.stream().filter(d -> d.getRoomName().equals("Soveværelse")).findFirst().get();
				SonosDevice børneværelse = devices.stream().filter(d -> d.getRoomName().equals("Børneværelse")).findFirst().get();
				SonosDevice køkken = devices.stream().filter(d -> d.getRoomName().equals("Køkken")).findFirst().get();
				
				
				//køkken.playUri(uri, title);
				//System.out.println("Joining them");
				//stue.join(køkken);
				//System.out.println("Done?");
				
				//System.out.println("Unjoining stue");
				//stue.unjoin();
				køkken.seek(new PlayPosition(10, 0));
				//køkken.playUri("http://www.dr.dk/mu/MediaRedirector/WithFileExtension/mads-monopolet-uge-15-mungo-park-kolding.mp3?highestBitrate=True&amp;podcastDownload=True", "Mads og monopolet");
			}
		});
		
	}
}
