package nl.rabobank.powerofattorney.akalasok.controller;

import nl.rabobank.powerofattorney.akalasok.dto.PoaDto;
import nl.rabobank.powerofattorney.akalasok.model.AssetType;
import nl.rabobank.powerofattorney.akalasok.model.AuthorityService;
import nl.rabobank.powerofattorney.akalasok.service.PoaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PoaController.class)
public class PoaControllerTest {

    private static final String POA_ID = "1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorityService authorityService;

    @MockBean
    private PoaService poaService;

    @MockBean
    private DataSource dataSource;

    @Test
    public void ifNoUser_401() throws Exception {
        mockMvc.perform(get("/power-of-attorneys/1"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "POA+GRANTOR+2")
    public void ifBadAuthority_403() throws Exception {
        when(authorityService.isAccessAllowed(AssetType.POA, POA_ID, List.of("POA+GRANTOR+2"))).thenReturn(false);

        mockMvc.perform(get("/power-of-attorneys/1"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "POA+GRANTOR+1")
    public void ifNotFound_404() throws Exception {
        when(authorityService.isAccessAllowed(AssetType.POA, POA_ID, List.of("POA+GRANTOR+1"))).thenReturn(true);
        when(poaService.getById(POA_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get("/power-of-attorneys/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "POA+GRANTOR+1")
    public void ifFound_200() throws Exception {
        when(authorityService.isAccessAllowed(AssetType.POA, POA_ID, List.of("POA+GRANTOR+1"))).thenReturn(true);
        PoaDto poa = poa();
        when(poaService.getById(POA_ID)).thenReturn(Optional.of(poa));

        mockMvc.perform(get("/power-of-attorneys/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(poa.getId())))
                .andExpect(jsonPath("$.grantor", is(poa.getGrantor())))
                .andExpect(jsonPath("$.grantee", is(poa.getGrantee())))
                .andExpect(jsonPath("$.account", is(poa.getAccount())))
                .andExpect(jsonPath("$.direction", is(poa.getDirection())))
                .andExpect(jsonPath("$.cards[0].id", is(poa.getCards().get(0).getId())))
                .andExpect(jsonPath("$.cards[0].type", is(poa.getCards().get(0).getType())))
                .andExpect(jsonPath("$.cards[1].id", is(poa.getCards().get(1).getId())))
                .andExpect(jsonPath("$.cards[1].type", is(poa.getCards().get(1).getType())))
                .andExpect(jsonPath("$.authorizations[0]", is(poa.getAuthorizations().get(0))))
                .andExpect(jsonPath("$.authorizations[1]", is(poa.getAuthorizations().get(1))))
        ;
    }

    @Test
    @WithMockUser(authorities = "POA+GRANTOR+1")
    public void ifFoundAll_200() throws Exception {
        when(authorityService.getAccessibleAssetIdsByType(AssetType.POA, List.of("POA+GRANTOR+1"))).thenReturn(List.of(POA_ID));
        PoaDto poa = poa();
        when(poaService.getById(POA_ID)).thenReturn(Optional.of(poa));

        mockMvc.perform(get("/power-of-attorneys"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(poa.getId())))
                .andExpect(jsonPath("$.[0].grantor", is(poa.getGrantor())))
                .andExpect(jsonPath("$.[0].grantee", is(poa.getGrantee())))
                .andExpect(jsonPath("$.[0].account", is(poa.getAccount())))
                .andExpect(jsonPath("$.[0].direction", is(poa.getDirection())))
                .andExpect(jsonPath("$.[0].cards[0].id", is(poa.getCards().get(0).getId())))
                .andExpect(jsonPath("$.[0].cards[0].type", is(poa.getCards().get(0).getType())))
                .andExpect(jsonPath("$.[0].cards[1].id", is(poa.getCards().get(1).getId())))
                .andExpect(jsonPath("$.[0].cards[1].type", is(poa.getCards().get(1).getType())))
                .andExpect(jsonPath("$.[0].authorizations[0]", is(poa.getAuthorizations().get(0))))
                .andExpect(jsonPath("$.[0].authorizations[1]", is(poa.getAuthorizations().get(1))))
        ;
    }

    private PoaDto poa() {
        PoaDto dto = new PoaDto();
        dto.setId(POA_ID);
        dto.setGrantor("grantor");
        dto.setGrantee("grantee");
        dto.setAccount("account");
        dto.setDirection("direction");
        dto.getCards().add(card("1", "CREDIT_CARD"));
        dto.getCards().add(card("2", "DEBIT_CARD"));
        dto.getAuthorizations().add("VIEW");
        dto.getAuthorizations().add("PAYMENT");
        return dto;
    }

    private PoaDto.Card card(String id, String type) {
        PoaDto.Card card = new PoaDto.Card();
        card.setId(id);
        card.setType(type);
        return card;
    }
}