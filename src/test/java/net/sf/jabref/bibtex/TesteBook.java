package net.sf.jabref.bibtex;

import java.io.IOException;
import java.io.StringWriter;
import net.sf.jabref.Globals;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.exporter.LatexFieldFormatter;
import net.sf.jabref.model.database.BibDatabaseMode;
import net.sf.jabref.model.entry.BibEntry;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TesteBook {

    private BibEntryWriter writer;
    private static JabRefPreferences backup;


    @BeforeClass
    public static void setUp() {
        Globals.prefs = JabRefPreferences.getInstance();
        backup = Globals.prefs;
    }

    @AfterClass
    public static void tearDown() {
        Globals.prefs.overwritePreferences(backup);
    }

    @Before
    //
    public void setUpWriter() {
        writer = new BibEntryWriter(new LatexFieldFormatter(), true);
    }

    @Test
    public void testInsercaoItemBibliografico() throws IOException {
        StringWriter stringWriter = new StringWriter();
        BibEntry entry = new BibEntry("1234", "book");
        //Required
        entry.setField("title", "Felicidade, Modo de usar");
        entry.setField("publisher", "PLaneta");
        entry.setField("year", "2018");
        entry.setField("author", "Karnal, L. and Cortella, M. S. and Pondé, L. F.");
        entry.setField("editor", "Planeta");
        // Optional
        entry.setField("pages", "124");
        entry.setField("note", "O livre é resultado do debate entre...");
        entry.setField("month", "May");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String enviada = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Book{," + Globals.NEWLINE +
                "  title   = {Felicidade, Modo de usar}," + Globals.NEWLINE +
                "  publisher    = {Planeta}," + Globals.NEWLINE +
                "  year    = {2018}," + Globals.NEWLINE +
                "  author  = {Karnal, L. and Cortella, M. S. and Pondé, L. F.}," + Globals.NEWLINE +
                "  editor   = {Planeta}," + Globals.NEWLINE +
                "  pages   = {124}," + Globals.NEWLINE +
                "  note    = {O livre é resultado do debate entre...}," + Globals.NEWLINE +
                "  month   = {May}," + Globals.NEWLINE +
                "}" + Globals.NEWLINE;
        // @formatter:on

        assertEquals(expected, sent);
    }
}