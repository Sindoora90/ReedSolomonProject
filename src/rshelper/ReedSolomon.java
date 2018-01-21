package rshelper;

import java.util.ArrayList;
import java.util.Arrays;

// TODO GF noch einbinden in entsprechenden Variablen...

public class ReedSolomon {
	
	Logger logger;

	private final int n;
	private final int k;
	private final int d;
	private final int[] generator;
	private final GaloisField GF;

//	public ReedSolomon(int p, int primpoly, int k) {
	public ReedSolomon(int[] fieldValues, Logger logger){
		this.logger = logger;

//		this.n = (int) Math.pow(2, p) - 1; // 7 - (2^k)-1;
		this.n = fieldValues[4];
		this.k = fieldValues[5];
		this.d = n - k;
		
		int fieldSize = (int) Math.pow(fieldValues[0], fieldValues[1]);
		this.GF = GaloisField.getInstance(fieldSize, fieldValues[3], fieldValues[2]);
//		System.out.println("Test in RS Konstruktor");
//		System.out.println("n: " + n + ", k: " + k + ", d: " + d);
		this.generator = calculateGeneratorPolynomial();

	}

	private int[] calculateGeneratorPolynomial() {
	    // Generatorpolynom immer von Grad d
		int[] start = new int[] { GF.getPrimitiveElement(), 1 };
		for (int i = 2; i <= d; i++) {
			int a = (int) GF.power(GF.getPrimitiveElement(), i); // 2

			int[] next = new int[] { a, 1 };
			start = GF.multiply(start, next);
		}

		return start;
	}

	public int[] createSystematicCode(int[] message) {
		logger.log(0, "Nachricht m=" + ArraytoString(message));
		logger.log(0, "1. Array auf die Länge des Codes (" + n + ") bringen: ");

		int[] s = new int[n];
	
		int j = 0; 
		for (int i = 0; i < n; i++) {
			if (i < n - message.length) {
				s[i] = 0; // message[i];
			} else {
				s[i] = message[j];
				j++;
			}
		}
		logger.log(0,ArraytoString(s));
		logger.log(0, "2. Array durch das Generatorpolynom g(x)="+ArraytoString(generator) + " teilen:");

		// int[] rem = GF.divide(s,g)
		int[] temp = s.clone();
		int[] rem = GF.remainder2(temp, generator)[1];
		logger.log(0,"3. Addition des Restpolynoms r(x)="+ArraytoString(rem)+" mit dem erweiterten Polynom");

		for (int i = 0; i < s.length - message.length; i++) {
			if (i < rem.length) {
				s[i] = rem[i];
			}
		}
		logger.log(0,"Code c="+ArraytoString(s));

		return s;

	}

	public int[] createCode(int[] message) {
		logger.log(0, "Zu codierende Nachricht m=" + ArraytoString(message));
		int[] s = new int[n];
//		 System.out.println("\nn " + n + "\n ");
		int prim = GF.getPrimitiveElement();
		// System.out.println("prim " + prim);
		logger.log(0, "Einsetzen der Potenzen des primitiven Elements:");

		for (int i = n - 1; i >= 0; i--) {
			// s[n-1-i] = GF.substitute(message, GF.power(prim, i));;
			s[i] = GF.substitute(message, GF.power(prim, i));
//			System.out.print(s[i] + "  " );
		}
		logger.log(0, "Code c=" + ArraytoString(s));
		return s;

	}

