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
import java.util.Scanner;

import tools.BroadcastARP;
import tools.ToolBox;

public class Broadcast {
	
	private String ip, mac;
	private ToolBox container;
	private BroadcastARP bc;
	
	public Broadcast() {
		Scanner in = new Scanner(System.in);
		container = new ToolBox();
		byte[] macArray = container.getMyMac();
		mac = new String("");
		for (int i = 0; i < 6; i++) {
			if(i != 0)
				mac += ":";
			mac += Integer.toHexString(macArray[i] & 0xFF);
		}
		
		this.help();
		System.out.print("Broadcast->");
		
		String input;
		boolean returnToMenu = false;
		while (!returnToMenu && in.hasNext()) {
			
			input = in.nextLine();
			switch (commandInterpreter(input)) {
			case 1:
				this.setIP(input);
				break;
			case 2:
				this.setMAC(input);
				break;
			case 3:
				returnToMenu = true;
				break;
			case 4:
				this.send();
				break;
			case 5:
				this.help();
				break;
			default:
				this.help();
				break;
			}
			if(!returnToMenu)
				System.out.print("Broadcast->");
		}
	}
	
	private void help() {
		System.out.println("Send custom ARP packet,");
		System.out.println("1 - set ip with setIP <ip>");
		System.out.println("2 - set mac with setMAC <mac> (default: " + mac + ")");
		System.out.println("3 - use send command to send broadcast ARP request\n");
		System.out.println("warning: this method can be easily detected");
	}
	
	private void setIP(String input) {
		String[] split = input.split(" ");
		if(split.length == 2) {
			try {
				(InetAddress.getByName(split[1])).getAddress();
				this.ip = split[1];
				bc = new BroadcastARP(ip, mac);
				System.out.println("IP set as " + ip);
			} catch (IOException e) {
				System.out.println("Error, IP " + split[1] + " not valid");
			}
		} else 
			System.out.println("setIP usage: setIP <ip>");
	}
	
	private void setMAC(String input) {
		String[] split = input.split(" ");
		if(split.length == 2) {
			if(checkMAC(split[1])) {
				if(ip != null) {
					this.mac = split[1];
					bc = new BroadcastARP(this.ip, this.mac);
					System.out.println("MAC set as " + this.mac + ", ready to send");
				} else {
					this.mac = split[1];
					System.out.println("MAC set as " + this.mac + ", set ip to continue");
				}
			} else {
				System.out.println("Error, mac " + split[1] + " not valid");
			}
		} else {
			System.out.println("setMAC usage: setMAC <mac>");
		}
	}
	
	private boolean checkMAC(String mac) {
		boolean allOK = true;
		String[] split = mac.split(":");
		if (split.length != 6)
			return false;
		byte[] ret = new byte[6];
		try {
			for (int i = 0; i < split.length; i++) {
				ret[i] = (byte) (int) Integer.valueOf(split[i], 16);
			} 
		} catch (NumberFormatException e) {
			return false;
		}
		
		return true;
	}
	
	private void send() {
		if(ip != null) {
			bc.send();
			System.out.println("Custom ARP packet sent!");
		} else {
			System.out.println("You need to set the ip before, use setIP <ip>");
		}
	}
	
	private int commandInterpreter(String commands) {
		String command = commands.split(" ")[0];
		if (command == null)
			return -1;
		if (command.toUpperCase().equals("SETIP"))
			return 1;
		if (command.toUpperCase().equals("SETMAC"))
			return 2;
		if (command.toUpperCase().equals("BACK"))
			return 3;
		if (command.toUpperCase().equals("SEND"))
			return 4;
		if (command.toUpperCase().equals("HELP"))
			return 5;
		return -1;
	}
	
}
