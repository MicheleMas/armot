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
				"<html>ARMOT (Arp MOnitoring Tool)<br>" +
				"<br>CREATED BY:<br>" +
				"<br>MICHELE MASSARO (programmer)<br>" +
				"ANDREA BASSO (programmer)<br>" +
				"ALEX TOMASELLO (programmer)<br>" +
				"FEDERICO LANCERIN (collaborator)<br>" +
				"<br>Armot Copyright (C) 2012<br>" +
				"This program comes with ABSOLUTELY NO WARRANTY<br>" +
				"This is free software, and you are welcome to <br>" +
				"redistribute it under the terms of the GNU <br>" +
				"General Public License as published by<br>" +
				"the Free Software Foundation");
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
