package com.yuceturkomer.hw04;

/**
 * Created by ömer on 21.3.2016.
 */
public class InfixToPostfixMain {
    public static void main(String[] args){
        PostfixList test1 = new PostfixList();
        test1.infixToPostfixFromFile("input.txt");
        test1.printAll();
    }
}
