package rshelper;


public class Gauss {
	static GaloisField GF;

	static int[] calcPotenzen(int a, int k) {
		int[] result = new int[k];
		for (int i = 0; i < k; i++) {
			result[result.length - i - 1] = GF.power(a, i);
		}

		return result;
	}

	static int[] getSolutionGF(int[][] matrix, int[] vector,
			boolean printSteps) {

		// Das Gleichungssystem hat keine eindeutige Loesung!
		if (matrix.length < matrix[0].length) {
			// TODO : Hier weiterarbeiten !!!!
			System.out.println("Gleichungssystem nicht eindeutig loesbar da überbestimmt!");
			return null;
		}

		// Merken der Spalte, welche eine Zahl ungleich null besitzt
		int tmpColumn = -1;

		// Alle Zeilen durchgehen: Ziel der for-Schleife -> Matrix in
		// Zeilenstufenform bringen!
		// -> Alle Zahlen unterhalb der Diagonale sind null
		for (int line = 0; line < matrix.length; line++) {
			tmpColumn = -1;

			// Umformungsschritt 1: Finden einer Spalte mit einem Wert ungleich
			// null
			for (int column = 0; column < matrix[line].length; column++) {
				for (int row = line; row < matrix.length; row++) {
					if (matrix[row][column] != 0) {
						tmpColumn = column;
						break;
					}
				}

				// Abbruch, zahl ungleich null wurde gefunden
				if (tmpColumn != -1) {
					break;
				}
			}

			// NullZeile(n) entdeckt!
			if (tmpColumn == -1) {
				for (int row = line; row < matrix.length; row++) {
					// Gleichungssystem hat keine Loesung!
					if (vector[line] != 0) {
						// Wenn die Zwischenschritte ausgegeben werden sollen
						if (printSteps) {
							printStepGF(matrix, vector);
						}

						System.out
								.println("Gleichungssystem besitzt keine Loesung!");
						
//						matrix anpassen zeilen auf nicht null setzen...
//						int[][] matrix2 = matrix.clone();
//						matrix2[line][line] = 1;
//						return getSolutionGF(matrix2, vector, printSteps);
						return null; // ? 
					}
				}
				// Nullzeile(n) vorhanden -> Ist das System noch eindeutig
				// loesbar?
//				if (matrix[0].length - 1 >= line) {
//					// Wenn die Zwischenschritte ausgegeben werden sollen
//					System.out.println("zwischen" +  matrix[0].length + " line " +  line);
//					// matrix muss gekürzt werden da unterbestimmtes gs:
//					for(int i = line; i < matrix[0].length; i++){
//						matrix[i][i] = 1;
//						vector[i] = 1;
//					}
//					return getSolutionGF(matrix, vector, printSteps);
//
//				}
				break;
			}

			// Umformungsschritt 2: Die Zahl matrix[line][tmpColumn] soll
			// UNgleich null sein
			if (matrix[line][tmpColumn] == 0) {
				for (int row = line + 1; row < matrix.length; row++) {
					if (matrix[row][tmpColumn] != 0) {
						// Wenn die Zwischenschritte ausgegeben werden sollen
						if (printSteps) {
							printStepGF(matrix, vector);
							System.out.println("Zeile " + (line + 1)
									+ " wird mit Zeile " + (row + 1)
									+ " getauscht");
						}

						// Vertauschen von Zeilen -> matrix[line][tmpColumn]
						// wird dann ungleich null
						swapTwoLinesGF(line, row, matrix, vector);
						break;
					}
				}
			}

			// Umformungsschritt 3: matrix[line][tmpColumn] soll gleich 1 sein.
			if (matrix[line][tmpColumn] != 0) {
				// Wenn die Zwischenschritte ausgegeben werden sollen
				if (printSteps) {
					printStepGF(matrix, vector);
					System.out.println("Zeile " + (line + 1) + " wird durch "
							+ matrix[line][tmpColumn] + " geteilt");
				}

				// Division der Zeile mit matrix[line][tmpColumn]
				divideLineGF(line, matrix[line][tmpColumn], matrix, vector);
			}

			// Wenn die Zwischenschritte ausgegeben werden sollen
			if (printSteps) {
				printStepGF(matrix, vector);
			}

			// Umformungsschritt 4: Alle Zahlen unter matrix[line][tmpColumn]
			// sollen null sein.
			for (int row = line + 1; row < matrix.length; row++) {
				// Wenn die Zwischenschritte ausgegeben werden sollen
				if (printSteps) {
					System.out.println("Zu Zeile " + (row + 1)
							+ " wird subtrahiert: " + matrix[row][tmpColumn]
							+ " * Zeile " + (line + 1));
				}

				// Subtraktion damit unter der Zahl im Umformungsschritt 3 nur
				// nullen stehen
				removeRowLeadingNumberGF(matrix[row][tmpColumn], line, row,
						matrix, vector);
			}
		}

		// Umformungsschritt 6: Matrix in Normalform bringen (Zahlen oberhalb
		// der Diagonale werden ebenfalls zu null)
		for (int column = matrix[0].length - 1; column > 0; column--) {
			// Wenn die Zwischenschritte ausgegeben werden sollen
			if (printSteps) {
				printStepGF(matrix, vector);
			}

			// Alle Werte oberhalb von "column" werden zu null
			for (int row = column; row > 0; row--) {
				// Wenn die Zwischenschritte ausgegeben werden sollen
				if (printSteps) {
					System.out.println("Zu Zeile " + (row)
							+ " wird subtrahiert: " + matrix[row - 1][column]
							+ " * Zeile " + (column + 1));
				}

				// Dazu wird Subtraktion angewandt
				removeRowLeadingNumberGF(matrix[row - 1][column], column,
						row - 1, matrix, vector);
			}
		}

		// Wenn die Zwischenschritte ausgegeben werden sollen
		if (printSteps) {
			printStepGF(matrix, vector);
		}

		// Unser ehemaliger Loesungsvektor ist jetzt zu unserem Zielvektor
		// geworden :)
		return vector;

	}

