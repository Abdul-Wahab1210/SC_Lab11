package expressivo;

import java.util.Stack;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import expressivo.parser.ExpressionListener;
import expressivo.parser.ExpressionParser;

/** 
 * Make a Expresion value from a parse tree.
 */
class ExpressionMaker implements ExpressionListener {
    private Stack<Expression> stack = new Stack<>();
    public Expression getExpression() {
        return stack.get(0);
    }

    @Override public void exitRoot(ExpressionParser.RootContext context) {
    }

    @Override public void exitSum(ExpressionParser.SumContext context) {  
        int products = context.product().size();
        assert stack.size() >= products;
        assert products > 0;

        Expression sum = stack.pop();
        for (int i = 0; i < products - 1; ++i) {
            sum = new Operation('+', stack.pop(), sum);
        }
        stack.push(sum);
    }

    @Override public void exitProduct(ExpressionParser.ProductContext context) {  
        int primitives = context.primitive().size();
        assert stack.size() >= primitives;
        assert primitives > 0;

        Expression product = stack.pop();
        for (int i = 0; i < primitives - 1; ++i) {
            product = new Operation('*', stack.pop(), product);
        }
        stack.push(product);
    }

    @Override public void exitPrimitive(ExpressionParser.PrimitiveContext context) {
        if (context.NUMBER() != null) {
            // matched the NUMBER alternative
            double n = Double.valueOf(context.NUMBER().getText());
            Expression number = new Number(n);
            stack.push(number);
        }
        else if (context.VARIABLE() != null) {
            // matched the VARIABLE alternative
            String var = context.VARIABLE().getText();
            Expression variable = new Variable(var);
            stack.push(variable);
        }
        else {
            }
    }

    @Override public void enterRoot(ExpressionParser.RootContext context) { }
    @Override public void enterSum(ExpressionParser.SumContext ctx) { }
    @Override public void enterProduct(ExpressionParser.ProductContext ctx) { }
    @Override public void enterPrimitive(ExpressionParser.PrimitiveContext context) { }

    @Override public void visitTerminal(TerminalNode terminal) { }
    @Override public void enterEveryRule(ParserRuleContext context) { }
    @Override public void exitEveryRule(ParserRuleContext context) { }
    @Override public void visitErrorNode(ErrorNode node) { }

}
