package nl.rabobank.powerofattorney.akalasok.controller;

import nl.rabobank.powerofattorney.akalasok.dto.AccountDto;
import nl.rabobank.powerofattorney.akalasok.model.AssetType;
import nl.rabobank.powerofattorney.akalasok.model.AuthorityService;
import nl.rabobank.powerofattorney.akalasok.service.AccountService;
import nl.rabobank.powerofattorney.akalasok.util.AuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@Validated
public class AccountController {

    private final AuthorityService authorityService;
    private final AccountService accountService;

    @Autowired
    public AccountController(AuthorityService authorityService, AccountService accountService) {
        this.authorityService = authorityService;
        this.accountService = accountService;
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<AccountDto> getById(Principal principal, @PathVariable("id") String id) {
        List<String> authorities = AuthenticationUtil.extractAuthorityList(principal);
        if (!authorityService.isAccessAllowed(AssetType.ACCOUNT, id, authorities)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.of(accountService.getById(id));
    }

}
