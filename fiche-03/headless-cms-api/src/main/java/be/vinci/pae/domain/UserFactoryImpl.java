package be.vinci.pae.domain;

public class UserFactoryImpl implements UserFactory {

  @Override
  public User getUser() {
    return new UserImpl();
  }

}
