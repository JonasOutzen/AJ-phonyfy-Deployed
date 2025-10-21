package app.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {

private Sample1Routes sample1Routes = new Sample1Routes();

public EndpointGroup getRoutes() {
    return () -> {
        path("/sample1", sample1Routes.getRoutes());
    };
}
}
