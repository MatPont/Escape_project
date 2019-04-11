package escape_environment;

public interface Viewer {	
	public void display(int[] coord_actuator, int[] coord_runner, int runner_room, int switch_on);
	public void getCodeAnimation(int[] coord_actuator, int[] coord_runner, int runner_room);
	public void pressButtonAnimation(int[] coord_actuator, int[] coord_runner, int runner_room, int num_button, boolean good_button);
	public void openDoorAnimation(int[] coord_actuator, int[] coord_runner, int runner_room, int num_door, boolean good_door);
	public void changeRoomAnimation(int[] coord_actuator, int[] coord_runner, int runner_room, int num_door);
	
	public void pause(long time);
}
