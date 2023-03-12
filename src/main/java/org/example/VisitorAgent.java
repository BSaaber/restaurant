package org.example;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class VisitorAgent extends Agent {
    public String name;
    public AID[] orders;
    public AID manager;

    protected void setup() {

        DFAgentDescription managerAgentTemplateDescription = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("manager-service-type");
        sd.setName("manager-service-name");
        managerAgentTemplateDescription.addServices(sd);
        try {
            DFAgentDescription[] result = DFService.search(this, managerAgentTemplateDescription);
            manager = result[0].getName();
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
        System.out.println("visitor created");
        addBehaviour(new RequestMenuBehaviour());
    }

    public class RequestMenuBehaviour extends Behaviour {
        private int step = 0;

        @Override
        public void action() {
            switch (step) {
                case 0:
                    ACLMessage menuRequest = new ACLMessage(ACLMessage.REQUEST);
                    menuRequest.addReceiver(manager);
                    menuRequest.setOntology("REQUEST_MENU");
                    menuRequest.setReplyWith("replymessage " + System.currentTimeMillis());
                    myAgent.send(menuRequest);
                    System.out.println("visitor sent menu request");
                    step++;
                    break;
                case 1:
                    MessageTemplate sendMenuTemplate = MessageTemplate.and(
                            MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                            MessageTemplate.MatchOntology("SEND_MENU")
                    );
                    ACLMessage reply = myAgent.receive(sendMenuTemplate);
                    if (reply != null) {
                        System.out.println("Visitor got menu: " + reply.getContent());
                        step++;
                    } else {
                        block();
                    }
                    break;
                case 2: // todo move to its own behaviour later
                    ACLMessage createOrderRequest = new ACLMessage(ACLMessage.REQUEST);
                    createOrderRequest.addReceiver(manager);
                    createOrderRequest.setOntology("CREATE_ORDER");
                    createOrderRequest.setReplyWith("replymessage " + System.currentTimeMillis());
                    myAgent.send(createOrderRequest);
                    step++;
                    System.out.println("visitor requested for order");
                    break;
                case 3:
                    MessageTemplate getConfirmTemplate = MessageTemplate.and(
                            MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                            MessageTemplate.MatchOntology("CONFIRM_ORDER_CREATION")
                    );
                    ACLMessage confirmReply = myAgent.receive(getConfirmTemplate);
                    if (confirmReply != null) {
                        System.out.println("Visitor: i ordered: " + confirmReply.getContent());
                        step++;
                    } else {
                        block();
                    }
                    break;
            }

        }

        @Override
        public boolean done() {
            return step == 4;
        }
    }
}
