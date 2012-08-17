package console;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import tools.SavePacketWindow;
import tools.Utilities;

public class Export {
	
	private Scanner in;
	
	public Export() {
		System.out.println("Choose export file: txt or pcap");
		System.out.println("or type back te return to the main menu");
		
		in = new Scanner(System.in);
		
		System.out.print("Export->");
		
		String input;
		boolean returnToMenu = false;
		while (!returnToMenu && in.hasNext()) {
			
			input = in.nextLine();
			switch (commandInterpreter(input)) {
			case 1:
				this.txt();
				break;
			case 2:
				// TODO
				break;
			case 3:
				returnToMenu = true;
				break;
			default:
				// TODO
				break;
			}
			if(!returnToMenu)
				System.out.print("Export->");
		}
	}
	
	private void txt() {
		System.out.println("Select an IP to save or type all");
		String reading = in.nextLine();
		if(reading != null) {
			if(reading.equalsIgnoreCase("ALL")) {
				// salva tutto
			} else {
				try {
					(InetAddress.getByName(reading)).getAddress();
					this.saveTxt(reading);
				} catch (UnknownHostException e) {
					System.out.println("IP not found");
				}
			}
		}
	}
	
	private int saveTxt(String ip) {
		System.out
				.println("enter the absolute path to the file without extension");
		System.out.println("(for example: /home/user/Documents/file)");
		String name = in.nextLine();
		File file = new File(name);
		// This is where a real application would save the file.
		if (!file.getAbsolutePath().endsWith(".txt")) {
			file = new File(file.getAbsolutePath() + ".txt");
		}
		try {
			file.createNewFile();
			try {
				file.setWritable(true);
				FileWriter fw = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter outFile = new PrintWriter(bw);

				String print = Utilities.printPackets(ip);
				outFile.println(print);

				outFile.close();
				file.setReadOnly();
				file.setWritable(false);

			} catch (FileNotFoundException e1) {
				System.out.println("path not found");
				e1.printStackTrace();
				return 0;

			} catch (IOException er) {
				System.out.println("error saving the file");
				er.printStackTrace();
				return 0;

			}
		} catch (IOException e2) {
			System.out.println("can't create the file");
			e2.printStackTrace();
			return 0;
		}
		return 1;
	}
	
	private int commandInterpreter(String commands) {
		String command = commands.split(" ")[0];
		if (command == null)
			return -1;
		if (command.toUpperCase().equals("TXT"))
			return 1;
		if (command.toUpperCase().equals("PCAP"))
			return 2;
		if (command.toUpperCase().equals("BACK"))
			return 3;
		return -1;
	}

}
