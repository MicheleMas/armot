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

import jpcap.JpcapSender;
import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;

public class FakeARP {
	
	static JpcapSender sender;
	
	/**
	 * This class send custom ARP packet
	 * @param sender from capture.getJpcapSenderInstance()
	 */
	public FakeARP(JpcapSender sender) {
		this.sender = sender;
	}
	
	/**
	 * Craft and send the custom ARP packet
	 * @param sourceIP requested IP
	 * @param sourceMAC spoofer MAC
	 * @param destIP victim IP
	 * @param destMAC victim MAC
	 */
	public void send(byte[] sourceIP, byte[] sourceMAC, byte[] destIP, byte[] destMAC) {
		
		// craft the arp packet
		ARPPacket arp = new ARPPacket();
		arp.hardtype = ARPPacket.HARDTYPE_ETHER;
		arp.prototype = ARPPacket.PROTOTYPE_IP;
		arp.operation = ARPPacket.ARP_REPLY;
		arp.hlen = 6; // mac address length
		arp.plen = 4; // ip address length
		arp.sender_hardaddr = sourceMAC;
		arp.sender_protoaddr = sourceIP;
		arp.target_hardaddr = destMAC;
		arp.target_protoaddr = destIP;
		
		// craft the ethernet packet
		EthernetPacket eth = new EthernetPacket();
		eth.frametype = EthernetPacket.ETHERTYPE_ARP;
		eth.src_mac = sourceMAC;
		eth.dst_mac = destMAC;
		arp.datalink = eth;
		sender.sendPacket(arp);
	}

}
