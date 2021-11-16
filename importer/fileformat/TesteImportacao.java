
package net.sf.jabref.importer.fileformat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.sf.jabref.Globals;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.importer.OutputPrinterToNull;
import net.sf.jabref.model.entry.BibEntry;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

// Esse codigo testa 3 formatos de importacao

public class TesteImportacao {


    static BibtexImporter importerBibtex;

    @BeforeClass
    public static void setUp() {
        Globals.prefs = JabRefPreferences.getInstance();
        importerBibtex = new BibtexImporter();
    }

    private final String FILEFORMAT_PATH = "src/test/resources/net/sf/jabref/importer/fileformat";

    // COPAC FORMAT


    public List<String> getTestFiles() throws IOException {
        List<String> files = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(FILEFORMAT_PATH))) {
            stream.forEach(n -> files.add(n.getFileName().toString()));
        }
        return files;
    }

    @Test(expected = IOException.class)
    public void testImportEntriesException() throws IOException {
        CopacImporter importer = new CopacImporter();
        importer.importEntries(null, new OutputPrinterToNull());
    }

    @Test
    public void testGetFormatName() {
        CopacImporter importer = new CopacImporter();
        Assert.assertEquals("Copac", importer.getFormatName());
    }

    @Test
    public void testGetCLIId() {
        CopacImporter importer = new CopacImporter();
        Assert.assertEquals("cpc", importer.getCLIId());
    }

    @Test
    public void testIsRecognizedFormatReject() throws IOException {
        CopacImporter importer = new CopacImporter();

        List<String> list = getTestFiles().stream().filter(n -> !n.startsWith("CopacImporterTest"))
                .collect(Collectors.toList());

        for (String str : list) {
            try (InputStream is = CopacImporterTest.class.getResourceAsStream(str)) {
                Assert.assertFalse(importer.isRecognizedFormat(is));
            }
        }
    }

    @Test
    public void testImportEmptyEntries() throws IOException {
        CopacImporter importer = new CopacImporter();

        try (InputStream is = CopacImporterTest.class.getResourceAsStream("Empty.txt")) {
            List<BibEntry> entries = importer.importEntries(is, new OutputPrinterToNull());
            Assert.assertEquals(0, entries.size());
            Assert.assertEquals(Collections.emptyList(), entries);
        }
    }

 // BIBTEX FORMAT

    @Test
    public void testIsRecognizedFormat() throws IOException {
        try (InputStream stream = BibtexImporterTest.class.getResourceAsStream("BibtexImporter.examples.bib")) {
            assertTrue(importerBibtex.isRecognizedFormat(stream));
        }
    }

    @Test
    public void testImportEntries() throws IOException {
        try (InputStream stream = BibtexImporterTest.class.getResourceAsStream("BibtexImporter.examples.bib")) {
            List<BibEntry> bibEntries = importerBibtex.importEntries(stream, new OutputPrinterToNull());

            assertEquals(4, bibEntries.size());

            for (BibEntry entry : bibEntries) {

                if (entry.getCiteKey().equals("aksin")) {
                    assertEquals("Aks{\\i}n, {\\\"O}zge and T{\\\"u}rkmen, Hayati and Artok, Levent and {\\c{C}}etinkaya, " +
                                    "Bekir and Ni, Chaoying and B{\\\"u}y{\\\"u}kg{\\\"u}ng{\\\"o}r, Orhan and {\\\"O}zkal, Erhan",
                            entry.getField("author"));
                    assertEquals("aksin", entry.getField("bibtexkey"));
                    assertEquals("2006", entry.getField("date"));
                    assertEquals("Effect of immobilization on catalytic characteristics", entry.getField("indextitle"));
                    assertEquals("#jomch#", entry.getField("journaltitle"));
                    assertEquals("13", entry.getField("number"));
                    assertEquals("3027-3036", entry.getField("pages"));
                    assertEquals("Effect of immobilization on catalytic characteristics of saturated {Pd-N}-heterocyclic " +
                            "carbenes in {Mizoroki-Heck} reactions", entry.getField("title"));
                    assertEquals("691", entry.getField("volume"));
                } else if (entry.getCiteKey().equals("stdmodel")) {
                    assertEquals("A \\texttt{set} with three members discussing the standard model of particle physics. " +
                                    "The \\texttt{crossref} field in the \\texttt{@set} entry and the \\texttt{entryset} field in " +
                                    "each set member entry is needed only when using BibTeX as the backend",
                            entry.getField("annotation"));
                    assertEquals("stdmodel", entry.getField("bibtexkey"));
                    assertEquals("glashow,weinberg,salam", entry.getField("entryset"));
                } else if (entry.getCiteKey().equals("set")) {
                    assertEquals("A \\texttt{set} with three members. The \\texttt{crossref} field in the \\texttt{@set} " +
                            "entry and the \\texttt{entryset} field in each set member entry is needed only when using " +
                            "BibTeX as the backend", entry.getField("annotation"));
                    assertEquals("set", entry.getField("bibtexkey"));
                    assertEquals("herrmann,aksin,yoon", entry.getField("entryset"));
                } else if (entry.getCiteKey().equals("Preissel2016")) {
                    assertEquals("Heidelberg", entry.getField("address"));
                    assertEquals("Preißel, René", entry.getField("author"));
                    assertEquals("Preissel2016", entry.getField("bibtexkey"));
                    assertEquals("3., aktualisierte und erweiterte Auflage", entry.getField("edition"));
                    assertEquals("978-3-86490-311-3", entry.getField("isbn"));
                    assertEquals("Versionsverwaltung", entry.getField("keywords"));
                    assertEquals("XX, 327 Seiten", entry.getField("pages"));
                    assertEquals("dpunkt.verlag", entry.getField("publisher"));
                    assertEquals("Git: dezentrale Versionsverwaltung im Team : Grundlagen und Workflows",
                            entry.getField("title"));
                    assertEquals("http://d-nb.info/107601965X", entry.getField("url"));
                    assertEquals("2016", entry.getField("year"));
                }
            }
        }
    }

    @Test
    public void testGetFormatNameBibtex() {
        assertEquals("BibTeX", importerBibtex.getFormatName());
    }

    @Test
    public void testGetExtensionsBibtex() {
        assertEquals("bib", importerBibtex.getExtensions());
    }

    // MsBib FORMAT

    @Test
    public final void testIsNotRecognizedFormat() throws Exception {
        MsBibImporter testImporter = new MsBibImporter();
        List<String> notAccepted = Arrays.asList("CopacImporterTest1.txt", "IsiImporterTest1.isi",
                "IsiImporterTestInspec.isi", "emptyFile.xml", "IsiImporterTestWOS.isi");
        for (String s : notAccepted) {
            try (InputStream stream = MsBibImporter.class.getResourceAsStream(s)) {
                Assert.assertFalse(testImporter.isRecognizedFormat(stream));
            }
        }

    }

    @Test
    public final void testImportEntriesEmpty() throws IOException {
        MsBibImporter testImporter = new MsBibImporter();

        List<BibEntry> entries = testImporter.importEntries(
                MsBibImporterTest.class.getResourceAsStream("MsBibImporterTest.xml"), new OutputPrinterToNull());
        Assert.assertEquals(Collections.emptyList(), entries);
    }

    @Test
    public final void testImportEntriesNotRecognizedFormat() throws IOException {
        MsBibImporter testImporter = new MsBibImporter();

        List<BibEntry> entries = testImporter.importEntries(
                MsBibImporterTest.class.getResourceAsStream("CopacImporterTest1.txt"), new OutputPrinterToNull());
        Assert.assertEquals(0, entries.size());
    }

    @Test
    public final void testGetFormatNameMsBib() {
        MsBibImporter testImporter = new MsBibImporter();
        Assert.assertEquals("MSBib", testImporter.getFormatName());
    }

    @Test
    public final void testGetCommandLineId() {
        MsBibImporter testImporter = new MsBibImporter();
        Assert.assertEquals("msbib", testImporter.getCommandLineId());
    }

    // BibTeXML FORMAT
    @Test
    public void testExceptionOnInputStream() throws IOException {
        try (InputStream is = Mockito.mock(InputStream.class)) {
            Mockito.doThrow(new IOException()).when(is).read();

            BibTeXMLImporter importer = new BibTeXMLImporter();
            List<BibEntry> entry = importer.importEntries(is, new OutputPrinterToNull());
            Assert.assertTrue(entry.isEmpty());
        }
    }

    @Test
    public void testGetItemsEmpty() {
        BibTeXMLHandler handler = new BibTeXMLHandler();
        Assert.assertEquals(Collections.emptyList(), handler.getItems());
    }

    @Test
    public void testGetFormatNameBibteXML() {
        BibTeXMLImporter importer = new BibTeXMLImporter();
        Assert.assertEquals("BibTeXML", importer.getFormatName());
    }

    @Test
    public void testGetCLIIdBibTeXML() {
        BibTeXMLImporter importer = new BibTeXMLImporter();
        Assert.assertEquals("bibtexml", importer.getCLIId());
    }

    @Test
    public void testIsRecognizedFormatRejectBibTeXML() throws IOException {
        BibTeXMLImporter importer = new BibTeXMLImporter();

        List<String> list = getTestFiles().stream().filter(n -> !n.startsWith("BibTeXMLImporterTest"))
                .collect(Collectors.toList());

        for (String str : list) {
            try (InputStream is = BibTeXMLImporter.class.getResourceAsStream(str)) {
                Assert.assertFalse(importer.isRecognizedFormat(is));
            }
        }
    }

}
