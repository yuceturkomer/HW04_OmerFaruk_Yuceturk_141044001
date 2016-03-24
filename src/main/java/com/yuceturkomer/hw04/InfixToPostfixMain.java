package com.yuceturkomer.hw04;

/**
 * Created by ömer on 21.3.2016.
 */
public class InfixToPostfixMain {
    public static void main(String[] args) {
        System.out.println("#################### INPUT FILE ############################");
        PostfixList test1 = new PostfixList();
        test1.infixToPostfixFromFile("input.txt");
        System.out.println("#################### POSTFIX FORM ### test1 #################");
        for(String s: test1){
            System.out.println(s);
        }
        System.out.println("#################### INPUT FILE ############################");
        PostfixList test2 = new PostfixList("input.txt");
        System.out.println("#################### POSTFIX FORM ### test 2 ################");
        for(String s: test2){
            System.out.println(s);
        }
        System.out.println("####################   CRITIC TEST :(  ###################");
        PostfixToAsm trekt = new PostfixToAsm(test2);
        System.out.println(trekt.toString());



    }
}
