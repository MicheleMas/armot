package console;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import tools.FakeHandler;
import tools.Sender;
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
				// TODO help
				break;
			case 5:
				this.poison(input);
				break;
			default:
				// TODO
				break;
			}
			if(!returnToMenu)
				System.out.print("IPs ->");
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
	
	private void poison(String input) { // TODO qualcosa non va qui!
		String[] commands = input.split(" ");
		if(commands.length != 3)
			System.out.println("Syntax error, poison usage: poison <IP> <targetIP>");
		else {
			try {
			(InetAddress.getByName(commands[2])).getAddress();
			if(table.contains(commands[1])) {
				handler.addThread(commands[2], commands[1], container.getMyMac(), container.getSender());
				System.out.println("Poisoner thread started, gathering packets...");
				System.out.println("use connections command in the main menu to handle current threads");
			}
			else
				System.out.println(commands[1] + " not found, no ARP packet received from this IP");
			} catch (UnknownHostException e) {
				System.out.println(commands[2] + " is not a valid IP");
			}
		}
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
