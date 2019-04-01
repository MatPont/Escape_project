package escape_environment;

import java.io.IOException;

public class SimpleViewer implements Viewer {
	private final static String switch_on = "*";
	private final static String switch_off = "o";
	private final static String agent = "x";
	private final static String wall = "#";
	private final static String door = "H";
	private final static String empty = " ";
	private final static String code = "C";
	
	public SimpleViewer() {
		super();
	}
	
	public static void clearScreen() {  
		for(int i = 0 ; i < 50 ; ++i)
			System.out.println();
	} 
	
	// ======= Viewer implementation =======
    public void display(int[] coord_actuator, int[] coord_runner, int runner_room, int switch_on_id) {
    	int num_row = 5;
    	
    	//this.clearScreen();
    	String[] lines = new String[num_row];
    	for(int i = 0 ; i < num_row; ++i) {
			lines[i] = "";
    		if(i == 0) {
    			lines[i] += wall+wall+wall+wall+wall+wall+wall;
				lines[i] += empty;
    			if(runner_room == 1)
    				lines[i] += empty+empty+empty;
    			lines[i] += wall+wall+wall+wall;
    		}else if(i == 1) {
    			lines[i] += wall;
    			for(int j = 0 ; j < 5 ; ++j)
    				lines[i] += (j == switch_on_id) ? switch_on : switch_off;
    			lines[i] += wall;
    			lines[i] += empty;
    			if(runner_room == 0) 
    				lines[i] += wall+empty+empty+door;	
    			else 
    				lines[i] += empty+empty+empty+door+empty+empty+wall;
    		}else if(i == 2) {
    			lines[i] += wall+empty+empty+empty+empty+empty+wall;
    			lines[i] += empty;
    			if(runner_room == 0) 
    				lines[i] += wall+empty+empty+code;	
    			else 
    				lines[i] += empty+empty+empty+code+empty+empty+wall;
    		}else if(i == 3) {
    			lines[i] += wall+empty+empty+empty+empty+empty+wall;
    			lines[i] += empty; 
    			if(runner_room == 0) 
    				lines[i] += wall+empty+empty+door;	
    			else 
    				lines[i] += empty+empty+empty+door+empty+empty+wall;
    		}else if(i == 4) {
    			lines[i] += wall+wall+wall+wall+wall+wall+wall;
				lines[i] += empty;
    			if(runner_room == 1)
    				lines[i] += empty+empty+empty;
    			lines[i] += wall+wall+wall+wall;
    		}
    	}
    	
    	lines[coord_actuator[0]] = lines[coord_actuator[0]].substring(0,coord_actuator[1])+agent+lines[coord_actuator[0]].substring(coord_actuator[1]+1);
    	lines[coord_runner[0]] = lines[coord_runner[0]].substring(0,coord_runner[1])+agent+lines[coord_runner[0]].substring(coord_runner[1]+1);
    	
    	for(int i = 0 ; i < num_row; ++i) {
    		System.out.println(lines[i]);
    	}
    }
}
