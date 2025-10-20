package app.routes;

import app.controllers.impl.SampleController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;


public class Sample1Routes {
    SampleController controller = new SampleController();

    public EndpointGroup getRoutes(){
        return () -> {
            get("/", controller::getAllSamples);
            get("/{id}", controller::getSampleById);
            post("/", controller::createSample);
            put("/{id}", controller::updateSample);
            delete("/{id}", controller::deleteSample);

        };
    }
}
