package be.vinci.pae.domain;

import org.mindrot.jbcrypt.BCrypt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import views.Views;

@JsonInclude(JsonInclude.Include.NON_NULL) // ignore all null fields in order to avoid sending props not linked to a
											// JSON view
class UserImpl implements User {

	@JsonView(Views.Public.class)
	private int id;
	@JsonView(Views.Public.class)
	private String login;

	@JsonView(Views.Internal.class)
	private String password;

	@Override
	public String getLogin() {
		return login;
	}

	@Override
	public void setLogin(String login) {
		this.login = login;
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public void setID(int id) {
		this.id = id;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean checkPassword(String password) {
		return BCrypt.checkpw(password, this.password);
	}

	@Override
	public String hashPassword(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}

	@Override
	public String toString() {
		return "{id:" + id + ", login:" + login + ", password:" + password + "}";
	}

}
