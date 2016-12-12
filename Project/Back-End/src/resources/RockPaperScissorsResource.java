package resources;

import java.io.IOException;

import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import rps.MoveType;
import rps.RockPaperScissorsController;



public class RockPaperScissorsResource extends ServerResource {
	// GumballMachine machine = GumballMachine.getInstance() ;
		RockPaperScissorsController controller = RockPaperScissorsController.getInstance();
		
		@Post
		public synchronized Representation post(JsonRepresentation jsonRep) throws IOException {
			//getting json body
			if(jsonRep != null){
				JSONObject body = jsonRep.getJsonObject();
				JSONObject response = new JSONObject();
				if(body.has("user_id")){
					String user_id = (String)body.get("user_id");
					//checking uniqueness of username
					MoveType smartMove = controller.getSmartMove(user_id);
					response.put("user_id", user_id);
					response.put("smartMove", smartMove.getSymbol().toLowerCase());				
					return new JsonRepresentation(response);
				}
			}
			return null;
		}
		
		@Put
		public synchronized Representation put(JsonRepresentation jsonRep)
		{
			//this call will update the hash map after the game
			if(jsonRep != null){
				JSONObject body = jsonRep.getJsonObject();
				JSONObject response = new JSONObject();
				if(body.has("user_id") && body.has("userMove") && body.has("smartMove")){
					String user_id = (String)body.get("user_id");
					String userMove = (String)body.get("userMove");
					String smartMove = (String) body.get("smartMove");
					controller.updateMove(user_id, userMove, smartMove);		
					return new JsonRepresentation(response);
				}
			}
			return null;
		}
}
