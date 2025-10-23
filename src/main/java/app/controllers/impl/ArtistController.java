package app.controllers.impl;

import app.config.HibernateConfig;
import app.controllers.IController;
import app.daos.impl.ArtistDAO;
import app.daos.impl.AlbumDAO; // if you expose albums via a DAO; optional if ArtistDAO can return AlbumDTOs
import app.dtos.ArtistDTO;
import app.dtos.AlbumDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class ArtistController implements IController<ArtistDTO, Integer> {

    private final ArtistDAO artistDao;
    private final AlbumDAO albumDao; // optional, see note below

    public ArtistController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.artistDao = ArtistDAO.getInstance(emf);
        this.albumDao  = AlbumDAO.getInstance(emf); // or remove if you’ll use artistDao.getAlbumsByArtistId(...)
    }

    @Override
    public void read(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid key").get();
        ArtistDTO dto = artistDao.read(id);
        ctx.res().setStatus(200);
        ctx.json(dto, ArtistDTO.class);
    }

    @Override
    public void readAll(Context ctx) {
        List<ArtistDTO> list = artistDao.readAll();
        ctx.res().setStatus(200);
        ctx.json(list, ArtistDTO.class);
    }

    public void createAlbumForArtist(Context ctx) {
        int artistId = ctx.pathParamAsClass("id", Integer.class)
                .check(this::validatePrimaryKey, "Not a valid artist id").get();

        AlbumDTO req = ctx.bodyValidator(AlbumDTO.class)
                .check(a -> a.getAlbumName() != null && !a.getAlbumName().isBlank(), "Album name must be set")
                .get();

        // bind album to artist from path
        req.setArtistId(artistId);

        AlbumDTO created = albumDao.create(req);
        ctx.status(201).json(created, AlbumDTO.class);
    }


    // Extra: GET /artists/{id}/albums
    public void readAlbumsByArtist(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid key").get();
        List<AlbumDTO> albums = artistDao.readAlbumsByArtistId(id);
        ctx.res().setStatus(200);
        ctx.json(albums, AlbumDTO.class);
    }

    @Override
    public void create(Context ctx) {
        ArtistDTO req = validateEntity(ctx);
        ArtistDTO created = artistDao.create(req);
        ctx.res().setStatus(201);
        ctx.json(created, ArtistDTO.class);
    }

    @Override
    public void update(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        ArtistDTO updated = artistDao.update(id, validateEntity(ctx));
        ctx.res().setStatus(200);
        ctx.json(updated, ArtistDTO.class);
    }

    @Override
    public void delete(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        artistDao.delete(id);
        ctx.res().setStatus(204);
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        return artistDao.validatePrimaryKey(id);
    }

    @Override
    public ArtistDTO validateEntity(Context ctx) {
        // Based on your Artist entity fields: artistName (required), type (optional). :contentReference[oaicite:1]{index=1}
        return ctx.bodyValidator(ArtistDTO.class)
                .check(a -> a.getArtistName() != null && !a.getArtistName().isEmpty(), "Artist name must be set")
                .get();
    }
}
