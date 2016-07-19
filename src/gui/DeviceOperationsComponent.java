package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.SonosDevice;

public class DeviceOperationsComponent extends JPanel {

	SonosDevice device;
	JLabel headerLabel;
	JLabel trackLabel;
	
	public DeviceOperationsComponent(){
		headerLabel = new JLabel("");
		trackLabel = new JLabel("");
		add(headerLabel);
		
		JButton volumeUpButton = new JButton("+");
		volumeUpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				device.setVolume(Math.min(100, device.getVolumeBlocking()+5));
			}
		});
		add(volumeUpButton);
		
		JButton volumeDownButton = new JButton("-");
		volumeDownButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				device.setVolume(Math.max(0, device.getVolumeBlocking()-5));
			}
		});
		add(volumeDownButton);
		
		JButton playButton = new JButton("Play");
		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				device.play();
			}
		});
		add(playButton);
		
		JButton pauseButton = new JButton("Pause");
		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				device.pause();
			}
		});
		add(pauseButton);
		
		add(trackLabel);
		
	}
	
	public void setDevice(SonosDevice device){
		this.device = device;
		headerLabel.setText(device.getRoomName());
		trackLabel.setText(device.getTrackInfoBlocking().getTitle());
	}
	
}
