package rshelper;


//import java.util.Vector;

public class Lagrange {


	public static GaloisField GF; 
	private static int ngf;
	private static int[] sumgf, productgf;
	
	/**
	 * Diese Funktion interpoliert mittels gegebener x und y Werte ein Polynom, welches diese Punkte enthaelt
	 * @param xx x-Werte der Funktion
	 * @param yy y-Werte der Funktion
	 * @return das interpolierte Polynom
	 */
	public static int[] calculateResultGF(int[] xx, int[] yy){
		ngf = xx.length;
		productgf = new int[1];	
		sumgf = new int[xx.length];
		
		for(int ii = 0; ii < sumgf.length; ii++){
			sumgf[ii] = 0;
		}

		for (int i = 0; i < ngf; i++) {
			productgf = new int[1];
			productgf[0] = yy[i];

			for (int j = 0; j <  ngf; j++) {

				if (i != j) {
					int[] factorpoly = new int[]{GF.divide(xx[j],GF.add(xx[i],xx[j])), GF.divide(1,GF.add(xx[i],xx[j]))};
					productgf = GF.multiply(productgf,  factorpoly);//GF.divide(xx[j], GF.add(xx[i],xx[j])));
				}
			}
			sumgf = GF.add(sumgf, productgf);
		}
		
		return sumgf;
	}

}
