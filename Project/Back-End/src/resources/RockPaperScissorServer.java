package resources;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

public class RockPaperScissorServer extends Application {

    public static void main(String[] args) throws Exception {
       Component server = new Component() ;
        server.getServers().add(Protocol.HTTP, 8080) ;
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

