

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class metodos {
	
	//Levanta el archivo
	public static BufferedReader abrirArchivo (String ruta) { 
		File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
 
        try {
			archivo = new File (ruta);
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);
        } catch(Exception e) {
        	e.printStackTrace();
        }
        return br;
	}
	
	//Crea el nivel con los parámetros del archivo
	public static Casilla[][] crearMatriz(int fila, int col, BufferedReader arch) throws Exception { 
		Casilla[][] matriz = new Casilla[fila][col];
		for (int i = 0; i < fila; i++) {
			String linea = arch.readLine();
			linea = linea.replace(" ", "");
			for (int j = 0; j < col; j++) {
				char a = linea.charAt(j);
				Casilla pos = new Casilla(i, j, a, false);
				matriz[i][j] = pos;
			}
		}
		return matriz;
	}
	
	//Muestra el nivel
	public static void mostrarMatriz(Casilla[][] m) {
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				if (m[i][j].numero == -1) {
					System.out.print("X");
				} else {
					System.out.print(m[i][j].numero);
				}
			}
			System.out.println();
		}
	}
	
	//Mostrar descubiertos
	public static void mostrarMatrizCubierta(Casilla[][] m) {
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				if (!m[i][j].visible) {
					System.out.print(" * ");
				} else {
					System.out.print(" " + m[i][j].numero + " ");
				}
			}
			System.out.println();
		}
	}
	
	//Juega la partida
	public static ArrayList<Casilla> buscaminas(Casilla[][] t, int cantBombas) {
		ArrayList<Casilla> solucion = null;
		int tamaño;
		HashSet<Casilla> bombas = new HashSet<Casilla>();
		int cantP = t.length * t[0].length;
		for (int i = 0; i < t.length; i++) {
			for (int j = 0; j < t[0].length; j++) {
				if (solucion == null) {
					tamaño = 999999999;
				} else {
					tamaño = solucion.size();
				}
				ArrayList<Casilla> solucionParcial = new ArrayList<Casilla>();
				solucion = jugar(t , t[i][j], cantP, cantBombas, bombas, solucionParcial, solucion, tamaño);
			}
		}
		return solucion;
	}
	
	private static ArrayList<Casilla> jugar(Casilla[][] m, Casilla c, int cantP, int cantBombas, HashSet<Casilla> bombas, ArrayList<Casilla> clicks, ArrayList<Casilla> solucionFinal, int tamaño) {
		if (c.numero != -1){
			cantP = revelar(m, c, cantP);
			clicks.add(c);
			if (cantP == cantBombas) {
				if (solucionFinal == null) {
					solucionFinal = new ArrayList<Casilla>(clicks);
				} else {
					if (clicks.size() < solucionFinal.size())
						solucionFinal = new ArrayList<Casilla>(clicks);
				}
			} else {
				if (clicks.size() + 2 < tamaño) {
					for (int i = 0; i < m.length; i++) {
						for (int j = 0; j < m[0].length; j++) {
							if (solucionFinal == null) {
								tamaño = 999999999;
							} else {
								tamaño = solucionFinal.size();
							}
							if (!m[i][j].visible && !bombas.contains(m[i][j]))
								solucionFinal = jugar(m , m[i][j], cantP, cantBombas, bombas, clicks, solucionFinal, tamaño);
						}
					}
				}
			}
			clicks.remove(c);
			cantP = ocultar(m, c, cantP);
		} else {
			bombas.add(c);
		}
		return solucionFinal;
	}
	
	private static int revelar(Casilla[][] m, Casilla c, int cant) {
		c.visible = true;
		cant--;
		if (c.numero == 0) {
			cant = revelarAdy(m, c, cant);
		}
		return cant;
	}
	
	private static int revelarAdy(Casilla[][] m, Casilla c, int cant) {
		for ( int x = -1; x <= 1; x++) {
			int i = c.i + x;
			if (i >= 0 && i < m.length) {
				for (int y = -1; y <= 1; y++) {
					int j = c.j + y;
					if (j >= 0 && j < m[0].length) {
						if (!m[i][j].visible)
							cant = revelar(m, m[i][j], cant);
					}
				}
			}
		}
		return cant;
	}
	
	private static int ocultar(Casilla[][] m, Casilla c, int cant) {
		c.visible = false;
		cant++;
		if (c.numero == 0) {
			cant = ocultarAdy(m, c, cant);
		}
		return cant;
	}
	
	private static int ocultarAdy(Casilla[][] m, Casilla c, int cant) {
		for ( int x = -1; x <= 1; x++) {
			int i = c.i + x;
			if (i >= 0 && i < m.length) {
				for (int y = -1; y <= 1; y++) {
					int j = c.j + y;
					if (j >= 0 && j < m[0].length) {
						if (m[i][j].visible)
							cant = ocultar(m, m[i][j], cant);
					}
				}
			}
		}
		return cant;
	}
	
	
	private static int contarPend(Casilla[][] m) {
		int cantP = m.length * m[0].length;
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[0].length; j++) {
				if (m[i][j].visible)
					cantP--;
			}
		}
		return cantP;
	}
	
	
	public static Casilla[][] clonarMatriz (Casilla[][] m){
		Casilla[][] t = new Casilla[m.length][m[0].length];
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j <m[0].length; j++) {
				Casilla x;
				if (m[i][j].numero == -1) {
					x = new Casilla(m[i][j].i, m[i][j].j, 'X', m[i][j].visible);
				} else {
					x = new Casilla(m[i][j].i, m[i][j].j, (m[i][j].numero + "").charAt(0), m[i][j].visible);
				}
				t[i][j] = x;
			}
		}
		return t;
	}
	
	//Muestra el resultado final
	public static void mostrarListaCasillas(ArrayList<Casilla> lista) {
		Iterator<Casilla> iterador = lista.iterator();
		System.out.print("[ ");
		while(iterador.hasNext()){
			Casilla x = iterador.next();
			System.out.print("["+ (x.i + 1) + ", " + (x.j + 1) + "]");
			if (!iterador.hasNext()) {
				System.out.println(" ]");
			} else {
				System.out.print(", ");
			}
		}
	}
	
}
