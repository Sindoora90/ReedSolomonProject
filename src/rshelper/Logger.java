package rshelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import mvcview.RSView;
/**
 * Die Logger Klasse erzeugt Logs als Strings und hängt sie an die Textarea auf der rechten Seite der Benutzeroberfläche.
 * Der Benutzer erhält somit Zwischenschritte oder Ergebnisse und Laufzeiten angezeigt. 
 * This class is only to append Strings to the logging textarea in the view with the current timestamp
 *
 */
public class Logger {

	
	RSView view;
	
	/**
	 * Damit der Logger die View kennt und direkt in diese schreiben kann. 
	 * @param view View
	 */
	public void setView(RSView view){
		this.view = view;
	}
	
	/**
	 * Log-Methode, die die Logs als Strings erzeugt und in der View anzeigt
	 * @param i 0 für Codierung, 1 für Decodierung
	 * @param a String, welcher angezeigt werden soll
	 */
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
	
	/**
	 * gibt den aktuellen Zeitstempel zurück
	 * @return timestamp als Datum
	 */
	public static String getCurrentTimeStamp() {
	    //return new SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(new Date());
	    return new SimpleDateFormat("HH:mm:ss").format(new Date());
	}
	
}
