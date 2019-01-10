package aiAula3;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

public class Customer extends Agent {
	private Position src;
	private Position dst;
	private Boolean finish = false;
	
	protected void setup() {
		src = new Position();
		dst = new Position();
		
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		AID dest = new AID("Manager", AID.ISLOCALNAME);
		msg.addReceiver(dest);
		String str = "Need a taxi to go to " + dst.toString() + " and I'm at " + src.toString();
		msg.setContent(str);
		send(msg);
		System.out.println(str);
		
		addBehaviour(new SimpleBehaviour(this) {
	    	public void action() {
	    		//RECEBE A PERGUNTA SOBRE O DESTINO E RESPONDE
		    	ACLMessage answer = receive();
		        if (answer != null) {
		        	String str = dst.toString();
		        	ACLMessage destMsg = new ACLMessage(ACLMessage.INFORM);
		            destMsg.setContent(str);
		            destMsg.addReceiver(new AID(answer.getSender().getLocalName(), AID.ISLOCALNAME));
		            send(destMsg);
		            System.out.println(myAgent.getLocalName() + " -> " + str);
		            finish = true;
		        }
	        }

			@Override
			public boolean done() {
				if (finish)
					return true;
				return false;
			}
		});
	}
}
