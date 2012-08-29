package arollavengers.core.entity;


import org.hibernate.validator.constraints.Email;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "USER")
public class User {

  @Id
  private String id;

  @Email
  @NotNull(message = "email is mandatory")
  private String email;


  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }
}
