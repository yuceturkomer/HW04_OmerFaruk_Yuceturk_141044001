package com.yuceturkomer.hw04;


import java.io.*;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Stack;

/**
 * PostfixToAsm class
 */
public class PostfixToAsm {
    /**
     * Nested exception class
     */
    public static class SyntaxErrorException extends Exception {
        SyntaxErrorException(String message) {
            super(message);
        }
    }


    private static final String OPERATORS = "+-*/=";
    private Stack<Operand> operandStack;
    /**
     * Represents the registers
     */
    private Register[] regArr = new Register[8];
    private LinkedList<String> toWriteList = new LinkedList<String>();

    /**
     * Evaluates operator, returns index of operations results kept.
     *
     * @param op     character operand
     * @param result result of operation
     * @return index of register result hold
     * @throws UninitializedVariableException if there is uninitialized variable
     * @throws AssignmentException            false assignment
     */
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
                        if (o != null && o.isChangable())
                            o.setRegIndex(-1);
                    }
                    break;
                default:
                    writeToFile();
            }
        } catch (RegisterIsFullException e) {
            System.err.println("The register is full. Program will exit.");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnIndex;
    }

    private boolean isOperator(char ch) {
        return OPERATORS.indexOf(ch) != -1;
    }

    /**
     * Takes the postfixlist and converts to Assembly file *.asm
     *
     * @param postfixList
     */
    public PostfixToAsm(PostfixList postfixList) {
        for (Register op : regArr) {
            op = new Register(new Operand(""), -1);
            op.setChangable(true);
        }
        try {
            while (postfixList.size() != 0) {
                eval(postfixList.poll());
            }
            System.err.println("=========================");
        } catch (SyntaxErrorException e) {
            System.err.println("Syntax exception.: ");
            e.printStackTrace();
        }
    }

    /**
     * Operand to be inserted to the register array
     *
     * @param op
     * @throws RegisterIsFullException
     */
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
        if (op == null)
            return returnval;
        for (int i = 0; i < regArr.length; ++i) {
            if (regArr[i] != null && regArr[i].equals(op)) {
                returnval = i;
            }
        }
        return returnval;
    }

    /**
     * Evaluates the postfix string and creates assembly codes for each operation done
     *
     * @param expression postfix string to be converted
     * @return returns result of the operations
     * @throws SyntaxErrorException syntax error
     */
    public int eval(String expression) throws SyntaxErrorException {
        Integer returnVal = null;
        operandStack = new Stack<Operand>();
        if (expression.contains("print")) {
            String[] tokens = expression.split("\\s+");
            Operand tempOp = new Operand(tokens[1]);
            for (Register r : regArr) {
                if (indexOf(tempOp) >= 0) {
                    toWriteList.offer("move   $a0,t" + indexOf(tempOp));
                    toWriteList.offer("li   $v0,1" + indexOf(tempOp));
                    toWriteList.offer("syscall");
                    try {
                        writeToFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                            writeToFile();
                            regArr[regIndex] = new Register(new Operand(String.valueOf(result)), regIndex);
                            operandStack.push(new Operand(String.valueOf(result)));
                            break;
                        case '=':
                            result = 0;
                            regIndex = evalOp(firstChar, result);
                            writeToFile();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (returnVal == null)
            throw new NullPointerException();
        return returnVal;
    }

    public void writeToFile() throws IOException {
        File fout = new File("output.asm");
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        if (!toWriteList.isEmpty()) {
            bw.append(toWriteList.poll());
            bw.newLine();
        }

        bw.close();
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
