package tools;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import jpcap.JpcapSender;

import net.miginfocom.swing.MigLayout;


public class FakeGuiPanel extends JPanel {
	private ArrayList<FakeGuiObj> list = new ArrayList<FakeGuiObj>();

	private int width;

	private MainWindow window;

	private String lastIp = "";

	private String targetIP;

	private byte[] myMac;

	private JpcapSender sender;

	public FakeGuiPanel(int width, int height, MainWindow w, byte[] myMac) {
		super();
		this.width = width;
		this.window = w;
		this.myMac = myMac;
		setSize(width / 2 - 50, height - 100);
		setLayout(new MigLayout());
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.RED), "Connections to selected IP",TitledBorder.CENTER, TitledBorder.TOP,
				new Font("Arial", Font.BOLD, 18)));
	}

	public void addFakeGuiObj(String ip) {
		FakeGuiObj nuovo = new FakeGuiObj(ip, width, targetIP, myMac, sender);
		list.add(nuovo);
		add(nuovo, "wrap");
		
	}

	public void newList(ArrayList<String> ip, boolean toAsk, String targetIP) {
		this.targetIP = targetIP;
		if (list.size() != 0 && toAsk) {
			/*boolean realToAsk = false;
			for (int i = 0; i < list.size() && !realToAsk; i++) {
				if (list.get(i).getCheckBox().isSelected())
					realToAsk = true;
			}
			if (realToAsk)
				JOptionPane.showMessageDialog(null, "Eventuali ip bloccati verranno rilasciati");
			stopAll();*/
			list = new ArrayList<FakeGuiObj>();
			removeAll();
			for (int i = 0; i < ip.size(); i++)
				addFakeGuiObj(ip.get(i));
		} else {
			removeAll();
			boolean[] prevStatus = new boolean[list.size()];
			for (int i = 0; i < prevStatus.length; i++) {
				prevStatus[i] = list.get(i).getCheckBox().isSelected();
				/*if (prevStatus[i])
					list.get(i).stopFakeArp();*/
			}
			list = new ArrayList<FakeGuiObj>();
			for (int i = 0; i < ip.size(); i++)
				addFakeGuiObj(ip.get(i));
			for (int i = 0; i < prevStatus.length; i++) {
				list.get(i).getCheckBox().setSelected(prevStatus[i]);
				/*if (prevStatus[i])
					list.get(i).startFakeArp();*/
			}
		}
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.RED), "Connections to "+targetIP,TitledBorder.CENTER, TitledBorder.TOP,
				new Font("Arial", Font.BOLD, 18)));
		window.paintComponents(window.getGraphics());
//		window.paintAll(window.getGraphics());
	} 

	public void colorIp(Color color, boolean noForward) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getCheckBox().isSelected()) {
				list.get(i).getCheckBox().setForeground(color);
				if (noForward)
					list.get(i).getCheckBox().setText("<html><STRIKE>" + list.get(i).getCheckBox().getText() + "</STRIKE></html>");
				else
					list.get(i).getCheckBox().setText(list.get(i).getIP());
			}
		}
	}

	public boolean showIp(String ip) {
		if (lastIp.equals(ip))
			return true;
		lastIp = ip;
		return false;

	}

	public void setSender(JpcapSender sender) {
		this.sender = sender;
	}

	/*public void stopAll() {
		for (int i = 0; i < list.size(); i++) {
			list.get(i).stopFakeArp();
		}
	}*/

	public void stopAndExit() {
		for (int i = 0; i < list.size(); i++) {
			list.get(i).stopFakeArp();
		}
		list = new ArrayList<FakeGuiObj>();
		lastIp = "";
	}
}
