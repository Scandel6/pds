package pds;

public class EjemploEquals {
    public static void main(String[] args) {
        String saludo1 = "Hola";
        String saludo2 = new String("Hola");

        compararCadenas(saludo1, saludo2);
    }

	private static void compararCadenas(String saludo1, String saludo2) {
		if (saludo1 == saludo2) {
            System.out.println("Son iguales");
        } else {
            System.out.println("Son diferentes");
        }
	}
}
