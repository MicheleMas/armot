package tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import net.miginfocom.swing.MigLayout;


public class MainWindow extends JFrame {
	/**
	 * to start scan of LAN
	 */
	private JButton startStop = new JButton("START", new ImageIcon(ClassLoader.getSystemResource("play.png")));
	/**
	 * status of startStop button
	 */
	private boolean isOnStart = true;
	/**
	 * list of IP in the LAN
	 */
	private IPGuiPanel ipLanPanel;
	/**
	 * 
	 */
	private FakeGuiPanel fakePanel;
	/**
	 * active or deactive forwarding
	 */
	private JButton changeForward;
	/**
	 * 
	 */
	private JpcapSender sender;
	/**
	 * my MAC address
	 */
	private byte[] myMac;
	/**
	 * table containing hash table
	 */
	private Table table;
	/**
	 * icon for forwarding status
	 */
	private ImageIcon forwardIcon;
	/**
	 * forwarding status
	 */
	private JLabel actualState;
	
	private JMenuBar menubar = new JMenuBar() 
	{
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
	}
	;
	/**
	 * window height
	 */
	private int height;
	/**
	 * window width
	 */
	private int width;
	/**
	 * my IP address
	 */
	private String myIp;
	/**
	 * how many ip is show
	 */
	private int countIPshow = 0;
	/**
	 * 
	 */
	private PacketReaderWindow packetReaderWindow;
	
	private Reader reader;
	
	private ToolBox container;
	

	public MainWindow(final int interfaceChosen, String myip) {
		super("Armot");
		this.myIp = myip;
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// window details
		this.setIconImage(new ImageIcon(ClassLoader.getSystemResource("my-net.png")).getImage());

		this.setLayout(new MigLayout());
		Rectangle maxBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		height = maxBounds.height;
		width = maxBounds.width;
		this.setSize(width, height);
		this.setJMenuBar(menubar);

		// timer to refresh gui
		final TimerGui timer = new TimerGui(MainWindow.this);
		final Poisoner poison = new Poisoner();
		

		// panel
		table = new Table();

		final NetworkInterface[] devices = JpcapCaptor.getDeviceList();
		myMac = devices[interfaceChosen].mac_address;

		fakePanel = new FakeGuiPanel(width, height, MainWindow.this, myMac);
		ipLanPanel = new IPGuiPanel(width, height, table, fakePanel, MainWindow.this);
		packetReaderWindow = new PacketReaderWindow(width, height, MainWindow.this, table);

		// button panel
		menubar.add(new JSeparator());
		menubar.add(startStop);
		menubar.add(new JSeparator());
		menubar.add(new JSeparator());
		menubar.add(new JSeparator());

		// check forward
		java.util.Timer forwardController = new java.util.Timer();
		forwardController.schedule(new TimerTask() {
			@Override
			public void run() {
				updateForwardState(Utilities.checkForward());
			}
		}, 3000, 5000);

		// modify start stop button
		startStop.setContentAreaFilled(false);
		startStop.setRolloverEnabled(true);
		startStop.setBorder(null);
		startStop.setForeground(Color.BLACK);
		startStop.setFont(new Font("Arial", Font.BOLD, 16));
		startStop.setRolloverIcon(new ImageIcon(ClassLoader.getSystemResource("playpress.png")));
		startStop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (isOnStart) {
					ipLanPanel.removeAll();
					fakePanel.removeAll();
					table.resetTable();
					countIPshow=0;

					timer.start();

					try {
						JpcapCaptor jpcap = JpcapCaptor.openDevice(devices[interfaceChosen], 65535, true, 20);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					poison.start(interfaceChosen);
					sender = poison.getSender();
					container = new ToolBox();
					container.setSender(sender);
					container.setMyMac(myMac);
					fakePanel.setSender(sender);

					// prova reader
					try {
						JpcapCaptor captor = JpcapCaptor.openDevice(devices[interfaceChosen], 65535, true, 20);
						container.setNic(devices[interfaceChosen]);
						reader = new Reader(captor, myIp);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					isOnStart = false;
					startStop.setText("STOP");
					startStop.setIcon(new ImageIcon(ClassLoader.getSystemResource("stop.png")));
					startStop.setRolloverIcon(new ImageIcon(ClassLoader.getSystemResource("stoppress.png")));
				} else {
					poison.stop();
					timer.stop();
					reader.stop();
					fakePanel.stopAndExit();
					isOnStart = true;
					startStop.setText("START");
					startStop.setIcon(new ImageIcon(ClassLoader.getSystemResource("play.png")));
					startStop.setRolloverIcon(new ImageIcon(ClassLoader.getSystemResource("playpress.png")));
				}

			}
		});

