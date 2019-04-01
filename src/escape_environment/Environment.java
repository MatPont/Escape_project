package escape_environment;

public class Environment {
	
	boolean use_FX_Viewer;
	Viewer viewer;
	
	int[] coord_actuator = new int[2];
	int[] coord_runner = new int[2];
	
	int runner_room;
	int switch_on;
	
	public Environment(boolean use_FX_Viewer) {
		this.use_FX_Viewer = use_FX_Viewer;
		if(use_FX_Viewer)
			this.viewer = new FXViewer();
		else
			this.viewer = new SimpleViewer();
		
		this.coord_actuator[0] = 3;
		this.coord_actuator[1] = 3;
		
		this.coord_runner[0] = 2;
		this.coord_runner[1] = 9;
		
		this.runner_room = 0;
		this.switch_on = -1;
	}
	
	public void display() {
		this.viewer.display(coord_actuator, coord_runner, runner_room, switch_on);
	}
}