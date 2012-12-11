/*
    ARMOT  Arp Monitoring Tool
    
    Copyright (C) 2012  Massaro Michele, Tomasello Alex

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
