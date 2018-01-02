package rshelper;
/**
 * 
 * @author Sindoora
 * Körper / Field: Addition, Multiplikation, Subtraktion, Division, neutrale & inverse Elemente bzgl. + & * 
 * 
 */
public class Field {
	
	int neutralAddElement; 
	int neutralMultElement; 
	
	
	int add(int a, int b) {
		int result = a+b; 
		return result;
	}
	int multiply(int a, int b) {
		int result = a*b; 
		return result;
	}
	int subtract(int a, int b){
		int result = a-b; 
		return result;
	}
	int divide(int a, int b) {
		int result = a / b; 
		return result;
	}
	int power(int a, int b) {
		int result = (int) Math.pow(a, b);
		return result;
	}
	// Multiplikatives Inverses, da additives nicht nötig (einfach '-' davor)
	int invert(int a){
		int result = (int) Math.pow(a, -1);
		return result; 
	}
	
}
