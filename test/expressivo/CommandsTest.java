/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import java.util.Map;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for the static methods of Commands.
 */
public class CommandsTest {

    @Test
    public void testDifferentiatePlus() {
        assertEquals("expected differentiated expression",
            Commands.differentiate("1 + x", "x"), "(0.0 + 1.0)");
    }

    @Test
    public void testDifferentiateMultiply() {
        assertEquals("expected differentiated expression",
            Commands.differentiate("x * 1", "x"), "((1.0 * 1.0) + (x * 0.0))");
    }

    @Test
    public void testDifferentiateSingleSameVariable() {
        assertEquals("expected differentiated expression",
            Commands.differentiate("(1.0 + x) * (x * 1.0)", "x"),
            "(((0.0 + 1.0) * (x * 1.0)) + ((1.0 + x) * ((1.0 * 1.0) + (x * 0.0))))");
    }

    @Test
    public void testDifferentiateSingleDifferentVariable() {
        assertEquals("expected differentiated expression",
            Commands.differentiate("(1.0 + x) * (x * 1.0)", "y"),
            "(((0.0 + 0.0) * (x * 1.0)) + ((1.0 + x) * ((0.0 * 1.0) + (x * 0.0))))");
    }

    @Test
    public void testDifferentiateMultipleVariables() {
        assertEquals("expected differentiated expression",
            Commands.differentiate("x * y", "x"), "((1.0 * y) + (x * 0.0))");
    }

    @Test
    public void testDifferentiateIllegalExpression() {
        try {
            Commands.differentiate("3 x", "x");
            assert false; // should not reach here
        }
        catch (IllegalArgumentException e) {
            assert true;
        }
    }

    @Test
    public void testDifferentiateIllegalVariable() {
        try {
            Commands.differentiate("3 + x", "3");
            assert false; // should not reach here
        }
        catch (IllegalArgumentException e) {
            assert true;
        }
    }

    @Test
    public void testSimplifyNumber() {
        assertEquals("expected simplified expression",
            Commands.simplify("1", Map.of("x", 2.0)), "1.0");
    }

    @Test
    public void testSimplifyPlusExpression() {
        assertEquals("expected simplified expression",
            Commands.simplify("1 + x", Map.of("x", 2.0)), "3.0");
    }

    @Test
    public void testSimplifyMultiplyNumber() {
        assertEquals("expected simplified expression",
            Commands.simplify("0 * 1", Map.of("x", 2.0)), "0.0");
    }

    @Test
    public void testSimplifyMultiplyExpression() {
        assertEquals("expected simplified expression",
            Commands.simplify("x * 1", Map.of("x", 2.0)), "2.0");
    }

    @Test
    public void testSimplifySingleSameVariable() {
        assertEquals("expected simplified expression",
            Commands.simplify("(1.0 + x) * (x * 1.0)", Map.of("x", 2.0)), "6.0");
    }

    @Test
    public void testSimplifyIllegalExpression() {
        try {
            Commands.simplify("3 x", Map.of("x", 2.0));
            assert false; // should not reach here
        }
        catch (IllegalArgumentException e) {
            assert true;
        }
    }

}
