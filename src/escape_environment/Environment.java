package escape_environment;

import neural_network.NeuralNetwork;
import neural_network.Np;

public class Environment {
	
	private final static int door_reward = 3;
	private final static int button_penality = -1;
	
	private boolean use_FX_Viewer;
	private Viewer viewer;
	
	private int[] coord_actuator = new int[2];
	private int[] coord_runner = new int[2];
	
	private int runner_room;
	private int num_button;
	private int code_size;
	
	private int real_button = 0;
	private int real_door = 0;
	private int hidden_dim;
	private int button_dim = 5;
	private int door_dim = 2;
	
	private int score = 0;
	
	public Environment(boolean use_FX_Viewer, int code_size, int hidden_dim) {
		this.use_FX_Viewer = use_FX_Viewer;
		if(use_FX_Viewer)
			this.viewer = new FXViewer();
		else
			this.viewer = new SimpleViewer();
		
		this.code_size = code_size;
		
		this.coord_actuator[0] = 3;
		this.coord_actuator[1] = 3;
		
		this.init_coord_runner();
		
		this.runner_room = 0;
		this.num_button = -1;
		
		this.hidden_dim = hidden_dim;
	}
	
	public void init_coord_runner() {
		this.coord_runner[0] = 2;
		this.coord_runner[1] = 9;
	}
	
	public int pressButton(int num_button) {
		boolean good_button = (num_button == real_button); 
		int result = good_button ? 1 : 0;
		
		this.num_button = num_button;
		viewer.pressButtonAnimation(coord_actuator, coord_runner, runner_room, num_button, good_button);
		
		if(! good_button)
			this.addScore(button_penality);
		
		return result;
	}
	
	public double[][] getCode(){
		double[][] code = new double[code_size][code_size];
		code = Np.random(code_size, code_size);
		code = Np.multiply(5, code);
		
		//this.real_button = (int) (Math.random() * (4));
		this.real_button = (int)Np.sum(code) % button_dim; 
		
		//this.real_door = (int) (Math.random() * (2));
		//this.real_door = 0;
		this.real_door = (int)Np.sum(code) % door_dim;
		
		viewer.getCodeAnimation(coord_actuator, coord_runner, runner_room);
		
		return code;
	}
	
	public int openDoor(int num_door) {
		boolean good_door = (num_door == real_door);
		int result = (good_door) ? 1 : 0;
		
		viewer.openDoorAnimation(coord_actuator, coord_runner, runner_room, num_door, good_door);
		
		if(result == 1) {
			this.addScore(door_reward);
			runner_room = (runner_room + 1) % 2;
			viewer.changeRoomAnimation(coord_actuator, coord_runner, runner_room, num_door);
		} else {
			score = 0;
			this.viewer.setScore(score);
			runner_room = 0;
			this.init_coord_runner();
			this.display();
			this.viewer.pause(1000);
		}
		
		return result;
	}
	
	public void setScore(int score) {
		this.score = score;
		this.viewer.setScore(score);
	}
	
	public void addScore(int n) {
		score += n;
		this.viewer.setScore(score);
	}
	
	public void subScore(int n) {
		score -= n;
		this.viewer.setScore(score);
	}
	
	public void display() {
		this.viewer.display(coord_actuator, coord_runner, runner_room, num_button, score);
	}
}