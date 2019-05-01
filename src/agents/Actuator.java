package agents;

import java.util.HashSet;
import java.util.Set;

import escape_environment.Environment;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import neural_network.NeuralNetwork;

public class Actuator extends Agent {
	
	// States name
	private static final String STATE_A = "WAIT_CODE";
	private static final String STATE_B = "DECIPHER";
	private static final String STATE_C = "PRESS_BUTTON";
	private static final String STATE_D = "SEND_DOOR_NUMBER";
	
	private String runner_agent_name;
	private Environment env;
	private boolean verbose;
	
	private int code_size = 4;
	private int[][] code = new int[code_size][code_size];
	private int num_button = 0;
	private int num_door = 0;
	
	private Set<Integer> already_pushed_button = new HashSet<>();
	
	private NeuralNetwork neural_network;
	
	protected void setup() {
		// Process parameters
		Object args[] = getArguments();
		runner_agent_name = args[0].toString();
		env = (Environment)args[1];
		code_size = (int)args[2];
		verbose = (boolean)args[3];
		
		code = new int[code_size][code_size];
		
		neural_network = new NeuralNetwork(code_size*code_size, 8, 5);
		
		// Declare FSM Behaviour
		FSMBehaviour fsm = new FSMBehaviour() {
			public int onEnd() {
				System.out.println("ACTUATOR FSM behaviour completed.");
				myAgent.doDelete();
				return super.onEnd();
			}
		};
		
		// Make States
		fsm.registerFirstState(new WaitCode(), STATE_A);
		fsm.registerState(new Decipher(), STATE_B);
		fsm.registerState(new PressButton(), STATE_C);
		fsm.registerState(new SendDoorNumber(), STATE_D);
		
		// Make Transitions
		fsm.registerDefaultTransition(STATE_A, STATE_B);
		fsm.registerDefaultTransition(STATE_B, STATE_C);
		fsm.registerTransition(STATE_C, STATE_B, 0);
		fsm.registerTransition(STATE_C, STATE_D, 1);
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
		doWait();
		ACLMessage msg = receive();
		String content = "";
		if(msg != null)
			content = msg.getContent();
		return content;
	}

	private int decipher_button(int[][] code) {
		int num_button = 0;
		do {
			num_button = (int) (Math.random() * (4));
		}while(already_pushed_button.contains(num_button));
		
		already_pushed_button.add(num_button);
		
		return num_button;
	}
	
	private int decipher_door(int[][] code) {
		//int num_door = 0;
		int num_door = (int) (Math.random() * (2));
		
		return num_door;
	}
	
	/* ==================
	   ===== STATES =====
	   ================== */
	private class WaitCode extends OneShotBehaviour {
		public void action() {
			if(verbose) System.out.println(getName()+" - "+STATE_A);
			
			// Action
			String msg_code = receiveMessage();
			if(verbose) System.out.println("door "+msg_code);
			String[] split_code = msg_code.split(",");
			// Parse code
			int i = 0;
			int j = 0;
			for(String c : split_code) {
				code[i][j] = Integer.parseInt(c);
				j += 1;
				if(j >= code_size){
					i = (i + 1) % code_size;
					if(i >= code_size)
						break;
					j = 0;
				}
			}
			
			already_pushed_button.clear();
		}		
	}
	
	private class Decipher extends OneShotBehaviour {
		public void action() {
			if(verbose) System.out.println(getName()+" - "+STATE_B);
			
			// Action
			num_button = decipher_button(code);
		}
	}
	
	private class PressButton extends OneShotBehaviour {
		private int exit_value;
		
		public void action() {
			if(verbose) System.out.println(getName()+" - "+STATE_C);
			
			// Action
			exit_value = env.pressButton(num_button);
			if(exit_value == 1) 
				num_door = decipher_door(code);
		}
		
		public int onEnd() {
			return exit_value;
		}
	}
	
	private class SendDoorNumber extends OneShotBehaviour {
		public void action() {
			if(verbose) System.out.println(getName()+" - "+STATE_D);
			
			// Action			
			String string_door = String.valueOf(num_door);
			sendMessage(runner_agent_name, string_door);
		}		
	}
	
}
