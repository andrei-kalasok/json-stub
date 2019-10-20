package nl.rabobank.powerofattorney.akalasok.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

public class AuthenticationUtil {

    public static List<String> extractAuthorityList(Principal principal) {
        Authentication authentication = (Authentication) principal;
        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}
