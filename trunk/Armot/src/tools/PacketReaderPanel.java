package tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import net.miginfocom.swing.MigLayout;


public class PacketReaderPanel extends JPanel {


	private JPanel ricevutiPanel = new JPanel();
	private JPanel inviatiPanel = new JPanel();
	private JTable ricevutiTable;
	private JTable inviatiTable;

	private Table table;

	public PacketReaderPanel(int width, int height, String ip, Table table) {
		super();
		this.table = table;
		setLayout(new MigLayout());

		updateTable(ip, width, height);

	}

	public void updateTable(String ip, int width, int height) {
		removeAll();
		if (table.getIpPacketSent(ip) != null) {
			inviatiTable = new JTable(table.getIpPacketSent(ip), table.getColumnSent());
			TableColumn column = null;
			for (int i = 0; i < 5; i++) {
				column = inviatiTable.getColumnModel().getColumn(i);
				if (i == 0) {
					column.setPreferredWidth(105); // third column is bigger
				} else if (i == 1) {
					column.setPreferredWidth(30); // third column is bigger
				} else {
					column.setPreferredWidth(10);
				}
			}
			inviatiTable.setAutoCreateRowSorter(true);
			inviatiTable.setPreferredScrollableViewportSize(new Dimension(width, height / 2 - 27));
			inviatiTable.setFillsViewportHeight(true);
			JScrollPane scrollPane = new JScrollPane(inviatiTable);
			add(scrollPane, "cell 1 0");
		}
		if (table.getIpPacketReceived(ip) != null) {
			ricevutiTable = new JTable(table.getIpPacketReceived(ip), table.getColumnReceived());
			TableColumn column = null;
			for (int i = 0; i < 5; i++) {
				column = ricevutiTable.getColumnModel().getColumn(i);
				if (i == 0) {
					column.setPreferredWidth(105); // third column is bigger
				} else if (i == 1) {
					column.setPreferredWidth(30); // third column is bigger
				} else {
					column.setPreferredWidth(10);
				}
			}
			ricevutiTable.setAutoCreateRowSorter(true);
			ricevutiTable.setPreferredScrollableViewportSize(new Dimension(width, height / 2 - 27));
			ricevutiTable.setFillsViewportHeight(true);
			JScrollPane scrollPane = new JScrollPane(ricevutiTable);
			add(scrollPane, "cell 1 1");
		}
		JLabel ricevutiLabel = new JLabel("<html>R<br> E<br>C<br>E<br>I<br>V<br>E<br> D</html>");
		ricevutiLabel.setForeground(Color.BLUE);
		ricevutiLabel.setFont(new Font("Arial", Font.BOLD, 16));
		ricevutiLabel.setBackground(new Color(173, 216, 230));
		ricevutiLabel.setOpaque(true);
		ricevutiLabel.setPreferredSize(new Dimension(30, height / 2 - 7));
		ricevutiLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		ricevutiLabel.setHorizontalAlignment(JLabel.CENTER);
		JLabel inviatiLabel = new JLabel("<html>S<br>E<br>N<br>T<br></html>");
		inviatiLabel.setForeground(Color.DARK_GRAY);
		inviatiLabel.setFont(new Font("Arial", Font.BOLD, 16));
		inviatiLabel.setBackground(new Color(211, 211, 211));
		inviatiLabel.setOpaque(true);
		inviatiLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		inviatiLabel.setPreferredSize(new Dimension(30, height / 2 - 7));
		inviatiLabel.setHorizontalAlignment(JLabel.CENTER);
		add(inviatiLabel, "cell 0 0");
		add(ricevutiLabel, "cell 0 1");

		paintAll(getGraphics());
	}
}
