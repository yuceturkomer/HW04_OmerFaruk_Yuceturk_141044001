package com.yuceturkomer.hw04;

/**
 * PostfixListInterface
 * Created by Omer Faruk Yuceturk on 20.3.2016.
 */
public interface PostfixListInterface {
    boolean infixToPostfixFromFile(String inFileName);
    String infixStringToPostfix(String strToConvert) throws NotIdentifierException, PostfixList.SyntaxErrorException;
}
