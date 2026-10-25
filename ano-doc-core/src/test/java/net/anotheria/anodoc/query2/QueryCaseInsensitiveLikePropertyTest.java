package net.anotheria.anodoc.query2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for {@link QueryCaseInsensitiveLikeProperty}.
 *
 * @author Alex Osadchy
 */
public class QueryCaseInsensitiveLikePropertyTest {

    @Test
    public void test() {
        QueryCaseInsensitiveLikeProperty qp = new QueryCaseInsensitiveLikeProperty("name", "ValUE");
        assertTrue(qp.doesMatch("value"), "Should match");
        assertTrue(qp.doesMatch("VALUE"), "Should match");
        assertTrue(qp.doesMatch("VALuE"), "Should match");
        assertFalse(qp.doesMatch("VaUE"), "Should match");
    }

}