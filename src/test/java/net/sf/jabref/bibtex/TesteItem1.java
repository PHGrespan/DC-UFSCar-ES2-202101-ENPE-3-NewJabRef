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

public class TesteItem1 {

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
        // Categoria artigo
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("1234", "article");
        // Define campos requeridos
        entry.setField("author", "Testador");
        entry.setField("title", "Teste");
        entry.setField("journal", "Jornal do Teste");
        entry.setField("year", "2021");

        // Define campos opicionais
        entry.setField("volume", "1");
        entry.setField("number", "1");
        entry.setField("pages", "300");
        entry.setField("month", "January");
        entry.setField("note", "Esse e teste");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String enviada = stringWriter.toString();

        // @formatter:off
        String esperada = Globals.NEWLINE + "@Article{," + Globals.NEWLINE +
                "  author  = {Testador}," + Globals.NEWLINE +
                "  title   = {Teste}," + Globals.NEWLINE +
                "  journal = {Jornal do Teste}," + Globals.NEWLINE +
                "  year    = {2021}," + Globals.NEWLINE +
                "  volume  = {1}," + Globals.NEWLINE +
                "  number  = {1}," + Globals.NEWLINE +
                "  pages   = {300}," + Globals.NEWLINE +
                "  month   = {January}," + Globals.NEWLINE +
                "  note    = {Esse e teste}," + Globals.NEWLINE +
                "}" + Globals.NEWLINE;
        // @formatter:on

        assertEquals(esperada, enviada);
    }
}
