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

public class RockPaperScissorServer extends Application {

	public RockPaperScissorServer()
	{
		CorsService corsService = new CorsService();         
	    corsService.setAllowedOrigins((Set<String>) new HashSet(Arrays.asList("*")));
	    //corsService.setAllowedCredentials(true);
	    getServices().add(corsService);
	}
    public static void main(String[] args) throws Exception {
       Component server = new Component() ;
        server.getServers().add(Protocol.HTTP, 8280) ;
        server.getDefaultHost().attach(new RockPaperScissorServer()) ;
        server.start();
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext()) ;
        router.attach("/rps", RockPaperScissorResource.class);
        return router;
    }
}

