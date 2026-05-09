
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.wadl.WadlFeature;

@ApplicationPath("/api")
public class EarnitApp extends Application {
    public EarnitApp() {
        ResourceConfig resourceConfig = new ResourceConfig()
                .packages("Routes") // Ensure this package name matches where your resources are located
                .register(WadlFeature.class);
    }
}
