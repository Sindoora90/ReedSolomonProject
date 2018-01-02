package mvcmodel;

import rshelper.Logger;
import rshelper.ReedSolomon;

/**
 * Das Model ist komplett unabhängig von den anderen
 * Klassen und weiß nicht was um ihn herum geschieht.
 * Es ist völlig egal ob man dieses Model aus einem
 * Fenster oder einer Konsolen Eingabe verwendet -
 * beiden würde funktionieren.
 */
public class RSModel {
    
	Logger logger;
    ReedSolomon _RS; 
    int[] _code;
    int[] _message; 
    

    public RSModel(Logger logger){
    	this.logger = logger;
        zurückSetzen();
    }

    // TODO test obs richtig ist
    public void zurückSetzen(){
    	this._code = null;
    	this._message = null;
    }
    
    
    public void starteCodierung(int[] fieldValues, String codingStyle, int[] m){
    	
    	System.out.println("test fieldvalues.length " + fieldValues.length + " codingstyle : " + codingStyle + " nachrichtlength " + m.length);
    	logger.log(0,"\n test fieldvalues.length " + fieldValues.length + " codingstyle : " + codingStyle + " nachrichtlength " + m.length);
//    	this._RS = new ReedSolomon(fieldValues[0], fieldValues[3], fieldValues[2]); // 0 = p primzahl, 1 = n, 2 = k, 3 = p(x) irreduzpoly
    	this._RS = new ReedSolomon(fieldValues,logger);
    	if(codingStyle=="stuetz"){
    		this._code = this._RS.createCode(m);
    	}
    	else if(codingStyle=="system"){
    		this._code = this._RS.createSystematicCode(m);
    	}
    	
    }
    
    public void starteDecodierung(int[] fieldValues, String decodingStyle, int[] c){
    	
    	System.out.println("test fieldvalues.length " + fieldValues.length + " decodingstyle : " + decodingStyle + " codelength " + c.length);
    	logger.log(1, "test fieldvalues.length " + fieldValues.length + " decodingstyle : " + decodingStyle + " codelength " + c.length);
//    	this._RS = new ReedSolomon(fieldValues[0], fieldValues[3], fieldValues[2]); // 0 = p primzahl, 1 = n, 2 = k, 3 = p(x) irreduzpoly
    	this._RS = new ReedSolomon(fieldValues,logger);
    	if(decodingStyle=="bwelch"){
    		this._message = this._RS.decodeCodeMethodWelch(c);
    	}
    	else if(decodingStyle=="bmassey"){
    		this._message = this._RS.decodeCodeMethodMassey(c);
    	}
    }
   
    
    public int[] getCode(){
        return this._code;
    }

	public int[] getMessage() {
		return this._message;
	}
}
