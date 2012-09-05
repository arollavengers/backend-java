package arollavengers.core.domain.pandemic;

import java.util.HashSet;
import java.util.Set;

public class Team {
  private final Set<Member> team = new HashSet<Member>();


  public Set<MemberRole> roles() {
    Set<MemberRole> roles = new HashSet<MemberRole>();
    for (Member member : team) {
      roles.add(member.role());
    }
    return roles;
  }

  public boolean hasRole(MemberRole role) {
    return roles().contains(role);
  }

  void enrole(final Member member) {
    team.add(member);
  }
}
