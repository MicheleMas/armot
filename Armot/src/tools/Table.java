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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import jpcap.packet.IPPacket;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;


public class Table {

	// tabella che raccoglie gli indirizzi contattati da un dato ip

	static ArrayListMap<String, ArrayList<String>> from_to;

	// tabella che associa per ogni ip l'indirizzo mac ricevuto

	static ArrayListIPMAC<String, String> ip_mac;

	// tabella che associa ad ogni ip i pacchetti inviati

	static ArrayListPacket<String, PacketList<String, IPPacket>> ip_packet_sent;

	// tabella che associa ad ogni ip i pacchetti ricevuti

	static ArrayListPacket<String, PacketList<String, IPPacket>> ip_packet_received;

	public Table() {

		if (from_to == null)
			from_to = new ArrayListMap<String, ArrayList<String>>();
		if (ip_mac == null)
			ip_mac = new ArrayListIPMAC<String, String>();
		if (ip_packet_sent == null)
			ip_packet_sent = new ArrayListPacket<String, PacketList<String, IPPacket>>();
		if (ip_packet_received == null)
			ip_packet_received = new ArrayListPacket<String, PacketList<String, IPPacket>>();
	}

	// metodo che dice se un ip è contenuto o meno nella tabella dei mac

	public synchronized boolean contains(String ip) {
		return ip_mac.findKeyPos(ip) > -1;
	}

	// metodo che restituisce la tabella dei mac

	public synchronized ArrayListIPMAC<String, String> getIpMac() {
		return ip_mac;
	}

	// metodo che restituisce il mac di un dato ip

	public synchronized String getMac(String ip) {
		return ip_mac.get(ip);
	}

	// metodo che restituisce gli ip contattati da un dato ip

	public ArrayList<String> getConnections(String ip) {
		//System.out.print("selected ip: " + ip);
		//System.out.println("  size: " + from_to.get(ip).size());
		return from_to.get(ip);
	}

	// metodo che restituisce la tabella delle connessioni

	public synchronized ArrayListMap<String, ArrayList<String>> getFromTo() {

		return from_to;
	}

	// metodo che salva un pacchetto inviato da un dato ip e relativo timestamp

	public synchronized void putIpPacketSent(String ip, String time, IPPacket packet) {

		if (ip_packet_sent.findKeyPos(ip) < 0) {
			ip_packet_sent.put(ip, new PacketList<String, IPPacket>());
			ip_packet_sent.get(ip).put(time, packet);
		} else
			ip_packet_sent.get(ip).put(time, packet);
	}

	/**
	 * metodo che restituisce i pacchetti inviati da un dato ip con relativo
	 * timestamp in ordine: <br>"TIMESTAMP", "DESTINATION", "PROTOCOL", "SRC PORT", "DST PORT"
	 * @param ip
	 * @return
	 */
	public synchronized String[][] getIpPacketSent(String ip) {
		if (ip_packet_sent != null && ip_packet_sent.get(ip) != null) {
			int i = 0;
			Enumeration<String> chiavi = ip_packet_sent.get(ip).keys();
			int size = ip_packet_sent.get(ip).size();
			String[][] matrix = new String[size][5];
			while (chiavi.hasMoreElements() && i < size) {
				String key = chiavi.nextElement();
				IPPacket genericPacket = ip_packet_sent.get(ip).get(key);
				matrix[i][0] = key;
				matrix[i][1] = genericPacket.dst_ip.toString().substring(1);
				if (genericPacket.protocol == 6) {
					matrix[i][2] = "TCP";
					TCPPacket pacchetto = (TCPPacket) genericPacket;
					matrix[i][3] = "" + pacchetto.src_port;
					matrix[i][4] = "" + pacchetto.dst_port;
				}
				if (ip_packet_sent.get(ip).get(key).protocol == 17) {
					matrix[i][2] = "UDP";
					UDPPacket pacchetto = (UDPPacket) genericPacket;
					matrix[i][3] = "" + pacchetto.src_port;
					matrix[i][4] = "" + pacchetto.dst_port;
				}
				i++;
			}
			return matrix;
		}
		return null;
	}
	
