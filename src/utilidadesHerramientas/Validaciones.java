/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidadesHerramientas;

import cineModelos.Funcion;
import cineModelos.Sala;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Bruches
 */
public class Validaciones {

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

    public static int validarIdFuncion(Scanner leer, List<Funcion> listaDeFunciones) {
        int idValidada = -1;
        while (idValidada <= 0) {
            System.out.println("Ingrese id De Funcion: ");
            String idAValidar = leer.nextLine().trim();
            try {
                idValidada = Integer.parseInt(idAValidar);
                if (idValidada <= 0) {
                    System.out.println("El ID no puede ser 0 o negativo. Reingrese:");
                    idValidada = -1;
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Ingrese un numero entero para el ID:");
                idValidada = -1;
                continue;
            }

            if (listaDeFunciones != null && Funcion.buscarFuncionPorId(listaDeFunciones, idValidada) != -1) {
                System.out.println("La ID ya esta en uso. Ingrese otra.");
                idValidada = -1;
            }
        }
        return idValidada;
    }

    public static int validarFilas(Scanner leer, Sala sala) {
        int filasTotales = sala.filasTotales(sala);
        int fila = -1;
        while (fila <= 0) {
            System.out.println("Ingrese fila: ");
            String filaEnString = leer.nextLine().trim();
            try {
                fila = Integer.parseInt(filaEnString);
                if (fila <= 0) {
                    System.out.println("La fila no puede ser 0 o negativa. Reingrese:");
                    fila = -1;
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Ingrese una fila:");
                fila = -1;
                continue;
            }

            if (fila > filasTotales) {
                System.out.println("Valor de filas superado (maximo " + filasTotales + "), reingrese:");
                fila = -1;
            }
        }
        return fila;
    }

    public static int validarCantidadAsientos(Scanner leer, Sala sala) {
        int asientosTotales = sala.filasTotales(sala); // Reemplazar por el getter/metodo de asientos de Sala si difiere
        int asientos = -1;
        while (asientos <= 0) {
            System.out.println("Ingrese asiento: ");
            String asientoEnString = leer.nextLine().trim();
            try {
                asientos = Integer.parseInt(asientoEnString);
                if (asientos <= 0) {
                    System.out.println("El asiento no puede ser 0 o negativo. Reingrese:");
                    asientos = -1;
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Ingrese un numero de asiento:");
                asientos = -1;
                continue;
            }

            if (asientos > asientosTotales) {
                System.out.println("Valor del asiento superado (maximo " + asientosTotales + "), reingrese:");
                asientos = -1;
            }
        }
        return asientos;
    }
}
