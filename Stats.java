package aiAula3;

import java.util.concurrent.TimeUnit;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class Stats extends Agent {
	
	public void setup() {
		super.setup();
		
		addBehaviour(new CyclicBehaviour(this) {

			@Override
			public void action() {
				// TODO Auto-generated method stub
				try {
					TimeUnit.SECONDS.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ACLMessage msg = new ACLMessage(ACLMessage.AGREE);
				msg.addReceiver(new AID("Manager", AID.ISLOCALNAME));
				msg.setContent("stats pls");
				send(msg);
			}
			
		});
	}
}
