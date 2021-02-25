package be.vinci.pae.services;


import java.util.List;

//import be.vinci.pae.domain.PublicUser;
import be.vinci.pae.domain.User;

public interface DataServiceUserCollection {

	User getUser(String login);
	

	User getUser(int id);
	
	
	User addUser(User user);
	
	List<User>  getUsers();	

}
