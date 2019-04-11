package escape_environment;

public class Environment {
	
	boolean use_FX_Viewer;
	Viewer viewer;
	
	int[] coord_actuator = new int[2];
	int[] coord_runner = new int[2];
	
	int runner_room;
	int num_button;
	int code_size;
	
	public Environment(boolean use_FX_Viewer, int code_size) {
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
	}
	
	public void init_coord_runner() {
		this.coord_runner[0] = 2;
		this.coord_runner[1] = 9;
	}
	
	public int pressButton(int num_button) {
		int result = 1;
		
		this.num_button = num_button;
		viewer.pressButtonAnimation(coord_actuator, coord_runner, runner_room, num_button);
		
		return result;
	}
	
	public int[][] getCode(){
		int[][] code = new int[code_size][code_size];
		
		viewer.getCodeAnimation(coord_actuator, coord_runner, runner_room);
		
		return code;
	}
	
	public int openDoor(int num_door) {
		int result = 1;
		
		viewer.openDoorAnimation(coord_actuator, coord_runner, runner_room, num_door);
		
		if(result == 1) {
			runner_room = (runner_room + 1) % 2;
			viewer.changeRoomAnimation(coord_actuator, coord_runner, runner_room, num_door);
		} else {
			runner_room = 0;
			this.init_coord_runner();
		}
		
		return result;
	}
	
	public void display() {
		this.viewer.display(coord_actuator, coord_runner, runner_room, num_button);
	}
}