	// Berlekamp Welch Algorithmus:
	public int[] decodeCodeMethodWelch(int[] code, String codingStyle) {
		logger.log(1, "GF: " + GF.getFieldSize() + " RS: (" + n + ","+  k +","+  d);
		logger.log(1, "Code c(x)="+ArraytoString(code));

		int[] m = new int[k];
		Gauss.GF = this.GF;
		Lagrange.GF = this.GF;
		
		
		// TODO _____TEST____ nachher wieder raus
//    	System.out.println("test für unterbestimmtes gleichungssystem: ");
//    	
//    	int[][] blamatrix = new int[][] {{1,2,3},{3,2,3},{1,2,3}};
//    	int[] bla = new int[]{3,2,3};
//    	int[] resultbla = Gauss.getSolutionGF(blamatrix, bla, true);
//    	System.out.println("result: " );
//    	Gauss.printVectorGF(resultbla, logger);
		// bis hier raus..
				
//		logger.log(1, "For that we need to find the error polynomial e(x) with deg(e)=d/2");
//		logger.log(1, "Here as array the length must be d/2+1");
//		logger.log(1, "Then we have to make the linear equation system with f(x) = e(x)*r(x) as described");
//		logger.log(1, "length(f)=m+d/2 and r(x) given code");
//		logger.log(1, "Evaluate powers of the primitive element in the equation to get the les");
		logger.log(1, "Aufstellen des linearen Gleichungssystem mit f(x)=c*e(x)");


		int fehleranzahl = d/2;
		
		// f(x) als unbestimmtes Polynom von Grad m+d/2
		// darin sind schon die Potenzen vom primitiven element eingesetzt
		int[][] fneu = new int[m.length+d][m.length+fehleranzahl];
		for(int i=0; i < fneu.length; i++){
			fneu[i] = calcPotenzen(GF.power(GF.getPrimitiveElement(), fneu.length-1-i),fneu[0].length);
		}
		
//		System.out.println("werte 1-7 in fneu(x)");
//		for (int i = 0; i < fneu.length; i++) {
//			System.out.print("(");
//			for (int j = 0; j < fneu[0].length; j++) {
//				System.out.print(fneu[i][j] + ",");
//			}
//			System.out.print("), ");
//		}
		
		// g(x) als unbestimmtes polynom von grad d/2+1
		// darin sind ebenfalls die Potenzen des primitiven elements eingesetzt
		int[][] gneu = new int[m.length+d][fehleranzahl+1];
		for(int i=0; i < gneu.length; i++){
			gneu[i] = calcPotenzen(GF.power(GF.getPrimitiveElement(), gneu.length-1-i),gneu[0].length);
		}
		
//		System.out.println("\nwerte 1-7 in gneu(x)");
//		for (int i = 0; i < gneu.length; i++) {
//			System.out.print("(");
//			for (int j = 0; j < gneu[0].length; j++) {
//				System.out.print(gneu[i][j] + ",");
//			}
//			System.out.print("), ");
//		}
		
//		System.out
//				.println("\nwerte 1-7 in g(x) multipliziert mit yi - auskommentiert");

		// Multiplikation des polynoms g(x) mit den empfangenen codeelementen
		int[] c = code; 
		for (int i = 0; i < gneu.length; i++) {
//			System.out.print("(");
			for (int j = 0; j < gneu[0].length; j++) {
//				g[i][j] = GF.multiply(g[i][j], c[c.length-1-i]);
				
				gneu[i][j] = GF.multiply(gneu[i][j], c[c.length-1-i]);

//				System.out.print(gneu[i][j] + ",");
			}
//			System.out.print("),");
		}

//		System.out.println();
//		System.out.println("    ---- Test 2 aus pdf ipad---- ");

		// 
		int[] vector_fg = new int[gneu.length];
		for (int i = 0; i < vector_fg.length; i++) {
			vector_fg[i] = gneu[i][0];
		}
		System.out.print("rechte Seite des Gleichungssystems...");
		System.out.println(ArraytoString(vector_fg));
		System.out.println("Matrix - linke seite des lgs");

		int[][] matrix_fg = new int[(fneu.length)][fneu[0].length + gneu[0].length
				- 1];

		for (int i = 0; i < matrix_fg.length; i++) {
			for (int j = 0; j < matrix_fg[0].length; j++) {
				if (j < fneu[0].length) {
					matrix_fg[i][j] = fneu[i][j];
				} else {
					matrix_fg[i][j] = gneu[i][j - fneu[0].length + 1];
				}

			}
		}
		logger.log(1, "Linke Seite des Gleichungssystems: \n" + ArraytoString2(matrix_fg));
//		Gauss.printMatrixGF(matrix_fg,logger);
//		System.out.println("test meine print matrix methode. ");
		System.out.println(ArraytoString2(matrix_fg));

		logger.log(1, "Rechte Seite des Gleichungssystems:" + ArraytoString(vector_fg));
		int[] result_pdf = Gauss.getSolutionGF(matrix_fg, vector_fg, false);
		System.out.println("test result-length " + result_pdf.length);
		System.out.println(ArraytoString(result_pdf));

		logger.log(1, "Lösungsvektor des Gleichungssystems: " + ArraytoString(result_pdf));
		logger.log(1, "Dieser Vektor muss in die beiden Polynome f(x) und e(x) aufgespalten werden");
		

		System.out.println("der Lösungsvektor muss nun in 2 funktionen aufgespalten werden (f&g), PD ergibt die ursprüngliche Nachricht");
//		int[] ff = new int[result_pdf.length-2];
//		int[] gg = new int[]{0,0,1};
		
		int[] ff = new int[m.length+fehleranzahl];
		int[] gg = new int[fehleranzahl+1];//{0,0,1};
//		for(int i = 0; i < gg.length; i++){
//			gg[i] = 0; 
//		}
		gg[gg.length-1] = 1;

		// muss andersrum reingespeichert werden sonst passt es nicht zum rest !!!
		System.out.println("test result_pdf.length="+result_pdf.length + " , ff.length=" + ff.length + " , gg.length="+gg.length);
		for(int i = 0; i < result_pdf.length; i++){
			if(i<ff.length){
				ff[(ff.length-1)-i] = result_pdf[i];
			}
			else{
				gg[gg.length-2-i+ff.length] = result_pdf[i];
			}
		}
		
		
		System.out.println("FUnktionen f und g");
		System.out.println(ArraytoString(ff));
		System.out.println(ArraytoString(gg));
		
		System.out.println("Funktionen f und g gekuerzt");
		boolean kuerzenf = true;
		int i = 0; 
		while(kuerzenf){
			if(ff[ff.length-1-i] == 0){
				i++;
			}
			else{
				kuerzenf=false;
			}
		}
		int[] f_gekuerzt = Arrays.copyOf(ff, ff.length-i);
		
		
		boolean kuerzeng = true;
		int j = 0; 
		while(kuerzeng){
			if(gg[gg.length-1-j] == 0){
				j++;
			}
			else{
				kuerzeng=false;
			}
		}
		int[] g_gekuerzt = Arrays.copyOf(gg, gg.length-j);
		
		
		System.out.println(ArraytoString(f_gekuerzt));
		System.out.println(ArraytoString(g_gekuerzt));
		
		logger.log(1, "f(x)=" + ArraytoString(f_gekuerzt) + " und e(x)=" + ArraytoString(g_gekuerzt));
		int[][] quotient = GF.remainder2(f_gekuerzt, g_gekuerzt); 

		System.out.println("polynomdivision ergebnis und rest");
		System.out.println(ArraytoString(quotient[0]));
		System.out.println(ArraytoString(quotient[1]));
		
		m = quotient[0].clone();
		
		// Check ob systematischer code
		if(codingStyle=="system"){
			// einsetzen der ersten k potenzen in das ergebnis polynom
			for(int ii = 0 ; ii < k; ii++){
				m[m.length-1-ii] = GF.substitute(quotient[0], GF.power(GF.getPrimitiveElement(), n-ii-1 ));
			}
		}
		
		logger.log(1, "Die Polynomdivision der beiden ergibt die Nachricht");

		logger.log(1, "Decodierte Nachricht m(x)="+ArraytoString(m));

		return m;

	}


	
	
