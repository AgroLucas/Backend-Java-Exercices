package be.vinci.pae.api;

import java.util.List;

import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.Film;
import be.vinci.pae.domain.FilmFactory;

import be.vinci.pae.services.DataServiceFilmCollection;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

@Singleton
@Path("/films")
public class FilmResource {

	@Inject
	private DataServiceFilmCollection dataService;

	@Inject
	private FilmFactory filmFactory;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Authorize
	public Film create(Film film) {	
		if (film == null || film.getTitle() == null || film.getTitle().isEmpty())
			throw new WebApplicationException("Lacks of mandatory info", null, Status.BAD_REQUEST);
		this.dataService.addFilm(film);

		return film;
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Authorize
	public Film getFilm(@PathParam("id") int id) {
		if (id == 0)
			throw new WebApplicationException("Lacks of mandatory id info", null, Status.BAD_REQUEST);

		Film filmFound = this.dataService.getFilm(id);

		if (filmFound == null)
			throw new WebApplicationException("Ressource with id = " + id + " could not be found", null,
					Status.NOT_FOUND);

		return filmFound;
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Authorize
	public Film updateFilm(Film film, @PathParam("id") int id) {
		
		if (film == null || film.getTitle() == null || film.getTitle().isEmpty())
			throw new WebApplicationException("Lacks of mandatory info", null, Status.BAD_REQUEST);

		film.setId(id);
		Film updatedFilm = this.dataService.updateFilm(film);

		if (updatedFilm == null)
			throw new WebApplicationException("Ressource with id = " + id + " could not be found", null, Status.NOT_FOUND);

		return updatedFilm;
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Authorize
	public Film deleteFilm(@PathParam("id") int id) {		
		if (id == 0)
			throw new WebApplicationException("Lacks of mandatory info", null, Status.BAD_REQUEST);

		Film deletedFilm = this.dataService.deleteFilm(id);

		if (deletedFilm == null)
			throw new WebApplicationException("Ressource with id = " + id + " could not be found", null,
					Status.NOT_FOUND);

		return deletedFilm;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Authorize
	public List<Film> getAllFilms( @DefaultValue("-1") @QueryParam("minimum-duration") int minimumDuration) {
		if(minimumDuration != -1)
			return this.dataService.getFilms(minimumDuration);
		return this.dataService.getFilms();

	}

}
