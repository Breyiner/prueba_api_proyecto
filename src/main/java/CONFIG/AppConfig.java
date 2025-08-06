package CONFIG;

import DATABASE.ConnectionDB;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("api")
public class AppConfig extends ResourceConfig {
    
    public AppConfig() {
        
        packages("CONTROLLER", "CONFIG");
        ConnectionDB.connect();
    }
}

