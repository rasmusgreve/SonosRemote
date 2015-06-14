package main;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import model.PlayPosition;
import commands.PlayCommand;

public class DeviceDiscoverer {

	private static final String discoveryData = "M-SEARCH * HTTP/1.1\r\n"
			+ "HOST: 239.255.255.250:1900\r\n"
			+ "MAN: \"ssdp:discover\"\r\n"
			+ "MX: 1\r\n"
			+ "ST: urn:schemas-upnp-org:device:ZonePlayer:1\r\n";
	
	
	private static List<SonosDevice> devices = new ArrayList<>();
	private static List<DiscoveryListener> listeners = new LinkedList<>();
	private static boolean isDiscovering = false;
	
	public static void addDiscoveryListener(DiscoveryListener listener){
		if (!listeners.contains(listener))
			listeners.add(listener);
	}
	
	public static void removeDiscoveryListener(DiscoveryListener listener){
		listeners.remove(listener);
	}
	
	protected static void notifyListenersNewDevice(SonosDevice device){
		listeners.forEach(l->l.newDevice(device));
	}
	
	protected static void notifyListenersDiscoveryComplete(){
		listeners.forEach(l->l.discoveryComplete(devices));
	}
	
	public static void discover(){
		if (isDiscovering) return;
		isDiscovering = true;
		Runnable r = new Runnable() {
			@Override
			public void run() {
				DatagramSocket ds = null;
				try{
					devices.clear();
					ds = new DatagramSocket();
					ds.setSoTimeout(2000);
					ds.setBroadcast(true);
					
					DatagramPacket dp = new DatagramPacket(discoveryData.getBytes(), discoveryData.length());
					dp.setAddress(InetAddress.getByName("255.255.255.255"));
					dp.setPort(1900);
					ds.send(dp);
					
					DatagramPacket inp = new DatagramPacket(new byte[1024], 1024);
					
					while (true){
						ds.receive(inp);
						SonosDevice device = SonosDevice.build(inp.getAddress().toString().replace("/", ""));
						notifyListenersNewDevice(device);
						devices.add(device);
					}
					
				}
				catch (SocketTimeoutException e){
					//This is ok
				}
				catch (Exception e){
					throw new RuntimeException(e);
				}
				finally{
					if (ds != null)
						ds.close();
					isDiscovering = false;
					notifyListenersDiscoveryComplete();
				}
			}
		};
		Thread t = new Thread(r);
		t.start();
	}
	
	public static void discover(DiscoveryListener listener){
		addDiscoveryListener(listener);
		discover();
	}
	
	public static interface DiscoveryListener{
		public void newDevice(SonosDevice device);
		public void discoveryComplete(List<SonosDevice> devices);
	}
	
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
