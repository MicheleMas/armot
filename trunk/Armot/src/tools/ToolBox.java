package tools;

import jpcap.JpcapSender;
import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;

public class ToolBox {
	
	private static JpcapSender sender;
	private static byte[] myMac;
	private static JpcapCaptor captor;
	private static NetworkInterface nic;
	private static boolean flag;
	
	public void setSender (JpcapSender newSender) {
		sender = newSender;
	}
	
	public void setMyMac (byte[] newMac) {
		myMac = newMac;
	}
	
	public void setNic (NetworkInterface newNic) {
		nic = newNic;
	}
	
	public JpcapSender getSender () {
		return sender;
	}
	
	public byte[] getMyMac () {
		return myMac;
	}
	
	public NetworkInterface getNic() {
		return nic;
	}

	/*public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	public boolean getFlag() {
		return this.flag;
	}*/
}
