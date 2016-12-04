package resources;

import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import rps.MoveType;
import rps.RockPaperScissorController;



public class RockPaperScissorResource extends ServerResource {
	// GumballMachine machine = GumballMachine.getInstance() ;
		RockPaperScissorController controller = RockPaperScissorController.getInstance();
		
		@Post
		public synchronized Representation post(JsonRepresentation jsonRep) {
			//getting json body
			if(jsonRep != null){
				JSONObject body = jsonRep.getJsonObject();
				JSONObject response = new JSONObject();
				if(body.has("match")){
					String match = (String)body.get("match");
					//checking uniqueness of username
					MoveType smartMove = controller.getSmartThrow(match);
					response.put("smartMove", smartMove.getSymbol().toLowerCase());
					System.out.println("match:"+match);
					controller.printHash();
					return new JsonRepresentation(response);
				}
			}
			return null;
		}
		
		@Get
	    public synchronized Representation get() {
			JSONObject json = new JSONObject();
			json.put("test", "heelo");
			return new StringRepresentation ( "hello	" ) ;
	    }
}
