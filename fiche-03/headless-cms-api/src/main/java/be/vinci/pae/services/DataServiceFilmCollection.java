package be.vinci.pae.services;

import java.util.List;

import be.vinci.pae.domain.Film;

public interface DataServiceFilmCollection {

	Film getFilm(int id);

	List<Film> getFilms();
	List<Film> getFilms(int minimumDuration);

	Film addFilm(Film film);
	
	int nexFilmtId();
	
	Film updateFilm(Film film);
	
	Film deleteFilm(int id);

}