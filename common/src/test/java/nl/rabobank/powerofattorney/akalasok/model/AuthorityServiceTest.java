package nl.rabobank.powerofattorney.akalasok.model;

import org.junit.Test;

import java.util.List;

import static nl.rabobank.powerofattorney.akalasok.model.AssetType.*;
import static org.junit.Assert.*;

public class AuthorityServiceTest {

    private AuthorityService authorityService = new AuthorityService();
    private List<String> authorities = List.of(
            ACCOUNT.authorityFor("OWNER", "1"),
            CREDIT_CARD.authorityFor("HOLDER", "2"),
            DEBIT_CARD.authorityFor("GRANTEE", "3"),
            POA.authorityFor("GRANTOR", "4")
    );

    @Test
    public void buildOverview() {
        AuthorityOverview overview = authorityService.buildOverview(authorities);

        assertEquals(new AssetAuthority(ACCOUNT, "OWNER", "1"), overview.getAccounts().get(0));
        assertEquals(new AssetAuthority(CREDIT_CARD, "HOLDER", "2"), overview.getCreditCards().get(0));
        assertEquals(new AssetAuthority(DEBIT_CARD, "GRANTEE", "3"), overview.getDebitCards().get(0));
        assertEquals(new AssetAuthority(POA, "GRANTOR", "4"), overview.getPoas().get(0));
    }

    @Test
    public void isAccountAccessAllowed() {
        assertTrue(authorityService.isAccessAllowed(ACCOUNT, "1", authorities));
        assertFalse(authorityService.isAccessAllowed(ACCOUNT, "2", authorities));
    }

    @Test
    public void isCreditCardAccessAllowed() {
        assertTrue(authorityService.isAccessAllowed(CREDIT_CARD, "2", authorities));
        assertFalse(authorityService.isAccessAllowed(CREDIT_CARD, "3", authorities));
    }

    @Test
    public void isDebitCardAccessAllowed() {
        assertTrue(authorityService.isAccessAllowed(DEBIT_CARD, "3", authorities));
        assertFalse(authorityService.isAccessAllowed(DEBIT_CARD, "4", authorities));
    }

    @Test
    public void isPoaAccessAllowed() {
        assertTrue(authorityService.isAccessAllowed(POA, "4", authorities));
        assertFalse(authorityService.isAccessAllowed(POA, "5", authorities));
    }

    @Test
    public void getAccessibleAssetIdsByType() {
        assertEquals("1", authorityService.getAccessibleAssetIdsByType(ACCOUNT, authorities).get(0));
        assertEquals("2", authorityService.getAccessibleAssetIdsByType(CREDIT_CARD, authorities).get(0));
        assertEquals("3", authorityService.getAccessibleAssetIdsByType(DEBIT_CARD, authorities).get(0));
        assertEquals("4", authorityService.getAccessibleAssetIdsByType(POA, authorities).get(0));
    }
}