package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import gui.DeviceList.DeviceSelectedListener;
import model.SonosDevice;

public class GuiMain extends JFrame{

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		GuiMain frame = new GuiMain();
		frame.setSize(600, 600);
		frame.setLocation(200, 200);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	public GuiMain(){
		setLayout(new BorderLayout());
		
		final DeviceList deviceList = new DeviceList();
		final DeviceOperationsComponent operationsComponent = new DeviceOperationsComponent();

		deviceList.setDeviceSelectedListener(new DeviceSelectedListener() {
			@Override
			public void deviceSelected(SonosDevice device) {
				operationsComponent.setDevice(device);
			}
		});
		
		add(deviceList, BorderLayout.WEST);
		add(operationsComponent, BorderLayout.CENTER);
		
	}
	
	
	
}
