package app.controllers.impl;

import app.config.HibernateConfig;
import app.controllers.IController;
import app.daos.impl.SongDAO;
import app.dtos.SongDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class SongController implements IController<SongDTO, Integer> {

    private final SongDAO dao;

    public SongController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = SongDAO.getInstance(emf);
    }

    @Override
    public void read(Context ctx) {
        // What we are requesting
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid key").get();
        // The DTO
        SongDTO songDTO = dao.read(id);
        // Response
        ctx.res().setStatus(200);
        ctx.json(songDTO, SongDTO.class);
    }

    @Override
    public void readAll(Context ctx) {
        // List of DTOS
        List<SongDTO> songDTOS = dao.readAll();
        // Response
        ctx.res().setStatus(200);
        ctx.json(songDTOS, SongDTO.class);
    }

    @Override
    public void create(Context ctx) {
        // Request
        SongDTO jsonRequest = ctx.bodyAsClass(SongDTO.class);
        // DTO
        SongDTO songDTO = dao.create(jsonRequest);
        // Response
        ctx.res().setStatus(201);
        ctx.json(songDTO, SongDTO.class);
    }

    @Override
    public void update(Context ctx) {
        // Request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // DTO
        SongDTO songDTO = dao.update(id, validateEntity(ctx));
        // Response
        ctx.res().setStatus(200);
        ctx.json(songDTO, SongDTO.class);
    }

    @Override
    public void delete(Context ctx) {
        // Request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        dao.delete(id);
        // Response
        ctx.res().setStatus(204);
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        return dao.validatePrimaryKey(integer);
    }

    @Override
    public SongDTO validateEntity(Context ctx) {
        return ctx.bodyValidator(SongDTO.class)
                .check(s -> s.getSongName() != null && !s.getSongName().isEmpty(), "Song name must be set")
                .check(s -> s.getAlbumName() != null && !s.getAlbumName().isEmpty(), "Album name must be set")
                .check(s -> s.getMainArtistName() != null && !s.getMainArtistName().isEmpty(), "Main artist name must be set")
                .check(s -> s.getGenre() != null, "Genre type must be set")
                .check(s -> s.getDuration() != null && !s.getDuration().isEmpty(), "Duration must be set")
                .get();
    }
}
