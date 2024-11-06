package io.github.testcar;//	level1
//	123321
import java.util.Random;

public class RandomMathProblemGenerator {
    private Random random;
    private int min;
    private int max;
    float csol;
    public int Isol;
    int number;
    float operand1,operand2,operand3;
    String operation1,operation2;
    String a="+";
    String b="-";
    String c="*";
    String d="/";

    public RandomMathProblemGenerator(int min, int max) {
        this.random = new Random();
        this.min = min;
        this.max = max;
    }

    public void generateOperands() {
        operand1 = getRandomOperand();
        operand2 = getRandomOperand();
        operand3 = getRandomOperand();
    }

    public String generateProblem() {
    	generateOperands();
        operation1 = getRandomOperation();
        operation2 = getRandomOperation();
        number = random.nextInt(2,4);
        	switch(operation1){
        		case("+"):
        			csol=operand1 + operand2;
        			break;
        		case("-"):
        			csol=operand1 - operand2;
        			break;
        		case("*"):
        			csol=operand1 * operand2;
        			break;
        		case("/"):
        			csol= operand1 / operand2;
        			break;
        	}
        if(number==3){
        	switch(operation1){
        		case("+"):
        			if(operation2.equals(a))
        				csol=(operand1 + operand2 + operand3);
        			else if(operation2.equals(b))
        				csol=(operand1 + operand2 - operand3);
        			else if(operation2.equals(c))
        				csol=(operand1 + (operand2 * operand3));
        			else if(operation2.equals(d))
        				csol=(operand1 + (operand2 / operand3));
        			break;
        		case("-"):
        			if(operation2.equals(a))
        				csol=(operand1 - operand2 + operand3);
        			else if(operation2.equals(b))
        				csol=(operand1 - operand2 - operand3);
        			else if(operation2.equals(c))
        				csol=(operand1 - (operand2 * operand3));
        			else if(operation2.equals(d))
        				csol=(operand1 - (operand2 / operand3));
        			break;
        		case("*"):
        			if(operation2.equals(a))
        				csol=(operand1 * operand2 + operand3);
        			else if(operation2.equals(b))
        				csol=(operand1 * operand2 - operand3);
        			else if(operation2.equals(c))
        				csol=(operand1 * operand2 * operand3);
        			else if(operation2.equals(d))
        				csol=(operand1 * operand2 / operand3);
        			break;
        		case("/"):
        			if(operation2.equals(a))
        				csol=(operand1 / operand2 + operand3);
        			else if(operation2.equals(b))
        				csol=(operand1 / operand2 - operand3);
        			else if(operation2.equals(c))
        				csol=(operand1 / operand2 * operand3);
        			else if(operation2.equals(d))
        				csol=(operand1 / operand2 / operand3);
        			break;
        	}
        	while(true){
        		if(csol%1==0){
        			if (csol!=1&&csol!=2&&csol!=3){
        				generateProblem();
       			}
        			Isol=(int)csol;
        			break;
        		}
        		else
        			generateProblem();
        	}
        	return ((int)operand1 + " " + operation1 + " " + (int)operand2 + " " +operation2 + " " +(int) operand3);
        }
        while(true){
        	if(csol%1==0){
        		if (csol!=1&&csol!=2&&csol!=3){
        			generateProblem();
       		}
        		Isol=(int)csol;
        		break;
        	}
        	else
        		generateProblem();
        }
        return	(int)operand1 + " " + operation1 + " " + (int)operand2;
    }

    private int getRandomOperand() {
        return (random.nextInt((max - min) + 1) + min);
    }

    private String getRandomOperation() {
        String[] operations = {"+", "-", "*", "/"};
        int index = random.nextInt(operations.length);
        String operation=operations[index];
        return operation;
    }

    public static void main(String[] args) {
//        RandomMathProblemGenerator generator = new RandomMathProblemGenerator(1, 3);
//            System.out.println("Question = "+generator.generateProblem());
//            System.out.println("Solution = "+generator.Isol);
    }
}

