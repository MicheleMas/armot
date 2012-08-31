package tools;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class Credits extends JFrame {

	public Credits() {
		super("CREDITS");
		this.setIconImage(new ImageIcon(ClassLoader.getSystemResource("my-net.png")).getImage());
		Rectangle maxBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

		RoundedPanel panel = new RoundedPanel(
				"<html>ARMOT (Arp MOnitoring Tool)<br><br>CREATED BY:<br><br>MICHELE MASSARO (programmer)<br>ANDREA BASSO (programmer)<br>ALEX TOMASELLO (programmer)<br>FEDERICO LANCERIN (collaborator)");
		setContentPane(panel);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		MouseAdapter ml4 = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				hideMe();
			}
		};
		panel.addMouseListener(ml4);
		pack();
		this.setLocation(maxBounds.width / 2 - getWidth() / 2, maxBounds.height / 2 - getHeight() / 2);

	}

	public void showMe() {
		setVisible(true);
	}

	private void hideMe() {
		setVisible(false);
	}
}