	/**	
	 *  
	 * berechne syndromwerte
	 * iterativer algorithmus für elp
	 * chiensuche um nullstellen des elp zu bestimmen
	 * vandermonde matrix lösen
	 * 
	 * @param code
	 * @return decoded message
	 */
	
	public int[] decodeCodeMethodMassey(int[] code, String codingStyle) {
		logger.log(1, "GF: " + GF.getFieldSize() + " RS: (" + n + ","+  k +","+  d);
		logger.log(1, "Start decoding the given code c(x)="+ArraytoString(code)+" with the Berlekamp-Massey algorithm");
		int[] message = new int[k];
		
		Gauss.GF = this.GF;
		Lagrange.GF = this.GF;

		
		System.out.println(ArraytoString(code));
		int[] syndromes = calculateSyndromes(code);
		System.out.println("syndromes: ");
		System.out.println(ArraytoString(syndromes));
		logger.log(1, "Calculate needed syndromes: s(x)="+ArraytoString(syndromes));

		logger.log(1, "Berlekamp Massey Algorithm...");

		int[] C_x = new int[d/2+1]; // =1
		C_x[0] = 1;
		int[]  B_x = new int[d/2+1]; // =1
		B_x[0] = 1;
		for(int h = 1; h < B_x.length; h++){
			C_x[h] = 0; 
			B_x[h] = 0; 
		}
		
		int L = 0; 
		int m = 1; 
		int b = 1;
		int n; 
		
		logger.log(1, "Start values: "
				+ " L=0 , m=1, b=1, n=0"
				+ "C_x="  + ArraytoString(C_x) + " B_x=" + ArraytoString(B_x));

		// Berlekamp Massey Algorithm
		
		for(n=0; n<d;n++){
			logger.log(1, "iterative Step " + n);
			int kd = syndromes[n];
			for(int i=1; i<=L;i++){
				kd = GF.add(kd,GF.multiply(C_x[i],syndromes[n-i]));
			}
			if(kd==0){
				m++;
			}
			else if(2*L<=n){
				int[] T_x = Arrays.copyOf(C_x, C_x.length);
				int temp = GF.multiply(kd, GF.invert(b));
				int[] x_m = new int[d/2+1];
//				System.out.println(" TEMP" + temp);
				x_m[m] = temp;
				int[] newbx = GF.multiply(x_m, B_x);
//				for (int i=0; (i+4-m)<n; i++){
//					 C_x[i+4-m]= C_x[i+4-m]+B_x[i];
//				}
                C_x = GF.add(C_x, newbx);   
                L = n+1-L;
                
//                System.out.println("t-x");
//    			for(int x =0 ; x < T_x.length; x++){
//    				System.out.print("    " + T_x[x]);
//    			}
//    			System.out.println();                
                
                B_x = Arrays.copyOf(T_x, T_x.length);
                b = kd;
                m = 1;
                
			}else{
//				for (int i=0; (i+4-m)<n; i++){
//					 C_x[i+4-m]^=B_x[i];
//				}		
				int temp = GF.multiply(kd, GF.invert(b));
				int[] x_m = new int[d/2+1];
//				System.out.println(" TEMP" + temp);
				x_m[m] = temp;
				int[] newbx = GF.multiply(x_m, B_x);
//				for (int i=0; (i+4-m)<n; i++){
//					 C_x[i+4-m]= C_x[i+4-m]+B_x[i];
//				}
                C_x = GF.add(C_x, newbx); 
				m++;
			}
			
//			System.out.println(" Test ende jeder while L=" + L + " m=" + m + " b=" +  b + " i=" + n + " kd=" + kd);
//			System.out.println("c-x");
//			for(int x =0 ; x < C_x.length; x++){
//				System.out.print("    " + C_x[x]);
//			}
//			System.out.println();

//			System.out.println("b-x");
//			for(int x =0 ; x < C_x.length; x++){
//				System.out.print("    " + B_x[x]);
//			}
//			System.out.println();
			logger.log(1, "Values: "
					+ " L="+L+ " , m="+m+", b="+b+", n="+n
					+ "C_x="  + ArraytoString(C_x) + " B_x=" + ArraytoString(B_x));
		}
		
		
		System.out.println("c-x");
		for(int x =0 ; x < C_x.length; x++){
			System.out.print("    " + C_x[x]);
		}
		System.out.println();
		logger.log(1,"End of Berlekamp-Massey algorithm: C_x" + ArraytoString(C_x));
	
		logger.log(1, "Next step chien-search: ");
		// Chien Search..
		int [] x_werte = chienSearch(C_x);
		System.out.println("x_werte von c_x");
		for(int x =0 ; x < x_werte.length; x++){
			System.out.print("    " + x_werte[x]);
		}
		System.out.println();
		logger.log(1,"Result of chien search (x Values) " + ArraytoString(x_werte));
		logger.log(1, "Calculate logarithm value of x_werte as roots of the error polynomial: ");
		
//		 TODO als log funktion in GF einfügen
		int[] nullstellen = new int[x_werte.length];
		for(int i = 0; i < x_werte.length; i++){
			for(int j = 0; j < GF.getFieldSize()-1; j++){
				if(GF.power(GF.getPrimitiveElement(), j) == x_werte[i]){
					nullstellen[i] = j; 
				}
			}
		}
		logger.log(1,"Nullstellen - Roots of error polynomial: " + ArraytoString(nullstellen));

		
		System.out.println("nullstellen von c_x");
		for(int x =0 ; x < nullstellen.length; x++){
			System.out.print("    " + nullstellen[x]);
		}
		System.out.println();
//		Vandermonde Matrix für y werte der nullstellen
	   
		int[][] matrix = new int[syndromes.length][x_werte.length];
		for(int i=0; i < x_werte.length; i++){
			for(int j = 0; j < syndromes.length; j++){
				matrix[j][i] = GF.power(x_werte[i], j+1);
			}
		}
		logger.log(1, "Vandermonde Matrix to calculate the y-values: ");
		Gauss.printMatrixGF(matrix,logger);
		System.out.println("hier gibts wohl ein problem..." );
		int[] y_werte = Gauss.getSolutionGF(matrix, syndromes, true);
		//Gauss.printVectorGF(y_werte,logger);
		logger.log(1,"y-Values calculated by the gaussian algorithm: " + ArraytoString(y_werte));

		
		// Error locator polynom 
		int[] elp = new int[code.length];
		for(int i = 0; i < nullstellen.length; i++){
			elp[nullstellen[i]] = y_werte[i];
		}
		
		//Gauss.printVectorGF(elp,logger);
		logger.log(1,"Error polynomial: elp_x=" + ArraytoString(elp));

		int [] korrektercode = GF.add(code, elp);
		
		logger.log(1,"Adding elp and the received code you get the corrected code: c(x)=" + ArraytoString(korrektercode));

		//Gauss.printVectorGF(korrektercode,logger);
		
		// TODO message = korrektercode;
		int[] tempmessage = lagrangeInterpolation(korrektercode);
		logger.log(1,"Korrigierter Code c=" + ArraytoString(tempmessage));
//		if(codingStyle=="system"){
//			// einsetzen der ersten k potenzen in das ergebnis polynom
//			for(int ii = 0 ; ii < k; ii++){
//				System.out.println("system true");
//				message[message.length-1-ii] = GF.substitute(tempmessage, GF.power(GF.getPrimitiveElement(), n-ii-1 ));
//			}
//		}
//		else{
			message = tempmessage.clone();
//		}
		logger.log(1,"With the lagrange interpolation you get the decoded message m(x)=" + ArraytoString(message));

		return message;
	}	
	
