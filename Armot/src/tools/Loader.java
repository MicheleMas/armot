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

import console.TChooseInterface;

public class Loader {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ChooseInterface ci;
		TChooseInterface tci;
		if (args.length > 0 && "-t".equals(args[0]))
			tci = new TChooseInterface();
		else
			ci = new ChooseInterface();
	}

}
