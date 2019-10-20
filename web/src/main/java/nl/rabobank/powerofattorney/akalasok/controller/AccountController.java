package nl.rabobank.powerofattorney.akalasok.controller;

import nl.rabobank.powerofattorney.akalasok.model.AuthorityOverview;
import nl.rabobank.powerofattorney.akalasok.model.AuthorityService;
import nl.rabobank.powerofattorney.akalasok.util.AuthenticationUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class AccountController {

    private final AuthorityService authorityService;

    public AccountController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @GetMapping("/account")
    public String messages(Model model, Principal principal) {
        List<String> authorities = AuthenticationUtil.extractAuthorityList(principal);
        AuthorityOverview buildOverview = authorityService.buildOverview(authorities);
        model.addAttribute("authorities", buildOverview);
        return "account";
    }

}
