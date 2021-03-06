package mvccontroller;

import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Random;

import rshelper.Logger;
import mvcmodel.RSModel;
import mvcview.RSView;

/**
 * Der Controller muss die View und das Model kennen, da dieser für die
 * Kommunikation zwischen den beiden sorgt
 */
public class RSController {

	private RSView _view;
	private RSModel _model;
	private Logger _logger;

	public RSController() {
		this._logger = new Logger();

		this._model = new RSModel(this._logger);
		this._view = new RSView(this._logger);
		this._logger.setView(this._view);

		addListener();
	}

	public void showView() {
		this._view.setVisible(true);
	}

	/**
	 * Die Listener, die wir aus den Internen Klassen generieren, werden der View
	 * bekannt gemacht, sodass diese mit uns (dem Controller) kommunizieren kann
	 */
	private void addListener() {
		this._view.setStartCodingListener(new StartCodingListener());
		this._view.setStartDecodingListener(new StartDecodingListener());
		this._view.setResetFormListener(new ResetFormListener());
		this._view.setComboBoxListener(new ComboBoxListener());
		this._view.setRandButtonListener(new RandButtonListener());

	}

	/**
	 * Inneren Listener Klassen implementieren das Interface ActionListener
	 *
	 * 1: Hier werden erst die eingegebenen Werte aus der View geholt 
	 * 2: Die Werte werden dem Model übergeben und die Codierung bzw. Decodierung berechnet 
	 * 3: Die berechneten Werte werden aus dem Model geladen und 
	 * 4: Wieder der View zum Darstellen übergeben
	 *
	 */
	class StartCodingListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int[] fieldValues = _view.getFieldValues();
			String codingStyle = _view.getCodingStyle();
			int[] m = _view.getMessage();

			_model.starteCodierung(fieldValues, codingStyle, m);
			int[] codearray = _model.getCode();
			String code = "";

			for (int i = 0; i < codearray.length; i++) {
				code = code + codearray[codearray.length - 1 - i] + ",";
			}

			_view.setCode(code);
		}
	}

	/**
	 * Hier wird der View und dem Model gesagt ihre gespeicherten Werte zu
	 * löschen.
	 */
	class ResetFormListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// _view.resetView();
			//	_model.zurückSetzen();
		}
	}

	/**
	 * 
	 * Hier wird die Decodierung gestartet. Dabei werden die Werte aus der View an das Model weitergegeben.
	 * Nach der Decodierung werden diese aus dem Model geholt und in der View angezeigt. 
	 *
	 */
	
	class StartDecodingListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int[] fieldValues = _view.getFieldValues();
			String decodingStyle = _view.getDecodingStyle();
			int[] c = _view.getCode();
			String codingStyle = _view.getCodingStyle();
			_model.starteDecodierung(fieldValues, decodingStyle, c, codingStyle);
			int[] messagearray = _model.getMessage();
			String message = "";

			for (int i = 0; i < messagearray.length; i++) {
				message = message + messagearray[messagearray.length - 1 - i]
						+ ",";
			}

			_view.setMessage(message);
		}
	}
	
	
	/**
	 * 
	 * Bei Klick auf den Random-Button wird hier eine zufällige Nachricht generiert und in der View angezeigt.
	 * Diese Nachricht hat die Länge k, die auch in der View festgelegt wurde.
	 *
	 */
	class RandButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int[] fieldValues = _view.getFieldValues();
			int n = (int) (Math.pow(fieldValues[0], fieldValues[1])); // p^m
			int[] messagearray = new int[fieldValues[5]]; // k
			String message = "" + newRand(n);
			for (int i = 1; i < messagearray.length; i++) {
				message = message + "," + newRand(n) ;
			}
			_view.setInputMessage(message);
		}
		
		private int newRand(int n){
			int i = 0; 
			i = new Random().nextInt(n);
			return i; 
		}
	}
	

	/**
	 * 
	 * Dieser Listener ist für das Dropdown-Menü zuständig.
	 *
	 */
	class ComboBoxListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			ItemSelectable is = e.getItemSelectable();
//			System.out.println(" item changed: " + is.getSelectedObjects()[0]);

			String selected = (String) is.getSelectedObjects()[0];

			if (selected == "benutzerdefiniert") {
				_view.changeGaloisFields("3", "13", "7", "3");

			} else if (selected == "2D Barcodes") {
				_view.changeGaloisFields("4", "19", "10", "4");

			}

			else if (selected == "MIDS") {
				_view.changeGaloisFields("5", "37", "31", "15");

			} else if (selected == "CD1") {
				_view.changeGaloisFields("8", "285", "28", "24");

			}else if (selected == "CD2") {
				_view.changeGaloisFields("8", "285", "32", "28");
			} 
			else if (selected == "Voyager") {
				_view.changeGaloisFields("8", "285", "255", "223"); // p(x)=285=x^8+x^4+x^3+x^2+1

			}

		}

	}
}