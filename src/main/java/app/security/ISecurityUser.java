package app.security;

import app.entities.Role;
import java.util.Set;

public interface ISecurityUser {

    Set<String> getRolesAsStrings();

    boolean verifyPassword(String pw);

    void addRole(Role role);

    void removeRole(String role);
}

//ISecurityUser sikrer, at alle brugere har de nødvendige metoder til passwordkontrol og håndtering af roller.