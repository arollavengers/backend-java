package arollavengers.core.domain;

import arollavengers.core.infrastructure.Id;

public class Member {
  private final MemberRole role;

  public Member(final MemberRole role) {
    this.role = role;
  }

  public Id id() {
    throw new RuntimeException("not implemented");
  }

  public MemberRole role() {
    return role;
  }
}
