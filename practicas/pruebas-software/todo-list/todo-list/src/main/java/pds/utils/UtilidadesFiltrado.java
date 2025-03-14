package pds.utils;

public class UtilidadesFiltrado {

	/**
	 * Comprueba si el texto dado como entrada satisface el patrón dado.
	 * Un patrón está compuesto de caracteres que tienen que encajar exáctamente
	 * en el texto y de '*' que significa que encaja 0 o más ocurrencias de caracteres.
	 * 
	 * Por ejemplo, esCompatibleConPatron('abc', 'a*') => true
	 *              esCompatibleConPatron('abc', 'a*d') => false
	 */
	public static boolean esCompatibleConPatron(String entrada, String patron) {
        int entradaIdx = 0;
        int patronIdx = 0; 
        int asteriscoIdx = -1;
        int match = 0;
        
        while (entradaIdx < entrada.length()) {    
        	if (patronIdx < patron.length() && 
            	(patron.charAt(patronIdx) == entrada.charAt(entradaIdx) || 
            		patron.charAt(patronIdx) == '*')) {
                
            	if (patron.charAt(patronIdx) == '*') {
                    asteriscoIdx = patronIdx;
                    match = entradaIdx;
                    patronIdx++;
                } else {
                	//asteriscoIdx = -1;
                    entradaIdx++;
                    patronIdx++;
                }
            } else if (asteriscoIdx != -1) {
                patronIdx = asteriscoIdx + 1;
                match++;
                entradaIdx = match;
            } else {
                return false;
            }
        }
        
        // Una vez que la entrada se ha agotado, lo único válido sería tener '*'
        while (patronIdx < patron.length() && patron.charAt(patronIdx) == '*') {
            patronIdx++;
        }
        return patronIdx == patron.length();
    }

}
