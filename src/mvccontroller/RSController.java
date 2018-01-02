package mvccontroller;

import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import rshelper.Logger;
import mvcmodel.RSModel;
import mvcview.RSView;

/**
 * Der Controller muss beide die View und das Model kennen,
 * da dieser für die Kommunikation zwischen den Beiden sorgt
 */
public class RSController {

    private RSView _view;
    private RSModel _model;
    private Logger _logger;

    public RSController(){
        this._logger = new Logger();

        this._model = new RSModel(this._logger);
        this._view = new RSView(this._logger);
        this._logger.setView(this._view);

        addListener();
    }

    public void showView(){
        this._view.setVisible(true);
    }
    
/**
     * Die Listener, die wir aus den Internen Klassen generieren werden der View bekannt gemacht, sodass diese mit
     * uns (dem Controller) kommunizieren kann
     */
    private void addListener(){
       this._view.setStartCodingListener(new StartCodingListener());
       this._view.setStartDecodingListener(new StartDecodingListener());
       this._view.setResetFormListener(new ResetFormListener());
       this._view.setComboBoxListener(new ComboBoxListener());

    }

    /**
     * Inneren Listener Klassen implementieren das Interface ActionListener
     *
     * 1: Hier werden erst die eingegebenen Werte aus der View geholt
     * 2: Die Werte werden dem Model übergeben und die Codierung bzw. Decodierung berechnet
     * 3: Die berechneten Werte werden aus dem Model geladen und
     * 4: Wieder der View zum Darstellen übergeben
     *
     * TODO: Fehlerprüfung muss noch implementeirt werden
     */
    class StartCodingListener implements ActionListener{
    	@Override
        public void actionPerformed(ActionEvent e) {
        	int [] fieldValues = _view.getFieldValues();
        	String codingStyle = _view.getCodingStyle();
        	int [] m = _view.getMessage();

        	
        	_model.starteCodierung(fieldValues, codingStyle, m);
        	int[] codearray = _model.getCode();
        	String code = ""; 
        	
        	for(int i = 0; i < codearray.length; i++){
        		code = code+codearray[codearray.length-1-i] + ",";
        	}
        	
        	_view.setCode(code);
        }
    }

    /**
     * TODO
     * Hier wird dem View und dem Model gesagt ihre gespeicherten
     * Werte zu löschen.
     */
    class ResetFormListener implements ActionListener{
    	@Override
        public void actionPerformed(ActionEvent e) {
            _view.resetView();
            _model.zurückSetzen();
        }
    }
    
    
    class StartDecodingListener implements ActionListener{
    	@Override
        public void actionPerformed(ActionEvent e) {
        	int [] fieldValues = _view.getFieldValues();
        	String decodingStyle = _view.getDecodingStyle();
        	int [] c = _view.getCode();

        	
        	_model.starteDecodierung(fieldValues, decodingStyle, c);
        	int[] messagearray = _model.getMessage();
        	String message = ""; 
        	
        	for(int i =0; i < messagearray.length; i++){
        		message = message + messagearray[messagearray.length-1-i] + ",";
        	}
                	
        	
        	_view.setMessage(message);
        }
    }
    
    class ComboBoxListener implements ItemListener{

		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			ItemSelectable is = e.getItemSelectable();
			System.out.println(" item changed: " + is.getSelectedObjects()[0]);

			String selected = (String) is.getSelectedObjects()[0];
			
			if(selected=="benutzerdefiniert"){
				System.out.println(" hgfcgfcbvn");
				_view.changeGaloisFields("3","13","7","3");

			}
			else if(selected=="2D Barcodes"){
				System.out.println(" ssssss");
				_view.changeGaloisFields("4","19","10","4");

			}

			else if(selected=="MIDS"){
				System.out.println(" jjjjjj");
				_view.changeGaloisFields("5","37","31","15");

			}
			else if(selected=="CD"){
				System.out.println(" 4446ztz8");
				_view.changeGaloisFields("3","13","7","3");

			}
			else if(selected=="Voyager"){
				System.out.println(" bbbbbbb");
				_view.changeGaloisFields("3","13","7","3");

			}
			
		}
    	
    }
}