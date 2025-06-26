package CONFIG;

import UTILS.ConnectionDB;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("api")
public class AppConfig extends ResourceConfig {
    
    public AppConfig() {
        
        ConnectionDB.connect();
        packages("CONTROLLER", "CONFIG", "MIDDLEWARES");
    }
}

