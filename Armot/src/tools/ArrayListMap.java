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


public class ArrayListMap<String, V> {
	private ArrayList<Entry<String, ArrayList<String>>> a;

	public ArrayListMap() {
		a = new ArrayList<Entry<String, ArrayList<String>>>();
	}

	public int size() {
		return a.size();
	}

	public boolean isEmpty() {
		return a.isEmpty();
	}

	public ArrayList<String> get(String key) {
		int keyPos = findKeyPos(key);
		if (keyPos == -1)
			return null;
		return a.get(keyPos).getValue();
	}

	public ArrayList<String> put(String key, ArrayList<String> value) {
		if (value == null)
			throw new IllegalArgumentException();
		int keyPos = findKeyPos(key);
		if (keyPos == -1) {
			a.add(new Entry<String, ArrayList<String>>(key, value));
			return null;
		}
		ArrayList<String> old = a.get(keyPos).getValue();
		a.get(keyPos).setValue(value);
		return old;
	}

	public ArrayList<String> remove(String key) {
		int keyPos = findKeyPos(key);
		if (keyPos == -1)
			return null;
		ArrayList<String> old = a.get(keyPos).getValue();
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
		private ArrayList<String> value;

		public Entry(String k, ArrayList<String> v) {
			key = k;
			value = v;
		}

		public String getKey() {
			return key;
		}

		public ArrayList<String> getValue() {
			return value;
		}

		public void setKey(String k) {
			key = k;
		}

		public void setValue(ArrayList<String> v) {
			value = v;
		}
	}
}