package mvcmain;
import java.io.IOException;

import mvccontroller.RSController;


/**
 * MainClass startet das Reed-Solomon-Tool mit einem Controller und einer View
 * 
 * MainClass starts the Reed-Solomon tool with a controller and a view
 */
public class MainClass {

	static RSController controller; 
	
	public static void main(String[] args) throws IOException{
		
		 controller = new RSController();
		 controller.showView();	
		 System.out.println("Controller started");
	}
}