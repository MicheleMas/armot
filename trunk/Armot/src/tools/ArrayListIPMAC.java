package tools;

import java.util.ArrayList;

public class ArrayListIPMAC<String, V> {
	private ArrayList<Entry<String, String>> a;

	public ArrayListIPMAC() {
		a = new ArrayList<Entry<String, String>>();
	}

	public int size() {
		return a.size();
	}

	public boolean isEmpty() {
		return a.isEmpty();
	}

	public String get(String key) {
		int keyPos = findKeyPos(key);
		if (keyPos == -1)
			return null;
		return a.get(keyPos).getValue();
	}

	public String put(String key, String value) {
		if (value == null)
			throw new IllegalArgumentException();
		int keyPos = findKeyPos(key);
		if (keyPos == -1) {
			a.add(new Entry<String, String>(key, value));
			return null;
		}
		String old = a.get(keyPos).getValue();
		a.get(keyPos).setValue(value);
		return old;
	}

	public String remove(String key) {
		int keyPos = findKeyPos(key);
		if (keyPos == -1)
			return null;
		String old = a.get(keyPos).getValue();
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
		if (keys == null)
			throw new IllegalArgumentException();
		for (int i = 0; i < size(); i++)
			keys.add(a.get(i).getKey());
		return keys;
	}

	public void clear() {
		a.clear();
	}

	private class Entry<String, V> {
		private String key;
		private String value;

		public Entry(String k, String v) {
			key = k;
			value = v;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

		public void setKey(String k) {
			key = k;
		}

		public void setValue(String v) {
			value = v;
		}
	}
}