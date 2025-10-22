package app.routes.impl;

import app.controllers.impl.ArtistController;
import app.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class ArtistRoutes {

    private final ArtistController artistController = new ArtistController();

    public EndpointGroup getRoutes() {
        return () -> {
            // Post
            post("/", artistController::create, Role.ADMIN);

            // Get
            get("/", artistController::readAll);
            get("/{id}", artistController::read);
            get("/{id}/albums", artistController::readAlbumsByArtist);

            // Put
            put("/{id}", artistController::update, Role.ADMIN);

            // Delete
            delete("/{id}", artistController::delete, Role.ADMIN);

        };
    }
}
