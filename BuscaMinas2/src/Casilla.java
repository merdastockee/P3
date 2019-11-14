
public class Casilla {
	int numero;
	int i;
	int j;
	boolean visible;
	
	public Casilla(int i, int j, char a, boolean visible) {
		this.i = i;
		this.j = j;
		this.visible = visible;
		if (a != 'X') {
			this.numero = Integer.parseInt(a + "");
		} else {
			this.numero = -1;
		}
	}
}