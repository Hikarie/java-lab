package lab3.termcalc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import lab3.expression.DerivativeExpression;
import lab3.expression.Expression;
import lab3.expression.NewtonsMethod;
import lab3.expression.Variable;

public class ExpressionTest {
	
	ExpressionMaker em = new ExpressionMakerImp();
    TerminalCalculator tc = new TerminalCalculator(em);

	@Test
	public void regularCaculateTest() {
		assertEquals(-1.4,tc.run("1+2*(3-4)/5-2").eval());
		assertEquals(5.0,tc.run("(3*3+4*4)^0.5").eval());
		assertEquals(-0.5,tc.run("abs(-0.5)*3-2").eval());
		assertEquals(Double.POSITIVE_INFINITY,tc.run("3/0+1").eval());
		assertEquals(1e+22,tc.run("10000000000/0.000000000001").eval());
	}
	
	@Test
	public void derivativeExpressionTest() {
		Variable var = new Variable("x");
		ExpressionMaker maker = new ExpressionMakerImp();
		// x*x-2
		Expression fn = maker.differenceExpression(maker.productExpression(var, var), maker.numberExpression(2));
		var.store(2);
		Expression der = new DerivativeExpression(fn,var);
		assertEquals(4,der.eval(),0.001);
	}
	
	@Test
	public void newtonsMethodTest() {
		Variable var = new Variable("x");
		ExpressionMaker maker = new ExpressionMakerImp();
		// x^2-3*x+2
		Expression fn = maker.sumExpression(maker.differenceExpression(maker.exponentiationExpression(var, maker.numberExpression(2)), maker.productExpression(maker.numberExpression(3), var)),maker.numberExpression(2));
		NewtonsMethod nt = new NewtonsMethod();
		assertEquals(2, nt.zero(fn, var, 3, 0.0001),0.0001);
		
        Variable x = new Variable("x");
        fn = maker.differenceExpression(maker.productExpression(x, x), maker.numberExpression(2));
        assertEquals(1.4166666, nt.zero(fn, x, 2, 0.01), 1e-6);
	}

}
