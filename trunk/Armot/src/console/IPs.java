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

package console;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import jpcap.JpcapCaptor;
import tools.FakeHandler;
import tools.Table;
import tools.ToolBox;

public class IPs {
	
	private Table table;
	private ToolBox container;
	private FakeHandler handler;
	
	public IPs () {
		this.table = new Table();
		container = new ToolBox();
		handler = new FakeHandler();
		Scanner in = new Scanner(System.in);
		String input;
		
		System.out.println("IP Selection (use help for more commands, back to return to the previous menu)");
		
		System.out.print("IPs ->");
		
		boolean returnToMenu = false;
		
		while (!returnToMenu && in.hasNext()) {
			
			input = in.nextLine();
			switch (commandInterpreter(input)) {
			case 1:
				this.show();
				break;
			case 2:
				this.scan(input);
				break;
			case 3:
				returnToMenu = true;
				break;
			case 4:
				this.help();
				break;
			case 5:
				this.poison(input);
				break;
			default:
				this.help();
				break;
			}
			if(!returnToMenu)
				System.out.print("IPs->");
		}
	}
	
	private void show() {
		String[][] ip_mac = table.getEntriesIpMac();
		System.out.println("List of IPs using ARP:");
		for(int i=0; i < ip_mac.length; i++) {
			if(ip_mac[i][0] != null && ip_mac[i][1] != null)
				System.out.println("IP: " + ip_mac[i][0] + " MAC: " + ip_mac[i][1]);
		}
		if(ip_mac.length == 0)
			System.out.println("No ARP packet captured jet");
	}
	
	private void scan(String input) {
		String[] commands = input.split(" ");
		if(commands.length != 2)
			System.out.println("Syntax error, scan usage: scan <IP>");
		else {
			ArrayList<String> target = table.getConnections(commands[1]);
			if(target == null)
				System.out.println("IP not found, use show command to find correct IPs");
			else {
				System.out.println(commands[1] + " connections:");
				for(int i=0; i < target.size(); i++) {
					System.out.println(" - " + target.get(i));
				}
			}
		}
	}
	
	private void poison(String input) {
		String[] commands = input.split(" ");
		if(commands.length != 3)
			System.out.println("Syntax error, poison usage: poison <IP> <targetIP>");
		else {
			try {
			(InetAddress.getByName(commands[2])).getAddress();
			if(table.contains(commands[1])) {
				JpcapCaptor captor = JpcapCaptor.openDevice(container.getNic(), 65535,
						true, 20);
				handler.addThread(commands[2], commands[1], container.getMyMac(), captor.getJpcapSenderInstance());
				System.out.println("Poisoner thread started, gathering packets...");
				System.out.println("use connections command in the main menu to handle current threads");
			}
			else
				System.out.println(commands[1] + " not found, no ARP packet received from this IP");
			} catch (UnknownHostException e) {
				System.out.println(commands[2] + " is not a valid IP");
			} catch (IOException e) {
				System.out.println("error opening JPcap captor");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void help() {
		System.out.println("IPs commands list:");
		System.out.println("show                 - list all known IPs");
		System.out.println("scan <ip>            - list all IPs in comunication with <ip>");
		System.out.println("poison <target> <ip> - impersonate <ip> for <target> (all TCP/UDP packets from ip to target will be redirected to you)");
		System.out.println("back                 - return to the previous menu");
	}
	
	private int commandInterpreter(String commands) {
		String command = commands.split(" ")[0];
		if (command == null)
			return -1;
		if (command.toUpperCase().equals("SHOW"))
			return 1;
		if (command.toUpperCase().equals("SCAN"))
			return 2;
		if (command.toUpperCase().equals("BACK"))
			return 3;
		if (command.toUpperCase().equals("HELP"))
			return 4;
		if (command.toUpperCase().equals("POISON"))
			return 5;
		return -1;
	}
	
	

}
