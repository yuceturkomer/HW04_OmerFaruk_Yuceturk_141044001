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
    private Register[] regArr = new Register[8];
    private LinkedList<String> toWriteList = new LinkedList<String>();

    private int evalOp(char op, Integer result) throws UninitializedVariableException, AssignmentException {
        Operand rhs = operandStack.pop();
        Operand lhs = operandStack.pop();
        int rhsIndex, rhsVal;
        int lhsIndex, lhsVal;
        int tempIndex1 = -1;
        int tempIndex2 = -1;
        int returnIndex = -1, resultVal;

        try {
            switch (op) {
                case '+':
                    rhsIndex = indexOf(rhs);
                    lhsIndex = indexOf(lhs);
                    if (rhsIndex < 0 && rhs.isIntOp()) {
                        tempIndex1 = giveIndexReg();
                        toWriteList.offer("li   $t" + tempIndex1 + "," + rhs.toString());
                        insertArr(rhs);
                    } else if (rhs.isVariable() || lhs.isVariable())
                        throw new UninitializedVariableException();
                    if (lhsIndex < 0 && lhs.isIntOp()) {
                        tempIndex2 = giveIndexReg();
                        toWriteList.offer("li   $t" + tempIndex2 + "," + lhs.toString());
                        insertArr(lhs);
                    }
                    if (rhsIndex >= 0)
                        rhsVal = Integer.parseInt(regArr[rhsIndex].getOperand().getOperand());
                    else
                        rhsVal = Integer.parseInt(rhs.getOperand());
                    if (lhsIndex >= 0)
                        lhsVal = Integer.parseInt(regArr[lhsIndex].getOperand().getOperand());
                    else
                        lhsVal = Integer.parseInt(lhs.getOperand());

                    resultVal = rhsVal + lhsVal;
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
                    } else if (rhs.isVariable() || lhs.isVariable())
                        throw new UninitializedVariableException();
                    if (lhsIndex < 0 && lhs.isIntOp()) {
                        tempIndex2 = giveIndexReg();
                        toWriteList.offer("li   $t" + tempIndex2 + "," + lhs.toString());
                        insertArr(lhs);
                    }
                    if (rhsIndex >= 0)
                        rhsVal = Integer.parseInt(regArr[rhsIndex].getOperand().getOperand());
                    else
                        rhsVal = Integer.parseInt(rhs.getOperand());
                    if (lhsIndex >= 0)
                        lhsVal = Integer.parseInt(regArr[lhsIndex].getOperand().getOperand());
                    else
                        lhsVal = Integer.parseInt(lhs.getOperand());
                    resultVal = lhsVal - rhsVal;
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
                    } else if (rhs.isVariable() || lhs.isVariable())
                        throw new UninitializedVariableException();
                    if (lhsIndex < 0 && lhs.isIntOp()) {
                        tempIndex2 = giveIndexReg();
                        toWriteList.offer("li   $t" + tempIndex2 + "," + lhs.toString());
                        insertArr(lhs);
                    }
                    toWriteList.offer("div  $t" + indexOf(lhs) + ",$" + indexOf(rhs));
                    returnIndex = giveIndexReg();
                    toWriteList.offer("mflo $" + returnIndex);
                    if (rhsIndex >= 0)
                        rhsVal = Integer.parseInt(regArr[rhsIndex].getOperand().getOperand());
                    else
                        rhsVal = Integer.parseInt(rhs.getOperand());
                    if (lhsIndex >= 0)
                        lhsVal = Integer.parseInt(regArr[lhsIndex].getOperand().getOperand());
                    else
                        lhsVal = Integer.parseInt(lhs.getOperand());
                    resultVal = lhsVal / rhsVal;
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
                    } else if (lhs.isVariable() || rhs.isVariable())
                        throw new UninitializedVariableException();
                    if (lhsIndex < 0 && lhs.isIntOp()) {
                        tempIndex2 = giveIndexReg();
                        toWriteList.offer("li   $t" + tempIndex2 + "," + lhs.toString());
                        insertArr(lhs);
                    }
                    if (rhsIndex >= 0)
                        rhsVal = Integer.parseInt(regArr[rhsIndex].getOperand().getOperand());
                    else
                        rhsVal = Integer.parseInt(rhs.getOperand());
                    if (lhsIndex >= 0)
                        lhsVal = Integer.parseInt(regArr[lhsIndex].getOperand().getOperand());
                    else
                        lhsVal = Integer.parseInt(lhs.getOperand());
                    returnIndex = giveIndexReg();
                    resultVal = lhsVal * rhsVal;
                    toWriteList.offer("mult  $t" + indexOf(lhs) + ",$" + indexOf(rhs));
                    toWriteList.offer("mflo $" + returnIndex);
                    if (indexOf(rhs) >= 0 && rhs.isIntOp())
                        clearIndexReg(tempIndex1);
                    if (indexOf(lhs) >= 0 && lhs.isIntOp())
                        clearIndexReg(tempIndex2);
                    break;
                case '=':
                    rhsIndex = indexOf(rhs);
                    lhsIndex = indexOf(lhs);
                    int returnVal;
                    if (lhsIndex < 0 && lhs.isIntOp()) {
                        throw new AssignmentException("Left hand side cant be integer for assignment");
                    }
                    if (rhs.isVariable() && rhsIndex < 0)
                        throw new UninitializedVariableException();


                    if (rhs.isIntOp() && lhsIndex < 0) {
                        tempIndex1 = giveIndexReg();
                        toWriteList.offer("li   $t" + tempIndex1 + "," + rhs.toString());
                        insertArr(rhs);
                        regArr[tempIndex1].setChangable(false);
                    } else if (lhsIndex >= 0 && rhsIndex >= 0) {
                        toWriteList.offer("move  $t" + indexOf(lhs) + ",$t" + indexOf(rhs));
                        tempIndex1 = indexOf(lhs);
                    } else if (lhsIndex >= 0 && rhsIndex < 0) {
                        tempIndex2 = giveIndexReg();
                        insertArr(rhs);
                        toWriteList.offer("li   $t" + tempIndex2 + "," + rhs.toString());
                        toWriteList.offer("move  $t" + lhsIndex + ",$t" + indexOf(rhs));
                        tempIndex1 = lhsIndex;
                    }
                    returnIndex = tempIndex1;
                    returnVal = Integer.parseInt(rhs.getOperand());

                    if (indexOf(rhs) >= 0 && regArr[indexOf(rhs)].isChangable())
                        clearIndexReg(tempIndex1);
                    for (Register o : regArr) {
                        if (o.isChangable())
                            o = null;
                    }
                    break;
            }
        } catch (RegisterIsFullException e) {
            System.err.println("The register is full. Program will exit.");
            System.exit(1);
        }
        return returnIndex;
    }

    private boolean isOperator(char ch) {
        return OPERATORS.indexOf(ch) != -1;
    }

    public PostfixToAsm(PostfixList postfixList) {
        for (Register op : regArr) {
            op=new Register(new Operand(""),-1);
            op.setChangable(true);
        }
        try {
            eval(postfixList.poll());
        } catch (SyntaxErrorException e) {
            System.err.println("Syntax exception.: ");
            e.printStackTrace();
        }
    }

    public void insertArr(Operand op) throws RegisterIsFullException {
        if (giveIndexReg() != -1)
            regArr[giveIndexReg()] = new Register(op.getCopy(), giveIndexReg());
        else
            throw new RegisterIsFullException();
    }

    public void clearIndexReg(int index) {
        regArr[index].setChangable(true);
    }

    public int giveIndexReg() {
        for (int i = 0; i < regArr.length; ++i) {
            if (regArr[i] == null || regArr[i].isChangable()) {
                return i;
            }
        }
        return -1;
    }

    public int indexOf(Operand op) {
        int returnval = -1;
        for (int i = 0; i < regArr.length; ++i) {
            if (regArr[i].equals(op)) {
                returnval = i;
            }
        }
        return returnval;
    }

    public int eval(String expression) throws SyntaxErrorException {
        Integer returnVal=null;
        operandStack = new Stack<Operand>();
        if (expression.contains("print")) {
            String[] tokens = expression.split("\\s+");
            Operand tempOp = new Operand(tokens[1]);
            for (Register r : regArr) {
                if (indexOf(tempOp) >= 0) {
                    toWriteList.offer("move   $a0,t" + indexOf(tempOp));
                    toWriteList.offer("li   $v0,1" + indexOf(tempOp));
                    toWriteList.offer("syscall");
                    return 1;
                }
            }
        }

        String[] tokens = expression.split("\\s+");
        try {
            for (String nextToken : tokens) {
                char firstChar = nextToken.charAt(0);
                Operand tempOp = new Operand(nextToken);
                if (Character.isDigit(firstChar)) {
                    int value = Integer.parseInt(nextToken);
                    operandStack.push(new Operand(String.valueOf(value)));
                } else if (isOperator(firstChar)) {
                    switch (firstChar) {
                        case '+':
                        case '-':
                        case '*':
                        case '/':
                            Integer result = 0;
                            int regIndex = evalOp(firstChar, result);
                            regArr[regIndex] = new Register(new Operand(String.valueOf(result)), regIndex);
                            operandStack.push(new Operand(String.valueOf(result)));
                            break;
                        case '=':
                            result = 0;
                            regIndex = evalOp(firstChar, result);
                            regArr[regIndex] = new Register(new Operand(String.valueOf(result)), regIndex);
                            operandStack.push(new Operand(String.valueOf(result)));
                            returnVal = result;
                            break;
                    }
                } else if (tempOp.isVariable()) {
                    operandStack.push(tempOp);
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
        } catch (UninitializedVariableException e) {
            e.printStackTrace();
        } catch (AssignmentException e) {
            e.printStackTrace();
        }
        if(returnVal==null)
            throw new NullPointerException();
        return returnVal;
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
