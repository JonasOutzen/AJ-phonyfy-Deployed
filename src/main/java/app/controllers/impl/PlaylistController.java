package app.controllers.impl;

import app.config.HibernateConfig;
import app.controllers.IController;
import app.daos.impl.PlaylistDAO;
import app.dtos.PlaylistDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class PlaylistController implements IController<PlaylistDTO, Integer> {

    private final PlaylistDAO dao;

    public PlaylistController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = PlaylistDAO.getInstance(emf);
    }

    @Override
    public void read(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid key").get();
        PlaylistDTO dto = dao.read(id);
        ctx.res().setStatus(200);
        ctx.json(dto, PlaylistDTO.class);
    }

    @Override
    public void readAll(Context ctx) {
        List<PlaylistDTO> list = dao.readAll();
        ctx.res().setStatus(200);
        ctx.json(list, PlaylistDTO.class);
    }

    // Extra: GET /playlists/user/{username}
    public void readPlaylistsByOwner(Context ctx) {
        String username = ctx.pathParam("username");
        List<PlaylistDTO> playlists = dao.readPlaylistsByOwner(username);
        ctx.res().setStatus(200);
        ctx.json(playlists);
    }

    @Override
    public void create(Context ctx) {
        PlaylistDTO req = validateEntity(ctx);
        PlaylistDTO created = dao.create(req);
        ctx.res().setStatus(201);
        ctx.json(created, PlaylistDTO.class);
    }

    @Override
    public void update(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        PlaylistDTO updated = dao.update(id, validateEntityPartial(ctx));
        ctx.res().setStatus(200);
        ctx.json(updated, PlaylistDTO.class);
    }

    @Override
    public void delete(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        dao.delete(id);
        ctx.res().setStatus(204);
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        return dao.validatePrimaryKey(id);
    }

    @Override
    public PlaylistDTO validateEntity(Context ctx) {
        // For create: require name + ownerUsername; songIds optional. Matches entity & DTO. 
        return ctx.bodyValidator(PlaylistDTO.class)
                .check(p -> p.getPlayListName() != null && !p.getPlayListName().isEmpty(), "Playlist name must be set")
                .check(p -> p.getOwnerUsername() != null && !p.getOwnerUsername().isEmpty(), "Owner username must be set")
                .get();
    }

    // For PATCH-like updates (name/owner/songs) where fields are optional
    public PlaylistDTO validateEntityPartial(Context ctx) {
        return ctx.bodyValidator(PlaylistDTO.class).get();
    }
}
