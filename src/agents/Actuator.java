package agents;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import escape_environment.Environment;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import neural_network.NeuralNetwork;
import neural_network.Np;

public class Actuator extends Agent {
	
	// States name
	private static final String STATE_A = "WAIT_CODE";
	private static final String STATE_B = "DECIPHER";
	private static final String STATE_C = "PRESS_BUTTON";
	private static final String STATE_D = "SEND_DOOR_NUMBER";
	
	private String runner_agent_name;
	private Environment env;
	private boolean verbose;
	
	private int code_size = 1;
	private double[][] code = new double[code_size][code_size];
	private int num_button = 0;
	private int num_door = 0;
	private int num_try = 0;
	private int hidden_dim;
	private int button_dim = 5;
	
	private double epsilon = 0.05; //1.0;				
	private double epsilon_min = 0.05;
	private double epsilon_decay = 0.9;
	
	private Set<Integer> already_pushed_button = new HashSet<>();
	
	private NeuralNetwork button_neural_network;
	
	protected void setup() {
		// Process parameters
		Object args[] = getArguments();
		runner_agent_name = args[0].toString();
		env = (Environment)args[1];
		code_size = (int)args[2];
		verbose = (boolean)args[3];
		hidden_dim = (int)args[4];
		
		code = new double[code_size][code_size];
		
		button_neural_network = new NeuralNetwork(code_size*code_size, hidden_dim, button_dim);
		//button_neural_network.generate_memory_unique();
		button_neural_network.generate_memory(50000);
		
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
		ACLMessage msg = receive();
		if(msg == null) {
			doWait();
			msg = receive();
		}
		String content = "";
		if(msg != null)
			content = msg.getContent();
		return content;
	}
	
	private boolean choose_to_explore() {
		return Math.random() < epsilon;
		//return false;
	}

	private int decipher_button(double[][] code) {
		int num_button = 0;
		
		int cpt = 0;
		
		do {
			if(cpt < 5) {
				code = Np.code_to_input(code, code_size);
				num_button = this.button_neural_network.forward_argmax(code);
				//System.out.println(num_button);
				
				if(already_pushed_button.contains(num_button)) {				
					for(int i = 0 ; i < 512 ; ++i)
						this.button_neural_network.run_mini_batch();
				}
				
				cpt++;	
			}
			else {
				num_button = (int) (Math.random() * (button_dim));
			}
		}while(already_pushed_button.contains(num_button));
		already_pushed_button.add(num_button);		
		
		for(int i = 0 ; i < 512 ; ++i)
			this.button_neural_network.run_mini_batch();
		
		//this.button_neural_network.generate_memory(50000);
		
		epsilon = Math.max(epsilon*epsilon_decay, epsilon_min);
		
		num_try++;
		
		return num_button;
	}
	
	private int decipher_door(double[][] code) {
		int num_door = 0;
		//int num_door = (int) (Math.random() * (2));
		
		num_button = this.button_neural_network.forward_argmax(code);
		
		return num_door;
	}
	
	private void add_to_memory(double[][] code, int num_button) {
		double[] code_f = Np.flatten(code);
		
		double[] y = new double[button_dim];
		y[num_button] = 1;
		this.button_neural_network.add_to_memory(code_f, y);
	}
	
	/* ==================
	   ===== STATES =====
	   ================== */
	private class WaitCode extends OneShotBehaviour {
		public void action() {
			if(verbose) System.out.println(getName()+" - "+STATE_A);
			
			// Action
			String msg_code = receiveMessage();
			if(verbose) System.out.println("code "+msg_code);
			String[] split_code = msg_code.split(",");
			// Parse code
			int i = 0;
			int j = 0;
			
			for(String c : split_code) {
				code[i][j] = Double.parseDouble(c);
				j += 1;
				if(j >= code_size){
					i = (i + 1) % code_size;
					if(i >= code_size)
						break;
					j = 0;
				}
			}
			
			num_try = 0;
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
			if(exit_value == 1) {
				num_door = decipher_door(code);
				add_to_memory(code, num_button);
			}
		}
		
		public int onEnd() {
			return exit_value;
		}
	}
	
	private class SendDoorNumber extends OneShotBehaviour {
		public void action() {
			if(verbose) System.out.println(getName()+" - "+STATE_D);
			
			BufferedWriter writer;
			try {
				writer = new BufferedWriter(new FileWriter("./samplefile.txt", true));
				writer.newLine();   //Add new line
				writer.write(Integer.toString(num_try));
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			//System.out.println(num_try);
			
			// Action			
			String string_door = String.valueOf(num_door);
			sendMessage(runner_agent_name, string_door);
		}		
	}
	
}
