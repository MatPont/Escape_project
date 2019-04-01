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
    public void display(int[] coord_actuator, int[] coord_runner, int runner_room, int switch_on) {
    	
    }
}
