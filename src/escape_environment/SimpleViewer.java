package escape_environment;

import java.io.IOException;

public class SimpleViewer implements Viewer {
	private final static String switch_on = "¤";
	private final static String switch_off = "o";
	private final static String agent = "X";
	private final static String wall = "#";
	private final static String door = "H";
	private final static String empty = " ";
	private final static String code = "C";
	
	private final static long pause_time = 250;
	
	public SimpleViewer() {
		super();
	}
	
	public static void clearScreen() {  
		for(int i = 0 ; i < 50 ; ++i)
			System.out.println();
	} 
	
	public void pause(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void display_message_pause(int[] coord_actuator, int[] coord_runner, int runner_room, int num_button, String message, long pause) {
		this.display(coord_actuator, coord_runner, runner_room, num_button);
    	System.out.println(message);
    	this.pause(pause);
	}
	
	// ======= Viewer implementation =======
    public void display(int[] coord_actuator, int[] coord_runner, int runner_room, int num_button) {
    	int num_row = 5;
    	
    	this.clearScreen();
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
    				lines[i] += (j == num_button) ? switch_on : switch_off;
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
    
    public void getCodeAnimation(int[] coord_actuator, int[] coord_runner, int runner_room) {
    	coord_runner[1] += (runner_room == 0) ? 1 : -1;
    	this.display_message_pause(coord_actuator, coord_runner, runner_room, -1, "the code was obtained by the Runner!", pause_time);
    	coord_runner[1] += (runner_room == 0) ? -1 : 1;
    	this.display(coord_actuator, coord_runner, runner_room, -1);
    }
    
    public void pressButtonAnimation(int[] coord_actuator, int[] coord_runner, int runner_room, int num_button, boolean good_button) {
    	String message = "";
    	switch(num_button) {
    		case 0:
    			coord_actuator[1] -= 1;
    			break;
    		case 1:
    			coord_actuator[1] -= 1;
    			break;
    		case 4:
    			coord_actuator[1] += 1;
    			break;
    		case 3:
    			coord_actuator[1] += 1;
    			break;
    	}
    	this.display_message_pause(coord_actuator, coord_runner, runner_room, -1, "", pause_time);
		
		switch(num_button) {
			case 0:
				coord_actuator[1] -= 1;
				break;
			case 4:
				coord_actuator[1] += 1;
				break;
		}
		this.display_message_pause(coord_actuator, coord_runner, runner_room, -1, "", pause_time);
		
		coord_actuator[0] -= 1;
		message = "the button "+num_button+" was pushed by the Actuator!";
		this.display_message_pause(coord_actuator, coord_runner, runner_room, num_button, message, pause_time);
		
		coord_actuator[0] += 1;
		this.display_message_pause(coord_actuator, coord_runner, runner_room, -1, "", pause_time);
		
		// Return to start position
		switch(num_button) {
			case 0:
				coord_actuator[1] += 1;
				break;
			case 1:
				coord_actuator[1] += 1;
				break;
			case 4:
				coord_actuator[1] -= 1;
				break;
			case 3:
				coord_actuator[1] -= 1;
				break;
		}
		this.display_message_pause(coord_actuator, coord_runner, runner_room, -1, "", pause_time);
		
		switch(num_button) {
			case 0:
				coord_actuator[1] += 1;
				break;
			case 4:
				coord_actuator[1] -= 1;
				break;
		}
		
		message = (good_button) ? "Correct button!" : "Wrong button!";
		
		this.display_message_pause(coord_actuator, coord_runner, runner_room, -1, message, pause_time);
    }
    
    public void openDoorAnimation(int[] coord_actuator, int[] coord_runner, int runner_room, int num_door, boolean good_door) {
    	switch(num_door) {
			case 0:
				coord_runner[0] -= 1;
				break;
			case 1:
				coord_runner[0] += 1;
				break;
    	}
    	this.display_message_pause(coord_actuator, coord_runner, runner_room, -1, "", pause_time);
    	
    	coord_runner[1] += (runner_room == 0) ? 1 : -1;
		this.display_message_pause(coord_actuator, coord_runner, runner_room, -1, "", pause_time);
		
		coord_runner[1] += (runner_room == 0) ? 1 : -1;
		this.display_message_pause(coord_actuator, coord_runner, runner_room, -1, "Runner open the door "+num_door, pause_time);
		
		if(!good_door)
			this.display_message_pause(coord_actuator, coord_runner, runner_room, -1, "This was the wrong door, runner agent died!", pause_time*3);
    }
    
    public void changeRoomAnimation(int[] coord_actuator, int[] coord_runner, int runner_room, int num_door) {
    	coord_runner[1] += (runner_room == 0) ? -1 : 1;
		this.display_message_pause(coord_actuator, coord_runner, runner_room, -1, "", pause_time);
		
		coord_runner[1] += (runner_room == 0) ? -1 : 1;
		this.display_message_pause(coord_actuator, coord_runner, runner_room, -1, "", pause_time);
		
		switch(num_door) {
		case 0:
			coord_runner[0] += 1;
			break;
		case 1:
			coord_runner[0] -= 1;
			break;
		}
		this.display_message_pause(coord_actuator, coord_runner, runner_room, -1, "", pause_time);
    }
}
