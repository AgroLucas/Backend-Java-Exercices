package be.vinci.pae.domain;

public class FilmImpl implements Film {

	private int id;
	private String title;
	private int duration;
	private long budget;
	private String link;

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public int getDuration() {
		return duration;
	}

	@Override
	public void setDuration(int duration) {
		this.duration = duration;
	}

	@Override
	public long getBudget() {
		return budget;
	}

	@Override
	public void setBudget(long budget) {
		this.budget = budget;
	}

	@Override
	public String getLink() {
		return link;
	}

	@Override
	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		return "FilmImpl [id=" + id + ", title=" + title + ", duration=" + duration + ", budget=" + budget + ", link="
				+ link + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FilmImpl other = (FilmImpl) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	

}
