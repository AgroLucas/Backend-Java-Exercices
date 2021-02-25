package be.vinci.pae.services;

import java.util.List;
import be.vinci.pae.api.utils.Json;
import be.vinci.pae.domain.UserFactory;
import be.vinci.pae.domain.User;
import be.vinci.pae.utils.Config;
import jakarta.inject.Inject;

public class DataServiceUserCollectionImpl implements DataServiceUserCollection {

	private static final String DB_FILE_PATH = Config.getProperty("DatabaseFilePath");
	private static final String COLLECTION_NAME = "users";

	private List<User> users;

	@Inject
	private UserFactory factory;

	public DataServiceUserCollectionImpl() {
		this.users = Json.loadDataFromFile(DB_FILE_PATH, COLLECTION_NAME, User.class);
		System.out.println("USER DATA SERVICE");
	}

	@Override
	public User getUser(String login) {
		return this.users.stream().filter(u -> u.getLogin().equals(login)).findAny().orElse(null);
	}

	@Override
	public User getUser(int id) {
		return this.users.stream().filter(u -> u.getID() == id).findAny().orElse(null);
	}

	@Override
	public List<User> getUsers() {

		return users;
	}

	@Override
	public User addUser(User user) {
		user.setID(this.users.size() + 1);
		this.users.add(user);
		Json.saveDataToFile(this.users, DB_FILE_PATH, COLLECTION_NAME);
		return user;
	}

}
