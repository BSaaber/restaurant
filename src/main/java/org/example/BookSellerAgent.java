package org.example;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.HashSet;
import java.util.Set;

public class BookSellerAgent extends Agent {

    Set<String> books;
    protected void setup() {
        System.out.println("Hello! Seller-agent " + getAID().getName() + " is ready.");

        // setup bookshelf
        books = new HashSet<>();
        books.add("hello");
        books.add("world");
        books.add("bible");
        books.add("war and peace");

        // setup listeners
        addBehaviour(new OfferRequestsBehaviour());
    }

    private class OfferRequestsBehaviour extends CyclicBehaviour {

        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage message = myAgent.receive(mt);
            if (message != null) {
                System.out.println("Hello! Seller-agent " + myAgent.getAID().getName() + " received a message.");

                ACLMessage reply = message.createReply();
                String requestedTitle = message.getContent();
                if (books.contains(requestedTitle)) {
                    reply.setPerformative(ACLMessage.PROPOSE);
                    reply.setContent("we have one");
                } else {
                    reply.setPerformative(ACLMessage.REFUSE);
                    reply.setContent("we do not have one");
                }
                myAgent.send(reply);
            } else {
                block();
            }
        }
    }
}
