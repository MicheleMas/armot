package tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;


public class IPGuiObj extends JPanel {

	private JButton viewIP = new JButton("show ip");

	private JButton packetsDetails = new JButton("packets details");
	
	private JButton becomeAnIP = new JButton("become this ip (bc)");
	
	private JButton showSpeed = new JButton("real time comunication");

	public IPGuiObj(final String ip,final String mac, int width, final Table table, final FakeGuiPanel fakePanel, final MainWindow mainWindow) {
		super();
		// setPreferredSize(new Dimension(width / 2 - 80, 40));
		setLayout(new MigLayout());
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), ip+"  "+mac, TitledBorder.LEFT, TitledBorder.TOP,
				new Font("Arial", Font.BOLD, 14)));		

		viewIP.setContentAreaFilled(false);
		viewIP.setFont(new Font("Arial", Font.BOLD, 14));
		packetsDetails.setContentAreaFilled(false);
		packetsDetails.setFont(new Font("Arial", Font.BOLD, 14));
		becomeAnIP.setContentAreaFilled(false);
		becomeAnIP.setFont(new Font("Arial", Font.BOLD, 14));
		showSpeed.setContentAreaFilled(false);
		showSpeed.setFont(new Font("Arial", Font.BOLD, 14));
		
		packetsDetails.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mainWindow.addTabOnPacketReaderWIndow(ip);

			}
		});

		viewIP.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO mostra gli ip [FakeGuiObj] con i quali sta dialogando insersendoli nel FakeGuiPanel
				ArrayList<String> tableString = table.getConnections(ip);
				int count = tableString.size();
				fakePanel.newList(tableString, !fakePanel.showIp(ip), ip);
			}
		});
		
		becomeAnIP.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				BroadcastARP bARP = new BroadcastARP(ip);
				
				// TODO inviare un gratuitous arp per tutta la rete
				// becomeAnIP.setBackground(Color.GREEN);
			}
		});
		
		showSpeed.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ConnectionsWindow connectionsWindow = new ConnectionsWindow(ip);
			}
		});

		add(viewIP);
		add(packetsDetails, "gapleft 20 , wrap");
		add(becomeAnIP);
		add(showSpeed, "gapleft 20");
	}

}
