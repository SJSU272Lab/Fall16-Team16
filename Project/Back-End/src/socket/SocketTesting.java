package socket;

import java.net.UnknownHostException;


public class SocketTesting {
	public static void main(String[]arg) throws UnknownHostException
	{
		RPSSocketServer server1 = new RPSSocketServer(8990);
		server1.start();
		
	}
}
