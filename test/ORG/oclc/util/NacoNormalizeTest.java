/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ORG.oclc.util;

import junit.framework.TestCase;

/**
 *
 * @author levan
 */
public class NacoNormalizeTest extends TestCase {
    
    public NacoNormalizeTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of nacoNormalize2007 method, of class NacoNormalize.
     */
    public void testNacoNormalize2007_String_boolean() {
        System.out.println("nacoNormalize2007");
        boolean keepFirstComma = true;
        String aeDigraph="\u01fd", aUmlaut="\u00e4", smallAlpha="\u03b1", smallIota="\u03b9";
        // case folding
        assertEquals("ralph levan", NacoNormalize.nacoNormalize2007("Ralph LeVan", !keepFirstComma));
        // leading, trailing blanks
        assertEquals("ralph levan", NacoNormalize.nacoNormalize2007("  Ralph LeVan  ", !keepFirstComma));
        // drop first command and remove redundant blanks
        assertEquals("levan ralph", NacoNormalize.nacoNormalize2007("LeVan, Ralph", !keepFirstComma));
        // keep first comma
        assertEquals("levan, ralph", NacoNormalize.nacoNormalize2007("LeVan, Ralph", keepFirstComma));
        // strip diacritics
        assertEquals("levan, ralph", NacoNormalize.nacoNormalize2007("LeVan, R"+aUmlaut+"lph", keepFirstComma));
        // Uppercase O-hook -> o
        assertEquals("o", NacoNormalize.nacoNormalize2007("\u01a0", keepFirstComma));
        // Lowercase o-hook -> o
        assertEquals("o", NacoNormalize.nacoNormalize2007("\u01a1", keepFirstComma));
        // Uppercase U-hook -> u
        assertEquals("u", NacoNormalize.nacoNormalize2007("\u01af", keepFirstComma));
        // Lowercase u-hook-> u
        assertEquals("u", NacoNormalize.nacoNormalize2007("\u01b0", keepFirstComma));
        // Superscript two -> 2
        assertEquals("2", NacoNormalize.nacoNormalize2007("\u00b2", keepFirstComma));
        // superscript zero -> 0
        assertEquals("0", NacoNormalize.nacoNormalize2007("\u2070", keepFirstComma));
        // Latin capital ligature IJ -> ij
        assertEquals("ij", NacoNormalize.nacoNormalize2007("\u0132", keepFirstComma));
        // Latin small ligature ij -> ij
        assertEquals("ij", NacoNormalize.nacoNormalize2007("\u0133", keepFirstComma));
        // expand digraphs
        assertEquals("levan, raelph", NacoNormalize.nacoNormalize2007("LeVan, R"+aeDigraph+"lph", keepFirstComma));
        // convert arabic numerals
        assertEquals("1", NacoNormalize.nacoNormalize2007("\u0661", keepFirstComma));
        // unconditional mappings: LATIN SMALL LETTER SHARP S -> "ss"
        assertEquals("ss", NacoNormalize.nacoNormalize2007("\u00DF", keepFirstComma));
        // unconditional mappings: LATIN SMALL LIGATURE FF -> "ff"
        assertEquals("ff", NacoNormalize.nacoNormalize2007("\uFB00", keepFirstComma));
        // unconditional mappings: GREEK SMALL LETTER ALPHA WITH PSILI AND YPOGEGRAMMENI -> small alpha small iota
        assertEquals(smallAlpha+smallIota, NacoNormalize.nacoNormalize2007("\u1F80", keepFirstComma));
        // unconditional mappings: GREEK SMALL LETTER ALPHA WITH DASIA AND PERISPOMENI AND PROSGEGRAMMENI -> small alpha small iota
        assertEquals(smallAlpha+smallIota, NacoNormalize.nacoNormalize2007("\u1F8F", keepFirstComma));
        // additional substitutions
        // Uppercase digraph AE
        assertEquals("ae", NacoNormalize.nacoNormalize2007("\u00c6", keepFirstComma));
        // Uppercase Scandinavian O
        assertEquals("o", NacoNormalize.nacoNormalize2007("\u00d8", keepFirstComma));
        // Uppercase Icelandic thorn
        assertEquals("th", NacoNormalize.nacoNormalize2007("\u00de", keepFirstComma));
        // Lowercase digraph AE
        assertEquals("ae", NacoNormalize.nacoNormalize2007("\u00e6", keepFirstComma));
        // Lowercase eth
        assertEquals("d", NacoNormalize.nacoNormalize2007("\u00f0", keepFirstComma));
        // Lowercase Scandinavian O
        assertEquals("o", NacoNormalize.nacoNormalize2007("\u00f8", keepFirstComma));
        // Lowercase Icelandic thorn
        assertEquals("th", NacoNormalize.nacoNormalize2007("\u00fe", keepFirstComma));
        // Uppercase D with crossbar
        assertEquals("d", NacoNormalize.nacoNormalize2007("\u0110", keepFirstComma));
        // Lowercase D with crossbar
        assertEquals("d", NacoNormalize.nacoNormalize2007("\u0111", keepFirstComma));
        // Lowercase Turkish i
        assertEquals("i", NacoNormalize.nacoNormalize2007("\u0131", keepFirstComma));
        // Uppercase Polish L
        assertEquals("l", NacoNormalize.nacoNormalize2007("\u0141", keepFirstComma));
        // Lowercase Polish l
        assertEquals("l", NacoNormalize.nacoNormalize2007("\u0142", keepFirstComma));
        // Uppercase digraph OE
        assertEquals("oe", NacoNormalize.nacoNormalize2007("\u0152", keepFirstComma));
        // Lowercase digraph oe
        assertEquals("oe", NacoNormalize.nacoNormalize2007("\u0153", keepFirstComma));
        // Script small l
        assertEquals("l", NacoNormalize.nacoNormalize2007("\u2113", keepFirstComma));
        // remove ayn
        assertEquals("aa", NacoNormalize.nacoNormalize2007("a\u02bba", keepFirstComma));
        // remove alif
        assertEquals("aa", NacoNormalize.nacoNormalize2007("a\u02bca", keepFirstComma));
        // remove control characters
        // we leave control characters in now because we use them as truncation characters
        //assertEquals("aa", NacoNormalize.nacoNormalize2007("a\u0000a", keepFirstComma));
        // misc tests
        assertEquals("brades, susan ferleger", NacoNormalize.nacoNormalize2007("Brades, Susan Ferleger.", keepFirstComma));
        assertEquals("labor obstetrics", NacoNormalize.nacoNormalize2007("Labor (Obstetrics)", keepFirstComma));
    }

}
