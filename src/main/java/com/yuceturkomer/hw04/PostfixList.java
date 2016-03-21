package com.yuceturkomer.hw04;

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
    private Stack<Character> operatorStack = new Stack<Character>();

    private String evalOperator(Character operator, String toPostfix) throws AssignmentException {
        Character c;
        try {
            while (true) {
                c = operatorStack.peek();
                if (getPrecedence(c) < getPrecedence(operator)) {
                    operatorStack.push(operator);
                    break;
                } else {
                    toPostfix = toPostfix + operatorStack.pop().toString() + " ";
                }
            }
        } catch (EmptyStackException e) {
            operatorStack.push(operator);
        }
        if (operatorStack.peek() == '=' && size() > 1) {
            toPostfix = toPostfix + operatorStack.pop().toString() + " ";
        }
        return toPostfix;
    }

    //Testing
    public void printAll() {
        System.out.println(operatorStack.toString());
        System.out.println(toString());
    }

    private int getPrecedence(Character op) {
        return PRECEDENCE[OPERATORS.indexOf(op)];
    }

    public String infixStringToPostfix(String strToConvert) throws NotIdentifierException {
        String toReturn = "";
        String[] split = strToConvert.split("\\s+");
        for (String aSplit : split) {
            Character c = aSplit.charAt(0);
            try {
                switch (c) {
                    case '*':
                    case '/':
                    case '+':
                    case '-':
                    case '=':
                        toReturn = evalOperator(c, toReturn);
                        break;
                    default:
                        toReturn += aSplit + " ";
                        break;
                }
            } catch (AssignmentException e) {
                e.printStackTrace();
            }
        }
        while (!operatorStack.empty()) {
            Character c = operatorStack.pop();
            if (c != '=')
                toReturn += c.toString() + " ";
            else
                toReturn += c.toString();
        }

        return toReturn;
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
