package be.vinci.pae.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = UserImpl.class)
public interface User {

	  int getID();

	  void setID(int id);

	  String getLogin();

	  void setLogin(String login);

	  String getPassword();

	  void setPassword(String password);

	  boolean checkPassword(String password);

	  String hashPassword(String password);	  
 
	  String toString();

	}