/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidadesHerramientas;
import java.util.Scanner;

/**
 *
 * @author Bruches
 */
public class Validaciones {

    Scanner leer = new Scanner(System.in);

    public static boolean esMayorA(int numero1, int numero2) {
        return numero1 > numero2;
    }

    public static long validacionDuracionMinutos(long minutos, Scanner leer) {
        while (minutos <= 0) {
            System.out.println("Ingrese duracion en minutos:");
            String entrada = leer.nextLine().trim();

            try {
                minutos = Long.parseLong(entrada);
                if (minutos <= 0) {
                    System.out.println("La duracion no puede ser 0 o negativo. Reingrese:");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Ingrese un numero entero en minutos:");
                minutos = 0;
            }
        }
        return minutos;        
    }
}
