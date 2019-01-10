package aiAula3;

import jade.core.Runtime;

import java.util.concurrent.TimeUnit;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class MainContainer {

	Runtime rt;
	ContainerController container;
	protected static int numCust = 1;

	public void initMainContainerInPlatform(String host, String port, String containerName) {

		//Get the JADE runtime interface (singleton)
		this.rt = Runtime.instance();
		
		//Create a Profile, where the launch arguments are stored
		Profile prof = new ProfileImpl();
		prof.setParameter(Profile.CONTAINER_NAME, containerName);
		prof.setParameter(Profile.MAIN_HOST, host);
		prof.setParameter(Profile.MAIN_PORT, port);
		prof.setParameter(Profile.MAIN, "true");
		prof.setParameter(Profile.GUI, "true");
		
		//create a main agent container
		this.container = rt.createMainContainer(prof);
		rt.setCloseVM(true);
	}
	
	public void startAgentInPlatform(String name, String classpath){
	    try {
	       AgentController ac = container.createNewAgent(
	  name, 
	  classpath, 
	  new Object[0]);
	       ac.start();
	    } catch (Exception e) {
	       e.printStackTrace();
	    }
	 }
	
	
	public static void main(String[] args) {
		MainContainer a = new MainContainer();
		
		a.initMainContainerInPlatform("localhost","9888","MainContainer");
		a.startAgentInPlatform("Manager", "aiAula3.Manager");
		for (int i = 1; i <=3; i++)
			a.startAgentInPlatform("Taxi" + i, "aiAula3.Taxi");
		a.startAgentInPlatform("Stats", "aiAula3.Stats");
		while(true) {
			System.out.println("Criado customer " + numCust);
			a.startAgentInPlatform("Customer "+ numCust, "aiAula3.Customer");
			numCust++;
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}