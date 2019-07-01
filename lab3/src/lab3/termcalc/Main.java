package lab3.termcalc;

import java.util.Scanner;

import lab3.expression.Expression;
import lab3.expression.NewtonsMethod;
import lab3.expression.Variable;

/**
 * Main entry point for the command line calculator
 */
public class Main {
    /**
     * @param args program arguments
     */
    public static void main(String[] args) {
        //Create Instances(ExpressionMaker,TerminalCalculator)
    	ExpressionMaker em = new ExpressionMakerImp();
        TerminalCalculator tc = new TerminalCalculator(em);

        //Use TerminalCalculator
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter an expression: ");
        String expression = sc.next();
        double res = tc.run(expression).eval();
        System.out.println("Result: "+String.valueOf(res));
        
    }
}
