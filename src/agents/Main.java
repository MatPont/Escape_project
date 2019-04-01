package agents;

import escape_environment.Environment;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class Main {

	public static void main(String[] args) {
		/*Runtime runtime = Runtime.instance();
		Profile config = new ProfileImpl("localhost", 8888, null);
		config.setParameter("gui", "true");
		AgentContainer mc = runtime.createMainContainer(config);
		AgentController actuator, runner;*/
		
		Environment env = new Environment(false);
		env.display();
		
		/*try {
			String tab[] = {"string", "bonjour", "iufheui"};
			//ac = mc.createNewAgent("agent1", HelloAgent.class.getName(), (Object[])tab);
			//ac = mc.createNewAgent("agent1", MyAgent.class.getName(), (Object[])tab);
			//ac = mc.createNewAgent("agent1", FSMAgent.class.getName(), (Object[])tab);
			
			a = mc.createNewAgent("AgentA", AgentA.class.getName(), (Object[])tab);
			b = mc.createNewAgent("AgentB", AgentB.class.getName(), (Object[])tab);
			
			a.start();
			b.start();
		} catch(StaleProxyException e) {
			
		}*/
	}
}
