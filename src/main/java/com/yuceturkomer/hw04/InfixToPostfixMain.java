package com.yuceturkomer.hw04;

/**
 * Created by ömer on 21.3.2016.
 */
public class InfixToPostfixMain {
    public static void main(String[] args) {
        System.out.println("#################### INPUT FILE ############################");
        PostfixList test1 = new PostfixList();
        test1.infixToPostfixFromFile("input.txt");
        System.out.println("#################### POSTFIX FORM ###########################");
        for(String s: test1){
            System.out.println(s);
        }


    }
}
