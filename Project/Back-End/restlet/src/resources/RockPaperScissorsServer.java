package resources;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;
import org.restlet.service.CorsService;

public class RockPaperScissorsServer extends Application {

	public RockPaperScissorsServer()
	{
		CorsService corsService = new CorsService();         
	    corsService.setAllowedOrigins((Set<String>) new HashSet(Arrays.asList("*")));
	    //corsService.setAllowedCredentials(true);
	    getServices().add(corsService);
	}
    public static void main(String[] args) throws Exception {
       Component server = new Component() ;
        server.getServers().add(Protocol.HTTP, 8080) ;
        server.getDefaultHost().attach(new RockPaperScissorsServer()) ;
        server.start();
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext()) ;
        router.attach("/rps", RockPaperScissorsResource.class);
        router.attach("/end", RockPaperScissorsEndBotResource.class);
        return router;
    }
}