	// TODO aktuell über gauss verfahren und nciht lagrange interpolation...	
		private int[] lagrangeInterpolation(int[] korrektercode) {
			
			// TODO Lagrange..
			int[] xvalues = new int[k];
			for(int i = 0; i < xvalues.length; i++){
				xvalues[i] = GF.power(GF.getPrimitiveElement(), i);
			}
			int[] yvalues = Arrays.copyOf(korrektercode,k);
			
			System.out.println("x: " + ArraytoString(xvalues) + " y: " + ArraytoString(yvalues));
			int[] ergebnis = Lagrange.calculateResultGF(xvalues,yvalues);
			System.out.println("ergebnis wenn zb. 1 eingesetzt..." + ergebnis.length + "  " + ArraytoString(ergebnis));
			
			// mit gauss
//			int[][] testmatrix = new int[k][k];//{{3,6,1},{5,3,1},{2,7,1}};
//			for(int i = 0; i < testmatrix.length; i++){
//				for(int j = 0; j < testmatrix[0].length; j++){
//					testmatrix[i][j] = GF.power(GF.power(GF.getPrimitiveElement(), n-1-i),k-1-j); 
//				}
//			}
//			Gauss.printMatrixGF(testmatrix,logger);
//			int[] testvector = Arrays.copyOf(korrektercode, k);
//			Gauss.printVectorGF(testvector,logger);
//			for(int i = 0; i < k; i++){
//				testvector[i] = korrektercode[korrektercode.length-1-i];
//			}
//
//			int [] testresult = Gauss.getSolutionGF(testmatrix, testvector, true);
//			Gauss.printVectorGF(testresult,logger);
//			
//			int [] invertedresult = new int[testresult.length];
//			for(int i = 0; i < testresult.length; i++){
//				invertedresult[i] = testresult[testresult.length-i-1];
				

		//return testresult;
//		return invertedresult;
			return ergebnis;

	}

