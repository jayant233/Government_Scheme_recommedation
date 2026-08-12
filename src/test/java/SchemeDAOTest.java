package com.govscheme;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;
import java.util.ArrayList;


public class SchemeDAOTest {
    private SchemeDAO dao;

    @BeforeEach
    void setUp() {
        dao = new SchemeDAO();
    }

    @Test
    void testGetEligibleSchemesReturnsResults() throws SQLException {
        ArrayList<Scheme> results = dao.getEligibleSchemes(22, 200000, "Male", "SC", "Student", "All India (Central)");
        assertFalse(results.isEmpty(), "Should find at least one scheme");
    }

    @Test
    void testGetEligibleSchemesNoMatch() throws SQLException {
        ArrayList<Scheme> results = dao.getEligibleSchemes(90, 9900000, "Male", "General", "Retired", "Goa");
        assertTrue(results.isEmpty(), "Should find no schemes");
    }


    @Test
    void testAddAndDeleteScheme() throws SQLException {
        Scheme s = new Scheme(0, "Test Scheme", 18, 30, 0, 100000, "ALL", "ANY", "ANY", "All India (Central)", "Test");
        assertTrue(dao.addScheme(s));
    }
}