	public synchronized int countIpPacketSentByPort(String ip, int src_port, int dst_port) {
		if (ip_packet_sent != null && ip_packet_sent.get(ip) != null) {
			int i = 0;
			int size = ip_packet_sent.get(ip).size();
			int counter = 0;
			Enumeration<String> chiavi = ip_packet_sent.get(ip).keys();
			while (chiavi.hasMoreElements() && i < size) {
				String key = chiavi.nextElement();
				IPPacket genericPacket = ip_packet_sent.get(ip).get(key);
				
				if (genericPacket.protocol == 6) {
					TCPPacket pacchetto = (TCPPacket) genericPacket;
					if(pacchetto.src_port == src_port && pacchetto.dst_port == dst_port)
						counter++;
				}
				if (genericPacket.protocol == 17) {
					UDPPacket pacchetto = (UDPPacket) genericPacket;
					if(pacchetto.src_port == src_port && pacchetto.dst_port == dst_port)
						counter++;
				}
				i++;
			}
			return counter;
		}
		return 0;
	}
	
	public synchronized ArrayListPacket<String, PacketList<String, IPPacket>> getSentPacketArrayList() {
		return ip_packet_sent;
	}

	// metodo che restituisce il numero di pacchetti inviati da un dato ip

	public synchronized int getNumIpPacketSent(String ip) {
		if (ip_packet_sent.get(ip) == null)
			return 0;
		return ip_packet_sent.get(ip).size();
	}

	// metodo che salva un pacchetto ricevuto da un dato ip e raltivo timestamp

	public synchronized void putIpPacketReceived(String ip, String time, IPPacket packet) {

		if (ip_packet_received.findKeyPos(ip) < 0) {
			ip_packet_received.put(ip, new PacketList<String, IPPacket>());
			ip_packet_received.get(ip).put(time, packet);
		} else
			ip_packet_received.get(ip).put(time, packet);
	}
	
	/**
	 * metodo che restituisce i pacchetti ricevuti da un dato ip e relativo
	 * timestamp in ordine: <br>"TIMESTAMP", "SOURCE", "PROTOCOL", "DST PORT", "SRC PORT"
	 * @param ip
	 * @return
	 */
	public synchronized String[][] getIpPacketReceived(String ip) {
		if (ip_packet_received != null && ip_packet_received.get(ip) != null) {
			int i = 0;
			Enumeration<String> chiavi = ip_packet_received.get(ip).keys();
			int size = ip_packet_received.get(ip).size();
			String[][] matrix = new String[size][5];
			while (chiavi.hasMoreElements() && i < size) {
				String key = chiavi.nextElement();
				matrix[i][0] = key;
				matrix[i][1] = ip_packet_received.get(ip).get(key).src_ip.toString().substring(1);
				if (ip_packet_received.get(ip).get(key).protocol == 6) {
					matrix[i][2] = "TCP";
					TCPPacket pacchetto = (TCPPacket) ip_packet_received.get(ip).get(key);
					matrix[i][3] = "" + pacchetto.dst_port;
					matrix[i][4] = "" + pacchetto.src_port;
				}
				if (ip_packet_received.get(ip).get(key).protocol == 17) {
					matrix[i][2] = "UDP";
					UDPPacket pacchetto = (UDPPacket) ip_packet_received.get(ip).get(key);
					matrix[i][3] = "" + pacchetto.dst_port;
					matrix[i][4] = "" + pacchetto.src_port;
				}
				i++;
			}
			return matrix;
		}
		return null;
	}
	
	public synchronized int countIpPacketReceivedByPort(String ip, int src_port, int dst_port) {
		if (ip_packet_received != null && ip_packet_received.get(ip) != null) {
			int i = 0;
			int size = ip_packet_received.get(ip).size();
			int counter = 0;
			Enumeration<String> chiavi = ip_packet_received.get(ip).keys();
			while (chiavi.hasMoreElements() && i < size) {
				String key = chiavi.nextElement();
				IPPacket genericPacket = ip_packet_received.get(ip).get(key);
				
				if (genericPacket.protocol == 6) {
					TCPPacket pacchetto = (TCPPacket) genericPacket;
					if(pacchetto.src_port == src_port && pacchetto.dst_port == dst_port)
						counter++;
				}
				if (genericPacket.protocol == 17) {
					UDPPacket pacchetto = (UDPPacket) genericPacket;
					if(pacchetto.src_port == src_port && pacchetto.dst_port == dst_port)
						counter++;
				}
				i++;
			}
			return counter;
		}
		return 0;
	}
	
