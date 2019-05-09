package escape_environment;

import javafx.application.Application;
import javafx.stage.Stage;

public class FXViewer extends Application implements Viewer {
	public FXViewer() {
		super();
	}
	
	public static void main(String[] args) 
    {
        launch(args);
    }
 
    public void start(Stage theStage) 
    {
        theStage.setTitle("Hello, World!");
        theStage.show();
    }
    
    // ======= Viewer implementation =======
    public void display(int[] coord_actuator, int[] coord_runner, int runner_room, int switch_on, int score) {
    	
    }
    
    public void getCodeAnimation(int[] coord_actuator, int[] coord_runner, int runner_room) {
    	
    }
    
    public void pressButtonAnimation(int[] coord_actuator, int[] coord_runner, int runner_room, int num_button, boolean good_button) {
    	
    }
    
    public void openDoorAnimation(int[] coord_actuator, int[] coord_runner, int runner_room, int num_door, boolean good_door) {
    	
    }
    
    public void changeRoomAnimation(int[] coord_actuator, int[] coord_runner, int runner_room, int num_door) {
    	
    }
    
    public void pause(long time) {
    	
    }
    
    public void setScore(int score) {
    	
    }
}
