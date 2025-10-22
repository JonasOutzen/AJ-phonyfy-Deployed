package app.routes;

import app.routes.impl.ArtistRoutes;
import app.routes.impl.PlaylistRoutes;
import app.routes.impl.SongRoutes;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {

    private final SongRoutes songRoutes = new SongRoutes();
    private final ArtistRoutes artistRoutes = new ArtistRoutes();
    private final PlaylistRoutes playlistRoutes = new PlaylistRoutes();

    public EndpointGroup getRoutes() {
        return () -> {
            path("/songs", songRoutes.getRoutes());
            path("/artists", artistRoutes.getRoutes());
            path("/playlists", playlistRoutes.getRoutes());
        };
    }
}
