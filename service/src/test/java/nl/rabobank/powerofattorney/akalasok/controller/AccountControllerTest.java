package nl.rabobank.powerofattorney.akalasok.controller;

import nl.rabobank.powerofattorney.akalasok.dto.AccountDto;
import nl.rabobank.powerofattorney.akalasok.model.AssetType;
import nl.rabobank.powerofattorney.akalasok.model.AuthorityService;
import nl.rabobank.powerofattorney.akalasok.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    private static final String ACCOUNT_ID = "1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AuthorityService authorityService;

    @MockBean
    private DataSource dataSource;

    @Test
    public void ifNoUser_401() throws Exception {
        mockMvc.perform(get("/accounts/1"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "ACCOUNT+OWNER+2")
    public void ifBadAuthority_403() throws Exception {
        when(authorityService.isAccessAllowed(AssetType.ACCOUNT, ACCOUNT_ID, List.of("ACCOUNT+OWNER+2"))).thenReturn(false);

        mockMvc.perform(get("/accounts/1"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ACCOUNT+OWNER+1")
    public void ifNotFound_404() throws Exception {
        when(authorityService.isAccessAllowed(AssetType.ACCOUNT, ACCOUNT_ID, List.of("ACCOUNT+OWNER+1"))).thenReturn(true);
        when(accountService.getById(ACCOUNT_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get("/accounts/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ACCOUNT+OWNER+1")
    public void ifFound_200() throws Exception {
        when(authorityService.isAccessAllowed(AssetType.ACCOUNT, ACCOUNT_ID, List.of("ACCOUNT+OWNER+1"))).thenReturn(true);
        AccountDto account = account();
        when(accountService.getById(ACCOUNT_ID)).thenReturn(Optional.of(account));

        mockMvc.perform(get("/accounts/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(account.getId())))
                .andExpect(jsonPath("$.owner", is(account.getOwner())))
                .andExpect(jsonPath("$.balance", is(account.getBalance())))
                .andExpect(jsonPath("$.created", is(account.getCreated().format(DateTimeFormatter.ofPattern(AccountDto.DD_MM_YYYY)))))
                .andExpect(jsonPath("$.ended", is(account.getEnded().format(DateTimeFormatter.ofPattern(AccountDto.DD_MM_YYYY)))));
    }

    private AccountDto account() {
        AccountDto dto = new AccountDto();
        dto.setId(AccountControllerTest.ACCOUNT_ID);
        dto.setOwner("owner");
        dto.setBalance(700.);
        dto.setCreated(LocalDate.now());
        dto.setEnded(LocalDate.now());
        return dto;
    }
}