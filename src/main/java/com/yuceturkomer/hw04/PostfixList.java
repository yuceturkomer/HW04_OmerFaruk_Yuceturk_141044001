package com.yuceturkomer.hw04;

import java.io.*;
import java.util.LinkedList;
import java.util.Stack;
import java.util.EmptyStackException;
import java.util.regex.Pattern;
import java.util.Scanner;
/**
 * Created by Omer Faruk Yuceturk on 20.3.2016.
 *
 */
public class PostfixList extends LinkedList<String> implements PostfixListInterface {

    // Nested Class

    /**
     * Class to report a syntax error.
     */
    public static class SyntaxErrorException extends Exception {

        /**
         * Construct a SyntaxErrorException with the specified
         * message.
         *
         * @param message The message
         */
        SyntaxErrorException(String message) {
            super(message);
        }
    }
    // Data Fields
    /**
     * The operator stack
     */
    private Stack<Character> operatorStack;
    /**
     * The operators
     */
    private static final String OPERATORS = "-+*/=";
    /**
     * The precedence of the operators, matches order in OPERATORS.
     */
    private static final int[] PRECEDENCE = {2, 2, 3, 3, 1};
    /**
     * The Pattern to extract tokens
     * A token is either a string of digits (\d+)
     * or a JavaIdentifier
     * or an operator
     */
    private static final Pattern tokens =
            Pattern.compile("\\d+\\.\\d*|\\d+|"
                    + "\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*"
                    + "|[" + OPERATORS + "]");
    /**
     * The postfix string
     */
    private StringBuilder postfix;
    /**
     * Method to process operators.
     *
     * @param op The operator
     * @throws EmptyStackException
     */
    private void processOperator(char op) {
        if (operatorStack.empty()) {
            operatorStack.push(op);
        } else {
            // Peek the operator stack and
            // let topOp be top operator.
            char topOp = operatorStack.peek();
            if (precedence(op) > precedence(topOp)) {
                operatorStack.push(op);
            } else {
                // Pop all stacked operators with equal
                // or higher precedence than op.
                while (!operatorStack.empty()
                        && precedence(op) <= precedence(topOp)) {
                    operatorStack.pop();
                    postfix.append(topOp);
                    postfix.append(' ');
                    if (!operatorStack.empty()) {
                        // Reset topOp.
                        topOp = operatorStack.peek();
                    }
                }
                // assert: Operator stack is empty or
                //         current operator precedence >
                //         top of stack operator precedence.
                operatorStack.push(op);
            }
        }
    }

    /**
     * Determine whether a character is an operator.
     *
     * @param ch The character to be tested
     * @return true if ch is an operator
     */
    private boolean isOperator(char ch) {
        return OPERATORS.indexOf(ch) != -1;
    }

    /**
     * Determine the precedence of an operator.
     *
     * @param op The operator
     * @return the precedence
     */
    private int precedence(char op) {
        return PRECEDENCE[OPERATORS.indexOf(op)];
    }

    public PostfixList() {
        super();
    }
    public PostfixList(String inFileName){
        infixToPostfixFromFile(inFileName);
    }

    /**
     * Convert a string from infix to postfix.
     *
     * @param infix The infix expression
     * @throws SyntaxErrorException
     */


    public String infixStringToPostfix(String infix) throws SyntaxErrorException,NotIdentifierException {
        operatorStack = new Stack<Character>();
        postfix = new StringBuilder();
        Scanner s = new Scanner(infix);
        try {
            // Process each token in the infix string.
            String nextToken = null;
            while ((nextToken = s.findInLine(tokens)) != null) {
                char firstChar = nextToken.charAt(0);
                // Is it an operand?
                if (Character.isJavaIdentifierStart(firstChar)
                        || Character.isDigit(firstChar)) {
                    postfix.append(nextToken);
                    postfix.append(' ');
                } // Is it an operator?
                else if (isOperator(firstChar)) {
                    processOperator(firstChar);
                } else {
                    throw new SyntaxErrorException
                            ("Unexpected Character Encountered: " + firstChar);
                }
            } // End while.

            // Pop any remaining operators and
            // append them to postfix.
            while (!operatorStack.empty()) {
                char op = operatorStack.pop();
                postfix.append(op);
                postfix.append(' ');
            }
            // assert: Stack is empty, return result.
            return postfix.toString();
        } catch (EmptyStackException ex) {
            throw new SyntaxErrorException("Syntax Error: The stack is empty");
        }
    }


    /**
     * Reads lines from file and then evaluates the postfix
     * form of every line. Then it fills the PostfixList.
     *
     * @param inFileName The input file name string.
     * @return Returns true if successful.
     */
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
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            System.err.println("Error reading file. ->" + inFileName);
            e.printStackTrace();
            return false;
        } catch (NotIdentifierException e) {
            System.err.println("Invalid identifier caught.");
            e.printStackTrace();
        } catch (SyntaxErrorException e) {
            System.err.println("Syntax error. Check your input file.");
            e.printStackTrace();
        }
        return true;
    }
}
