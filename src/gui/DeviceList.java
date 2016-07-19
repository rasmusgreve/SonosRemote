package gui;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import gui.DeviceManager.DevicesUpdatedListener;
import model.SonosDevice;

public class DeviceList extends JPanel {

	private static final long serialVersionUID = 1L;

	DeviceSelectedListener listener;
	private JList<SonosDevice> list = new JList<>();
	
	public DeviceList(){
		add(list);
		final DefaultListModel<SonosDevice> model = new DefaultListModel<>();
		list.setModel(model);
		DeviceManager.getInstance().subscribeToUpdates(new DevicesUpdatedListener() {
			@Override
			public void deviceListUpdated() {
				model.clear();
				for (SonosDevice device : DeviceManager.getInstance().getDevices()){
					model.addElement(device);
				}
			}
		});
		DeviceManager.getInstance().updateDevices();
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()){
					if (listener != null){
						listener.deviceSelected(list.getSelectedValue());
					}
				}
			}
		});
	}
	
	
	public void setDeviceSelectedListener(DeviceSelectedListener listener){
		this.listener = listener;
	}
	
	public interface DeviceSelectedListener{
		void deviceSelected(SonosDevice device);
	}
	
	
}
