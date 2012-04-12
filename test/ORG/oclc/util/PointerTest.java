/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ORG.oclc.util;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author levan
 */
public class PointerTest {
    
    public PointerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of clone method, of class Pointer.
     */
    @Test
    public void testClone() {
        System.out.println("clone");
        Pointer instance = new Pointer();
        Object expResult = null;
        Object result = instance.clone();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of copy method, of class Pointer.
     */
    @Test
    public void testCopy() {
        System.out.println("copy");
        Pointer instance = new Pointer();
        Pointer expResult = null;
        Pointer result = instance.copy();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of endOffset method, of class Pointer.
     */
    @Test
    public void testEndOffset() {
        System.out.println("endOffset");
        int endOffset = 0;
        Pointer instance = new Pointer();
        instance.endOffset(endOffset);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of equals method, of class Pointer.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object o = null;
        Pointer instance = new Pointer();
        boolean expResult = false;
        boolean result = instance.equals(o);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of free method, of class Pointer.
     */
    @Test
    public void testFree() {
        System.out.println("free");
        Pointer instance = new Pointer();
        instance.free();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getByteData method, of class Pointer.
     */
    @Test
    public void testGetByteData() {
        System.out.println("getByteData");
        Pointer instance = new Pointer();
        byte[] expResult = null;
        byte[] result = instance.getByteData();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hashCode method, of class Pointer.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        Pointer instance = new Pointer();
        int expResult = 0;
        int result = instance.hashCode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of increment method, of class Pointer.
     */
    @Test
    public void testIncrement() {
        System.out.println("increment");
        int amount = 0;
        Pointer instance = new Pointer();
        int expResult = 0;
        int result = instance.increment(amount);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of indexOf method, of class Pointer.
     */
    @Test
    public void testIndexOf_char() {
        System.out.println("indexOf");
        char c = ' ';
        Pointer instance = new Pointer();
        int expResult = 0;
        int result = instance.indexOf(c);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of indexOf method, of class Pointer.
     */
    @Test
    public void testIndexOf_Pointer() {
        System.out.println("indexOf");
        Pointer s = null;
        Pointer instance = new Pointer();
        int expResult = 0;
        int result = instance.indexOf(s);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of indexOf method, of class Pointer.
     */
    @Test
    public void testIndexOf_Pointer_int() {
        System.out.println("indexOf");
        Pointer s = null;
        int offset = 0;
        Pointer instance = new Pointer();
        int expResult = 0;
        int result = instance.indexOf(s, offset);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of indexOf method, of class Pointer.
     */
    @Test
    public void testIndexOf_7args() {
        System.out.println("indexOf");
        char[] str = null;
        int strOffset = 0;
        int strLength = 0;
        char[] frag = null;
        int fragOffset = 0;
        int fragLength = 0;
        int startOffset = 0;
        int expResult = 0;
        int result = Pointer.indexOf(str, strOffset, strLength, frag, fragOffset, fragLength, startOffset);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of reset method, of class Pointer.
     */
    @Test
    public void testReset_0args() {
        System.out.println("reset");
        Pointer instance = new Pointer();
        instance.reset();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of reset method, of class Pointer.
     */
    @Test
    public void testReset_byteArr() {
        System.out.println("reset");
        byte[] data = null;
        Pointer instance = new Pointer();
        instance.reset(data);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of reset method, of class Pointer.
     */
    @Test
    public void testReset_byteArr_int() {
        System.out.println("reset");
        byte[] data = null;
        int offset = 0;
        Pointer instance = new Pointer();
        instance.reset(data, offset);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of reset method, of class Pointer.
     */
    @Test
    public void testReset_3args_1() {
        System.out.println("reset");
        byte[] data = null;
        int offset = 0;
        int length = 0;
        Pointer instance = new Pointer();
        instance.reset(data, offset, length);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of reset method, of class Pointer.
     */
    @Test
    public void testReset_String() {
        System.out.println("reset");
        String str = "";
        Pointer instance = new Pointer();
        instance.reset(str);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of reset method, of class Pointer.
     */
    @Test
    public void testReset_charArr() {
        System.out.println("reset");
        char[] data = null;
        Pointer instance = new Pointer();
        instance.reset(data);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of reset method, of class Pointer.
     */
    @Test
    public void testReset_charArr_int() {
        System.out.println("reset");
        char[] data = null;
        int offset = 0;
        Pointer instance = new Pointer();
        instance.reset(data, offset);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of reset method, of class Pointer.
     */
    @Test
    public void testReset_3args_2() {
        System.out.println("reset");
        char[] data = null;
        int offset = 0;
        int length = 0;
        Pointer instance = new Pointer();
        instance.reset(data, offset, length);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of reset method, of class Pointer.
     */
    @Test
    public void testReset_Pointer() {
        System.out.println("reset");
        Pointer ptr = null;
        Pointer instance = new Pointer();
        instance.reset(ptr);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of squeezeout method, of class Pointer.
     */
    @Test
    public void testSqueezeout_int() {
        System.out.println("squeezeout");
        int offset = 0;
        Pointer instance = new Pointer();
        instance.squeezeout(offset);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of squeezeout method, of class Pointer.
     */
    @Test
    public void testSqueezeout_int_int() {
        System.out.println("squeezeout");
        int offset = 0;
        int length = 0;
        Pointer instance = new Pointer();
        instance.squeezeout(offset, length);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class Pointer.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Pointer instance = new Pointer();
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toStringVar method, of class Pointer.
     */
    @Test
    public void testToStringVar() {
        System.out.println("toStringVar");
        Pointer instance = new Pointer();
        String expResult = "";
        String result = instance.toStringVar();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toBriefString method, of class Pointer.
     */
    @Test
    public void testToBriefString() {
        System.out.println("toBriefString");
        Pointer instance = new Pointer();
        String expResult = "";
        String result = instance.toBriefString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of byteArrayToString method, of class Pointer.
     */
    @Test
    public void testByteArrayToString() {
        System.out.println("byteArrayToString");
        byte[] array = null;
        int offset = 0;
        int length = 0;
        String expResult = "";
        String result = Pointer.byteArrayToString(array, offset, length);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
