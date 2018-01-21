package rshelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import mvcview.RSView;
/**
 * This class is only to append Strings to the logging textarea in the view with the current timestamp
 *
 */
public class Logger {

	
	RSView view;
	
	public void setView(RSView view){
		this.view = view;
	}
	public void log(int i, String a){
		if(i == 0){
			view.kod_log_textarea.append("\n" +getCurrentTimeStamp() + "  ");
			view.kod_log_textarea.append(a);
			view.kod_log_textarea.setCaretPosition(view.kod_log_textarea.getDocument().getLength());

		}
		
		else{
			view.dek_log_textarea.append("\n" +getCurrentTimeStamp() + "  ");
			view.dek_log_textarea.append(a);
			view.dek_log_textarea.setCaretPosition(view.dek_log_textarea.getDocument().getLength());

		}
		
	}
	
	public static String getCurrentTimeStamp() {
	    //return new SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(new Date());
	    return new SimpleDateFormat("HH:mm:ss").format(new Date());
	}
	
}
