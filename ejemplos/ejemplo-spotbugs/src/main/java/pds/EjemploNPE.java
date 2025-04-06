package pds;

public class EjemploNPE {

	public static void main(String[] args) {
		String text = null;
		int length = getStringLength(text);
		System.out.println("Longitud de la cadena: " + length);
	}

	public static int getStringLength(String str) {
		// str puede ser null
		return str.length();
	}
}
