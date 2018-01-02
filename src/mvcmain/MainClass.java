package mvcmain;
import java.io.IOException;

import mvccontroller.RSController;

public class MainClass {

	static RSController controller; 
	
	public static void main(String[] args) throws IOException{
		
		 controller = new RSController();
		 controller.showView();	
		 System.out.println("Controller started");
	}
}