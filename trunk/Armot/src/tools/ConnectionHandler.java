package tools;

import java.util.ArrayList;
import java.util.Hashtable;

public class ConnectionHandler implements Runnable {

	private String ip;
	private String[][] listIN; // IP RICEVENTE - D PORT - S PORT - PROTOCOLLO -
								// SPEED - CONTATORE PACCHETTI
	private String[][] listOUT;// IP RICEVENTE - S PORT - D PORT - PROTOCOLLO -
								// SPEED - CONTATORE PACCHETTI
	private Table table;
	private ConnectionsWindow reference;
	private int lengthIN;
	private int lengthOUT;
	private String[] columnNameIN;
	private String[] columnNameOUT;
	private boolean stop;

	public ConnectionHandler(String ip, ConnectionsWindow reference) {
		this.ip = ip;
		table = new Table();
		this.populateLists();
		this.reference = reference;
		this.lengthIN = 0;
		this.lengthOUT = 0;
		this.columnNameIN = new String[]{"ip", "d port", "s port", "protocol", "speed", "counter"};
		this.columnNameOUT = new String[]{"ip", "s port", "d port", "protocol", "speed", "counter"};
		stop = false;
	}

	private void populateLists() {
		System.out.println("starting populating list...");
		String[][] matrix = table.getIpPacketSent(ip); // "TIMESTAMP",
														// "DESTINATION",
														// "PROTOCOL",
														// "SRC PORT",
														// "DST PORT"
		ArrayList<String> alreadyAdd = new ArrayList<String>();
		if (matrix != null) {
			listIN = new String[matrix.length][6];
			lengthIN = 0;
			for (int i = 0; i < matrix.length; i++) {
				if (matrix[i][1] != null && matrix[i][2] != null
						&& matrix[i][3] != null && matrix[i][4] != null)
					if (!alreadyAdd.contains("" + matrix[i][3] + matrix[i][4])) {
						System.out.println(matrix[i][1] + " - " + matrix[i][4]
								+ " - " + matrix[i][3] + " - " + matrix[i][2]
								+ " - ");
						listIN[lengthIN][0] = matrix[i][1];
						listIN[lengthIN][1] = matrix[i][4];
						listIN[lengthIN][2] = matrix[i][3];
						listIN[lengthIN][3] = matrix[i][2];
						listIN[lengthIN][4] = "0";
						listIN[lengthIN][5] = ""
								+ table.countIpPacketSentByPort(ip,
										Integer.parseInt(listIN[lengthIN][2]),
										Integer.parseInt(listIN[lengthIN][1]));
						alreadyAdd.add("" + matrix[i][3] + matrix[i][4]);
						lengthIN++;
					}
				// listIN.add(new Connection(matrix[i][0],
				// Integer.parseInt(matrix[i][4]),
				// Integer.parseInt(matrix[i][2]),
				// table.countIpPacketSentByPort(ip,
				// Integer.parseInt(matrix[i][4]),
				// Integer.parseInt(matrix[i][2]))));
			}
			// fix listIN length
			String[][] temp = new String[lengthIN][6];
			for(int i = 0; i < lengthIN; i++) {
				for(int j = 0; j < 6; j++) {
					temp[i][j] = listIN[i][j];
				}
			}
			listIN = temp;
		} else {
			listIN = new String[0][0];
		}
		matrix = table.getIpPacketReceived(ip); // "TIMESTAMP", "SOURCE",
												// "PROTOCOL", "DST PORT",
												// "SRC PORT"
		if (matrix != null) {
			listOUT = new String[matrix.length][6];
			lengthOUT = 0;
			alreadyAdd = new ArrayList<String>();
			for (int i = 0; i < matrix.length; i++) {
				if (matrix[i][1] != null && matrix[i][2] != null
						&& matrix[i][3] != null && matrix[i][4] != null)
					if (!alreadyAdd.contains("" + matrix[i][3] + matrix[i][4])) {
						listOUT[lengthOUT][0] = matrix[i][1];
						listOUT[lengthOUT][1] = matrix[i][4];
						listOUT[lengthOUT][2] = matrix[i][3];
						listOUT[lengthOUT][3] = matrix[i][2];
						listOUT[lengthOUT][4] = "0";
						listOUT[lengthOUT][5] = ""
								+ table.countIpPacketReceivedByPort(
										ip,
										Integer.parseInt(listOUT[lengthOUT][1]),
										Integer.parseInt(listOUT[lengthOUT][2]));
						alreadyAdd.add("" + matrix[i][3] + matrix[i][4]);
						lengthOUT++;
					}
			}
			// fix listOUT length
			String[][] temp = new String[lengthOUT][6];
			for(int i = 0; i < lengthOUT; i++) {
				for(int j = 0; j < 6; j++) {
					temp[i][j] = listOUT[i][j];
				}
			}
			listOUT = temp;
		} else {
			listOUT = new String[0][0];
		}
		System.out.println("list populated!");
	}

