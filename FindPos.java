package aiAula3;

import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

public class FindPos extends SimpleBehaviour {
	
	private AID taxi;
	private boolean done = false;
	
	public FindPos(AID taxi) {
		this.taxi = taxi;
	}

	@Override
	public void action() {
		ACLMessage answer = new ACLMessage(ACLMessage.REQUEST);
		answer.setContent("Can you tell me your position?");
		answer.addReceiver(taxi);
		myAgent.send(answer);
		System.out.println(taxi.getLocalName()+", can you tell me your position?");
		done = true;
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return done;
	}
}
