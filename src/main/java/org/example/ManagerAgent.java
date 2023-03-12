package org.example;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class ManagerAgent extends Agent {
    AID[] visitors;
    AID[] orders;

    protected void setup() {
        // register in DF
        DFAgentDescription agentDescription = new DFAgentDescription();
        agentDescription.setName(getAID());
        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType("manager-service-type");
        serviceDescription.setName("manager-service-name");
        agentDescription.addServices(serviceDescription);
        try {
            DFService.register(this, agentDescription);
            System.out.println("manager registered. aid is: " + getAID());
        } catch (FIPAException e) {
            e.printStackTrace();
            throw new RuntimeException("error while registering manager agent in DF");
        }

        addBehaviour(new MenuGiverBehaviour());
        addBehaviour(new OrderCreatingBehaviour(this));

    }

    private class MenuGiverBehaviour extends CyclicBehaviour {

        @Override
        public void action() {
            MessageTemplate menuRequestTemplate = MessageTemplate.and(
                    MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                    MessageTemplate.MatchOntology("REQUEST_MENU")
            );

            ACLMessage menuRequest = myAgent.receive(menuRequestTemplate);
            if (menuRequest != null) {
                System.out.println("Manager got menu request");
                ACLMessage menuReply = menuRequest.createReply();
                menuReply.setPerformative(ACLMessage.INFORM);
                menuReply.setContent("burgers: with beef, with fish; drinks: cola, water");
                menuReply.setOntology("SEND_MENU");
                myAgent.send(menuReply);
            } else {
                block();
            }
        }
    }

    private class OrderCreatingBehaviour extends CyclicBehaviour {
        AgentContainer container;

        public OrderCreatingBehaviour(Agent a) {
            super(a);
            // get a container controller for creating new agents
            container = getContainerController();
        }

        @Override
        public void action() {
            MessageTemplate createOrderTemplate = MessageTemplate.and(
                    MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                    MessageTemplate.MatchOntology("CREATE_ORDER")
            );

            ACLMessage createOrderRequest = myAgent.receive(createOrderTemplate);
            if (createOrderRequest != null) {
                // TODO:
                // 0. make mock request from visitor after menu getting
                // 1. learn how to send messages with structure (ontology)
                // 2. create structure for menu sending
                // 3. create structure for order creating request
                // 4.

                AgentController orderAgent;
                String agentName;
                try {
                    orderAgent = container.createNewAgent("order for " + createOrderRequest.getSender().getName(), "org.example.OrderAgent", null);
                    orderAgent.start();
                    agentName = orderAgent.getName();
                } catch (StaleProxyException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Failed to create new order agent");
                }

                ACLMessage confirmOrderCreationReply = createOrderRequest.createReply();
                confirmOrderCreationReply.setPerformative(ACLMessage.INFORM);
                confirmOrderCreationReply.setContent("this is your order:" + agentName);
                confirmOrderCreationReply.setOntology("CONFIRM_ORDER_CREATION");
                myAgent.send(confirmOrderCreationReply);

            } else {
                block();
            }
        }
    }
}
