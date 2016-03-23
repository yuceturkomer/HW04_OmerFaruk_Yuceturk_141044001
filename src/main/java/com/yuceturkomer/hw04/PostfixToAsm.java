package com.yuceturkomer.hw04;


import java.util.Arrays;
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
        int tempIndex1 = -1;
        int tempIndex2 = -1;
        int returnIndex = -1;
        switch (op) {
            case '+':
                rhsIndex = indexOf(rhs);
                lhsIndex = indexOf(lhs);
                if (rhsIndex < 0 && rhs.isIntOp()) {
                    tempIndex1 = giveIndexReg();
                    toWriteList.offer("li   $t" + tempIndex1 + "," + rhs.toString());
                    insertArr(rhs);
                }
                if (lhsIndex < 0 && lhs.isIntOp()) {
                    tempIndex2 = giveIndexReg();
                    toWriteList.offer("li   $t" + tempIndex2 + "," + lhs.toString());
                    insertArr(lhs);
                }
                toWriteList.offer("add  $t" + giveIndexReg() + ",$t" + indexOf(rhs) + ",$" + indexOf(lhs));
                returnIndex = giveIndexReg();
                if (indexOf(rhs) >= 0 && rhs.isIntOp())
                    clearIndexReg(tempIndex1);
                if (indexOf(lhs) >= 0 && lhs.isIntOp())
                    clearIndexReg(tempIndex2);
                break;
            case '-':
                rhsIndex = indexOf(rhs);
                lhsIndex = indexOf(lhs);
                if (rhsIndex < 0 && rhs.isIntOp()) {
                    tempIndex1 = giveIndexReg();
                    toWriteList.offer("li   $t" + tempIndex1 + "," + rhs.toString());
                    insertArr(rhs);
                }
                if (lhsIndex < 0 && lhs.isIntOp()) {
                    tempIndex2 = giveIndexReg();
                    toWriteList.offer("li   $t" + tempIndex2 + "," + lhs.toString());
                    insertArr(lhs);
                }
                toWriteList.offer("sub  $t" + giveIndexReg() + ",$t" + indexOf(rhs) + ",$" + indexOf(lhs));
                returnIndex = giveIndexReg();
                if (indexOf(rhs) >= 0 && rhs.isIntOp())
                    clearIndexReg(tempIndex1);
                if (indexOf(lhs) >= 0 && lhs.isIntOp())
                    clearIndexReg(tempIndex2);
                break;
            case '/':
                rhsIndex = indexOf(rhs);
                lhsIndex = indexOf(lhs);
                if (rhsIndex < 0 && rhs.isIntOp()) {
                    tempIndex1 = giveIndexReg();
                    toWriteList.offer("li   $t" + tempIndex1 + "," + rhs.toString());
                    insertArr(rhs);
                }
                if (lhsIndex < 0 && lhs.isIntOp()) {
                    tempIndex2 = giveIndexReg();
                    toWriteList.offer("li   $t" + tempIndex2 + "," + lhs.toString());
                    insertArr(lhs);
                }
                toWriteList.offer("div  $t" + indexOf(lhs) + ",$" + indexOf(rhs));
                returnIndex = giveIndexReg();
                toWriteList.offer("mflo $" + returnIndex);
                if (indexOf(rhs) >= 0 && rhs.isIntOp())
                    clearIndexReg(tempIndex1);
                if (indexOf(lhs) >= 0 && lhs.isIntOp())
                    clearIndexReg(tempIndex2);
                break;
            case '*':
                rhsIndex = indexOf(rhs);
                lhsIndex = indexOf(lhs);
                if (rhsIndex < 0 && rhs.isIntOp()) {
                    tempIndex1 = giveIndexReg();
                    toWriteList.offer("li   $t" + tempIndex1 + "," + rhs.toString());
                    insertArr(rhs);
                }
                if (lhsIndex < 0 && lhs.isIntOp()) {
                    tempIndex2 = giveIndexReg();
                    toWriteList.offer("li   $t" + tempIndex2 + "," + lhs.toString());
                    insertArr(lhs);
                }
                toWriteList.offer("mult  $t" + indexOf(lhs) + ",$" + indexOf(rhs));
                returnIndex = giveIndexReg();
                toWriteList.offer("mflo $" + returnIndex);
                if (indexOf(rhs) >= 0 && rhs.isIntOp())
                    clearIndexReg(tempIndex1);
                if (indexOf(lhs) >= 0 && lhs.isIntOp())
                    clearIndexReg(tempIndex2);
                break;
            case '=':
                rhsIndex = indexOf(rhs);
                lhsIndex = indexOf(lhs);
                if (rhsIndex < 0 && rhs.isIntOp()) {
                    tempIndex1 = giveIndexReg();
                    toWriteList.offer("li   $t" + tempIndex1 + "," + rhs.toString());
                    insertArr(rhs);
                }
                if (lhsIndex < 0 && lhs.isIntOp()) {
                    tempIndex2 = giveIndexReg();
                    toWriteList.offer("li   $t" + tempIndex2 + "," + lhs.toString());
                    insertArr(lhs);
                }
                toWriteList.offer("move  $t" + indexOf(lhs) + ",$" + indexOf(rhs));
                returnIndex = giveIndexReg();

                if (indexOf(rhs) >= 0 && rhs.isIntOp())
                    clearIndexReg(tempIndex1);
                if (indexOf(lhs) >= 0 && lhs.isIntOp())
                    clearIndexReg(tempIndex2);
                for (Operand o : regArr) {
                    if (o.isIntOp())
                        o = null;
                }
                break;
        }
        return returnIndex;
    }

    private boolean isOperator(char ch) {
        return OPERATORS.indexOf(ch) != -1;
    }

    public PostfixToAsm(PostfixList postfixList) {
        for (Operand op : regArr) {
            op = null;
        }
        try {
            eval(postfixList.poll());
        } catch (SyntaxErrorException e) {
            System.err.println("Syntax exception.: ");
            e.printStackTrace();
        }
    }

    public void insertArr(Operand op) {
        regArr[giveIndexReg()] = op;
    }

    public void clearIndexReg(int index) {
        regArr[index] = null;
    }

    public int giveIndexReg() {
        for (int i = 0; i < regArr.length; ++i) {
            if (regArr[i] == null) {
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
        operandStack = new Stack<Operand>();
        regArr = new Operand[8];

        String[] tokens = expression.split("\\s+");
        try {
            for (String nextToken : tokens) {
                char firstChar = nextToken.charAt(0);
                Operand tempOp = new Operand(nextToken);
                if (Character.isDigit(firstChar)) {
                    int value = Integer.parseInt(nextToken);
                    operandStack.push(new Operand(String.valueOf(value)));
                } else if (isOperator(firstChar)) {
                    int result = evalOp(firstChar);
                    operandStack.push(new Operand(String.valueOf(result)));
                } else if (tempOp.isVariable()) {

                } else {
                    throw new SyntaxErrorException(
                            "Invalid char encountered" + firstChar);
                }
            }
            int answer = 1;
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

    @Override
    public String toString() {
        return "PostfixToAsm{" +
                "operandStack=" + operandStack +
                ", regArr=" + Arrays.toString(regArr) +
                ", toWriteList=" + toWriteList +
                '}';
    }
}
