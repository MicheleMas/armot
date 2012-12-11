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

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import jpcap.JpcapCaptor;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

public class Reader implements Runnable {

	// inizializzo il lettore di pacchetti e lo setto sui pacchetti IP

	private JpcapCaptor captor;
	static private Table table;
	static private boolean stop;

	public Reader(JpcapCaptor captor, String myIP) {
		this.captor = captor;
		table = new Table();
		try {
			captor.setFilter("ip", true);
		} catch (IOException e) {
			System.out.println("Error on reading thread!");
			System.err.println(e.toString());
		}
		Thread thread = new Thread(this);
		thread.setName("Reader");
		thread.setPriority(8);
		thread.start();
	}

	// dai pacchetti che leggo, estraggo mittente e destinatario.
	// Se il mittente corrisponde ad un utente della lan, allora salvo il
	// pacchetto tra gli inviati.
	// Se invece corrisponde ad un destinatario, salvo il pacchetto fra i
	// ricevuti.

	@Override
	public void run() {
		stop = false;
		Packet packet = new Packet();
		while (!stop) {
			try {
				captor.setFilter("ip", true);
			} catch (IOException e) {
				System.err.println("error setting Reader filter");
			}
			
			try {
				packet = captor.getPacket();
				IPPacket pk = (IPPacket) packet;
				if (pk == null) {
				} else {
					String src = pk.src_ip.toString().substring(1);
					String dst = pk.dst_ip.toString().substring(1);
					Date date = new Date();
					Timestamp time = new Timestamp(date.getTime());
					String timestamp = time.toString();
					if (Table.keyInIPMAC(src)) {
						table.putIpPacketSent(src, timestamp, pk);
					}
					if (Table.keyInIPMAC(dst)) {
						time.setTime(date.getTime());
						table.putIpPacketReceived(dst, timestamp, pk);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("packet" + packet.getClass());
			}
		}

	}

	public void stop() {
		stop = true;

	}

}
