package console;

import java.util.Scanner;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import tools.Poisoner;
import tools.Table;
import tools.ToolBox;

public class Main {

	private String myIP;
	private Poisoner poisoner;
	private Table table;
	private byte[] myMac;
	private ToolBox container;

	public Main(final int interfaceChosen, String myIP) {
		this.myIP = myIP;
		poisoner = new Poisoner();
		table = new Table();
		final NetworkInterface[] devices = JpcapCaptor.getDeviceList();
		myMac = devices[interfaceChosen].mac_address;
		container = new ToolBox();

		Scanner in = new Scanner(System.in);
		String input;

		// switch case loop
		while (in.hasNext()) {
			input = in.nextLine();
			switch (commandInterpreter(input)) {
			case 1:
				this.start();
				break;
			case 2:
				this.stop();
				break;
			case 3:
				// TODO
				break;
			case 4:
				// TODO
				break;
			case 5:
				// TODO
				break;
			case 6:
				// TODO
				break;
			case 7:
				// TODO
				break;
			case 8:
				// TODO
				break;
			case 9: {
				System.out.println("GoodBye");
				System.exit(0);
				break;
			}
			default:
				// TODO
				break;
			}
		}

	}
	
	private void start() {
		// TODO
		System.out.println("start");
	}
	
	private void stop() {
		// TODO
		System.out.println("stop");
	}

	/**
	 * 1 = START<br>
	 * 2 = STOP<br>
	 * 3 = IPs<br>
	 * 4 = CONNECTIONS<br>
	 * 5 = FORWARD<br>
	 * 6 = BROADCAST<br>
	 * 7 = READ<br>
	 * 8 = EXPORT<br>
	 * 9 = EXIT <br>
	 * <br>
	 * -1 = ERROR
	 * 
	 * @param command
	 */
	private int commandInterpreter(String command) {
		if (command == null)
			return -1;
		if (command.toUpperCase().equals("START"))
			return 1;
		if (command.toUpperCase().equals("STOP"))
			return 2;
		if (command.toUpperCase().equals("IPS"))
			return 3;
		if (command.toUpperCase().equals("CONNECTIONS"))
			return 4;
		if (command.toUpperCase().equals("FORWARD"))
			return 5;
		if (command.toUpperCase().equals("BC"))
			return 6;
		if (command.toUpperCase().equals("READ"))
			return 7;
		if (command.toUpperCase().equals("EXPORT"))
			return 8;
		if (command.toUpperCase().equals("EXIT"))
			return 9;

		return -1;
	}
}
