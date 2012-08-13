package tools;

import java.util.Hashtable;
import jpcap.JpcapSender;

public class FakeHandler {

	private static Hashtable<String, Thread> handler;

	public FakeHandler() {
		if (handler == null) {
			handler = new Hashtable<String, Thread>();
		}
	}

	public void addThread(String ip, String targetIP, byte[] myMac,
			JpcapSender sender) {
		Thread packetSender = new Thread(
				new Sender(targetIP, ip, myMac, sender));
		packetSender.start();
		handler.put(ip + "-" + targetIP, packetSender);
		// System.out.println("FakeHandler: adding " + ip + "-" + targetIP); // +++++ DEBUG +++++
	}

	public boolean isActive(String ip, String targetIP) {
		return handler.containsKey(ip + "-" + targetIP);
	}

	public void stopThread(String ip, String targetIP) {
		Thread packetSender = handler.get(ip + "-" + targetIP);
		if (packetSender != null) {
			packetSender.interrupt();
			// System.out.println("FakeHandler: removed " + ip + "-" + targetIP); // +++++ DEBUG +++++
		} else
			System.err.println("FakeHandler: can't close Fake Thread from "
					+ ip + " to " + targetIP); // +++++ DEBUG +++++
	}

}
