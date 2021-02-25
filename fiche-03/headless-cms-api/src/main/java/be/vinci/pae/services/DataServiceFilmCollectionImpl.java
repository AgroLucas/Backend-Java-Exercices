package be.vinci.pae.services;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.text.StringEscapeUtils;

import be.vinci.pae.api.utils.Json;
import be.vinci.pae.domain.Film;
import be.vinci.pae.domain.FilmFactory;
import be.vinci.pae.utils.Config;
import jakarta.inject.Inject;

public class DataServiceFilmCollectionImpl implements DataServiceFilmCollection {
	private static final String DB_FILE_PATH = Config.getProperty("DatabaseFilePath");
	private static final String COLLECTION_NAME = "films";

	private List<Film> films;

	@Inject
	private FilmFactory factory;

	public DataServiceFilmCollectionImpl() {
		this.films = Json.loadDataFromFile(DB_FILE_PATH, COLLECTION_NAME, Film.class);
		System.out.println("FILM DATA SERVICE");
	}

	@Override
	public Film getFilm(int id) {
		return this.films.stream().filter(item -> item.getId() == id).findAny().orElse(null);
	}

	@Override
	public List<Film> getFilms() {
		return this.films;
	}

	@Override
	public List<Film> getFilms(int minimumDuration) {
		return this.films.stream().filter(item -> item.getDuration() >= minimumDuration).collect(Collectors.toList());
	}

	@Override
	public Film addFilm(Film film) {
		film.setId(nexFilmtId());
		// escape dangerous chars to protect against XSS attacks
		film.setTitle(StringEscapeUtils.escapeHtml4(film.getTitle()));
		film.setLink(StringEscapeUtils.escapeHtml4(film.getLink()));
		this.films.add(film);
		Json.saveDataToFile(this.films, DB_FILE_PATH, COLLECTION_NAME);
		return film;
	}

	@Override
	public int nexFilmtId() {
		if (this.films.size() == 0)
			return 1;
		return this.films.get(this.films.size() - 1).getId() + 1;
	}

	@Override
	public Film deleteFilm(int id) {
		if (this.films.size() == 0 | id == 0)
			return null;
		Film filmToDelete = getFilm(id);
		if (filmToDelete == null)
			return null;
		int index = this.films.indexOf(filmToDelete);
		this.films.remove(index);
		Json.saveDataToFile(this.films, DB_FILE_PATH, COLLECTION_NAME);
		return filmToDelete;
	}

	@Override
	public Film updateFilm(Film film) {
		if (this.films.size() == 0 | film == null)
			return null;
		Film filmToUpdate = getFilm(film.getId());
		if (filmToUpdate == null)
			return null;
		// escape dangerous chars to protect against XSS attacks
		film.setTitle(StringEscapeUtils.escapeHtml4(film.getTitle()));
		film.setLink(StringEscapeUtils.escapeHtml4(film.getLink()));
		// update the data structure
		int index = this.films.indexOf(filmToUpdate);
		this.films.set(index, film);
		Json.saveDataToFile(this.films, DB_FILE_PATH, COLLECTION_NAME);
		return film;
	}
}