		private int[] chienSearch(int[] c_x) {
			int a = GF.getPrimitiveElement();
			
			int[] a_x = new int[c_x.length];
			for(int i = 0; i < a_x.length; i++){
				a_x[i] = GF.power(a, i);
			}
			
			int[] y_x = Arrays.copyOf(c_x, c_x.length);
			ArrayList<Integer> nullstellen = new ArrayList<Integer>();
						

			for(int i =0 ; i <= GF.getFieldSize()-1; i++){
				int summe = 0;//y_x[0];
				for(int j = 0; j < y_x.length; j++){
					summe = GF.add(summe,y_x[j]);
				}
				System.out.println("summe bei i="+i+" :" + summe);

				if(summe == 0){
					nullstellen.add(i);
				}
				
				System.out.println(" neue y werte: " ); 
				for(int j = 0; j < y_x.length; j++){
					y_x[j] = GF.multiply(y_x[j], a_x[j]);
					System.out.print("  " + y_x[j] + "   ");
					
				}
				System.out.println("");
				
			}
			
			int[] result = new int[nullstellen.size()];
			for(int i = 0; i < result.length; i++){
				result[i] = GF.invert(GF.power(a, nullstellen.get(i)));
//				result[i] = GF.invert(nullstellen.get(i));

			}
		return result;
	}
	
	
	
