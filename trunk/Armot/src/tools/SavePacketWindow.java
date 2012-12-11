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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import jpcap.JpcapCaptor;
import jpcap.JpcapWriter;
import jpcap.packet.IPPacket;
import net.miginfocom.swing.MigLayout;

public class SavePacketWindow extends JFrame {

	private Table table;
	private JButton txtButton;
	private JButton pcapButton;
	private ToolBox tb;
	private final String ip;

	/**
	 * Constructor for <b>SAVE ALL</b> option
	 */
	public SavePacketWindow() {

		super("Save or Export Packets");
		this.setSize(400, 150);
		ip = "";
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("error setting system UI");
		}
		this.setIconImage(new ImageIcon(ClassLoader
				.getSystemResource("my-net.png")).getImage());
		Rectangle maxBounds = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getMaximumWindowBounds();
		this.setLocation(maxBounds.width / 2 - getWidth() / 2, maxBounds.height
				/ 2 - getHeight() / 2);
		Container content = this.getContentPane();
		content.setBackground(Color.WHITE);
		content.setLayout(new MigLayout());

		table = new Table();
		txtButton = new JButton("save to TXT file");
		pcapButton = new JButton("export to PCAP file");
		tb = new ToolBox();

		pcapButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// System.out.println("saving all packets");
				savePcapAll();
			}
		});

		txtButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// System.out.println("saving all packets");
				saveTxtAll();
			}
		});

		this.add(txtButton);
		this.add(pcapButton);
		pack();
	}

	/**
	 * Constructor for <b>SAVE</b> option
	 * 
	 * @param ip
	 *            of the packet that are going to be saved
	 */
	public SavePacketWindow(String newIP) {

		super("Save or Export Packets");
		this.setSize(400, 150);
		ip = newIP;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("error setting system UI");
		}
		this.setIconImage(new ImageIcon(ClassLoader
				.getSystemResource("my-net.png")).getImage());
		Rectangle maxBounds = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getMaximumWindowBounds();
		this.setLocation(maxBounds.width / 2 - getWidth() / 2, maxBounds.height
				/ 2 - getHeight() / 2);
		Container content = this.getContentPane();
		content.setBackground(Color.WHITE);
		content.setLayout(new MigLayout());

		table = new Table();
		txtButton = new JButton("save to TXT file");
		pcapButton = new JButton("export to PCAP file");
		tb = new ToolBox();

		System.out.println("saving packet of ip: " + ip);

		pcapButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// System.out.println("saving all packets");
				savePcap(ip);
			}
		});

		txtButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// System.out.println("saving all packets");
				saveTxt(ip);
			}
		});

		this.add(txtButton);
		this.add(pcapButton);
		pack();
	}

	private int saveTxtAll() {
		Properties pc = System.getProperties();
		String homePath = pc.getProperty("user.home");
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(homePath));
		fc.setSelectedFile((new File("my-net_"
				+ Calendar.getInstance().get(Calendar.YEAR) + "_"
				+ (Calendar.getInstance().get(Calendar.MONTH) + 1) + "_"
				+ Calendar.getInstance().get(Calendar.DAY_OF_MONTH))));
		int returnVal = fc.showSaveDialog(SavePacketWindow.this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {

			File file = fc.getSelectedFile();
			try {
				file.setWritable(true);
				FileWriter fw = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter outFile = new PrintWriter(bw);

				ArrayListPacket<String, PacketList<String, IPPacket>> sentPacket = table
						.getSentPacketArrayList();
				ArrayListPacket<String, PacketList<String, IPPacket>> receivedPacket = table
						.getReceivedPacketArrayList();

				ArrayList<String> IPs = sentPacket.getKeys();
				ArrayList<String> RecIPs = receivedPacket.getKeys();
				for (int i = 0; i < RecIPs.size(); i++) {
					if (!IPs.contains(RecIPs.get(i)))
						IPs.add(RecIPs.get(i));
				}
				for (int i = 0; i < IPs.size(); i++) {
					String print = Utilities.printPackets(IPs.get(i));
					outFile.println(print);
				}

				outFile.close();
				file.setReadOnly();
				file.setWritable(false);
				return 1;

			} catch (FileNotFoundException e1) {
				JOptionPane.showMessageDialog(null, "Riprova");
				e1.printStackTrace();
				return 0;

			} catch (IOException er) {
				JOptionPane.showMessageDialog(null, "Riprova");
				er.printStackTrace();
				return 0;
			}

		}
		return 0;
	}

	private int savePcapAll() {

		Properties pc = System.getProperties();
		String homePath = pc.getProperty("user.home");
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(homePath));
		fc.setSelectedFile((new File("my-net_"
				+ Calendar.getInstance().get(Calendar.YEAR) + "_"
				+ (Calendar.getInstance().get(Calendar.MONTH) + 1) + "_"
				+ Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + ".pcap")));
		int returnVal = fc.showSaveDialog(SavePacketWindow.this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String fileName = fc.getSelectedFile().getAbsolutePath();
			try {
				JpcapCaptor captor = JpcapCaptor.openDevice(tb.getNic(), 65535,
						true, 20);
				JpcapWriter writer = JpcapWriter.openDumpFile(captor, fileName);

				ArrayListPacket<String, PacketList<String, IPPacket>> sentPacket = table
						.getSentPacketArrayList();
				ArrayListPacket<String, PacketList<String, IPPacket>> receivedPacket = table
						.getReceivedPacketArrayList();

				ArrayList<String> IPs = sentPacket.getKeys();
				ArrayList<String> RecIPs = receivedPacket.getKeys();
				for (int i = 0; i < RecIPs.size(); i++) {
					if (!IPs.contains(RecIPs.get(i)))
						IPs.add(RecIPs.get(i));
				}
				for (int i = 0; i < IPs.size(); i++) {
					PacketList<String, IPPacket> ht = sentPacket.get(IPs.get(i));
					if(ht != null) {
					Enumeration<String> keys = ht.keys();
					int size = ht.size();
					while (keys.hasMoreElements() && i < size) { 
						String key = keys.nextElement();
						IPPacket packet = ht.get(key);
						writer.writePacket(packet);
					} }// TODO check!
				}

				writer.close();
			} catch (IOException e) {
				return 0;
			}
		}

		hideMe();
		return 1;
	}

	private int saveTxt(String ip) {
		Properties pc = System.getProperties();
		String homePath = pc.getProperty("user.home");
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(homePath));
		fc.setSelectedFile(new File(ip + ".txt"));
		int returnVal = fc.showSaveDialog(SavePacketWindow.this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			// This is where a real application would save the file.
			if (!file.getAbsolutePath().endsWith(".txt")) {
				file = new File(file.getAbsolutePath() + ".txt");
			}
			try {
				file.createNewFile();
				try {
					file.setWritable(true);
					FileWriter fw = new FileWriter(file);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter outFile = new PrintWriter(bw);
					
					String print = Utilities.printPackets(ip);
					outFile.println(print);

					outFile.close();
					file.setReadOnly();
					file.setWritable(false);

				} catch (FileNotFoundException e1) {
					JOptionPane.showMessageDialog(null, "Riprova");
					e1.printStackTrace();
					return 0;

				} catch (IOException er) {
					JOptionPane.showMessageDialog(null, "Riprova");
					er.printStackTrace();
					return 0;

				}
			} catch (IOException e2) {
				JOptionPane.showMessageDialog(null, "Riprova");
				e2.printStackTrace();
				return 0;
			}

		}
		hideMe();
		return 1;

	}

	private int savePcap(String ip) {

		Properties pc = System.getProperties();
		String homePath = pc.getProperty("user.home");
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(homePath));
		fc.setSelectedFile((new File(ip + ".pcap")));
		int returnVal = fc.showSaveDialog(SavePacketWindow.this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String fileName = fc.getSelectedFile().getAbsolutePath();
			try {
				JpcapCaptor captor = JpcapCaptor.openDevice(tb.getNic(), 65535,
						true, 20);
				JpcapWriter writer = JpcapWriter.openDumpFile(captor, fileName);

				ArrayListPacket<String, PacketList<String, IPPacket>> sentPacket = table
						.getSentPacketArrayList();
				ArrayListPacket<String, PacketList<String, IPPacket>> receivedPacket = table
						.getReceivedPacketArrayList();

				Enumeration<String> keys;
				PacketList<String, IPPacket> ht = sentPacket.get(ip);
				if (ht != null) {
					keys = ht.keys();
					while (keys.hasMoreElements()) {
						String key = keys.nextElement();
						IPPacket packet = ht.get(key);
						writer.writePacket(packet);
					}
				}

				ht = receivedPacket.get(ip);
				if (ht != null) {
					keys = ht.keys();
					while (keys.hasMoreElements()) {
						String key = keys.nextElement();
						IPPacket packet = ht.get(key);
						writer.writePacket(packet);
					}
				}

				writer.close();
			} catch (IOException e) {
				return 0;
			}
		}

		hideMe();
		return 1;

	}

	public void showMe() {
		setVisible(true);
	}

	private void hideMe() {
		setVisible(false);
	}

}
