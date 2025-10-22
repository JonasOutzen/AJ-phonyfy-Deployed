package app.routes.impl;

import static io.javalin.apibuilder.ApiBuilder.*;
import io.javalin.apibuilder.EndpointGroup;
import app.controllers.impl.SongController;
import app.security.enums.Role;

public class SongRoutes {

    private final SongController songController = new SongController();

    public EndpointGroup getRoutes(){
        return () -> {
            // Post
            post("/", songController::create, Role.ADMIN);

            // Get
            get("/", songController::readAll);
            get("/{id}", songController::read);

            // Put
            put("/{id}", songController::update, Role.ADMIN);

            // Delete
            delete("/{id}", songController::delete, Role.ADMIN);

        };
    }
}
