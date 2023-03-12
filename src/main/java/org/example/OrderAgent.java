package org.example;

import jade.core.AID;
import jade.core.Agent;

public class OrderAgent extends Agent {
    AID manager;
    AID visitor;

    protected void setup() {
        System.out.println("i am new order! my name is " + getAID());
    }
}
