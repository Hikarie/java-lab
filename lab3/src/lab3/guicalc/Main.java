package lab3.guicalc;

import java.util.HashSet;
import java.util.Set;

import lab3.operator.BinaryOperator;
import lab3.operator.BinaryOperatorImp;
import lab3.operator.UnaryOperator;
import lab3.operator.UnaryOperatorImp;

/**
 * Main program that runs the GUI Calculator
 */
public class Main {

    /**
     * Add BinaryOperators and UnaryOperators implements to the calculator.
     * 
     * @param args as input
     */
    public static void main(String[] args) {
        // Generating OperatorSet
        Set<UnaryOperator> unaryOperators = new HashSet<UnaryOperator>();
        Set<BinaryOperator> binaryOperators = new HashSet<BinaryOperator>();
        UnaryOperatorImp uimp = null;
        BinaryOperatorImp bimp = null;
        for(UnaryOperatorImp u:uimp.values()) {
        	unaryOperators.add(u);
        }
        for(BinaryOperatorImp b:bimp.values()) {
        	binaryOperators.add(b);
        }
        // Run the calculator!
        GuiCalculator calc = new GuiCalculator(unaryOperators, binaryOperators);

    }
}
