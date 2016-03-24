package com.yuceturkomer.hw04;

/**
 * Created by ömer on 23.3.2016.
 * Register Class
 */
public class Register {
    Operand operand;
    private boolean changable;
    private int regIndex;

    public Register(Operand operand,int regIndex) {
        this.operand = operand;
        this.regIndex = regIndex;
        if (operand.isVariable())
            setChangable(false);
        else
            setChangable(true);
    }

    public Operand getOperand() {
        return operand;
    }

    public void setOperand(Operand operand) {
        this.operand = operand;
    }

    public boolean isChangable() {
        return operand==null||changable;
    }

    public void setChangable(boolean changable) {
        this.changable = changable;
    }

    public int getRegIndex() {
        return regIndex;
    }

    public void setRegIndex(int regIndex) {
        this.regIndex = regIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Register)) return false;

        Register register = (Register) o;
        return getOperand() != null ? getOperand().equals(register.getOperand()) : register.getOperand() == null;

    }


    @Override
    public String toString() {
        return "Register{" +
                "operand=" + operand +
                ", changable=" + changable +
                ", regIndex=" + regIndex +
                '}';
    }
    public Register getCopy(){
        return new Register(operand.getCopy(),regIndex);
    }

}
