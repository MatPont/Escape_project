package agents;

import java.io.IOException;

import escape_environment.Environment;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class Main {

	public static void main(String[] args) {
		Runtime runtime = Runtime.instance();
		Profile config = new ProfileImpl("localhost", 8888, null);
		config.setParameter("gui", "false");
		AgentContainer mc = runtime.createMainContainer(config);
		AgentController actuator, runner;
		
		String actuator_name = "ActuatorAgent";
		String runner_name = "RunnerAgent";
		boolean verbose = false;
		
		int code_size = 4;
		
		Environment env = new Environment(false, code_size);
		env.display();
		try {
			System.in.read();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Object[] actuator_param = {runner_name, env, code_size, verbose};
		Object[] runner_param = {actuator_name, env, code_size, verbose};
		
		try {
			actuator = mc.createNewAgent(actuator_name, Actuator.class.getName(), actuator_param);
			runner = mc.createNewAgent(runner_name, Runner.class.getName(), runner_param);
			
			actuator.start();
			runner.start();
		} catch(StaleProxyException e) {
			
		}
	}
}
