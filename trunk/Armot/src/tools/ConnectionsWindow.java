package tools;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import net.miginfocom.swing.MigLayout;

public class ConnectionsWindow extends JFrame {

	private JPanel ricevutiPanel = new JPanel();
	private JPanel inviatiPanel = new JPanel();
	private JTable ricevutiTable;
	private JTable inviatiTable;
	//private ConnectionHandler handler;
	private ConnectionsUpdater updater;
	private Thread thread;
	private ConnectionPanel panel;

	public ConnectionsWindow(String ip) {
		super("Real Time Connections");
		this.addWindowListener(new WindowsClosing());
		this.setSize(500, 450);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("error setting system UI");
		}
		this.setIconImage(new ImageIcon(ClassLoader
				.getSystemResource("my-net.png")).getImage());
		Rectangle maxBounds = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getMaximumWindowBounds();
		this.setLocation(maxBounds.width / 2 - getWidth() / 2, maxBounds.height
				/ 2 - getHeight() / 2);
		Container content = this.getContentPane();
		content.setBackground(Color.WHITE);
		content.setLayout(new MigLayout());

		panel = new ConnectionPanel();
		updater = new ConnectionsUpdater(ip, this);
		thread = new Thread(updater);
		thread.setName("Updater");
		thread.setPriority(8);
		
		//handler = new ConnectionHandler(ip, this);
		//thread = new Thread(handler);
		//thread.setName("Handler");
		//thread.setPriority(8);
		add(new JLabel(ip + " connections:"), "wrap");
		add(panel);

		showMe();
	}

	public void showMe() {
		thread.start();
		setVisible(true);
	}

	private void hideMe() {
		// System.out.println("sto chiudendo la finestra!");
		thread.interrupt();
		setVisible(false);
	}

	public void updateTables(String[][] listIN, String[] columnNameIN,
			String[][] listOUT, String[] columnNameOUT) {
		panel.updateTables(listIN, columnNameIN, listOUT, columnNameOUT);
		pack();
	}
	
	public void createTables(DefaultTableModel modelIN, DefaultTableModel modelOUT) {
		panel.createTables(modelIN, modelOUT);
	}

	private class WindowsClosing implements WindowListener {

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowClosing(WindowEvent e) {
			hideMe();
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub

		}

	}

	private class ConnectionPanel extends JPanel {

		public ConnectionPanel() { // old
			super();
			setLayout(new MigLayout());
		}
		
		public void createTables(DefaultTableModel modelIN, DefaultTableModel modelOUT) {
			ricevutiTable = new JTable(modelIN);
			inviatiTable = new JTable(modelOUT);
			inviatiTable.setAutoCreateRowSorter(true);
			inviatiTable.setPreferredScrollableViewportSize(new Dimension(
					450, 500 / 2 - 27));
			inviatiTable.setFillsViewportHeight(true);
			JScrollPane scrollPane = new JScrollPane(inviatiTable);
			add(scrollPane, "cell 1 0");
			
			ricevutiTable.setAutoCreateRowSorter(true);
			ricevutiTable.setPreferredScrollableViewportSize(new Dimension(
					450, 500 / 2 - 27));
			ricevutiTable.setFillsViewportHeight(true);
			JScrollPane scrollPane2 = new JScrollPane(ricevutiTable);
			add(scrollPane2, "cell 1 1");
			
			JLabel ricevutiLabel = new JLabel(
					"<html>R<br> E<br>C<br>E<br>I<br>V<br>E<br> D</html>");
			ricevutiLabel.setForeground(Color.BLUE);
			ricevutiLabel.setFont(new Font("Arial", Font.BOLD, 16));
			ricevutiLabel.setBackground(new Color(173, 216, 230));
			ricevutiLabel.setOpaque(true);
			ricevutiLabel.setPreferredSize(new Dimension(30, 500 / 2 - 7));
			ricevutiLabel
					.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			ricevutiLabel.setHorizontalAlignment(JLabel.CENTER);
			JLabel inviatiLabel = new JLabel(
					"<html>S<br>E<br>N<br>T<br></html>");
			inviatiLabel.setForeground(Color.DARK_GRAY);
			inviatiLabel.setFont(new Font("Arial", Font.BOLD, 16));
			inviatiLabel.setBackground(new Color(211, 211, 211));
			inviatiLabel.setOpaque(true);
			inviatiLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			inviatiLabel.setPreferredSize(new Dimension(30, 500 / 2 - 7));
			inviatiLabel.setHorizontalAlignment(JLabel.CENTER);
			add(inviatiLabel, "cell 0 0");
			add(ricevutiLabel, "cell 0 1");

			paintAll(getGraphics());
		}

		public void updateTables(String[][] listIN, String[] columnNameIN,
				String[][] listOUT, String[] columnNameOUT) {

			// keep selected rows after update
			int firstSelectedRow = -1;
			int selectedColumn = -1;
			if (inviatiTable != null && ricevutiTable != null) {
				firstSelectedRow = inviatiTable.getSelectedRow();
				selectedColumn = inviatiTable.getSelectedColumn();
			}

			removeAll();

			System.out.println("update IN table, size: " + listIN.length);
			if (listIN != null) {
				inviatiTable = new JTable(listIN, columnNameIN);
				TableColumn column = null;
				for (int i = 0; i < 6; i++) {
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
				inviatiTable.setPreferredScrollableViewportSize(new Dimension(
						450, 500 / 2 - 27));
				inviatiTable.setFillsViewportHeight(true);
				JScrollPane scrollPane = new JScrollPane(inviatiTable);

				// restore selected row
				if (firstSelectedRow != -1) {
					Rectangle cellLocation = inviatiTable.getCellRect(
							firstSelectedRow, 0, false);
					scrollPane.getVerticalScrollBar().setValue(cellLocation.y);
				}

				add(scrollPane, "cell 1 0");
			}
			if (listOUT != null) {
				ricevutiTable = new JTable(listOUT, columnNameOUT);
				TableColumn column = null;
				for (int i = 0; i < 6; i++) {
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
				ricevutiTable.setPreferredScrollableViewportSize(new Dimension(
						450, 500 / 2 - 27));
				ricevutiTable.setFillsViewportHeight(true);
				JScrollPane scrollPane = new JScrollPane(ricevutiTable);
				add(scrollPane, "cell 1 1");
			}
			JLabel ricevutiLabel = new JLabel(
					"<html>R<br> E<br>C<br>E<br>I<br>V<br>E<br> D</html>");
			ricevutiLabel.setForeground(Color.BLUE);
			ricevutiLabel.setFont(new Font("Arial", Font.BOLD, 16));
			ricevutiLabel.setBackground(new Color(173, 216, 230));
			ricevutiLabel.setOpaque(true);
			ricevutiLabel.setPreferredSize(new Dimension(30, 500 / 2 - 7));
			ricevutiLabel
					.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			ricevutiLabel.setHorizontalAlignment(JLabel.CENTER);
			JLabel inviatiLabel = new JLabel(
					"<html>S<br>E<br>N<br>T<br></html>");
			inviatiLabel.setForeground(Color.DARK_GRAY);
			inviatiLabel.setFont(new Font("Arial", Font.BOLD, 16));
			inviatiLabel.setBackground(new Color(211, 211, 211));
			inviatiLabel.setOpaque(true);
			inviatiLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			inviatiLabel.setPreferredSize(new Dimension(30, 500 / 2 - 7));
			inviatiLabel.setHorizontalAlignment(JLabel.CENTER);
			add(inviatiLabel, "cell 0 0");
			add(ricevutiLabel, "cell 0 1");

			paintAll(getGraphics()); 
		}
	}
}
