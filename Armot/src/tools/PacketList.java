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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import jpcap.packet.IPPacket;


public class PacketList<String, V> {
	private ArrayList<Entry<String, IPPacket>> a;

	public PacketList() {
		a = new ArrayList<Entry<String, IPPacket>>();
	}

	public int size() {
		return a.size();
	}

	public boolean isEmpty() {
		return a.isEmpty();
	}

	public IPPacket get(String key) {
		int keyPos = findKeyPos(key);
		if (keyPos == -1)
			return null;
		return a.get(keyPos).getValue();
	}

	public IPPacket put(String key, IPPacket value) {
		if (value == null)
			throw new IllegalArgumentException();
		int keyPos = findKeyPos(key);
		if (keyPos == -1) {
			a.add(new Entry<String, IPPacket>(key, value));
			return null;
		}
		IPPacket old = a.get(keyPos).getValue();
		a.get(keyPos).setValue(value);
		return old;
	}

	public IPPacket remove(String key) {
		int keyPos = findKeyPos(key);
		if (keyPos == -1)
			return null;
		IPPacket old = a.get(keyPos).getValue();
		a.set(keyPos, a.get(size() - 1));
		a.remove(size() - 1);
		return old;
	}

	public int findKeyPos(String key) {
		if (key == null)
			throw new IllegalArgumentException();
		for (int i = 0; i < size(); i++)
			if (a.get(i).getKey().equals(key))
				return i;
		return -1;
	}

	public ArrayList<String> getKeys() {
		ArrayList<String> keys = new ArrayList<String>();
		if (keys == null || keys.size() != size())
			throw new IllegalArgumentException();
		for (int i = 0; i < size(); i++)
			keys.add(a.get(i).getKey());
		return keys;
	}
	
	public Enumeration<String> keys() {
		ArrayList<String> tmp = new ArrayList<String>();
		for(int i=0; i < a.size(); i++) {
			tmp.add(a.get(i).getKey());
		}
		
		return Collections.enumeration(tmp);
	}

	public void clear() {
		a.clear();
	}

	private class Entry<String, V> {
		private String key;
		private IPPacket value;

		public Entry(String k, IPPacket v) {
			key = k;
			value = v;
		}

		public String getKey() {
			return key;
		}

		public IPPacket getValue() {
			return value;
		}

		public void setKey(String k) {
			key = k;
		}

		public void setValue(IPPacket v) {
			value = v;
		}
	}
}