	/*
	 * Gauss-Jordan-Algorithmus nur fuer eindeutige Gleichungssysteme geeignet
	 * (andernfalls wird NULL zurueckgegeben) matrix[row][column]
	 */

	private static void swapTwoLinesGF(int rowOne, int rowTwo, int[][] matrix,
			int[] vector) {
		int[] tmpLine;
		int tmpVar;

		tmpLine = matrix[rowOne];
		tmpVar = vector[rowOne];

		matrix[rowOne] = matrix[rowTwo];
		vector[rowOne] = vector[rowTwo];

		matrix[rowTwo] = tmpLine;
		vector[rowTwo] = tmpVar;
	}

	/*
	 * eine Zeile wird durch "div" geteilt. "div" darf nicht null sein
	 */
	private static void divideLineGF(int row, int div, int[][] matrix,
			int[] vector) {
		for (int column = 0; column < matrix[row].length; column++) {
			int test = GF.divide(matrix[row][column], div);
//			 System.out.println("test divide: " + test);
			matrix[row][column] = test;
		}
		vector[row] = GF.divide(vector[row], div);
	}

	/*
	 * Eine Zeile (row) wird mit einem entsprechendem vielfachen (factor) von
	 * einer anderen Zeile (rowRoot) subtrahiert.
	 */
	private static void removeRowLeadingNumberGF(int factor, int rowRoot,
			int row, int[][] matrix, int[] vector) {
		for (int column = 0; column < matrix[row].length; column++) {
			matrix[row][column] = GF.add(matrix[row][column],
					GF.multiply(factor, matrix[rowRoot][column]));
		}
		vector[row] = GF.add(vector[row], GF.multiply(factor, vector[rowRoot]));
	}

	/*
	 * Ein Vector wird auf der Konsole ausgegeben (transponiert)
	 */

	public static void printVectorGF(int[] vector, Logger logger) {
		if (vector == null) {
			return;
		}
		System.out.println();
		System.out.print("vektor ist: (");
		String logvector = "vektor ist: (";
		for (int i = 0; i < vector.length; i++) {
			if (i != 0) {
				System.out.print(",");
				logvector += ",";
			}
			System.out.print(vector[i]);
			logvector +=""+vector[i];
		}
		System.out.println(")^T");
		logvector += ")^T";
		logger.log(1,logvector);
	}

	/*
	 * Eine Matrix wird auf der Konsole ausgegeben matrix[row][column]
	 */

	public static void printMatrixGF(int[][] matrix, Logger logger) {
		if (matrix == null) {
			return;
		}
		String logmatrix ="\n";
		for (int row = 0; row < matrix.length; row++) {
			System.out.print("(");
			logmatrix += "(";
			for (int column = 0; column < matrix[row].length; column++) {
				if (column != 0) {
					System.out.print(",");
					logmatrix += ",";
				}
				System.out.print(matrix[row][column]);
				logmatrix += ""+matrix[row][column];
			}
			System.out.println(")");
			logmatrix += ")\n";
		}
		logger.log(1,logmatrix);
	}

	/*
	 * Diese Methode zeigt die Zwischenschritte der Berechnung auf der Konsole
	 * an. Fuer die Aufgabe nicht weiter relevant (unbekannte Konzepte werden
	 * verwendet!)
	 */

	private static void printStepGF(int[][] matrix, int[] vector) {
		System.out.println();

		// Werte werden fuer die Ausgabe auf ein bestimmtes Format gebracht
		// -> Damit die Ausgabe auch immer schick aussieht
		java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
		for (int row = 0; row < matrix.length; row++) {
			for (int column = 0; column < matrix[row].length; column++) {
				if (matrix[row][column] >= 0) {
					System.out.print("+");
				}
				System.out.print(df.format(matrix[row][column]) + "    ");
			}
			System.out.print("|    ");
			if (vector[row] >= 0) {
				System.out.print("+");
			}
			System.out.println(df.format(vector[row]));
		}
	}
}
