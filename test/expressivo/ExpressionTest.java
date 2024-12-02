/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import java.util.Map;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for the Expression abstract data type.
 */
public class ExpressionTest {

    private final Expression zero = new Number(0);
    private final Expression one = new Number(1);
    private final Expression two = new Number(2);
    private final Expression x = new Variable("x");
    private final Expression y = new Variable("y");

    private final Expression exp1 = new Operation('+', one, x);
    private final Expression exp2 = new Operation('*', x, one);
    private final Expression exp3 = new Operation('*', exp1, exp2);
    private final Expression exp4 = new Operation('*', x, y);

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testToStringPlus() {
        assertEquals("expected sum", "(1.0 + x)", exp1.toString());
    }

    @Test
    public void testToStringMultiply() {
        assertEquals("expected product", "(x * 1.0)", exp2.toString());
    }

    @Test
    public void testEqualityVariable() {
        Expression exp = new Variable("x");
        assertEquals("expected equality", x, exp);
        assertEquals("expected equal hashcode", x.hashCode(), exp.hashCode());
    }

    @Test
    public void testEqualityPlus() {
        Expression exp = new Operation('+', new Number(1.0), new Variable("x"));
        assertEquals("expected equality", exp1, exp);
        assertEquals("expected equal hashcode", exp1.hashCode(), exp.hashCode());
    }

    @Test
    public void testEqualityMultiply() {
        Expression exp = new Operation('*', new Variable("x"), new Number(1.0));
        assertEquals("expected equality", exp2, exp);
        assertEquals("expected equal hashcode", exp2.hashCode(), exp.hashCode());
    }

    @Test
    public void testInequalityVariable() {
        Expression exp = new Variable("X");
        assertNotEquals("expected inequality", x, exp);
    }

    @Test
    public void testInequalityOperator() {
        Expression exp = new Operation('*', new Number(1.0), new Variable("x"));
        assertNotEquals("expected inequality", exp1, exp);
    }

    @Test
    public void testInequalityDifferentOrder() {
        Expression exp = new Operation('*', new Number(1.0), new Variable("x"));
        assertNotEquals("expected inequality", exp2, exp);
    }

    @Test
    public void testInequalityDifferentGrouping() {
        Expression left = new Operation('*', exp1, x);
        Expression exp = new Operation('*', left, one);
        assertNotEquals("expected inequality", exp3, exp);
    }

    @Test
    public void testParseNumber() {
        Expression exp = Expression.parse("1");
        assertEquals("expected parsed expression", one, exp);
    }

    @Test
    public void testParseVariable() {
        Expression exp = Expression.parse("x");
        assertEquals("expected parsed expression", x, exp);
    }

    @Test
    public void testParsePlus() {
        Expression exp = Expression.parse("1 + x");
        assertEquals("expected parsed expression", exp1, exp);
    }

    @Test
    public void testParseMultiply() {
        Expression exp = Expression.parse("x * 1");
        assertEquals("expected parsed expression", exp2, exp);
    }

    @Test
    public void testParseExpressions() {
        Expression exp = Expression.parse("(1 + x) * (x * 1)");
        assertEquals("expected parsed expression", exp3, exp);
    }

    @Test
    public void testParseIlligal() {
        try {
            Expression exp = Expression.parse("3 x");
            assert false; // should not reach here
        }
        catch (IllegalArgumentException e) {
            assert true;
        }
    }

    @Test
    public void testDifferentiateMultiply() {
        Expression exp = new Operation('+', new Operation('*', one, one),
            new Operation('*', x, zero));
        assertEquals("expected differentiated expression", exp2.differentiate("x"), exp);
    }

    @Test
    public void testSimplifyVariable() {
        assertEquals("expected simplified expression", x.simplify(Map.of("x", 2.0)), two);
    }

    @Test
    public void testSimplifyPlusNumber() {
        Expression exp = new Operation('+', zero, one);
        assertEquals("expected simplified expression", exp.simplify(Map.of("x", 2.0)), one);
    }

    @Test
    public void testSimplifyPlusExpression() {
        assertEquals("expected simplified expression", exp1.simplify(Map.of("x", 2.0)), new Number(3));
    }

    @Test
    public void testSimplifyMultiplyNumber() {
        Expression exp = new Operation('*', zero, one);
        assertEquals("expected simplified expression", exp.simplify(Map.of("x", 2.0)), zero);
    }

    @Test
    public void testSimplifySingleDifferentVariable() {
        assertEquals("expected simplified expression", exp3.simplify(Map.of("y", 2.0)), exp3);
    }

    @Test
    public void testSimplifyMultipleVariables() {
        Expression exp = new Operation('*', two, y);
        assertEquals("expected simplified expression", exp4.simplify(Map.of("x", 2.0)), exp);
    }

}
