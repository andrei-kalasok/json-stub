package nl.rabobank.powerofattorney.akalasok.controller;

import nl.rabobank.powerofattorney.akalasok.dto.CreditCardDto;
import nl.rabobank.powerofattorney.akalasok.model.AssetType;
import nl.rabobank.powerofattorney.akalasok.model.AuthorityService;
import nl.rabobank.powerofattorney.akalasok.service.CreditCardService;
import nl.rabobank.powerofattorney.akalasok.util.AuthenticationUtil;
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
public class CreditCardController {

    private final AuthorityService authorityService;
    private final CreditCardService creditCardService;

    public CreditCardController(AuthorityService authorityService, CreditCardService creditCardService) {
        this.authorityService = authorityService;
        this.creditCardService = creditCardService;
    }

    @GetMapping("/credit-cards/{id}")
    public ResponseEntity<CreditCardDto> getById(Principal principal, @PathVariable("id") String id) {
        List<String> authorities = AuthenticationUtil.extractAuthorityList(principal);
        if (!authorityService.isAccessAllowed(AssetType.CREDIT_CARD, id, authorities)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.of(creditCardService.getById(id));
    }
}
