package console;

import java.io.IOException;
import java.util.Scanner;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import tools.FakeHandler;
import tools.Poisoner;
import tools.Reader;
import tools.Table;
import tools.ToolBox;
import tools.Utilities;

public class Main {

	private String myIP;
	private Poisoner poisoner;
	private Table table;
	private byte[] myMac;
	private ToolBox container;
	private boolean isStarted;
	private int interfaceChosen;
	private Reader reader;
	private final NetworkInterface[] devices = JpcapCaptor.getDeviceList();
	private FakeHandler handler;

	public Main(final int interfaceChosen, String myIP) {
		this.myIP = myIP;
		poisoner = new Poisoner();
		table = new Table();
		this.interfaceChosen = interfaceChosen;
		this.myMac = devices[interfaceChosen].mac_address;
		container = new ToolBox();
		handler = new FakeHandler();

		Scanner in = new Scanner(System.in);
		String input;
		
		System.out.println("Initialization complete, type help for a commands list");

		System.out.print("->");
		
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
				this.IPs();
				break;
			case 4:
				this.connections();
				break;
			case 5:
				this.forward(input);
				break;
			case 6:
				this.broadcast();
				break;
			case 7:
				this.read();
				break;
			case 8:
				this.export();
				break;
			case 9: {
				System.out.println("GoodBye");
				System.exit(0);
				break;
			}
			case 10: 
				this.help();
				break;
			default:
				this.help();
				break;
			}
			System.out.print("->");
		}

	}
	
	private void start() {
		if(!isStarted) {
			System.out.print("starting...");
			table.resetTable();
			poisoner.start(this.interfaceChosen);
			container.setSender(poisoner.getSender());
			container.setMyMac(myMac);
			try {
				JpcapCaptor captor = JpcapCaptor.openDevice(devices[interfaceChosen], 65535, true, 20);
				container.setNic(devices[interfaceChosen]);
				reader = new Reader(captor, myIP);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			isStarted = true;
			System.out.println(" done");
		} else {
			System.out.println("Already started!");
		}
	}
	
	private void stop() {
		if(isStarted) {
			System.out.print("closing open connections...");
			handler.stopAll();
			System.out.println(" done");
			poisoner.stop();
			reader.stop();
			isStarted = false;
			System.out.println("stopped");
		} else {
			System.out.println("Nothing to stop");
		}
	}
	
	private void IPs() {
		IPs ips;
		if(isStarted)
			ips = new IPs();
		else
			System.out.println("capture not started, use start command");
	}
	
	private void connections() {
		Connections connections = new Connections();
	}
	
	private void forward(String input) {
		String[] split = input.split(" ");
		if(split.length >= 2) {
			if("ON".equalsIgnoreCase(split[1])) {
				if(Utilities.enableForward())
					System.out.println("Forward activated");
				else
					System.out.println("Something gone wrong");
			} else {
				if("OFF".equalsIgnoreCase(split[1])) {
					if(Utilities.disableForward())
						System.out.println("Forward deactivated");
					else
						System.out.println("Something gone wrong");
				} else {
					System.out.println("Forward accepts only ON or OFF");
				}
			}
		} else {
			System.out.println("Error, type \"forward on\" of \"forward off\" ");
		}
	}
	
	private void broadcast() {
		Broadcast bc;
		if(isStarted)
			bc = new Broadcast();
		else
			System.out.println("capture not started, use start command");
	}
	
	private void read() {
		Read read = new Read();
	}
	
	private void export() {
		Export export = new Export();
	}
	
	private void help() {
		System.out.println("start       - start to capture packets");
		System.out.println("stop        - stop to capture packets");
		System.out.println("IPs         - show IPs in the network and allows to poison other hosts");
		System.out.println("connections - show and stop current poisoning");
		System.out.println("forward     - enable or disable packets forwarding");
		System.out.println("bc          - send broadcast custom ARP packets");
		System.out.println("read        - show saved packets");
		System.out.println("export      - save packets in txt or wireshark compatible format");
		System.out.println("exit        - quit Armot");
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
	 * 10 = HELP <br>
	 * <br>
	 * -1 = ERROR
	 * 
	 * @param command
	 */
	private int commandInterpreter(String commands) {
		String command = commands.split(" ")[0];
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
		if (command.toUpperCase().equals("CONNECTION"))
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
		if (command.toUpperCase().equals("HELP"))
			return 10;
		if (command.toUpperCase().equals("QUIT"))
			return 9;

		return -1;
	}
}
