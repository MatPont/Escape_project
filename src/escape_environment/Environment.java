package escape_environment;

public class Environment {
	
	boolean use_FX_Viewer;
	Viewer viewer;
	
	int[] coord_actuator;
	int[] coord_runner;
	
	int runner_room;
	int switch_on;
	
	public Environment(boolean use_FX_Viewer) {
		this.use_FX_Viewer = use_FX_Viewer;
		if(use_FX_Viewer)
			this.viewer = new FXViewer();
		else
			this.viewer = new SimpleViewer();
		
		/*this.coord_actuator[0] = 0;
		this.coord_actuator[0] = 0;*/
		
		this.runner_room = 0;
		this.switch_on = -1;
	}
	
	public void display() {
		this.viewer.display(coord_actuator, coord_runner, runner_room, switch_on);
		this.runner_room = 1;
		this.viewer.display(coord_actuator, coord_runner, runner_room, switch_on);
	}
}