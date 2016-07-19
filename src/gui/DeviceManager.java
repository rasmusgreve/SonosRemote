package gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.DeviceDiscoverer;
import main.DeviceDiscoverer.DiscoveryListener;
import model.SonosDevice;

public class DeviceManager {

	private List<SonosDevice> devices = new ArrayList<>();
	private List<DevicesUpdatedListener> listeners = new ArrayList<DevicesUpdatedListener>();

	private static DeviceManager instance;

	public static DeviceManager getInstance(){
		if (instance == null) instance = new DeviceManager();
		return instance;
	}

	public List<SonosDevice> getDevices(){
		return Collections.unmodifiableList(devices);
	}

	public void subscribeToUpdates(DevicesUpdatedListener listener){
		synchronized (listeners) {
			listeners.add(listener);
		}
	}
	
	public void updateDevices(){
		DeviceDiscoverer.discover(new DiscoveryListener(){
			@Override
			public void newDevice(SonosDevice device) {}

			@Override
			public void discoveryComplete(List<SonosDevice> devices) {
				DeviceManager.this.devices = devices;
				synchronized (listeners) {
					for (DevicesUpdatedListener listener : listeners)
						listener.deviceListUpdated();
				}
			}

		});
	}

	public interface DevicesUpdatedListener{
		void deviceListUpdated();
	}

}
