package org.example;

import org.example.ioinstruments.InputParser;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        InputParser inputParser = new InputParser();
        inputParser.parseInput();
    }
}