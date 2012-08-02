package tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import jpcap.JpcapSender;
import net.miginfocom.swing.MigLayout;


public class FakeGuiObj extends JPanel {

	/**
	 * check box to do fake arp
	 */
	private JCheckBox checkbox;
	/**
	 * IP address
	 */
	private String ip;

	private Sender packetSender;

	private Thread ps;

	private String targetIP;

	private byte[] myMac;

	private JpcapSender sender;
	
	private FakeHandler handler;

	public FakeGuiObj(final String ip, int width, final String targetIP, final byte[] myMac, final JpcapSender sender) {
		super();
		this.handler = new FakeHandler();
		this.targetIP = targetIP;
		this.myMac = myMac;
		this.sender = sender;
		checkbox = new JCheckBox(ip, handler.isActive(ip, targetIP));
		System.out.println("the checkbox " + ip + " to " + targetIP + " is " + handler.isActive(ip, targetIP)); // +++++ DEBUG +++++
		this.ip = ip;
		setLayout(new MigLayout());
		setSize(width / 2 - 80, 80);
		checkbox.setFont(new Font("Arial", Font.BOLD, 16));

		checkbox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkbox.isSelected()) {
					// System.out.println("target "+targetIP+" connected "+ip);

					handler.addThread(ip, targetIP, myMac, sender);
					
					/*packetSender = new Sender(targetIP, ip, myMac, sender);
					ps = new Thread(packetSender);
					ps.start();*/
				} else {
					checkbox.setForeground(Color.BLACK);
					checkbox.setText(getIP());
					
					handler.stopThread(ip, targetIP);
					
					//ps.interrupt();
					// TODO stoppare
				}
			}
		});

		add(checkbox);
	}

	public JCheckBox getCheckBox() {
		return checkbox;
	}

	public String getIP() {
		return ip;
	}

	public void stopFakeArp() {
		if (checkbox.isSelected())
			handler.stopThread(ip, targetIP);
	}

	/*public void startFakeArp() {
		packetSender = new Sender(targetIP, ip, myMac, sender);
		ps = new Thread(packetSender);
		ps.start();
	}*/
}
