import java.io.BufferedReader;
import java.io.FileReader;

public class File {
	
	private static String NOMBRE_ARCHIVO_DE_CONFIGURACION = "Config.txt";
	private static String PATH_NOMBRE_CARPETA_DE_ARCHIVOS = "\\src\\Archivos\\";
	
	static class Elem{
		String nombreArchivo;
		String nombreDato;
	}


	public Casilla[][] LeerArchivo(String nombreArchvio) {
		Casilla[][] tablero = null;
		int n;
		String path, linea, primerLinea;
		String [] values;
		String [] tamanio;
		FileReader fr;
		BufferedReader br;
		int i = 0;	
		path = ObtenerPathDeCarpetaDeArchivos() + "\\" + nombreArchvio;
		
		
		try {
			fr = new FileReader(path);
			br = new BufferedReader(fr);
			primerLinea =  br.readLine();
			tamanio = primerLinea.split(",");
			n = Integer.parseInt(tamanio[0]);
			tablero = new Casilla[n][n];
			while ((linea = br.readLine()) != null) {
				values = linea.split(" ");
				for(int j = 0; j < n; j++) {	
					//tablero.AgregarCasilla(values[j], i, j);
					tablero[i][j] = new Casilla(i,j, values[j].charAt(0) ,false);
				}
				i++;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return tablero;
	}
		
	private static String ObtenerPathDeCarpetaDeArchivos() {
		return System.getProperty("user.dir") + PATH_NOMBRE_CARPETA_DE_ARCHIVOS;
	}
	
	
}
