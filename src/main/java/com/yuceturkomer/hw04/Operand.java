package com.yuceturkomer.hw04;

/**
 * Created by ömer on 22.3.2016.
 */
public class Operand {
    private String operand;

    public Operand(String operand) {
        setOperand(operand);

    }
    public boolean isIntOp(){
        return (operand.length()>0 && operand.charAt(0) >= '0') && (operand.charAt(0) <= '9');
    }
    public boolean isVariable(){
        return (operand.length()>0 && !isIntOp() && Character.isJavaIdentifierStart(operand.charAt(0)));
    }
    public String getOperand() {
        return operand;
    }

    public void setOperand(String operand) {
        this.operand = operand;
    }
    public Operand getCopy(){
        return new Operand(getOperand());
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Operand)) return false;

        Operand operand1 = (Operand) o;

        return getOperand().equals(operand1.getOperand());

    }
    @Override
    public String toString() {
        return operand;
    }
}
