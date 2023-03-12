package org.example;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;

public class BookBuyerAgent extends Agent {
    protected void setup() {

        // Printout a welcome message
        System.out.println("Hello! Buyer-agent " + getAID().getName() + " is ready.");
        addBehaviour(new TickerBehaviour(this, 5000) {
            @Override
            protected void onTick() {
                myAgent.addBehaviour(new RequestPerformer());
            }
        });
    }

    /**
     * BookBuyerAgent`s inner class.
     * This is buyer`s behaviour for buying a book.
     */
    class RequestPerformer extends Behaviour {
        private int step = 0;

        @Override
        public void action() {
            switch (step) {
                case 0:
                    // send messages

            }
        }

        @Override
        public boolean done() {
            return false;
        }
    }
}
