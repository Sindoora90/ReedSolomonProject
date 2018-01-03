package mvcmodel;

import java.util.Date;

import rshelper.Gauss;
import rshelper.Logger;
import rshelper.ReedSolomon;

/**
 * Das Model ist komplett unabhängig von den anderen
 * Klassen und weiß nicht was um ihn herum geschieht.
 * Es ist völlig egal ob man dieses Model aus einem
 * Fenster oder einer Konsolen Eingabe verwendet -
 * beides würde funktionieren.
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

    public void zurückSetzen(){
    	this._code = null;
    	this._message = null;
    }
    
    
    public void starteCodierung(int[] fieldValues, String codingStyle, int[] m){
    	logger.log(0, "START CODIERUNG");
    	long start = System.currentTimeMillis();
//    	this._RS = new ReedSolomon(fieldValues[0], fieldValues[3], fieldValues[2]); // 0 = p primzahl, 1 = n, 2 = k, 3 = p(x) irreduzpoly
    	this._RS = new ReedSolomon(fieldValues,logger);
    	if(codingStyle=="stuetz"){
    		this._code = this._RS.createCode(m);
    	}
    	else if(codingStyle=="system"){
    		this._code = this._RS.createSystematicCode(m);
    	}
    	long end = System.currentTimeMillis();
    	long diff = end - start; 
    	logger.log(0 , " END - TIME: " + diff);
		logger.log(0, "\n ---------------------- \n");

    	
    }
    
    public void starteDecodierung(int[] fieldValues, String decodingStyle, int[] c){  	
    	logger.log(1, "START DECODIERUNG");
    	long start = System.currentTimeMillis();
//    	this._RS = new ReedSolomon(fieldValues[0], fieldValues[3], fieldValues[2]); // 0 = p primzahl, 1 = n, 2 = k, 3 = p(x) irreduzpoly
    	this._RS = new ReedSolomon(fieldValues,logger);
    	if(decodingStyle=="bwelch"){
    		this._message = this._RS.decodeCodeMethodWelch(c);
    	}
    	else if(decodingStyle=="bmassey"){
    		this._message = this._RS.decodeCodeMethodMassey(c);
    	}
    	long end = System.currentTimeMillis();
    	long diff = end - start; 
    	logger.log(1 , " END - TIME: " + diff);
		logger.log(1, "\n ---------------------- \n");

    	
    }
   
    
    public int[] getCode(){
        return this._code;
    }

	public int[] getMessage() {
		return this._message;
	}
}
