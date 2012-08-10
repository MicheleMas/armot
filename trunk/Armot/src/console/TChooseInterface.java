package console;

import java.util.Scanner;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterfaceAddress;

public class TChooseInterface {

	public TChooseInterface() {
		jpcap.NetworkInterface[] devices = JpcapCaptor.getDeviceList();

		if (devices.length == 0) {
			System.out.println("No device found, make sure you started Armot as root");
			System.exit(0);
		}

		System.out.println("Select the number of the nic you wish to use:"); // TODO
																				// check
																				// translation
		for (int i = 0; i < devices.length; i++) {
			System.out.println(i + " :" + devices[i].name);
			System.out.println("    data link:" + devices[i].datalink_name
					+ "(" + devices[i].datalink_description + ")");
			System.out.print("    MAC address:");
			for (byte b : devices[i].mac_address)
				System.out.print(Integer.toHexString(b & 0xff) + ":");
			System.out.println();
			for (NetworkInterfaceAddress a : devices[i].addresses)
				System.out.println("    address:" + a.address + " " + a.subnet
						+ " " + a.broadcast);
		}
		Scanner in = new Scanner(System.in);
		String input;
		while (in.hasNext()) {
			input = in.nextLine();
			try {
				int selection = Integer.parseInt(input);
				if (selection < 0 || selection >= devices.length)
					System.out
							.println("Selection error, please use a number between 0 and "
									+ (devices.length - 1));
				else {
					String myIp;
					try {
						myIp = ("" + devices[selection].addresses[0].address).substring(1);
						Main main = new Main(selection, myIp);
					} catch (Exception err) {
						System.out.println("Selection error, the device selected is not in use");
					}
				}
			} catch (NumberFormatException e) {
				System.out
						.println("Selection error, please use a number between 0 and "
								+ (devices.length - 1));
			}
		}

	}
}
