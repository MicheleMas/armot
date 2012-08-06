package tools;

import java.util.ArrayList;
import java.util.Hashtable;

import jpcap.packet.IPPacket;

public class ArrayListPacket<String, V> {
	private ArrayList<Entry<String, PacketList<String, IPPacket>>> a;

	public ArrayListPacket() {
		a = new ArrayList<Entry<String, PacketList<String, IPPacket>>>();
	}

	public int size() {
		return a.size();
	}

	public boolean isEmpty() {
		return a.isEmpty();
	}

	public PacketList<String, IPPacket> get(String key) {
		int keyPos = findKeyPos(key);
		if (keyPos == -1)
			return null;
		return a.get(keyPos).getValue();
	}

	public PacketList<String, IPPacket> put(String key, PacketList<String, IPPacket> value) {
		if (value == null)
			throw new IllegalArgumentException();
		int keyPos = findKeyPos(key);
		if (keyPos == -1) {
			a.add(new Entry<String, PacketList<String, IPPacket>>(key, value));
			return null;
		}
		PacketList<String, IPPacket> old = a.get(keyPos).getValue();
		a.get(keyPos).setValue(value);
		return old;
	}

	public PacketList<String, IPPacket> remove(String key) {
		int keyPos = findKeyPos(key);
		if (keyPos == -1)
			return null;
		PacketList<String, IPPacket> old = a.get(keyPos).getValue();
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
		/*if (keys == null || keys.size() != size())
			throw new IllegalArgumentException();*/
		for (int i = 0; i < size(); i++)
			keys.add(a.get(i).getKey());
		return keys;
	}

	public void clear() {
		a.clear();
	}

	private class Entry<String, V> {
		private String key;
		private PacketList<String, IPPacket> value;

		public Entry(String k, PacketList<String, IPPacket> v) {
			key = k;
			value = v;
		}

		public String getKey() {
			return key;
		}

		public PacketList<String, IPPacket> getValue() {
			return value;
		}

		public void setKey(String k) {
			key = k;
		}

		public void setValue(PacketList<String, IPPacket> v) {
			value = v;
		}
	}
}