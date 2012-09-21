package arollavengers.core.domain.user;

import arollavengers.pattern.annotation.ValueObject;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@ValueObject
public class UserDetails {
  private final String firstname;
  private final String lastname;
  private final String email;


  public UserDetails(String firstname, String lastname, String email) {
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
  }

  public String getFirstname() {
    return firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public String getEmail() {
    return email;
  }

  public Builder toBuilder() {
    return new Builder().usingEmail(email).usingFirstname(firstname).usingLastname(lastname);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UserDetails that = (UserDetails) o;

    if (email != null ? !email.equals(that.email) : that.email != null) {
      return false;
    }
    if (firstname != null ? !firstname.equals(that.firstname) : that.firstname != null) {
      return false;
    }
    if (lastname != null ? !lastname.equals(that.lastname) : that.lastname != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = firstname != null ? firstname.hashCode() : 0;
    result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
    result = 31 * result + (email != null ? email.hashCode() : 0);
    return result;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String firstname;
    private String lastname;
    private String email;

    public Builder usingFirstname(String firstname) {
      this.firstname = firstname;
      return this;
    }

    public Builder usingLastname(String lastname) {
      this.lastname = lastname;
      return this;
    }

    public Builder usingEmail(String email) {
      this.email = email;
      return this;
    }

    public UserDetails build() {
      return new UserDetails(firstname, lastname, email);
    }
  }
}
