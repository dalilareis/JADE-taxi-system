package aiAula3;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class Manager extends Agent {
	protected static AID[] taxiAgents;
	private int[] count = {0,0,0}; //count[0] -> nº vezes taxi1 foi escolhido
	private double[] dist = {0,0,0}; //dist[0] -> distância percorrida pelo taxi1
	private String customerActive;
	private int answers = 0;
	private static Position customerPos = null;
	private static Position customerDest = null;
	protected static Position taxi1Pos;
	protected static Position taxi2Pos;
	protected static Position taxi3Pos;
	
	protected void setup() {
		super.setup();
		System.out.println("Manager ready");
		
		addBehaviour(new CyclicBehaviour(this) {
			
			@Override
			public void action() {
				ACLMessage msg = receive();
				if (msg!=null) {
					
					if (msg.getPerformative() == 7) {
					
						DFAgentDescription template = new DFAgentDescription();
						ServiceDescription sd = new ServiceDescription();
						sd.setType("Taxi");
						template.addServices(sd);
						DFAgentDescription[] result;
						try {
							result = DFService.search(myAgent, template);
							Manager.taxiAgents = new AID[result.length];
							for (int i=0; i < result.length; i++) {
								Manager.taxiAgents[i] = result[i].getName();
							}
						} catch (FIPAException e) {
							e.printStackTrace();
						}
						
						System.out.println("Taxis available:");
						for(int i=0; i < taxiAgents.length; i++)
							System.out.println(taxiAgents[i].getLocalName());
						
						customerActive = msg.getSender().getLocalName();
						String[] parts = msg.getContent().split(" ");
						int f1 = Integer.parseInt(parts[6]);
						int f2 = Integer.parseInt(parts[7]);
						int x = Integer.parseInt(parts[parts.length-2]);
						int y = Integer.parseInt(parts[parts.length-1]);
						customerPos = new Position(x,y);
						customerDest = new Position(f1,f2);
						
						ParallelBehaviour pb = new ParallelBehaviour(myAgent, ParallelBehaviour.WHEN_ALL);		
						addBehaviour(pb);
						for (int i = 0; i < taxiAgents.length; i++)
							pb.addSubBehaviour(new FindPos(taxiAgents[i]));
						System.out.println("All taxis have been informed");
					}
					
					if (msg.getPerformative() == 4) {
						String[] parts = msg.getContent().split(" ");
						int x = Integer.parseInt(parts[parts.length-1]);
						int y = Integer.parseInt(parts[parts.length-2]);
						switch (parts[0]) {
							case "Taxi1": taxi1Pos = new Position(x,y);
								break;
							case "Taxi2": taxi2Pos = new Position(x,y);
							 	break;
							case "Taxi3": taxi3Pos = new Position(x,y);
							 	break;
						}
						answers++;
						if (answers == 3) {
							ACLMessage chooseTaxi = new ACLMessage(ACLMessage.PROPOSE);
							int i = Manager.closestTaxi();
							chooseTaxi.addReceiver(new AID("Taxi"+i, AID.ISLOCALNAME));
							chooseTaxi.setContent(customerActive + " at " + customerPos.toString());
							send(chooseTaxi);
							count[i-1]++;
							dist[i-1] += customerDest.calcDist(customerPos);
							System.out.println("Táxi escolhido: " + i + " -> " + customerActive + " at " + customerPos.toString());
							customerPos = null;
							answers = 0;
						}
					}
					
					if (msg.getPerformative() == 1) {
						for (int i = 1; i <= 3; i++) {
							System.out.println("Taxi"+i+" travelled " + dist[i-1] + "km and was selected " + count[i-1] +  " times");
						}
					}
				}
			}
		});
	}
	
	// CÓDIGO À PADEIRO, SÓ PARA DESENRASCAR
		public static int closestTaxi() {
			double menor;
			int taxi;
			double dist = customerPos.calcDist(taxi1Pos);
			menor = dist;
			taxi = 1;
			dist = customerPos.calcDist(taxi2Pos);
			if (dist < menor) {
				menor = dist;
				taxi = 2;
			}
			dist = customerPos.calcDist(taxi3Pos);
			if (dist < menor) {
				menor = dist;
				taxi = 3;
			}
			return taxi;
		}
}
