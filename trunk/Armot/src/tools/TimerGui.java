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

public class TimerGui implements Runnable {

	static private boolean stop;
	static volatile private MainWindow reference;
	
	public TimerGui(MainWindow reference) {
		this.reference = reference;
	}
	
	public void start() {
		Thread thread = new Thread(this);
		thread.setName("Timer");
		thread.setPriority(8);
		thread.start();
	}
	
	public void stop() {
		stop = true;
	}
	
	@Override
	public void run() {
		stop = false;
		try {
			Thread.currentThread().sleep(1000L);
		} catch (InterruptedException e1) {
			this.stop();
		}
		while(!stop) {
			try {
				reference.setIPList();
				Thread.currentThread().sleep(1000L);
			} catch (InterruptedException e) {
				stop = true;
			}
		}
		
	}
	
	

}
