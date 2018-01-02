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
		// TODO Primitives Polynom muss noch geregelt werden...
//		this.GF = GaloisField.getInstance(n + 1, primpoly); // 13 = p(x) =
															// x^3+x^2+1
		int fieldSize = (int) Math.pow(fieldValues[0], fieldValues[1]);
		this.GF = GaloisField.getInstance(fieldSize, fieldValues[3], fieldValues[2]);
		System.out.println("Test in RS Konstruktor");
		System.out.println("n: " + n + ", k: " + k + ", d: " + d);
		this.generator = calculateGeneratorPolynomial();

	}

	// TODO später wieder auf private setzen
	public int[] calculateGeneratorPolynomial() {
		// TODO Auto-generated method stub
		// int[] gen = new int[d]; // Generatorpolynom immer von Grad d
		int[] start = new int[] { 2, 1 };
		for (int i = 2; i <= d; i++) {
			int a = (int) GF.power(2, i); // TODO: kann 2 als primitives Element
											// verwendet werden ?
			int[] next = new int[] { a, 1 }; // TODO: Potenz rechnung in JAVA
												// googlen bzw methode in GF
												// schreiben für pow.... -_-

			start = GF.multiply(start, next);
		}

		return start;
	}

	public int[] createSystematicCode(int[] message) {
		logger.log(0, "Create systematic Code for message: " + ArraytoString(message));
		logger.log(0, "1. Extend message array to length of code (" + n + "): ");

		int[] s = new int[n];
		// TODO: die freien stellen müssen nach vorne und nicht hinten angehängt
		// werden !!! also 0000137 !!!
		int j =0; 
		for (int i = 0; i < n; i++) {
			if (i < n - message.length) {
				s[i] = 0; // message[i];
			} else {
				s[i] = message[j];
				j++;
			}
		}
		logger.log(0,ArraytoString(s));
		logger.log(0, "2. Divide extended array with generator polynomial g(x)="+ArraytoString(generator));

		// int[] rem = GF.divide(s,g) TODO: divide methode muss noch geschrieben
		// werden -> erhalten von rem(x) -> hinten ans s(x) anhängen -> c(x)
		int[] temp = s.clone();
		int[] rem = GF.remainder2(temp, generator)[1];
		logger.log(0,"The remainder r(x)="+ArraytoString(rem)+"has to be added to the extended array");

		for (int i = 0; i < s.length - message.length; i++) {
			if (i < rem.length) {
				s[i] = rem[i];
			}
		}
		logger.log(0,"The result is the systematic Code c="+ArraytoString(s));
		logger.log(0, "\n ---------------------- \n");

		return s;

	}

	public int[] createCode(int[] message) {
		logger.log(0, "Create code by evaluating n different elements of the used GF in the message polynom: " + ArraytoString(message));
		int[] s = new int[n];
		 System.out.println("\nn " + n + "\n ");

		int prim = GF.getPrimitiveElement();
		// System.out.println("prim " + prim);
		logger.log(0, "Evaluate the powers of the primitive element starting bei (n-1) descending until zero:");

		for (int i = n - 1; i >= 0; i--) {
			// System.out.println("test " + s[n-1-i]);
			// s[n-1-i] = GF.substitute(message, GF.power(prim, i));;

			s[i] = GF.substitute(message, GF.power(prim, i));
			System.out.print(s[i] + "  " );
			;
		}
		logger.log(0, "The result is the code polynom: c(x)=" + ArraytoString(s));
		logger.log(0, "\n ---------------------- \n");

		return s;

	}

	// Berlekamp Welch Algorithmus:
	public int[] decodeCodeMethodWelch(int[] code) {
		logger.log(1, "Start decoding the given code c(x)="+ArraytoString(code)+" with the Berlekamp-Welch algorithm");

		int[] m = new int[k];

		Gauss.GF = this.GF;

		System.out.println("code: ");
		Gauss.printVectorGF(code, logger);
		
		logger.log(1, "code: ");
		
		logger.log(1, "For that we need to find the error polynomial e(x) with deg(e)=d/2");
		logger.log(1, "Here as array the length must be d/2+1");
		logger.log(1, "Then we have to make the linear equation system with f(x) = e(x)*r(x) as described");
		logger.log(1, "length(f)=m+d/2 and r(x) given code");
		logger.log(1, "Evaluate powers of the primitive element in the equation to get the les");
		

		
		int[][] fneu = new int[m.length+d][m.length+d/2];
		for(int i=0; i < fneu.length; i++){
			fneu[i] = calcPotenzen(GF.power(2, fneu.length-1-i),fneu[0].length);
		}
		
		System.out.println("werte 1-7 in fneu(x)");
		for (int i = 0; i < fneu.length; i++) {
			System.out.print("(");
			for (int j = 0; j < fneu[0].length; j++) {
				System.out.print(fneu[i][j] + ",");
			}
			System.out.print("), ");
		}
		
		
		int[][] gneu = new int[m.length+d][d/2+1];
		for(int i=0; i < gneu.length; i++){
			gneu[i] = calcPotenzen(GF.power(2, gneu.length-1-i),gneu[0].length);
		}
		
		System.out.println("\nwerte 1-7 in gneu(x)");
		for (int i = 0; i < gneu.length; i++) {
			System.out.print("(");
			for (int j = 0; j < gneu[0].length; j++) {
				System.out.print(gneu[i][j] + ",");
			}
			System.out.print("), ");
		}
		
		System.out
				.println("\nwerte 1-7 in g(x) multipliziert mit yi - auskommentiert");

		int[] c = code; 
		for (int i = 0; i < gneu.length; i++) {
			System.out.print("(");
			for (int j = 0; j < gneu[0].length; j++) {
//				g[i][j] = GF.multiply(g[i][j], c[c.length-1-i]);
				
				gneu[i][j] = GF.multiply(gneu[i][j], c[c.length-1-i]);

				System.out.print(gneu[i][j] + ",");
			}
			System.out.print("),");
		}

		System.out.println();
		System.out.println("    ---- Test 2 aus pdf ipad---- ");

		int[] vector_fg = new int[gneu.length];
		for (int i = 0; i < vector_fg.length; i++) {
			vector_fg[i] = gneu[i][0];
		}
		System.out.print("Nicht Lösungsvektor sondern rechte Seite des Gleichungssystems...");
		Gauss.printVectorGF(vector_fg,logger);
		System.out.println("Matrix mit 2 Fehlern");

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

		Gauss.printMatrixGF(matrix_fg,logger);
		System.out.println();
		System.out.println("selbst eingegebene Matrix, sollte der drüber entsprechen ");

		int[][] matrix_pdf = { { 5, 7, 3, 6, 1, 4, 5 },
				{ 6, 2, 5, 3, 1, 2, 5 }, { 4, 3, 2, 7, 1, 5, 6 },
				{ 3, 4, 6, 5, 1, 5, 1 }, { 2, 6, 7, 4, 1, 5, 2 },
				{ 7, 5, 4, 2, 1, 2, 1 }, { 1, 1, 1, 1, 1, 5, 5 } };
		

//		int[] vector_pdf3 = {3, 2, 2, 2, 5, 3, 2};
		int[] vector_pdf = {2, 6, 1, 6, 3, 4, 5};

		
		Gauss.printMatrixGF(matrix_pdf,logger);
//		System.out.println("testtetsestsets");
		System.out.println("berechneter und selbst festgelegter vektor für rechte seite vom lgs");
		
		Gauss.printVectorGF(vector_fg,logger);
		Gauss.printVectorGF(vector_pdf,logger);

//		int[] vector_pdf = { 2, 6, 1, 6, 3, 4, 5 };
		logger.log(1, "Matrix and vector for the gaussian algorithm");
		logger.log(1, matrix_fg + " = " + vector_fg);
		int[] result_pdf = Gauss.getSolutionGF(matrix_fg, vector_fg, true);
		System.out.println("test result-length " + result_pdf.length);
		Gauss.printVectorGF(result_pdf,logger);
//		Gauss.printMatrixGF(matrix_pdf);
		logger.log(1, "Result of the les: " + ArraytoString(result_pdf));
		logger.log(1, "This vector has to be split into to polynomials (f & e), the polynomial division of them gives the message");
		

		System.out.println("der Lösungsvektor muss nun in 2 funktionen aufgespalten werden (f&g), PD ergibt die ursprüngliche Nachricht");
//		int[] ff = new int[result_pdf.length-2];
//		int[] gg = new int[]{0,0,1};
		
		int[] ff = new int[m.length+d/2];
		int[] gg = new int[d/2+1];//{0,0,1};
		for(int i = 0; i < gg.length; i++){
			gg[i] = 0; 
		}
		gg[gg.length-1] = 1;

		// muss andersrum reingespeichert werden sonst passt es nicht zum rest !!!
		for(int i = 0; i < result_pdf.length; i++){
			if(i<ff.length){
				ff[(ff.length-1)-i] = result_pdf[i];
			}
			else{
				gg[gg.length-2-i+ff.length] = result_pdf[i];
			}
		}
		
		
		System.out.println("FUnktionen f und g");
		Gauss.printVectorGF(ff,logger);
		Gauss.printVectorGF(gg,logger);
		
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
		
		
		Gauss.printVectorGF(f_gekuerzt,logger);
		Gauss.printVectorGF(g_gekuerzt,logger);
		
		//TODO: PD richtig nicht nur rest ausgeben..
		System.out.println("Rest von PD von f und g");
		int[][] quotient = GF.remainder2(f_gekuerzt, g_gekuerzt); 
		// TODO die nullen vorne müssen weg

		Gauss.printVectorGF(quotient[0],logger);
		Gauss.printVectorGF(quotient[1],logger);
		
		m = quotient[0].clone();
		
		
		logger.log(1, "The decoded message: m(x)="+ArraytoString(m));
		logger.log(1, "\n ---------------------- \n");

		return m;

	}

	
    // Implementation of Berlekamp-Massey algorithm for calculating linear 
    // complexity of binary sequence
    // s = byte array with binary sequence
    // returns Length of LFSR with smallest length which generates s
    // for an example: int L=BerlekampMassey(new byte[] {1,0,1,0,1,1,1,0,1,0})
    //        reference: "Handbook of Applied Cryptography", p201

	
	// TODO noch nötig  ?
