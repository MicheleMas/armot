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

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;


public class IPGuiPanel extends JPanel {

	private ArrayList<IPGuiObj> list = new ArrayList<IPGuiObj>();

	private int width;

	private Table table;

	private FakeGuiPanel fakePanel;

	private MainWindow mainwindow;

	public IPGuiPanel(int width, int height, Table table, FakeGuiPanel fakePanel, MainWindow mainWindow) {
		super();
		this.width = width;
		this.table = table;
		this.fakePanel = fakePanel;
		this.mainwindow = mainWindow;
		setSize(width / 2 - 50, height - 100);
		setLayout(new MigLayout());
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "IP in the LAN",TitledBorder.CENTER, TitledBorder.TOP,
				new Font("Arial", Font.BOLD, 18)));
	}

	public void addIPGuiObj(String ip,String mac) {
		IPGuiObj nuovo = new IPGuiObj(ip,mac, width, table, fakePanel, mainwindow);
		list.add(nuovo);
		add(nuovo, "wrap");
	}

	public int getCount() {
		return list.size();
	}
}
