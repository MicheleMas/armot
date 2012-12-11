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

import java.io.IOException;
import java.util.Scanner;

import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;

public class Poisoner implements Runnable {

	static JpcapCaptor captor; // capture packets from the network interface
	static JpcapSender sender; // send packets in the network interface
	static Scanner sc = new Scanner(System.in);
	static int nicInUse;
	static jpcap.NetworkInterface[] nics;
	static ARPPacket packet;
	static Table table;
	static byte[] macAddress;
	static private boolean stop;
	/**
	 * IP MAC association
	 */
	/**
	 * IP sender and IP receiver
	 */
	ARPPacket reply;
	EthernetPacket ethReply;

	/**
	 * Scan the system to found network interfaces
	 * 
	 * @return an array of strings containing the name of the NIC (with the
	 *         right order)
	 */
	public String[] availableInterfaces() {

		System.out.println("scanning for network interfaces...");
		String[] results = new String[nics.length];
		nics = JpcapCaptor.getDeviceList();
		for (int i = 0; i < nics.length; i++) {
			results[i] = nics[i].name + ": " + nics[i].description;
		}

		return results;
	}

	/**
	 * Start the ARP Poisoning thread
	 * 
	 * @param number
	 *            of the interface; find out this number with
	 *            availableInterfaces() method
	 */
	public void start(int number) {

		boolean ok = true;
		table = new Table();

		// refresh interfaces list
		nics = JpcapCaptor.getDeviceList();

		// select the correct interface
		try {
			captor = JpcapCaptor.openDevice(nics[number], 65535, true, 20);
			sender = captor.getJpcapSenderInstance();
			macAddress = nics[number].mac_address; 
		} catch (IOException e) {
			ok = false;
		} catch (NullPointerException e) {
			ok = false;
		}

		// set ARP filter
		try {
			captor.setFilter("arp", true);
		} catch (IOException e) {
			ok = false;
		}

		// if no problem occurred the thread can be started
		if (ok) {
			Thread thread = new Thread(this);
			thread.setName("Poisoner");
			thread.setPriority(8);
			thread.start();
		}
	}

	public void stop() {
		stop = true;

	}

	public void run() {
		stop = false;
		// start a loop
		while (!stop) {
			// read an arp packet from the interface
			packet = (ARPPacket) captor.getPacket();
			if (packet != null && packet.operation == 1) {

				// read the informations
				final byte[] destIP = packet.target_protoaddr;
				byte[] sourceMAC = packet.sender_hardaddr;
				byte[] sourceIP = packet.sender_protoaddr;

				// convert from byte to IP and MAC address
				String ipLog = new String("");
				for (int i = 0; i < 4; i++)
					ipLog += "." + (int) (sourceIP[i] & 0xFF);
				String macLog = new String("");
				for (int i = 0; i < 6; i++)
					macLog += ":" + Integer.toHexString(sourceMAC[i] & 0xFF);

				// put IP and his MAC address associated

				table.putIpMac(ipLog, macLog);

				// convert from byte to IP address
				String toLog = new String("");
				for (int i = 0; i < 4; i++)
					toLog += "." + (int) (destIP[i] & 0xFF);

				// put IP sender

				table.putFromTo(ipLog, toLog);

			}
		}
	}

	public JpcapSender getSender() {
		return sender;
	}
}
