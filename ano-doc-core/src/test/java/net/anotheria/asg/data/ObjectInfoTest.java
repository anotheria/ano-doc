package net.anotheria.asg.data;

import net.anotheria.util.NumberUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the line the cms prints under an edit dialog.
 *
 * <p>It is a {@code toString()}, which usually is not worth a test, but this one is a user interface: it is
 * the footer of every document dialog, and the only place an editor can see when a document was last changed
 * and when it was last published.
 */
public class ObjectInfoTest {

    @Test
    public void theFooterSaysWhenADocumentWasLastTransferred() {
        ObjectInfo info = new ObjectInfo();
        info.setId("7");
        info.setAuthor("saenq");
        info.setFootprint("66D9CFFD16B6065F8DA0618E832883FD");
        info.setLastChangeTimestamp(1789334489952L);
        info.setLastTransferTimestamp(1789334500000L);

        String footer = info.toString();

        assertTrue(footer, footer.startsWith("Id: 7, Ts: 1789334489952, "
                + "Footprint: 66D9CFFD16B6065F8DA0618E832883FD, Author: saenq, IsoTs: "));
        assertTrue(footer, footer.endsWith(", lastTransferTs: "
                + NumberUtils.makeISO8601TimestampString(1789334500000L)));
    }

    @Test
    public void aDocumentThatWasNeverTransferredSaysSo() {
        //0 is what every document that was never published carries, and printing the epoch for it would read
        //as "published in 1970" - which is worse than saying nothing.
        ObjectInfo info = new ObjectInfo();
        info.setId("7");

        assertEquals("never", info.getLastTransferTimestampAsISO());
        assertTrue(info.toString(), info.toString().endsWith(", lastTransferTs: never"));
    }

    @Test
    public void theTransferTimestampIsPartOfTheXmlExport() {
        ObjectInfo info = new ObjectInfo();
        info.setId("7");
        info.setLastTransferTimestamp(1789334500000L);

        String xml = info.toXML().toString();

        assertTrue(xml, xml.contains("lasttransfertimestamp"));
        assertTrue(xml, xml.contains("iso8601lasttransfertimestamp"));
    }
}
