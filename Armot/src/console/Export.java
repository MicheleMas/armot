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

package console;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import jpcap.JpcapCaptor;
import jpcap.JpcapWriter;
import jpcap.packet.IPPacket;
import tools.ArrayListPacket;
import tools.PacketList;
import tools.SavePacketWindow;
import tools.Table;
import tools.ToolBox;
import tools.Utilities;

public class Export {
	
	private Scanner in;
	private Table table;
	private ToolBox tb;
	
	public Export() {
		System.out.println("Choose export file: txt or pcap");
		System.out.println("or type back te return to the main menu (or type help)");
		
		in = new Scanner(System.in);
		table = new Table();
		tb = new ToolBox();
		
		System.out.print("Export->");
		
		String input;
		boolean returnToMenu = false;
		while (!returnToMenu && in.hasNext()) {
			
			input = in.nextLine();
			switch (commandInterpreter(input)) {
			case 1:
				this.txt();
				break;
			case 2:
				this.pcap();
				break;
			case 3:
				returnToMenu = true;
				break;
			case 4:
				this.help();
				break;
			default:
				this.help();
				break;
			}
			if(!returnToMenu)
				System.out.print("Export->");
		}
	}
	
	private void txt() {
		System.out.println("Select an IP to save or type all");
		String reading = in.nextLine();
		if(reading != null) {
			if(reading.equalsIgnoreCase("ALL")) {
				this.saveTxt();
				System.out.println("saved!");
			} else {
				try {
					(InetAddress.getByName(reading)).getAddress();
					this.saveTxt(reading);
				} catch (UnknownHostException e) {
					System.out.println("IP not found");
					System.out.println("saved!");
				}
			}
		}
	}
	
	private void help() {
		System.out.println("Export commands list:");
		System.out.println("txt          - export as packets list in txt format");
		System.out.println("pcap         - export in wireshark compatible format, keep also packets data");
		System.out.println("back         - return to previous menu");
	}
	
	private int saveTxt(String ip) {
		System.out
				.println("enter the absolute path to the file without extension");
		System.out.println("(for example: /home/user/Documents/file)");
		String name = in.nextLine();
		File file = new File(name);
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
				System.out.println("path not found");
				e1.printStackTrace();
				return 0;

			} catch (IOException er) {
				System.out.println("error saving the file");
				er.printStackTrace();
				return 0;

			}
		} catch (IOException e2) {
			System.out.println("can't create the file");
			e2.printStackTrace();
			return 0;
		}
		return 1;
	}
	
	private int saveTxt() {
		System.out
				.println("enter the absolute path to the file without extension");
		System.out.println("(for example: /home/user/Documents/file)");
		String name = in.nextLine();
		File file = new File(name);
		if (!file.getAbsolutePath().endsWith(".txt")) {
			file = new File(file.getAbsolutePath() + ".txt");
		}

		try {
			file.createNewFile();
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
	
	private void pcap() {
		System.out.println("Select an IP to save or type all");
		String reading = in.nextLine();
		if(reading != null) {
			if(reading.equalsIgnoreCase("ALL")) {
				this.savePcap();
				System.out.println("saved!");
			} else {
				try {
					(InetAddress.getByName(reading)).getAddress();
					this.savePcap(reading);
					System.out.println("saved!");
				} catch (UnknownHostException e) {
					System.out.println("IP not found");
				}
			}
		}
	}
	
	private int savePcap() {
		System.out
				.println("enter the absolute path to the file without extension");
		System.out.println("(for example: /home/user/Documents/file)");
		String name = in.nextLine();
		File file = new File(name);
		// This is where a real application would save the file.
		if (!file.getAbsolutePath().endsWith(".pcap")) {
			file = new File(file.getAbsolutePath() + ".pcap");
		}
		try {
			JpcapCaptor captor = JpcapCaptor.openDevice(tb.getNic(), 65535,
					true, 20);
			JpcapWriter writer = JpcapWriter.openDumpFile(captor, file.getAbsolutePath());

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
		return 1;
	}
	
	private int savePcap(String ip) {
		System.out
				.println("enter the absolute path to the file without extension");
		System.out.println("(for example: /home/user/Documents/file)");
		String name = in.nextLine();
		File file = new File(name);
		// This is where a real application would save the file.
		if (!file.getAbsolutePath().endsWith(".pcap")) {
			file = new File(file.getAbsolutePath() + ".pcap");
		}
		try {
			JpcapCaptor captor = JpcapCaptor.openDevice(tb.getNic(), 65535,
					true, 20);
			JpcapWriter writer = JpcapWriter.openDumpFile(captor, file.getAbsolutePath());

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
		return 1;
	}
	
	private int commandInterpreter(String commands) {
		String command = commands.split(" ")[0];
		if (command == null)
			return -1;
		if (command.toUpperCase().equals("TXT"))
			return 1;
		if (command.toUpperCase().equals("PCAP"))
			return 2;
		if (command.toUpperCase().equals("BACK"))
			return 3;
		if (command.toUpperCase().equals("HELP"))
			return 4;
		return -1;
	}

}
