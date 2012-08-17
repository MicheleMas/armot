package console;

import java.util.ArrayList;
import java.util.Scanner;

import tools.ArrayListMap;
import tools.Table;

public class Read {
	
	private Table table;

	public Read() {
		
		Scanner in = new Scanner(System.in);
		table = new Table();
		
		System.out.println("use list to show active connections found,");
		System.out.println("use show <ip1> <ip2> to show packets between ip1 and ip2");

		System.out.print("Read->");

		String input;
		boolean returnToMenu = false;
		while (!returnToMenu && in.hasNext()) {

			input = in.nextLine();
			switch (commandInterpreter(input)) {
			case 1:
				this.list();
				break;
			case 2:
				this.show(input);
				break;
			case 3:
				returnToMenu = true;
				break;
			case 4:
				this.help();
				break;
			default:
				// TODO
				break;
			}
			if (!returnToMenu)
				System.out.print("Read->");
		}
	}
	
	private void help() {
		System.out.println("list             - show all connections");
		System.out.println("show <ip1> <ip2> - show packets between <ip1> and <ip2>");
		System.out.println("back             - return to the previous menu");
		System.out.println("to save packets use export command in the main menu");
	}

	private void list() {
		ArrayListMap<String, ArrayList<String>> connections = table.getFromTo();
		if (connections != null) {
			ArrayList<String> keys = connections.getKeys();
			ArrayList<String> connected;
			if (keys.size() == 0)
				System.out.println("no packets found");
			for (int i = 0; i < keys.size(); i++) {
				System.out.println(keys.get(i) + " connections:");
				connected = connections.get(keys.get(i));
				for (int j = 0; j < connected.size(); j++) {
					System.out.println("\t " + connected.get(j));
				}
			}
		} else {
			System.out.println("no connections found");
		}
	}

	private void show(String input) {
		String[] split = input.split(" ");
		if(split.length == 3 && !split[1].equals("") && !split[2].equals("")) {
			String[][] sent = table.getIpPacketSent(split[1]);
			String[][] received = table.getIpPacketReceived(split[1]);
			if(sent != null) {
				System.out.println("packet sent:");
				for(int i=0; i < sent.length; i++) {
					if(split[2].equals(sent[i][1])) {
						System.out.println("from " + split[1] + " to " + split[2] + ", P OUT " + sent[i][3] + ", P IN " + sent[i][4]);
					}
				}
			}
			if(received != null) {
				System.out.println("packet received:");
				for(int i=0; i < received.length; i++) {
					if(split[2].equals(received[i][1])) {
						System.out.println("from " + split[2] + " to " + split[1] + ", P OUT " + received[i][4] + ", P IN " + received[i][3]);
					}
				}
			}
			if(sent == null && received == null)
				System.out.println("No packets found between sender and receiver specified");
		} else {
			System.out.println("Show usage: show <ipSender> <ipReceiver>");
		}
	}
	
	private int commandInterpreter(String commands) {
		String command = commands.split(" ")[0];
		if (command == null)
			return -1;
		if (command.toUpperCase().equals("LIST"))
			return 1;
		if (command.toUpperCase().equals("SHOW"))
			return 2;
		if (command.toUpperCase().equals("BACK"))
			return 3;
		if (command.toUpperCase().equals("HELP"))
			return 4;
		return -1;
	}

}