//    public static int BerlekampMassey(byte[] s)                
//    {
//        int L, N, m, d;
//        int n=s.length;
//        byte[] c=new byte[n];
//        byte[] b=new byte[n];
//        byte[] t=new byte[n];
//
//        //Initialization
//        b[0]=c[0]=1;
//        N=L=0;
//        m=-1;
//                
//        //Algorithm core
//        while (N<n)
//        {
//            d=s[N];
//            for (int i=1; i<=L; i++)
//            d^=c[i]&s[N-i];            //(d+=c[i]*s[N-i] mod 2)
//            if (d==1)
//            {
//                t = Arrays.copyOf(c, n);    //T(D)<-C(D)
//                for (int i=0; (i+N-m)<n; i++)
//                    c[i+N-m]^=b[i];
//                if (L<=(N>>1))
//                {
//                    L=N+1-L;
//                    m=N;
//                    b = Arrays.copyOf(t, n);    //B(D)<-T(D)
//                }
//            }
//            N++;
//        }
//        return L;
//    }
	
	
	/**
	 * TODO
	 * 
	 * berechne syndromwerte
	 * iterativer algorithmus für elp
	 * chiensuche um nullstellen des elp zu bestimmen
	 * vandermonde matrix lösen
	 * 
	 * @param code
	 * @return decoded message
	 */
	
	public int[] decodeCodeMethodMassey(int[] code) {
		logger.log(1, "Start decoding the given code c(x)="+ArraytoString(code)+" with the Berlekamp-Massey algorithm");
		int[] message = new int[k];
		
		Gauss.printVectorGF(code,logger);
		int[] syndromes = calculateSyndromes(code);
		System.out.println("syndromes: ");
		Gauss.printVectorGF(syndromes,logger);
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
				// TODO primitives element nehmen
				if(GF.power(2, j) == x_werte[i]){
					nullstellen[i] = j; 
				}
			}
		}
		logger.log(1,"Nullstellen - Roots of error polynomial " + ArraytoString(nullstellen));

		
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
		Gauss.printMatrixGF(matrix,logger);
		System.out.println("hier gibts wohl ein problem..." );
		int[] y_werte = Gauss.getSolutionGF(matrix, syndromes, true);
		Gauss.printVectorGF(y_werte,logger);
		
		// Error locator polynom 
		int[] elp = new int[code.length];
		for(int i = 0; i < nullstellen.length; i++){
			elp[nullstellen[i]] = y_werte[i];
		}
		
		Gauss.printVectorGF(elp,logger);
		int [] korrektercode = GF.add(code, elp);
		Gauss.printVectorGF(korrektercode,logger);
		
		message = lagrangeInterpolation(korrektercode);
		return message;
	}	
	
	// TODO aktuell über gauss verfahren und nciht lagrange interpolation...	
		private int[] lagrangeInterpolation(int[] korrektercode) {
			
			
			
			int[][] testmatrix = new int[k][k];//{{3,6,1},{5,3,1},{2,7,1}};
			for(int i = 0; i < testmatrix.length; i++){
				for(int j = 0; j < testmatrix[0].length; j++){
					testmatrix[i][j] = GF.power(GF.power(GF.getPrimitiveElement(), n-1-i),k-1-j); 
				}
			}
			Gauss.printMatrixGF(testmatrix,logger);
			int[] testvector = Arrays.copyOf(korrektercode, k);
			Gauss.printVectorGF(testvector,logger);
			for(int i = 0; i < k; i++){
				testvector[i] = korrektercode[korrektercode.length-1-i];
			}

			int [] testresult = Gauss.getSolutionGF(testmatrix, testvector, true);
			Gauss.printVectorGF(testresult,logger);
			
			
		// TODO Auto-generated method stub
		return testresult;
	}

		private int[] chienSearch(int[] c_x) {
//			int a = GF.getPrimitiveElement();
			// TODO test..
			int a = 2; 
//			System.out.println("test gf: fieldsize: " + GF.getFieldSize() + " primitives element: " + GF.getPrimitiveElement());
			
			int[] a_x = new int[c_x.length];
			for(int i = 0; i < a_x.length; i++){
				a_x[i] = GF.power(a, i);
			}
			
			int[] y_x = Arrays.copyOf(c_x, c_x.length);
			ArrayList<Integer> nullstellen = new ArrayList<Integer>();
			
			// TODO GF.getfieldsize-1 oder -2 testen was richtig...
			
			System.out.println(" test gf fieldsize: " + GF.getFieldSize());
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
		// TODO Auto-generated method stub
		return result;
	}
	
	
	// Hilfsfunktionen..
	// TODO
	public int calculateDiscrepancy(){
		int d = 0; 
		
		return d; 
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
	public String Array2toString(int[][] poly){
		String array = "["+poly[0];
		for(int i =1; i < poly.length; i++){
			array+=","+poly[i];
		}
		array+="]";
		return array;
	}
}
