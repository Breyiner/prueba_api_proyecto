package CONFIG;

import UTILS.ConnectionDB;
import org.glassfish.jersey.server.ResourceConfig;

public class AppConfig extends ResourceConfig {
    public AppConfig() {
        
        ConnectionDB.connect();
        
        packages("CONTROLLER", "FILTERS");
    }
}

