import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class main {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 String ruta;
		ruta = System.getProperty("user.dir") + "\\src\\Archivos\\" + "chico.txt";
		BufferedReader archivo = metodos.abrirArchivo(ruta);
		if (archivo != null) {
			String linea1 = null;
			try {
				linea1 = archivo.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String[] datos = linea1.split(",");
			int fila = Integer.parseInt(datos[0]);
			int col = Integer.parseInt(datos[1]);
			int cantBombas = Integer.parseInt(datos[2]);
			
			Casilla[][] matriz = null;
			try {
				matriz = metodos.crearMatriz(fila, col, archivo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Matriz original: ");
			metodos.mostrarMatriz(matriz);
			try {
				archivo.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ArrayList<Casilla> clicks = metodos.buscaminas(matriz, cantBombas);
			System.out.print("La mejor solución es: ");
			metodos.mostrarListaCasillas(clicks);
		}
	}
	
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
			ArrayList<Casilla> revelados = new ArrayList<Casilla>();
			revelar(m, c, revelados);
			clicks.add(c);
			cantP -= revelados.size();
			if (cantP == cantBombas) {
				if (solucionFinal == null) {
					solucionFinal = new ArrayList<Casilla>(clicks);
				} else {
					if (clicks.size() < solucionFinal.size())
						solucionFinal = new ArrayList<Casilla>(clicks);
				}
			} else {
				if (clicks.size() < tamaño) {
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
			cantP += revelados.size();
			ocultar(m, revelados);
			clicks.remove(c);
		} else {
			bombas.add(c);
		}
		return solucionFinal;
	}
	
	private static void revelar(Casilla[][] m, Casilla c, ArrayList<Casilla> lista) {
		c.visible = true;
		lista.add(c);
		if (c.numero == 0) {
			revelarAdy(m, c, lista);
		}
	}
	
	private static void revelarAdy(Casilla[][] m, Casilla c, ArrayList<Casilla> lista) {
		for ( int x = -1; x <= 1; x++) {
			int i = c.i + x;
			if (i >= 0 && i < m.length) {
				for (int y = -1; y <= 1; y++) {
					int j = c.j + y;
					if (j >= 0 && j < m[0].length) {
						if (!m[i][j].visible)
							revelar(m, m[i][j], lista);
					}
				}
			}
		}
	}
	
	private static void ocultar(Casilla[][] m, ArrayList<Casilla> lista) {
		Iterator<Casilla> iterador = lista.iterator();
		while(iterador.hasNext()){
			Casilla x = iterador.next();
			m[x.i][x.j].visible = false;
			}
		}
}


