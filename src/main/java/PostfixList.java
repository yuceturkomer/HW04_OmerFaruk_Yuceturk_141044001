import java.io.*;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by ömer on 20.3.2016.
 */
public class PostfixList extends LinkedList<String> implements PostfixListInterface {
    private static final String OPERATORS = "=-+*/";
    private static final Integer[] PRECEDENCE = {1, 2, 2, 3, 3};
    private LinkedList<String> varQueue = new LinkedList<String>();
    private Stack<Character> operatorStack = new Stack<Character>();

    private void evalOperator(Character operator) throws AssignmentException {
        Character c;
        try {
            while (true) {
                c = operatorStack.peek();
                if (getPrecedence(c) < getPrecedence(operator))
                    operatorStack.push(operator);
                else {
                    while (getPrecedence(operatorStack.peek()) >= getPrecedence(operator)) {
                        offer(String.valueOf(operatorStack.pop()));
                    }
                    operatorStack.push(operator);
                }
            }
        } catch (EmptyStackException e) {
            operatorStack.push(operator);
        }
        if (operatorStack.peek() == '=') {
            offer(String.valueOf(operatorStack.pop()));
        } else {
            throw new AssignmentException("Assignment operator not found or is at wrong place");
        }

    }

    private int getPrecedence(Character op) {
        return PRECEDENCE[OPERATORS.indexOf(op)];
    }

    public String infixStringToPostfix(String strToConvert) throws NotIdentifierException {
        String[] split = strToConvert.split("\\s+");
        for (String aSplit : split) {
            if (aSplit.length() > 1 && aSplit.charAt(0) >= '0' && aSplit.charAt(0) <= '9')
                throw new NotIdentifierException("Not an identifier");
            Character c = aSplit.charAt(0);
            try {
                switch (c) {
                    case '*':
                    case '/':
                        evalOperator(c);
                        break;
                    case '+':
                    case '-':
                        evalOperator(c);
                        break;
                    case '=':
                        evalOperator(c);
                        break;
                    default:
                        varQueue.offer(aSplit);
                        break;
                }
            } catch (AssignmentException e) {
                e.getMessage();
            }
        }

        return null;
    }


    public boolean infixToPostfixFromFile(String inFileName) {

        try {
            FileInputStream fStream = new FileInputStream(inFileName);
            DataInputStream in = new DataInputStream(fStream);
            BufferedReader bReader = new BufferedReader(new InputStreamReader(in));
            String fLine;
            while ((fLine = bReader.readLine()) != null) {
                System.out.println(fLine);
                offer(infixStringToPostfix(fLine));
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error opening file to read. ->" + inFileName);
            e.getMessage();
            return false;
        } catch (IOException e) {
            System.err.println("Error reading file. ->" + inFileName);
            e.getMessage();
            return false;
        } catch (NotIdentifierException e) {
            System.err.println("Invalid identifier caught.");
            e.getMessage();
        }
        return true;

    }


}
