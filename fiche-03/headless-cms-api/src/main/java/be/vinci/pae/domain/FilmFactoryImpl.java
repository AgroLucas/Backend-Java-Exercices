package be.vinci.pae.domain;

public class FilmFactoryImpl implements FilmFactory {

	@Override
	public Film getFilm() {		
		return new FilmImpl();
	}

}
