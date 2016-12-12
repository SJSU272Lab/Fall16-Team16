package resources;

import java.io.IOException;

import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import rps.LoadDB;
import rps.MoveType;
import rps.RockPaperScissorsController;



public class RockPaperScissorsEndBotResource extends ServerResource {
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
					String match = controller.getMatch(user_id);
					LoadDB db = new LoadDB();
					db.connect();
					db.saveRPSGame(user_id, match);
					//checking uniqueness of username
					controller.removeInstance(user_id);
					response.put("user_id", user_id);
					response.put("match",match);
					return new JsonRepresentation(response);
				}
			}
			return null;
		}
}
