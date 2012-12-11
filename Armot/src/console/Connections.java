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

import java.util.Enumeration;
import java.util.Scanner;

import tools.FakeHandler;

public class Connections {
	
	private FakeHandler handler;
	
	public Connections () {
		System.out.println("Handle current poisoner thread,");
		System.out.println("type help for more commands, back to return to the main menu");
		Scanner in = new Scanner(System.in);
		handler = new FakeHandler();
		
		System.out.print("Connections->");
		
		String input;
		boolean returnToMenu = false;
		while (!returnToMenu && in.hasNext()) {
			
			input = in.nextLine();
			switch (commandInterpreter(input)) {
			case 1:
				this.show();
				break;
			case 2:
				this.stop(input);
				break;
			case 3:
				returnToMenu = true;
				break;
			case 4:
				this.help();
				break;
			default:
				this.help();
				break;
			}
			if(!returnToMenu)
				System.out.print("Connections->");
		}
	}
	
	private void help() {
		System.out.println("show                - show current ARP poisoning");
		System.out.println("stop <target> <ip>  - stop ARP poisoning to <target>");
		System.out.println("back                - return to the previous menu");
	}
	
	private void show() {
		Enumeration<String> en = handler.getThreads();
		String s;
		String[] split;
		while(en.hasMoreElements()) {
			s = en.nextElement();
			if(s != null) {
				split = s.split("-");
				if(split.length == 2) 
					System.out.println("- target:" + split[1] + ", with identity:" + split[0]);
			}
		}
	}
	
	private void stop(String input) {
		String[] split = input.split(" ");
		if(split.length != 3) 
			System.out.println("Syntax error, stop usage: stop <target> <ip>");
		else {
			if(handler.stopThread(split[2], split[1])) {
				System.out.println("Poisoning " + split[1] + " as " + split[2] + " ended");
			} else {
				System.out.println("No active thread founded between " + split[1] + " and " + split[2]);
			}
		}
	}
		
	private int commandInterpreter(String commands) {
		String command = commands.split(" ")[0];
		if (command == null)
			return -1;
		if (command.toUpperCase().equals("SHOW"))
			return 1;
		if (command.toUpperCase().equals("STOP"))
			return 2;
		if (command.toUpperCase().equals("BACK"))
			return 3;
		if (command.toUpperCase().equals("HELP"))
			return 4;
		return -1;
	}

}
