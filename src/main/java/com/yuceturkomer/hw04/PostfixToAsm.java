package com.yuceturkomer.hw04;


import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Stack;

/**
 *
 */
public class PostfixToAsm implements PostfixToAsmInterface {

    public static class SyntaxErrorException extends Exception {
        SyntaxErrorException(String message) {
            super(message);
        }
    }



    private static final String OPERATORS = "+-*/=";
    private Stack<Operand> operandStack;
    private Operand[] regArr = new Operand[8];
    private LinkedList<String> toWriteList = new LinkedList<String>();

    private int evalOp(char op) {
        Operand rhs = operandStack.pop();
        Operand lhs = operandStack.pop();
        int rhsIndex;
        int lhsIndex;
        int tempIndex;
        switch (op) {
            case '+':
                rhsIndex = indexOf(rhs);
                lhsIndex = indexOf(lhs);
                if(rhsIndex<0 && rhs.isIntOp()){
                    tempIndex = giveIndex();
                    toWriteList.offer("li   $t"+tempIndex+","+rhs.toString());
                    insertArr(rhs);
                }
                if(lhsIndex<0 && lhs.isIntOp()){
                    tempIndex = giveIndex();
                    toWriteList.offer("li   $t"+tempIndex+","+lhs.toString());
                    insertArr(lhs);
                }
                toWriteList.offer("add  $t"+giveIndex()+",$t"+indexOf(rhs)+",$"+indexOf(lhs));
                insertArr();


                result = lhs + rhs;
                break;
            case '-':
                result = lhs - rhs;
                break;
            case '/':
                result = lhs / rhs;
                break;
            case '*':
                result = lhs * rhs;
                break;
            case '=':
                lhs = rhs;
                break;
        }
        return index;
    }

    private boolean isOperator(char ch) {
        return OPERATORS.indexOf(ch) != -1;
    }
    public PostfixToAsm() {
        for (Operand op : regArr) {
            op = null;
        }
    }

    public void insertArr(Operand op) {
        regArr[giveIndex()]=op;
    }

    public int giveIndex(){
        for (int i = 0; i < regArr.length; ++i) {
            if (regArr[i]==null) {
                return i;
            }
        }
        return -1;
    }

    public int indexOf(Operand op) {
        for (int i = 0; i < regArr.length; ++i) {
            if (regArr[i].equals(op)) {
                return i;
            }
        }
        return -1;
    }

    public int eval(String expression) throws SyntaxErrorException {
        operandStack = new Stack<Integer>();
        String[] tokens = expression.split("\\s+");
        try {
            for (String nextToken : tokens) {
                char firstChar = nextToken.charAt(0);
                if (Character.isDigit(firstChar)) {
                    int value = Integer.parseInt(nextToken);
                    operandStack.push(value);
                } else if (isOperator(firstChar)) {
                    int result = evalOp(firstChar);
                    operandStack.push(result);
                } else {
                    throw new SyntaxErrorException(
                            "Invalid char encountered" + firstChar);
                }
            }
            int answer = operandStack.pop();
            if (operandStack.empty()) {
                return answer;
            } else {
                throw new SyntaxErrorException("Syntax Error" +
                        ": Stack should be empty");
            }
        } catch (EmptyStackException ex) {
            throw new SyntaxErrorException(
                    "Syntax Error: The stack is empty");
        }
    }


}
