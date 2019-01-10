package aiAula3;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class Taxi extends Agent {
	private Position pos;
	
	protected void setup() {
		
		DFAgentDescription template = new DFAgentDescription();
		template.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Taxi");
		sd.setName("Taxi-is-available");
		template.addServices(sd);
		try {
			DFService.register(this, template);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pos = new Position();
		
		addBehaviour(new CyclicBehaviour(this) {
			
			@Override
			public void action() {
				ACLMessage msg = receive();
				if (msg!=null) {
					if (msg.getPerformative() == 16) {
						ACLMessage answer = new ACLMessage(ACLMessage.CONFIRM);
						answer.addReceiver(new AID("Manager", AID.ISLOCALNAME));
						String str = myAgent.getLocalName() + " -> Estou na posição: " + pos.toString();
						answer.setContent(str);
						send(answer);
						System.out.println(str);
					}
					if (msg.getPerformative() == 11) {
						String[] parts = msg.getContent().split(" ");
						pos = new Position(Integer.parseInt(parts[parts.length-1]), Integer.parseInt(parts[parts.length-2]));
						ACLMessage answer = new ACLMessage(ACLMessage.REQUEST);
						answer.addReceiver(new AID(parts[0]+" "+parts[1], AID.ISLOCALNAME));
						answer.setContent("Destiny?");
						send(answer);
						System.out.println(myAgent.getLocalName() + " -> " + "Destiny?");
					}
					if (msg.getPerformative() == 7) {
						String[] parts = msg.getContent().split(" ");
						pos = new Position(Integer.parseInt(parts[parts.length-1]), Integer.parseInt(parts[parts.length-2]));
						System.out.println("Taxi reached destiny");
					}
				}
				block();
			}
		});
	}
	
	public void takeDown() {
		super.takeDown();
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
