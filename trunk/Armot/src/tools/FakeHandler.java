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

import java.util.Enumeration;
import java.util.Hashtable;

import jpcap.JpcapSender;

public class FakeHandler {

	private static Hashtable<String, Thread> handler;
	private static ToolBox container;

	public FakeHandler() {
		if (handler == null) {
			handler = new Hashtable<String, Thread>();
			container = new ToolBox();
		}
	}

	public void addThread(String ip, String targetIP, byte[] myMac,
			JpcapSender sender) {
		Thread packetSender = new Thread(
				new Sender(targetIP, ip, myMac, sender));
		packetSender.start();
		handler.put(ip + "-" + targetIP, packetSender);
		/*container.setFlag(true);*/
		// System.out.println("FakeHandler: adding " + ip + "-" + targetIP); // +++++ DEBUG +++++
	}

	public boolean isActive(String ip, String targetIP) {
		return handler.containsKey(ip + "-" + targetIP);
	}

	public boolean stopThread(String ip, String targetIP) {
		String key = ip + "-" + targetIP;
		Thread packetSender = handler.get(key);
		boolean allOk = false;
		if (packetSender != null) {
			packetSender.interrupt();
			handler.remove(key);
			allOk = true;
			// System.out.println("FakeHandler: removed " + ip + "-" + targetIP); // +++++ DEBUG +++++
		} /*else
			System.err.println("FakeHandler: can't close Fake Thread from "
					+ ip + " to " + targetIP);*/ // +++++ DEBUG +++++
		/*if (handler.size() == 0)
			container.setFlag(false);*/
		return allOk;
	}
	
	public Enumeration<String> getThreads() {
		return handler.keys();
	}
	
	public void stopAll () {
		Enumeration<String> e = handler.keys();
		String s;
		while(e.hasMoreElements()) {
			s = e.nextElement();
			Thread packetSender = handler.get(s);
			if(packetSender != null)
				packetSender.interrupt();
		}
	}

}
