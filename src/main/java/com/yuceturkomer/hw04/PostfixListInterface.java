package com.yuceturkomer.hw04;

/**
 * Created by ömer on 20.3.2016.
 */
public interface PostfixListInterface {
    boolean infixToPostfixFromFile(String inFileName);
    String infixStringToPostfix(String strToConvert) throws NotIdentifierException;
}
