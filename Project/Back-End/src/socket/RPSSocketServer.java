package socket;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.NotYetConnectedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;
import org.restlet.resource.ClientResource;

import java.util.UUID;

public class RPSSocketServer extends WebSocketServer{
	private HashMap<String, String> sessions = new HashMap<String, String>() ;
	
	public RPSSocketServer(int port)throws UnknownHostException {
		super( new InetSocketAddress( port ) );
	}
	
	public RPSSocketServer( InetSocketAddress address ) {
		super( address );
	}
	
	@Override
	public void onClose(WebSocket arg0, int arg1, String arg2, boolean arg3) {
		// TODO Auto-generated method stub
		System.out.println("someone disconnects!!");
		//will remove the tokens out
		//will send a REST call to update
		String token = sessions.get(arg0.getRemoteSocketAddress().getAddress().getHostAddress());
		sessions.remove(token);
		ClientResource clientResource = new ClientResource( "http://169.44.13.218:8080/end"); 
        JSONObject object = new JSONObject();
        object.put("user_id", token);
        clientResource.post(object);
        
		//check to save the game
	}

	@Override
	public void onError(WebSocket arg0, Exception arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(WebSocket arg0, String message) {
		// TODO Auto-generated method stub
		System.out.println("some one send message " + message);
		JSONObject  object = new JSONObject();
		object.put("smartMove", "paper");
		object.put("match", "");
		arg0.send( object.toString());
		
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake arg1) {
		// TODO Auto-generated method stub
		//this.sendToAll( "some one joins the room!!!" );
		String token = UUID.randomUUID().toString();
		conn.send(token);
		sessions.put(conn.getRemoteSocketAddress().getAddress().getHostAddress(), token);
		System.out.println( conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!" );
	}
	
	/**
	 * Sends <var>text</var> to all currently connected WebSocket clients.
	 * 
	 * @param text
	 *            The String to send across the network.
	 * @throws InterruptedException
	 *             When socket related I/O errors occur.
	 */
	public void sendToAll( String text ) {
		Collection<WebSocket> con = connections();
		synchronized ( con ) {
			for( WebSocket c : con ) {
				c.send( text );
			}
		}
	}

}
