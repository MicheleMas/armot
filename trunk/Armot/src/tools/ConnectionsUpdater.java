package tools;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class ConnectionsUpdater implements Runnable {

	private ArrayListConnection<String, String[]> dataIN;
	private ArrayListConnection<String, String[]> dataOUT;
	private Table table;
	private String ip;
	private DefaultTableModel modelIN;
	private DefaultTableModel modelOUT;
	private ConnectionsWindow reference;

	public ConnectionsUpdater(String ip, ConnectionsWindow reference) {
		this.reference = reference;
		this.table = new Table();
		this.ip = ip;
		dataIN = new ArrayListConnection<String, String[]>();
		dataOUT = new ArrayListConnection<String, String[]>();
		this.populateTables();
	}

	public void populateTables() {

		String[] columnNameIN = new String[] { "IP", "S port", "D port",
				"Protocol", "Speed", "Counter" };
		String[] columnNameOUT = new String[] { "IP", "D port", "S port",
				"Protocol", "Speed", "Counter" };

		// populate dataIN
		String[][] matrix = table.getIpPacketReceived(ip); // "TIMESTAMP",
															// "SOURCE",
															// "PROTOCOL",
															// "DST PORT",
															// "SRC PORT"
		if (matrix != null) {
			for (int i = 0; i < matrix.length; i++) {
				if (matrix[i][1] != null && matrix[i][2] != null
						&& matrix[i][3] != null && matrix[i][4] != null) {
					if (!dataIN.containsKey(matrix[i][1] + " " + matrix[i][4]
							+ " " + matrix[i][3])) {
						System.out.println("R debug! : " + matrix[i][0] + " "
								+ matrix[i][1] + " " + matrix[i][2] + " "
								+ matrix[i][3] + " " + matrix[i][4]);
						String[] row = new String[] { matrix[i][1], // IP
								matrix[i][4], // S PORT
								matrix[i][3], // D PORT
								matrix[i][2], // PROTOCOL
								"0", // SPEED
								""
										+ table.countIpPacketReceivedByPort(ip,
												Integer.parseInt(matrix[i][4]),
												Integer.parseInt(matrix[i][3])) }; // COUNTER
						dataIN.put(matrix[i][1] + " " + matrix[i][4] + " "
								+ matrix[i][3], row);
					}
				}
			}
		}

		// populate dataOUT
		matrix = table.getIpPacketSent(ip); // "TIMESTAMP", "DESTINATION",
											// "PROTOCOL", "SRC PORT",
											// "DST PORT"
		if (matrix != null) {
			for (int i = 0; i < matrix.length; i++) {
				if (matrix[i][1] != null && matrix[i][2] != null
						&& matrix[i][3] != null && matrix[i][4] != null) {
					if (!dataOUT.containsKey(matrix[i][1] + " " + matrix[i][3]
							+ " " + matrix[i][4])) {
						System.out.println("S debug! : " + matrix[i][0]
								+ matrix[i][1] + matrix[i][2] + matrix[i][3]
								+ matrix[i][4]);
						String[] row = new String[] { matrix[i][1], // IP
								matrix[i][4], // D PORT
								matrix[i][3], // S PORT
								matrix[i][2], // PROTOCOL
								"0", // SPEED
								""
										+ table.countIpPacketSentByPort(ip,
												Integer.parseInt(matrix[i][3]),
												Integer.parseInt(matrix[i][4])) }; // COUNTER
						dataOUT.put(matrix[i][1] + " " + matrix[i][3] + " "
								+ matrix[i][4], row);
					}
				}
			}
		}

		// create table with lists informations
		int size = dataIN.size();
		String[][] tableIN = new String[size][6];
		Enumeration<String> e = dataIN.keys();
		int counter = 0;
		while (e.hasMoreElements() && counter < size) {
			String key = e.nextElement();
			tableIN[counter] = dataIN.get(key);
		}

		size = dataOUT.size();
		String[][] tableOUT = new String[size][6];
		e = dataOUT.keys();
		counter = 0;
		while (e.hasMoreElements() && counter < size) {
			String key = e.nextElement();
			tableOUT[counter] = dataOUT.get(key);
		}

		modelIN = new DefaultTableModel(tableIN, columnNameIN);
		modelOUT = new DefaultTableModel(tableOUT, columnNameOUT);
		reference.createTables(modelIN, modelOUT);
	}

	private void updateLists() {

		// check dataIN update
		String[][] matrix = table.getIpPacketReceived(ip); // "TIMESTAMP",
															// "SOURCE",
															// "PROTOCOL",
															// "DST PORT",
															// "SRC PORT"
		if (matrix != null) {
			for (int i = 0; i < matrix.length; i++) {
				if (matrix[i][1] != null && matrix[i][2] != null
						&& matrix[i][3] != null && matrix[i][4] != null) {
					if (!dataIN.containsKey(matrix[i][1] + " " + matrix[i][4]
							+ " " + matrix[i][3])) {
						String[] row = new String[] { matrix[i][1], // IP
								matrix[i][3], // D PORT
								matrix[i][4], // S PORT
								matrix[i][2], // PROTOCOL
								"0", // SPEED
								""
										+ table.countIpPacketReceivedByPort(ip,
												Integer.parseInt(matrix[i][4]),
												Integer.parseInt(matrix[i][3])) }; // COUNTER
						dataIN.put(matrix[i][1] + " " + matrix[i][4] + " "
								+ matrix[i][3], row);
						modelIN.addRow(row);

					}
				}
			}
		}

		// check dataOUT update
		matrix = table.getIpPacketSent(ip); // "TIMESTAMP", "DESTINATION",
											// "PROTOCOL", "SRC PORT",
											// "DST PORT"
		if (matrix != null) {
			for (int i = 0; i < matrix.length; i++) {
				if (matrix[i][1] != null && matrix[i][2] != null
						&& matrix[i][3] != null && matrix[i][4] != null) {
					if (!dataOUT.containsKey(matrix[i][1] + " " + matrix[i][3]
							+ " " + matrix[i][4])) {
						String[] row = new String[] { matrix[i][1], // IP
								matrix[i][4], // D PORT
								matrix[i][3], // S PORT
								matrix[i][2], // PROTOCOL
								"0", // SPEED
								""
										+ table.countIpPacketSentByPort(ip,
												Integer.parseInt(matrix[i][3]),
												Integer.parseInt(matrix[i][4])) }; // COUNTER
						dataOUT.put(matrix[i][1] + " " + matrix[i][3] + " "
								+ matrix[i][4], row);
						modelOUT.addRow(row);
					}
				}
			}
		}

	}

	private void updateSpeeds() {
		// this will be called every second

		// dataIN update
		int oldCounter;
		int newCounter;
		int speed;
		ArrayList<String> keys;
		String[] array;
		keys = dataIN.getKeys();
		for (int i = 0; i < keys.size(); i++) {
			array = dataIN.get(keys.get(i));
			oldCounter = Integer.parseInt(array[5]);
			newCounter = table.countIpPacketReceivedByPort(array[0],
					Integer.parseInt(array[1]), Integer.parseInt(array[2]));
			speed = ((newCounter - oldCounter) * 65535 / 100000); // TODO check
																	// speed
			dataIN.get(keys.get(i))[5] = "" + newCounter; // update counter
			modelOUT.setValueAt("" + newCounter, i, 5);
			modelIN.setValueAt("" + speed, i, 4);
		}

		// dataOUT update
		keys = dataOUT.getKeys();
		for (int i = 0; i < keys.size(); i++) {
			array = dataOUT.get(keys.get(i));
			oldCounter = Integer.parseInt(array[5]);
			newCounter = table.countIpPacketReceivedByPort(array[0],
					Integer.parseInt(array[1]), Integer.parseInt(array[2]));
			speed = ((newCounter - oldCounter) * 65535 / 100000); // TODO check
																	// speed
			dataOUT.get(keys.get(i))[5] = "" + newCounter; // update counter
			modelOUT.setValueAt("" + newCounter, i, 5);
			modelOUT.setValueAt("" + speed, i, 4);
		}
	}

	@Override
	public void run() {
		boolean stop = false;
		while (!stop) {
			try {
				Thread.currentThread().sleep(1000L);
				updateLists();
				updateSpeeds();
			} catch (InterruptedException e) {
				stop = true;
			}
		}

	}

}
