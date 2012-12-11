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
import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

public class BroadcastWindow extends JFrame {
	
	private JTextField ip;
	private JTextField mac;
	private JButton send;
	private JLabel check;
	
	public BroadcastWindow() {
		super("Broadcast ARP Poisoning");
		this.setSize(400, 150);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("error setting system UI");
		}
		this.setIconImage(new ImageIcon(ClassLoader.getSystemResource("my-net.png")).getImage());
		Rectangle maxBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		this.setLocation(maxBounds.width / 2 - getWidth() / 2, maxBounds.height / 2 - getHeight() / 2);
		Container content = this.getContentPane();
		content.setBackground(Color.WHITE);
		content.setLayout(new MigLayout());
		
		
		ip = new JTextField(25);
		mac = new JTextField(25);
		send = new JButton("send packet");
		check = new JLabel("");
		add(new JLabel("IP:"));
		add(ip, "wrap");
		add(new JLabel("MAC:"));
		add(mac, "wrap");
		add(send);
		add(check);
		
		send.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// invia il pacchetto e controlla se tutto e' andato bene
				BroadcastARP bARP = new BroadcastARP(ip.getText(), mac.getText());
				if(bARP.send())
					check.setText("packet sent!");
				else
					check.setText("check ip or mac address!");

			}
		});
		pack();

	}

	public void showMe() {
		setVisible(true);
	}

	private void hideMe() {
		setVisible(false);
	}
}