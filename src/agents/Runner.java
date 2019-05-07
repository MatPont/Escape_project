package agents;

import java.io.IOException;

import escape_environment.Environment;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class Runner extends Agent {

	// States name
	private static final String STATE_A = "FIND_CODE";
	private static final String STATE_B = "SEND_CODE";
	private static final String STATE_C = "WAIT_DOOR_NUMBER";
	private static final String STATE_D = "OPEN_DOOR";
	
	private String actuator_agent_name;
	private Environment env;
	private boolean verbose;
	
	private int code_size;
	private double[][] code;
	int num_door = 0;
	
	protected void setup() {
		// Process parameters
		Object args[] = getArguments();
		actuator_agent_name = args[0].toString();
		env = (Environment)args[1];
		code_size = (int)args[2];
		verbose = (boolean)args[3];
		
		code = new double[code_size][code_size];
		
		// Declare FSM Behaviour
		FSMBehaviour fsm = new FSMBehaviour() {
			public int onEnd() {
				System.out.println("RUNNER FSM behaviour completed.");
				myAgent.doDelete();
				return super.onEnd();
			}
		};
		
		// Make States
		fsm.registerFirstState(new FindCode(), STATE_A);
		fsm.registerState(new SendCode(), STATE_B);
		fsm.registerState(new WaitDoorNumber(), STATE_C);
		fsm.registerState(new OpenDoor(), STATE_D);
		
		// Make Transitions
		fsm.registerDefaultTransition(STATE_A, STATE_B);
		fsm.registerDefaultTransition(STATE_B, STATE_C);
		fsm.registerDefaultTransition(STATE_C, STATE_D);
		fsm.registerDefaultTransition(STATE_D, STATE_A);
		
		addBehaviour(fsm);
	}	
	
	private void sendMessage(String agent_name, String content) {
		if(verbose) System.out.println("envoi à "+agent_name+" : "+content);
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(new AID(agent_name, AID.ISLOCALNAME));
		msg.setContent(content);
		send(msg);	
	}
	
	private String receiveMessage() {
		//System.out.println("2 - doWait before");
		doWait();
		//System.out.println("2 - doWait after");
		
		/*try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		ACLMessage msg = receive();
		String content = "";
		if(msg != null)
			content = msg.getContent();
		return content;
	}

	/* ==================
	   ===== STATES =====
	   ================== */
	private class FindCode extends OneShotBehaviour {
		public void action() {
			if(verbose) System.out.println(getName()+" - "+STATE_A);
			
			// Action
			code = env.getCode();
		}
	}
	
	private class SendCode extends OneShotBehaviour {
		public void action() {
			if(verbose) System.out.println(getName()+" - "+STATE_B);
			
			// Action			
			String string_code = "";
			for(double[] code_line : code) 
				for(double c : code_line) {
					string_code += c+",";
				}
			
			sendMessage(actuator_agent_name, string_code);
		}
	}
	
	private class WaitDoorNumber extends OneShotBehaviour {
		public void action() {
			if(verbose) System.out.println(getName()+" - "+STATE_C);
			
			// Action
			String msg_door = receiveMessage();
			if(verbose) System.out.println("door "+msg_door);
			num_door = Integer.parseInt(msg_door);
		}
	}
	
	private class OpenDoor extends OneShotBehaviour {
		public void action() {
			if(verbose) System.out.println(getName()+" - "+STATE_D);
			
			// Action			
			int result = env.openDoor(num_door);
			
		}
	}
	
}
