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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;

import net.miginfocom.swing.MigLayout;


public class PacketReaderWindow extends JFrame {

	/**
	 * menu constampa, stampa tutti
	 */
	private JMenuBar menubar = new JMenuBar() {
		/** Stroke size. it is recommended to set it to 1 for better view */
		protected int strokeSize = 1;
		/** Double values for Horizzontal and Vertical radius of corner arcs */
		protected Dimension arcs = new Dimension(3, 3);
		/** Distance between border of shadow and border of opaque panel */
		protected int shadowGap = 0;;

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			int width = getWidth();
			int height = getHeight();
			int shadowGap = this.shadowGap;
			Graphics2D graphics = (Graphics2D) g;

			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			// 2 colori che fanno la sfumatura

			Point2D.Float p1 = new Point2D.Float(0.f, 37.f); // Gradient line
			// start
			Point2D.Float p2 = new Point2D.Float(0.f, 0.f); // Gradient line end
			GradientPaint g1 = new GradientPaint(p1, new Color(130, 130, 130), p2, new Color(204, 204, 204), true);
			graphics.setPaint(g1);
			graphics.fillRoundRect(0// X position
					, 0,// Y position
					width - strokeSize, // width
					height - strokeSize, // height
					arcs.width, arcs.height);// arc Dimension

			// Draws the rounded opaque panel with borders.

			graphics.fillRoundRect(0, 0, width - shadowGap, height - shadowGap - 10, arcs.width, arcs.height);
			graphics.setColor(Color.BLACK);
			graphics.setStroke(new BasicStroke(strokeSize));

			graphics.drawRoundRect(0, 0, width - shadowGap, height - shadowGap - 3, arcs.width, arcs.height);

			// Sets strokes to default, is better.
			graphics.setStroke(new BasicStroke());
			// graphics.drawString(getText(), 20, 20);
		}
	};

	private JButton save = new JButton("SAVE", new ImageIcon(ClassLoader.getSystemResource("save.png")));

	private JButton saveAll = new JButton("SAVE ALL", new ImageIcon(ClassLoader.getSystemResource("saveall.png")));

	private JButton update = new JButton("UPDATE", new ImageIcon(ClassLoader.getSystemResource("update.png")));

	/**
	 * punta alla frame principale
	 */
	private MainWindow mainWIndow;

	private int width;
	private int height;

	/**
	 * tab degli ip selezionati
	 */
	private JTabbedPane tab;

	/**
	 * ip aperti
	 */
	private ArrayList<String> ipInTheTab = new ArrayList<String>();

	private Table table;

	public PacketReaderWindow(int width, int height, MainWindow mainWindow, Table table) {
		super("Packets reader");
		this.table = table;
		tab = new JTabbedPane();
		this.mainWIndow = mainWindow;
		this.width = width;
		this.height = height;
		this.setIconImage(new ImageIcon(ClassLoader.getSystemResource("my-net.png")).getImage());
		this.setLayout(new MigLayout());
		this.setSize(width / 2, height);
		this.setJMenuBar(menubar);
		this.addWindowListener(new WindowsClosing());

		save.setContentAreaFilled(false);
		save.setRolloverEnabled(true);
		save.setBorder(null);
		save.setForeground(Color.WHITE);
		save.setFont(new Font("Arial", Font.BOLD, 16));
		save.setRolloverIcon(new ImageIcon(ClassLoader.getSystemResource("savepress.png")));
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				SavePacketWindow saveWindow = new SavePacketWindow(tab.getTitleAt(tab.getTabRunCount() - 1));
				saveWindow.showMe();
				
				// TODO add "already open" check
				
				/*
				Properties pc = System.getProperties();
				String homePath = pc.getProperty("user.home");
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(homePath));
				fc.setSelectedFile(new File(tab.getTitleAt(tab.getTabRunCount() - 1) + ".txt"));
				int returnVal = fc.showSaveDialog(PacketReaderWindow.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					// This is where a real application would save the file.
					if (!file.getAbsolutePath().endsWith(".txt")) {
						file = new File(file.getAbsolutePath() + ".txt");
					}
					try {
						file.createNewFile();
						try {
							file.setWritable(true);
							FileWriter fw = new FileWriter(file);
							BufferedWriter bw = new BufferedWriter(fw);
							PrintWriter outFile = new PrintWriter(bw);

							String print = Utilities.printPackets(tab.getTitleAt(tab.getTabRunCount() - 1));
							outFile.println(print);

							outFile.close();
							file.setReadOnly();
							file.setWritable(false);

						} catch (FileNotFoundException e1) {
							JOptionPane.showMessageDialog(null, "Riprova");
							e1.printStackTrace();

						} catch (IOException er) {
							JOptionPane.showMessageDialog(null, "Riprova");
							er.printStackTrace();

						}
					} catch (IOException e2) {
						JOptionPane.showMessageDialog(null, "Riprova");
						e2.printStackTrace();
					}

				} */
			}
		});

		saveAll.setContentAreaFilled(false);
		saveAll.setRolloverEnabled(true);
		saveAll.setBorder(null);
		saveAll.setForeground(Color.WHITE);
		saveAll.setFont(new Font("Arial", Font.BOLD, 16));
		saveAll.setRolloverIcon(new ImageIcon(ClassLoader.getSystemResource("saveallpress.png")));
		saveAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				SavePacketWindow saveWindow = new SavePacketWindow();
				saveWindow.showMe();
				/*
				Properties pc = System.getProperties();
				String homePath = pc.getProperty("user.home");
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(homePath));
				fc.setSelectedFile((new File("my-net_" + Calendar.getInstance().get(Calendar.YEAR) + "_"
						+ (Calendar.getInstance().get(Calendar.MONTH) + 1) + "_" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH))));
				int returnVal = fc.showSaveDialog(PacketReaderWindow.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File dir = fc.getSelectedFile();
					if (!dir.exists())
						dir.mkdir();
					for (int i = 0; i < tab.getTabCount(); i++) {
						File file = new File(dir + pc.getProperty("file.separator") + tab.getTitleAt(i) + ".txt");
						// This is where a real application would save the file.
						try {
							file.createNewFile();
							try {
								file.setWritable(true);
								FileWriter fw = new FileWriter(file);
								BufferedWriter bw = new BufferedWriter(fw);
								PrintWriter outFile = new PrintWriter(bw);

								String print = Utilities.printPackets(tab.getTitleAt(i));
								outFile.println(print);

								outFile.close();
								file.setReadOnly();
								file.setWritable(false);

							} catch (FileNotFoundException e1) {
								JOptionPane.showMessageDialog(null, "Riprova");
								e1.printStackTrace();

							} catch (IOException er) {
								JOptionPane.showMessageDialog(null, "Riprova");
								er.printStackTrace();

							}
						} catch (IOException e2) {
							JOptionPane.showMessageDialog(null, "Riprova");
							e2.printStackTrace();
						}

					}
				}*/

			}
		});

		update.setContentAreaFilled(false);
		update.setRolloverEnabled(true);
		update.setBorder(null);
		update.setForeground(Color.WHITE);
		update.setFont(new Font("Arial", Font.BOLD, 16));
		 update.setRolloverIcon(new ImageIcon(ClassLoader.getSystemResource("updatepress.png")));
		update.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < tab.getTabCount(); i++)
					((PacketReaderPanel) ((JViewport) (((JScrollPane) tab.getComponentAt(i)).getComponent(0))).getView()).updateTable(tab.getTitleAt(i),
							PacketReaderWindow.this.width / 2 - 100, PacketReaderWindow.this.height - 150);

			}
		});

		menubar.add(new JSeparator());
		menubar.add(save);
		menubar.add(new JSeparator());
		menubar.add(saveAll);
		menubar.add(new JSeparator());
		menubar.add(update);
		menubar.add(new JSeparator());
		setJMenuBar(menubar);

		setContentPane(tab);
	}

	public void showMe() {
		this.setLocation(width / 2, 0);
		this.setVisible(true);
		mainWIndow.setSize(width / 2, height);
	}

	public void hideMe() {
		mainWIndow.setSize(width, height);
		this.setVisible(false);
	}

	public void addTab(String ip) {
		if (ipInTheTab.size() == 0)
			showMe();

		boolean hasAlready = false;
		for (int i = 0; i < ipInTheTab.size() && !hasAlready; i++) {
			if (ipInTheTab.get(i).equals(ip))
				hasAlready = true;
		}

		if (!hasAlready) {
			tab.addTab(ip, new JScrollPane(new PacketReaderPanel(width / 2 - 100, height - 150, ip, table), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED) {
				@Override
				public void setVerticalScrollBar(JScrollBar verticalScrollBar) {
					// TODO Auto-generated method stub
					verticalScrollBar.setUnitIncrement(15);
					super.setVerticalScrollBar(verticalScrollBar);
				}
			});
			tab.setTabComponentAt(ipInTheTab.size(), new CloseTabObjReader(tab, PacketReaderWindow.this));
			ipInTheTab.add(ip);
		}
	}

	public void removeIp(String ip) {
		ipInTheTab.remove(ip);
		if (ipInTheTab.size() == 0)
			hideMe();
	}

	public void removeAll() {
		while (ipInTheTab.size() > 0) {
			removeIp(ipInTheTab.get(ipInTheTab.size() - 1));
		}
		tab.removeAll();
	}

	public int getCount() {
		return ipInTheTab.size();
	}

	private class WindowsClosing implements WindowListener {

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void windowClosing(WindowEvent e) {
			removeAll();
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
}