		// forwaring icon
		if (Utilities.checkForward()) {
			forwardIcon = new ImageIcon(ClassLoader.getSystemResource("green.png"));
			actualState = new JLabel(" Forwarding  active    ", forwardIcon, JLabel.CENTER) {
				@Override
				public void setForeground(Color fg) {
					// TODO Auto-generated method stub
					super.setForeground(Color.BLACK);
					super.setFont(new Font("Arial", Font.ITALIC, 16));
				}
			};
			menubar.add(actualState);
			changeForward = new JButton("DEACTIVATE", new ImageIcon(ClassLoader.getSystemResource("stopforward.png")));
			changeForward.setFont(new Font("Arial", Font.BOLD, 14));
			changeForward.setContentAreaFilled(false);
			changeForward.setRolloverEnabled(true);
			changeForward.setForeground(Color.BLACK);
			changeForward.setBorder(null);
			changeForward.setRolloverIcon(new ImageIcon(ClassLoader.getSystemResource("stopforwardpress.png")));
			menubar.add(changeForward);
		} else {
			forwardIcon = new ImageIcon(ClassLoader.getSystemResource("red.png"));
			actualState = new JLabel(" Forwarding  not active    ", forwardIcon, JLabel.CENTER) {
				@Override
				public void setForeground(Color fg) {
					// TODO Auto-generated method stub
					super.setForeground(Color.BLACK);
					super.setFont(new Font("Arial", Font.ITALIC, 16));
				}
			};
			menubar.add(actualState);
			changeForward = new JButton("ACTIVATE", new ImageIcon(ClassLoader.getSystemResource("startforward.png")));
			changeForward.setContentAreaFilled(false);
			changeForward.setRolloverEnabled(true);
			changeForward.setBorder(null);
			changeForward.setForeground(Color.BLACK);
			changeForward.setFont(new Font("Arial", Font.BOLD, 14));
			changeForward.setRolloverIcon(new ImageIcon(ClassLoader.getSystemResource("startforwardpress.png")));
			menubar.add(changeForward);
		}
		// action when forward is pressed
		changeForward.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (Utilities.checkForward()) {
					Utilities.disableForward();
					System.out.println("forward deactivated");
				} else {
					Utilities.enableForward();
					System.out.println("forward activated");
				}
				updateForwardState(Utilities.checkForward());

			}
		});

		menubar.add(new JSeparator());
		
		//BroadcastARP button
		JButton brARP = new JButton("Broadcast ARP");
		brARP.setContentAreaFilled(false);
		brARP.setForeground(Color.WHITE);
		final BroadcastWindow brARPWindow = new BroadcastWindow();
		brARP.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				brARPWindow.showMe();
			}
		});
		menubar.add(brARP);
		
		// credit button
		JButton credits=new JButton("credits");
		credits.setContentAreaFilled(false);
		credits.setForeground(Color.WHITE);		
		final Credits creditWindow=new Credits();
		credits.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				creditWindow.showMe();
			}
		});
		menubar.add(credits);
		

		add(new JScrollPane(ipLanPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED) {
			@Override
			public void setVerticalScrollBar(JScrollBar verticalScrollBar) {
				// TODO Auto-generated method stub
				verticalScrollBar.setUnitIncrement(15);
				super.setVerticalScrollBar(verticalScrollBar);
			}
		}, "width " + (width / 4 - 7) + ":" + (width / 2 - 7) + ":" + (width / 2 - 7) + " , dock west");

		add(new JScrollPane(fakePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED) {
			@Override
			public void setVerticalScrollBar(JScrollBar verticalScrollBar) {
				// TODO Auto-generated method stub
				verticalScrollBar.setUnitIncrement(15);
				super.setVerticalScrollBar(verticalScrollBar);
			}
		}, "width " + (width / 4 - 5) + ":" + (width / 2 - 5) + ":" + (width / 2 - 5) + " , gapleft 5 , dock east");

		this.setVisible(true);
	}

	/**
	 * 
	 */
	public void setIPList() {
		boolean change = false;
		String[][] tableString = table.getEntriesIpMac();
		int tableCount=tableString.length;
		if (tableCount > countIPshow) {
			for (int i = countIPshow; i <tableCount; i++) {
				ipLanPanel.addIPGuiObj(tableString[i][0],tableString[i][1]);
				change = true;
			}
		}
		if (change) {
//			paintComponents(getGraphics());
			paintAll(getGraphics());
			countIPshow = tableCount;
		}

	}

	/**
	 * 
	 * @param state
	 */
	public void updateForwardState(boolean state) {
		if (!state) {
			forwardIcon.setImage(new ImageIcon(ClassLoader.getSystemResource("red.png")).getImage());
			actualState.setText(" Forwarding  not active    ");
			changeForward.setText("ACTIVATE");
			changeForward.setIcon(new ImageIcon(ClassLoader.getSystemResource("startforward.png")));
			changeForward.setRolloverIcon(new ImageIcon(ClassLoader.getSystemResource("startforwardpress.png")));
			fakePanel.colorIp(Color.RED, true);

		} else {
			changeForward.setText("DEACTIVATE");
			actualState.setText(" Forwarding  active    ");
			forwardIcon.setImage(new ImageIcon(ClassLoader.getSystemResource("green.png")).getImage());
			changeForward.setIcon(new ImageIcon(ClassLoader.getSystemResource("stopforward.png")));
			changeForward.setRolloverIcon(new ImageIcon(ClassLoader.getSystemResource("stopforwardpress.png")));
			fakePanel.colorIp(Color.GREEN, false);
		}
	}

	public boolean getForwardStatus() {
		return Utilities.checkForward();
	}

	public void addTabOnPacketReaderWIndow(String ip) {
		packetReaderWindow.addTab(ip);
	}
}
