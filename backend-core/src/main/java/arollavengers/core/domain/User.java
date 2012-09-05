package arollavengers.core.domain;

public class User {
  private final UserId userId;

  private User(UserId userId) {
    this.userId = userId;
  }

  public static User withId(UserId userId){
    return new User(userId);
  }

  public static User fresh(){
    return new User(new UserId());
  }

  public UserId userId() {
    return userId;
  }
}
