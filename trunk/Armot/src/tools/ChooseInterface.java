package tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import net.miginfocom.swing.MigLayout;


/**
 * 
 * @author tomaseo
 * 
 */
public class ChooseInterface extends JFrame {

	/**
	 * list of interfaces
	 */
	private JRadioButton[] interfaces;
	/**
	 * after choose the net interface
	 */
	private JButton start = new JButton("start");
	/**
	 * controller of correct chosen , true default because i chose 0 interface
	 */
	private boolean chooseDone = true;
	/**
	 * number of network interface chosen , 0 default
	 */
	private int interfaceChosen = 0;
	/**
	 * my IP address
	 */
	private String myIp = "";
	/**
	 * 
	 */
	NetworkInterface[] devices = JpcapCaptor.getDeviceList();

	private JPanel all = new JPanel();

	public ChooseInterface() {
		super("CHOOSE INTERFACE");
		// set window
		setLayout(new MigLayout());
		all.setLayout(new MigLayout());
		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setIconImage(new ImageIcon(ClassLoader.getSystemResource("my-net.png")).getImage());
		Rectangle maxBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

		// add title
		all.add(new RoundedPanel("SELECT A NETWORK INTERFACE") {
			@Override
			public void setFont(Font font) {
				super.setFont(new Font("Arial", Font.BOLD, 17));
			}

			@Override
			public void setForeground(Color fg) {
				super.setForeground(Color.BLUE);
			}
		}, "dock north , gapleft 20 , gaptop 10 , gapbottom 10");
		setList();
		setAction();
		interfaces[interfaceChosen].setSelected(true);
		try {
			myIp = ("" + devices[interfaceChosen].addresses[0].address).substring(1);

		} catch (Exception err) {
			myIp = "";
		}

		all.add(start, "dock south , gapleft 20 , gaptop 20");

		this.add(all,"dock center");
		pack();
//		this.setSize(800, 350);
		this.setLocation((int) maxBounds.getWidth() / 2 - getWidth()/2, (int) maxBounds.getHeight() / 2 - getHeight()/2);
		this.setVisible(true);
	}

	/**
	 * set list of all available interfaces
	 */
	private void setList() {

		interfaces = new JRadioButton[devices.length];

		for (int i = 0; i < devices.length; i++) {
			String name = i + " : " + devices[i].name + "  ( " + devices[i].description + " )";
			name = name + "\n    MAC address ";
			for (byte b : devices[i].mac_address)
				name = name + "\n" + Integer.toHexString(b & 0xff) + ":";
			// for (NetworkInterfaceAddress a : devices[i].addresses)
			try {

				name = name + "\n    address:" + devices[i].addresses[0].address;
			} catch (Exception e) {
				// TODO: handle exception
			}
			interfaces[i] = new JRadioButton(name);
			all.add(interfaces[i], " cell 0 " + i + " ,  gapleft 20");
		}
		paintAll(getGraphics());
	}

	/**
	 * set actions for start button and all radio box;
	 */
	private void setAction() {

		for (int i = 0; i < interfaces.length; i++) {
			final int numberSelected = i;
			interfaces[i].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (!interfaces[numberSelected].isSelected()) {
						chooseDone = false;
					} else {
						if (chooseDone) {
							for (int j = 0; j < numberSelected; j++) {
								interfaces[j].setSelected(false);
							}
							for (int j = numberSelected + 1; j < interfaces.length; j++) {
								interfaces[j].setSelected(false);
							}
						} else
							chooseDone = true;
					}

					try {
						myIp = ("" + devices[numberSelected].addresses[0].address).substring(1);

					} catch (Exception err) {
						myIp = "";
					}
					interfaceChosen = numberSelected;
					paintAll(getGraphics());
				}
			});
		}

		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (chooseDone && !myIp.equals("")) {
					// System.out.println(myIp);
					ChooseInterface.this.setVisible(false);
					new MainWindow(interfaceChosen, myIp);
				} else {
					JOptionPane.showMessageDialog(null, "Please select a network interface");
				}

			}
		});

	}

	/*
	public void Main() {

		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}
		new ChooseInterface();
	}*/
}
