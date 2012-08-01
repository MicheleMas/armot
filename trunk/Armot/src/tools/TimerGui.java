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
