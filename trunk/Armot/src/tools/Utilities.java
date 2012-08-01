package tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

public class Utilities {

	// metodo che controlla lo stato del farwarding

	public static boolean checkForward() {
		String toReturn = "";
		try {
			Process cat = Runtime.getRuntime().exec(
					"cat /proc/sys/net/ipv4/ip_forward");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					cat.getInputStream()));
			toReturn = "";
			String partial = "";
			while ((partial = reader.readLine()) != null)
				toReturn += partial;
			if (toReturn.equals("1"))
				return true;
			else
				return false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	// metodo che abilita il farwarding

	public static boolean enableForward() {
		String toReturn = "";
		try {
			Process cat = Runtime.getRuntime().exec(
					"sysctl net.ipv4.ip_forward=1");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					cat.getInputStream()));
			toReturn = "";
			String partial = "";
			while ((partial = reader.readLine()) != null)
				toReturn += partial;
			System.out.println(toReturn);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	// metodo che disabilita il farwarding

	public static boolean disableForward() {
		String toReturn = "";
		try {
			Process cat = Runtime.getRuntime().exec(
					"sysctl net.ipv4.ip_forward=0");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					cat.getInputStream()));
			toReturn = "";
			String partial = "";
			while ((partial = reader.readLine()) != null)
				toReturn += partial;
			System.out.println(toReturn);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	// metodo che stampa un log dei pacchetti di un dato ip

	public static String printPackets(String ip) {
		String packets = new String();
		if (Table.ip_packet_received != null
				&& Table.ip_packet_received.get(ip) != null) {
			Enumeration<String> key = Table.ip_packet_received.get(ip).keys();
			while (key.hasMoreElements()) {
				String chiave = key.nextElement();
				packets = packets
						+ "RICEVUTO | "
						+ chiave
						+ " | "
						+ Table.ip_packet_received.get(ip).get(chiave)
								.toString() + "\n";
			}
		}
		if (Table.ip_packet_sent != null
				&& Table.ip_packet_sent.get(ip) != null) {
			Enumeration<String> key2 = Table.ip_packet_sent.get(ip).keys();
			while (key2.hasMoreElements()) {
				String chiave = key2.nextElement();
				packets = packets + "INVIATO | " + chiave + " | "
						+ Table.ip_packet_sent.get(ip).get(chiave).toString()
						+ "\n";
			}
		}
		return packets;
	}
}
