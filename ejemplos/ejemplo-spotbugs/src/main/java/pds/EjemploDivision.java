package pds;

import java.util.Random;

/**
 * En este ejemplo spotbugs no detecta que puede haber una posible división por 0.
 */
public class EjemploDivision {
    public static void main(String[] args) {
        int divisor = obtenerDivisor(); 
        
        // Posible división por cero detectada por SpotBugs
        int resultado = 100 / divisor; 
        System.out.println("Resultado: " + resultado);
    }

    private static Random random = new Random();
    
    // puede devolver 0
    static int obtenerDivisor() {
        // return (int)(Math.random() * 2); // 0 o 1
    	// return new Random().nextInt(1);
    	return random.nextInt(1);
    }
}

