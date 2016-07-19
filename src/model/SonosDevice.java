package model;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import commands.JoinCommand;
import commands.NextTrackCommand;
import commands.PauseCommand;
import commands.PlayCommand;
import commands.PlayURICommand;
import commands.PrevTrackCommand;
import commands.SeekCommand;
import commands.TrackInfoCommand;
import commands.UnjoinCommand;
import commands.VolumeGetCommand;
import commands.VolumeSetCommand;

public class SonosDevice {

	private String ip;
	private String roomName;
	private String playerType;
	private String uid;
	
	private SonosDevice(String ip, String roomName, String playerType, String uid) {
		this.ip = ip;
		this.roomName = roomName;
		this.playerType = playerType;
		this.uid = uid;
	}
	
	//Meta
	public String getIp() {
		return ip;
	}
	
	public String getRoomName() {
		return roomName;
	}
	
	public String getPlayerType() {
		return playerType;
	}
	
	//Commands
	
	public void play(){
		new PlayCommand(ip).execute();
	}
	
	public void pause(){
		new PauseCommand(ip).execute();
	}
	
	public void nextTrack(){
		new NextTrackCommand(ip).execute();
	}
	
	public void prevTrack(){
		new PrevTrackCommand(ip).execute();
	}
	
	public TrackInfo getTrackInfoBlocking(){
		return new TrackInfoCommand(ip).executeBlocking();
	}
	
	public void setVolume(int volume){
		new VolumeSetCommand(ip, volume).execute();
	}
	
	public int getVolumeBlocking(){
		return new VolumeGetCommand(ip).executeBlocking();
	}
	
	public void join(SonosDevice master){
		new JoinCommand(ip, master.uid).execute();
	}
	
	public void unjoin(){
		new UnjoinCommand(ip).execute();
	}
	
	public void playUri(String uri, String title){
		new PlayURICommand(ip, uri, title).execute();
		play();
	}
	
	public void seek(PlayPosition position){
		new SeekCommand(ip, position).execute();
	}
	
	
	//Builder
	public static SonosDevice build(String ip){
		String deviceDescriptionURL = "http://"+ip+":1400/xml/device_description.xml";
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection)new URL(deviceDescriptionURL).openConnection();
			connection.connect();
			InputStream xmlStream = connection.getInputStream();
			SonosDeviceDescriptionHandler handler = new SonosDeviceDescriptionHandler(ip);
			
			XMLReader reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(handler);
			reader.parse(new InputSource(xmlStream));
			
			return handler.getDevice();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally{
			if (connection != null){
				connection.disconnect();
			}
		}
	}
	
	@Override
	public String toString(){
		return getRoomName();
	}
	
	private static class SonosDeviceDescriptionHandler extends DefaultHandler{
		
		private String ip, roomName, playerType, uid;
		private StringBuilder stringBuilder;
		
		public SonosDeviceDescriptionHandler(String ip){
			this.ip = ip;
		}
		
		public SonosDevice getDevice(){
			return new SonosDevice(ip, roomName, playerType, uid);
		}
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			if (qName.equals("roomName") || qName.equals("displayName") || qName.equals("UDN"))
				stringBuilder = new StringBuilder();
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (qName.equals("roomName"))			roomName = stringBuilder.toString();
			if (qName.equals("displayName"))		playerType = stringBuilder.toString();
			if (qName.equals("UDN") && uid == null) uid = stringBuilder.toString().replace("uuid:", "").trim();
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			if (stringBuilder != null) stringBuilder.append(ch,start,length);
		}
		
		
	}

}
