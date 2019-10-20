package nl.rabobank.powerofattorney.akalasok.controller;

import nl.rabobank.powerofattorney.akalasok.dto.PoaDto;
import nl.rabobank.powerofattorney.akalasok.model.AssetType;
import nl.rabobank.powerofattorney.akalasok.model.AuthorityService;
import nl.rabobank.powerofattorney.akalasok.service.PoaService;
import nl.rabobank.powerofattorney.akalasok.util.AuthenticationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Validated
public class PoaController {

    private final AuthorityService authorityService;
    private final PoaService poaService;

    public PoaController(AuthorityService authorityService, PoaService poaService) {
        this.authorityService = authorityService;
        this.poaService = poaService;
    }

    @GetMapping("/power-of-attorneys/{id}")
    public ResponseEntity<PoaDto> getById(Principal principal, @PathVariable("id") String id) {
        List<String> authorities = AuthenticationUtil.extractAuthorityList(principal);
        if (!authorityService.isAccessAllowed(AssetType.POA, id, authorities)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.of(poaService.getById(id));
    }

    @GetMapping("/power-of-attorneys")
    public ResponseEntity<List<PoaDto>> getAllPoas(Principal principal) {
        List<String> authorities = AuthenticationUtil.extractAuthorityList(principal);

        return ResponseEntity.ok(
                authorityService.getAccessibleAssetIdsByType(AssetType.POA, authorities)
                        .stream()
                        .map(poaService::getById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList())
        );
    }
}
