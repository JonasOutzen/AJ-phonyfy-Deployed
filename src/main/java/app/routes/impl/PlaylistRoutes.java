package app.routes.impl;

import app.controllers.impl.PlaylistController;
import app.controllers.impl.SongController;
import app.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class PlaylistRoutes {

    private final PlaylistController playlistController = new PlaylistController();

    public EndpointGroup getRoutes(){
        return () -> {
            // Post
            post("/", playlistController::create, Role.ADMIN);

            // Get
            get("/", playlistController::readAll);
            get("/{id}", playlistController::read);
            get("/{username}", playlistController::readPlaylistsByOwner);

            // Put
            put("/{id}", playlistController::update, Role.ADMIN);

            // Delete
            delete("/{id}", playlistController::delete, Role.ADMIN);

        };
    }
}
