package nl.rabobank.powerofattorney.akalasok.controller;

import nl.rabobank.powerofattorney.akalasok.dto.DebitCardDto;
import nl.rabobank.powerofattorney.akalasok.model.AssetType;
import nl.rabobank.powerofattorney.akalasok.model.AuthorityService;
import nl.rabobank.powerofattorney.akalasok.service.DebitCardService;
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
public class DebitCardController {

    private final AuthorityService authorityService;
    private final DebitCardService debitCardService;

    public DebitCardController(AuthorityService authorityService, DebitCardService debitCardService) {
        this.authorityService = authorityService;
        this.debitCardService = debitCardService;
    }

    @GetMapping("/debit-cards/{id}")
    public ResponseEntity<DebitCardDto> getById(Principal principal, @PathVariable("id") String id) {
        List<String> authorities = AuthenticationUtil.extractAuthorityList(principal);
        if (!authorityService.isAccessAllowed(AssetType.DEBIT_CARD, id, authorities)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.of(debitCardService.getById(id));
    }
}
