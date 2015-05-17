package main;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class SonosDevice {

	String ip;
	String roomName;
	String playerType;
	
	private SonosDevice(String ip, String roomName, String playerType) {
		this.ip = ip;
		this.roomName = roomName;
		this.playerType = playerType;
	}
	
	
	
	public String getIp() {
		return ip;
	}
	
	public String getRoomName() {
		return roomName;
	}
	
	public String getPlayerType() {
		return playerType;
	}
	
	
	
	
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
	
	private static class SonosDeviceDescriptionHandler extends DefaultHandler{
		
		private String ip, roomName, playerType;
		
		public SonosDeviceDescriptionHandler(String ip){
			this.ip = ip;
		}
		
		public SonosDevice getDevice(){
			return new SonosDevice(ip, roomName, playerType);
		}
		
		private StringBuilder stringBuilder;

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			if (qName.equals("roomName") || qName.equals("displayName"))
				stringBuilder = new StringBuilder();
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (qName.equals("roomName"))
				roomName = stringBuilder.toString();
			if (qName.equals("displayName"))
				playerType = stringBuilder.toString();
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			if (stringBuilder != null) stringBuilder.append(ch,start,length);
		}
		
		
	}

}
