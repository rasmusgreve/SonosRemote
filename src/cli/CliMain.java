package cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import main.DeviceDiscoverer;
import main.VersionInfo;
import main.DeviceDiscoverer.DiscoveryListener;
import model.SonosDevice;

public class CliMain {

	static List<SonosDevice> devices = new ArrayList<>();

	public static void main(String[] args) {
		System.out.println("Sonos remote cli " + VersionInfo.Version);

		Scanner scanner = null;
		try {
			scanner = new Scanner(System.in);

			while (true){
				String line = scanner.nextLine();
				String[] split = line.split(" ");
				String command = split[0];
				String[] cmdArgs = Arrays.copyOfRange(split, Math.min(1, split.length), split.length);
				try {
					switch (command.toLowerCase()) {
					case "q":
					case "quit":
					case "exit":			System.exit(0);						break;
					case "play":			play(cmdArgs);						break;
					case "pause":			pause(cmdArgs);						break;
					case "discover":		discover();							break;
					case "list":
					case "devices":			devices();							break;
					case "volume":			volume(cmdArgs);					break;
					
					case "help":			printHelp();						break;


					default:
						System.out.println("Unknown command '"+command+"'. Try 'help' for a list of commands");
					}
				}
				catch (Exception e){
					System.out.println("Unhandled exception: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		finally {
			try {
				scanner.close();
			}
			catch (Exception ignore){}
		}
	}

	private static void volume(String[] cmdArgs) {
		SonosDevice device = findDeviceByIpOrName(cmdArgs);
		if (device == null) return;
		
		if (cmdArgs.length == 1){ //Get current volume
			System.out.println("Current volume of " + device.getRoomName() + " : " + device.getVolumeBlocking());
		} else {
			if (cmdArgs[1].matches("\\d{1,3}")){
				System.out.println("Setting volume of " + device.getRoomName() + " to " + cmdArgs[1]);
				device.setVolume(Integer.parseInt(cmdArgs[1]));
			} else if (cmdArgs[1].matches("\\+\\d{1,3}")){
				int newVolume = device.getVolumeBlocking() + Integer.parseInt(cmdArgs[1].substring(1));
				System.out.println("Increasing volume of " + device.getRoomName() + " by " + cmdArgs[1] + " to " + newVolume);
				device.setVolume(newVolume);
			}else if (cmdArgs[1].matches("\\-\\d{1,3}")){
				int newVolume = device.getVolumeBlocking() - Integer.parseInt(cmdArgs[1].substring(1));
				System.out.println("Decreasing volume of " + device.getRoomName() + " by " + cmdArgs[1] + " to " + newVolume);
				device.setVolume(newVolume);
			}
		}
		
	}

	private static void devices() {
		for (SonosDevice device : devices){
			System.out.println(device.getPlayerType() + " @ " + device.getIp() + " - " + device.getRoomName());
		}
	}

	private static void discover() {
		System.out.println("Started discover...");
		DeviceDiscoverer.discover(new DiscoveryListener() {

			@Override
			public void newDevice(SonosDevice device) {
				System.out.println("Found " + device.getPlayerType() + " @ " + device.getIp() + " - " + device.getRoomName());
			}

			@Override
			public void discoveryComplete(List<SonosDevice> devices) {
				CliMain.devices = devices;
				System.out.println("Discover complete, found " + devices.size() + " devices");
			}

		});
	}

	private static void pause(String[] cmdArgs) {
		SonosDevice device = findDeviceByIpOrName(cmdArgs);
		if (device == null) return;
		System.out.println("Pausing playback on " + device.getRoomName());
		device.pause();
	}

	private static void play(String[] cmdArgs) {
		SonosDevice device = findDeviceByIpOrName(cmdArgs);
		if (device == null) return;
		System.out.println("Starting playback on " + device.getRoomName());
		device.play();
	}

	private static void printHelp() {
		System.out.println(String.format("%20s - %s", "help", "Print this help information"));
		System.out.println(String.format("%20s - %s", "play $1", "Start playback on device $1. $1 should be an IP or room name"));
		System.out.println(String.format("%20s - %s", "pause $1", "Pause playback on device $1. $1 should be an IP or room name"));
		System.out.println(String.format("%20s - %s", "volume $1", "Get the volume of device $1. $1 should be an IP or room name."));
		System.out.println(String.format("%20s - %s", "volume $1 $2", "Set the volume of device $1. $1 should be an IP or room name. If $2 is a number (0-100), set the volume. Prefix $2 by + or - to increase/decrease volume by $2"));
		System.out.println(String.format("%20s - %s", "discover", "Discover devices on the network"));
		System.out.println(String.format("%20s - %s", "devices", "Show a list of currently known devices. Update this list by running 'discover'"));
		System.out.println(String.format("%20s - %s", "quit", "Quit the application"));
	}

	private static SonosDevice findDeviceByIpOrName(String[] cmdArgs){
		if (cmdArgs.length < 1){
			System.out.println("Must specify device");
			return null;
		}

		SonosDevice device = null;

		if (cmdArgs[0].matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")){
			try {
				device = SonosDevice.build(cmdArgs[0]);
			} catch (Exception e){
				System.out.println("Failed to connect: " + e.getMessage());
				return null;
			}
		}
		else {
			for (SonosDevice d : devices){
				if (d.getRoomName().toLowerCase().startsWith(cmdArgs[0])){
					device = d;
					break;
				}
			}
			if (device == null){
				System.out.println("No known device named: '" + cmdArgs[0] + "'");
				if (devices.isEmpty()){
					System.out.println("No devices registered. Run command 'discover' to discover devices on network");
				}
				return null;
			}
		}
		return device;
	}
}
