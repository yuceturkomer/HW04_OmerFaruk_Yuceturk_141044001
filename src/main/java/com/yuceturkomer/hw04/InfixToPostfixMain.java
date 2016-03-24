package com.yuceturkomer.hw04;

/**
 * Main function for tests
 *
 *
 */
public class InfixToPostfixMain {
    public static void main(String[] args) {

        String inFile;
        if(args.length==2){
            inFile=args[1];
        }
        else
            inFile="input.txt";


        System.out.println("#################### INPUT FILE ############################");
        PostfixList test1 = new PostfixList();
        test1.infixToPostfixFromFile(inFile);
        System.out.println("#################### POSTFIX FORM ### test1 #################");
        for(String s: test1){
            System.out.println(s);
        }
        System.out.println("#################### INPUT FILE ############################");
        PostfixList test2 = new PostfixList(inFile);
        System.out.println("#################### POSTFIX FORM ### test 2 ################");
        for(String s: test2){
            System.out.println(s);
        }
        System.out.println("####################   CRITIC TEST :(  ###################");
        PostfixToAsm trekt = new PostfixToAsm(test2);
        System.out.println(trekt.toString());



    }
}
