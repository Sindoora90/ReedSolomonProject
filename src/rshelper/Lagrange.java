package rshelper;


import java.util.Vector;

public class Lagrange {
	private int n = 0;
	private static int ngf = 0; 
	private static int sumgf = 0;
	private static int productgf = 0;
	private double sum = 0, product = 0;

	public static GaloisField GF; 
	private static int ngfp;
	private static int[] sumgfp, productgfp;
	

	public static int calculateResultOld(int t, int[] xx, int[] yy){
		ngf = xx.length;
			
		for (int i = 0; i < ngf; i++) {
			productgf = yy[i];
			for (int j = 0; j <  ngf; j++) {
				if (i != j) {
					productgf = GF.multiply(productgf,  GF.divide(GF.add(t,xx[j]), GF.add(xx[i],xx[j])));
				}
			}
			sumgf = GF.add(sumgf, productgf);
		}
		
		return sumgf;
	}
	
	// TODO
	public static int[] calculateResultGF(int[] xx, int[] yy){
		ngfp = xx.length;
		productgfp = new int[1];	
		sumgfp = new int[xx.length];
		
		for(int ii = 0; ii < sumgfp.length; ii++){
			sumgfp[ii] = 0;
		}

		for (int i = 0; i < ngfp; i++) {
			productgfp = new int[1];
			productgfp[0] = yy[i];

			for (int j = 0; j <  ngfp; j++) {

				if (i != j) {
					int[] factorpoly = new int[]{GF.divide(xx[j],GF.add(xx[i],xx[j])), GF.divide(1,GF.add(xx[i],xx[j]))};
					productgfp = GF.multiply(productgfp,  factorpoly);//GF.divide(xx[j], GF.add(xx[i],xx[j])));
				}
			}
			sumgfp = GF.add(sumgfp, productgfp);
		}
		
		return sumgfp;
	}
	
	
	
	public double calculateResult(double t, Vector<Double> xx, Vector<Double> yy){
		n = xx.size();
			
		for (int i = 0; i < n; i++) {
			product = yy.elementAt(i);
			for (int j = 0; j <  n; j++) {
				if (i != j) {
					product = product * (t - xx.elementAt(j)) / (xx.elementAt(i) - xx.elementAt(j));
				}
			}
			sum = sum + product;
		}
		
		return sum;
	}

	@Override
	public String toString(){
		return "Lagrange";
	}
}
