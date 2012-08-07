package tools;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class ConnectionsUpdater implements Runnable {

	private ArrayListConnection<String, String[]> dataIN;
	private ArrayListConnection<String, String[]> dataOUT;
	private JTable ricevutiTable;
	private JTable inviatiTable;
	private Table table;
	private String ip;

	public ConnectionsUpdater(JTable ricevutiTable, JTable inviatiTable,
			String ip) {
		this.ricevutiTable = ricevutiTable;
		this.inviatiTable = inviatiTable;
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
		String[][] matrix = table.getIpPacketReceived(ip);// IP,TIMESTAMP,S
															// PORT,PROTOCOLLO,D
															// PORT
		if (matrix != null) {
			for (int i = 0; i < matrix.length; i++) {
				if (!dataIN.containsKey(matrix[i][0] + " " + matrix[i][2] + " "
						+ matrix[i][4])) {
					String[] row = new String[] { matrix[i][0], // IP
							matrix[i][2], // S PORT
							matrix[i][4], // D PORT
							matrix[i][3], // PROTOCOL
							"0", // SPEED
							""
									+ table.countIpPacketReceivedByPort(ip,
											Integer.parseInt(matrix[i][2]),
											Integer.parseInt(matrix[i][4])) }; // COUNTER
					dataIN.put(matrix[i][0] + " " + matrix[i][2] + " "
							+ matrix[i][4], row);
				}
			}
		}

		// populate dataOUT
		matrix = table.getIpPacketSent(ip);// IP-TIMESTAMP-D PORT-PROTOCOLLO-S
											// PORT
		if (matrix != null) {
			for (int i = 0; i < matrix.length; i++) {
				if (!dataOUT.containsKey(matrix[i][0] + " " + matrix[i][2]
						+ " " + matrix[i][4])) {
					String[] row = new String[] { matrix[i][0], // IP
							matrix[i][2], // D PORT
							matrix[i][4], // S PORT
							matrix[i][3], // PROTOCOL
							"0", // SPEED
							""
									+ table.countIpPacketReceivedByPort(ip,
											Integer.parseInt(matrix[i][4]),
											Integer.parseInt(matrix[i][2])) }; // COUNTER
					dataOUT.put(matrix[i][0] + " " + matrix[i][2] + " "
							+ matrix[i][4], row);
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

		ricevutiTable = new JTable(new DefaultTableModel(tableIN, columnNameIN));
		inviatiTable = new JTable(
				new DefaultTableModel(tableOUT, columnNameOUT));

	}

	private void updateLists() {

		DefaultTableModel model = (DefaultTableModel) ricevutiTable.getModel();

		// check dataIN update
		String[][] matrix = table.getIpPacketReceived(ip);
		if (matrix != null) {
			for (int i = 0; i < matrix.length; i++) {
				if (!dataOUT.containsKey(matrix[i][0] + " " + matrix[i][2]
						+ " " + matrix[i][4])) {
					String[] row = new String[] { matrix[i][0], // IP
							matrix[i][2], // D PORT
							matrix[i][4], // S PORT
							matrix[i][3], // PROTOCOL
							"0", // SPEED
							""
									+ table.countIpPacketReceivedByPort(ip,
											Integer.parseInt(matrix[i][4]),
											Integer.parseInt(matrix[i][2])) }; // COUNTER
					dataOUT.put(matrix[i][0] + " " + matrix[i][2] + " "
							+ matrix[i][4], row);
					model.addRow(row);

				}
			}
		}

		// check dataOUT update
		model = (DefaultTableModel) inviatiTable.getModel();
		matrix = table.getIpPacketSent(ip);// IP-TIMESTAMP-D PORT-PROTOCOLLO-S PORT
		if (matrix != null) {
			for (int i = 0; i < matrix.length; i++) {
				if (!dataOUT.containsKey(matrix[i][0] + " " + matrix[i][2]
						+ " " + matrix[i][4])) {
					String[] row = new String[] { matrix[i][0], // IP
							matrix[i][2], // D PORT
							matrix[i][4], // S PORT
							matrix[i][3], // PROTOCOL
							"0", // SPEED
							""
									+ table.countIpPacketReceivedByPort(ip,
											Integer.parseInt(matrix[i][4]),
											Integer.parseInt(matrix[i][2])) }; // COUNTER
					dataOUT.put(matrix[i][0] + " " + matrix[i][2] + " "
							+ matrix[i][4], row);
					model.addRow(row);
				}
			}
		}

	}

	private void updateSpeeds() {
		// this will be called every second

		// dataIN update
		TableModel model = ricevutiTable.getModel();
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
			model.setValueAt("" + speed, i, 4);
		}

		// dataOUT update
		model = inviatiTable.getModel();
		keys = dataOUT.getKeys();
		for (int i = 0; i < keys.size(); i++) {
			array = dataOUT.get(keys.get(i));
			oldCounter = Integer.parseInt(array[5]);
			newCounter = table.countIpPacketReceivedByPort(array[0],
					Integer.parseInt(array[1]), Integer.parseInt(array[2]));
			speed = ((newCounter - oldCounter) * 65535 / 100000); // TODO check
																	// speed
			dataOUT.get(keys.get(i))[5] = "" + newCounter; // update counter
			model.setValueAt("" + speed, i, 4);
		}
	}
	
	@Override
	public void run() {
		boolean stop = false;
		while(!stop) {
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