	private void updateLists() {
		System.out.println("lenghtIN: " + lengthIN);
		for (int i = 0; i < lengthIN; i++) {
			int newCount = table.countIpPacketSentByPort(ip,
					Integer.parseInt(listIN[i][2]),
					Integer.parseInt(listIN[i][1]));
			int delta = newCount - Integer.parseInt(listIN[i][5]);
			listIN[i][5] = "" + newCount;
			listIN[i][4] = "" + (delta * 65535 / 1000);
			System.out.println("DEBUG --- update " + listIN[i][2] + " done, speed " + listIN[i][4]);
			/*
			 * Connection connection = listIN.get(i); int newCount =
			 * table.countIpPacketSentByPort(ip, connection.getSrcPort(),
			 * connection.getDstPort()); int delta = newCount -
			 * connection.getPacketCount(); listIN.get(i).setCount(newCount);
			 * listIN.get(i).setSpeed(delta*65535/1000); //65535 o 1526? result
			 * in Kbps
			 */
		}
		System.out.println("lengthOUT: " + lengthOUT);
		for (int i = 0; i < lengthOUT; i++) {
			int newCount = table.countIpPacketReceivedByPort(ip,
					Integer.parseInt(listOUT[i][1]),
					Integer.parseInt(listOUT[i][2]));
			int delta = newCount - Integer.parseInt(listOUT[i][5]);
			listOUT[i][5] = "" + newCount;
			listOUT[i][4] = "" + (delta * 65535 / 1000);
			/*
			 * Connection connection = listOUT.get(i); int newCount =
			 * table.countIpPacketReceivedByPort(ip, connection.getSrcPort(),
			 * connection.getDstPort()); int delta = newCount -
			 * connection.getPacketCount(); listOUT.get(i).setCount(newCount);
			 * listOUT.get(i).setSpeed(delta*65535/1000); // Kbps
			 */
		}
	}
	
	private void stop() {
		this.stop = true;
	}

	@Override
	public void run() {
		updateLists();
		try {
			Thread.currentThread().sleep(1000L);
		} catch (InterruptedException e) {
			stop();
		}
		int counter = 0;
		while (!stop) {
			try {
				if (counter < 5) {
					updateLists();
					counter++;
				} else {
					populateLists();
					counter = 0;
				}
				reference.updateTables(listIN, columnNameIN, listOUT, columnNameOUT);
				//reference.updateINTable(listIN, columnNameIN);
				//reference.updateOUTTable(listOUT, columnNameOUT);

				Thread.currentThread().sleep(1000L);

			} catch (InterruptedException e) {
				stop();
			}
		}

	}
	/*
	 * private class Connection { private String dst_ip; private int src_port;
	 * private int dst_port; private int packetCount; private float speed;
	 * 
	 * public Connection(String dst_ip, int src_port, int dst_port, int
	 * packetCount) { this.dst_ip = dst_ip; this.src_port = src_port;
	 * this.dst_port = dst_port; this.dst_ip = dst_ip; this.packetCount =
	 * packetCount; this.speed = 0; }
	 * 
	 * public void setCount(int newCount) { packetCount = newCount; }
	 * 
	 * public void setSpeed(float speed) { this.speed = speed; }
	 * 
	 * public float getSpeed() { return speed; }
	 * 
	 * public int getSrcPort() { return src_port; }
	 * 
	 * public int getDstPort() { return dst_port; }
	 * 
	 * public String getDstIP() { return dst_ip; }
	 * 
	 * public int getPacketCount() { return packetCount; } }
	 */

}