	public int[] calculateSyndromes(int[] code){
		System.out.println("test wert d = " + d);
		int[] syndromes = new int[d];
		int a = 2; // primitives element
		
		for(int i = 1; i <= d ; i++){
			syndromes[i-1] = GF.substitute(code, GF.power(a,i));
		}
		
		
		return syndromes;
	}
	
	public int[] calcPotenzen(int a, int k) {
		int[] result = new int[k];
		for (int i = 0; i < k; i++) {
			result[result.length - i - 1] = GF.power(a, i);
		}

		return result;
	}
	
	public String ArraytoString(int[] poly){
		if(poly.length>0){
			String array = "["+poly[0];
			for(int i =1; i < poly.length; i++){
				array+=","+poly[i];
			}
			array+="]";
			return array;
		}
		else{
			return "empty array..."; 
		}
		
	}
	
	
	// TODO matrizen ausprinten
	public String ArraytoString2(int[][] poly){
		String array =  ArraytoString(poly[0]);
		for(int i = 0 ; i < poly.length; i++){
				array+="\n"+ArraytoString(poly[i]);
		}
		return array;
	}
	
//	public String Array2toString(int[][] poly){
//		String array = "["+poly[0];
//		for(int i =1; i < poly.length; i++){
//			for(int j = 0; j < poly[0].length; j++){
//				array+=","+poly[i];
//
//			}
//			array+="]\n";
//
//		}
//		return array;
//	}
}
