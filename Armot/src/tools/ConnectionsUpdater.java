package tools;

import java.util.Enumeration;

import javax.swing.JTable;

public class ConnectionsUpdater implements Runnable {

	private ArrayListConnection<String, String[]> dataIN;
	private ArrayListConnection<String, String[]> dataOUT;
	private JTable ricevutiTable;
	private JTable inviatitable;
	private Table table;
	private String ip;

	public ConnectionsUpdater(JTable ricevutiTable, JTable inviatiTable,
			String ip) {
		this.ricevutiTable = ricevutiTable;
		this.inviatitable = inviatiTable;
		this.table = new Table();
		this.ip = ip;
		dataIN = new ArrayListConnection<String, String[]>();
		dataOUT = new ArrayListConnection<String, String[]>();
		this.populateTables();
	}

	public void populateTables() {
		
		String[] columnName = new String[]{"IP", "S port", "D port", "Protocol", "Speed", "Counter"};
		
		// populate dataIN
		String[][] matrix = table.getIpPacketReceived(ip);// IP,TIMESTAMP,S PORT,PROTOCOLLO,D PORT
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
		matrix = table.getIpPacketSent(ip);// IP-TIMESTAMP-D PORT-PROTOCOLLO-S PORT
		if (matrix != null) {
			for (int i = 0; i < matrix.length; i++) {
				if (!dataOUT.containsKey(matrix[i][0] + " " + matrix[i][2] + " "
						+ matrix[i][4])) {
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
		while(e.hasMoreElements() && counter < size) {
			String key = e.nextElement();
			tableIN[counter] = dataIN.get(key);
		}
		
		size = dataOUT.size();
		String[][] tableOUT = new String[size][6];
		e = dataOUT.keys(); 
		counter = 0;
		while(e.hasMoreElements() && counter < size) {
			String key = e.nextElement();
			tableOUT[counter] = dataOUT.get(key);
		}
		
		ricevutiTable = new JTable(tableIN, columnName);

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