	public synchronized ArrayListPacket<String, PacketList<String, IPPacket>> getReceivedPacketArrayList() {
		return ip_packet_received;
	}

	// metodo che restituisce il nome delle colonne della tabella dei ricevuti

	public String[] getColumnReceived() {
		String[] toReturn = { "TIMESTAMP", "SOURCE", "PROTOCOL", "DST PORT", "SRC PORT" };
		return toReturn;
	}

	// metodo che restituisce il nome delle colonne della tabella degli inviati

	public String[] getColumnSent() {
		String[] toReturn = { "TIMESTAMP", "DESTINATION", "PROTOCOL", "SRC PORT", "DST PORT" };
		return toReturn;
	}

	// metodo che restituisce il numero di pacchetti ricevuti da un dato ip

	public synchronized int getNumIpPacketReceived(String ip) {
		if (ip_packet_received.get(ip) == null)
			return 0;
		return ip_packet_received.get(ip).size();
	}

	// metodo che inserisce un elemento nella tabella ip_mac

	public synchronized void putIpMac(String ipLog, String macLog) {

		if (ip_mac.findKeyPos(ipLog.substring(1)) < 0) {
			ip_mac.put(ipLog.substring(1), macLog.substring(1));
			// debug
			// System.out.println(">> PUT :  " + ip_mac.size() + "  "
			// + ipLog.substring(1) + "  -  " + macLog.substring(1));
		}
		// replace IP and his MAC address associated
		else {
			if (!ip_mac.get(ipLog.substring(1)).equals(macLog.substring(1))) {
				ip_mac.put(ipLog.substring(1), macLog.substring(1));
				// debug
				// System.out.println(">> PUT :  " + ip_mac.size() + "  "
				// + ipLog.substring(1) + "  -  " + macLog.substring(1));
			}
		}
	}

	// metodo che inserisce una connessione nella tabella from_to

	public synchronized void putFromTo(String ipLog, String toLog) {

		if (from_to.findKeyPos(ipLog.substring(1)) < 0) {
			ArrayList<String> tmp = new ArrayList<String>();
			// add IP receiver
			tmp.add(toLog.substring(1));
			from_to.put(ipLog.substring(1), tmp);

			// debug
			// String printLog = new String("");
			// ArrayList<String> allTo = from_to.get(ipLog.substring(1));
			// for (int i = 0; i < allTo.size(); i++) {
			// printLog += "   +   " + allTo.get(i);
			// }
			// System.out.println(">> PUT :  " + ipLog.substring(1) + "  -  "
			// + printLog);

		} else {
			// add IP receiver
			if (!from_to.get(ipLog.substring(1)).contains(toLog.substring(1))) {
				ArrayList<String> temp = from_to.get(ipLog.substring(1));

				// overwrite old record if the buffer is full
				if (temp.size() >= 5)
					from_to.get(ipLog.substring(1)).remove(0);
				from_to.get(ipLog.substring(1)).add(toLog.substring(1));

				// debug
				// String printLog = new String("");
				// ArrayList<String> allTo = from_to.get(ipLog.substring(1));
				// for (int i = 0; i < allTo.size(); i++) {
				// printLog += "   +   " + allTo.get(i);
				// }
				// System.out.println(">> PUT :  " + ipLog.substring(1) + "  -  "
				// + printLog);

			}
		}
	}

	// metodo che restituisce tutte le righe della tabella ip_mac

	public synchronized String[][] getEntriesIpMac() {

		int i = 0;
		String[][] entries = new String[ip_mac.size()][2];
		ArrayList<String> chiavi = ip_mac.getKeys();

		for (int k = 0; k < chiavi.size(); k++) {
			entries[k][0] = chiavi.get(k);
			entries[k][1] = ip_mac.get(chiavi.get(k));
		}
		
		return entries;
	}

	// metodo che resetta le tabelle alla pressione del tasto play

	public synchronized void resetTable() {
		from_to.clear();
		ip_mac.clear();
		ip_packet_received.clear();
		ip_packet_sent.clear();
	}
	
	
	
	static public boolean keyInIPMAC(String ip) {
		return ip_mac.findKeyPos(ip) > 0;
	}

}