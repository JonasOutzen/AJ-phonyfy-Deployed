package app.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {

private Sample1Routes sample1Routes = new Sample1Routes();
private Sample2Routes sample2Routes = new Sample2Routes();

public EndpointGroup getRoutes() {
    return () -> {
        path("/sample1", sample1Routes.getRoutes());
        path("/sample2", sample2Routes.getRoutes());
    };
}
}
