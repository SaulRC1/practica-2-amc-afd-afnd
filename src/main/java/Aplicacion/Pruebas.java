/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Aplicacion;

import Modelo.AFND;
import java.util.ArrayList;

/**
 *
 * @author Saul Rodriguez Naranjo
 */
public class Pruebas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        /*String prueba = "'1'";
        
        System.out.println(prueba);
        
        String[] split = prueba.split("\'");
        System.out.println("longitud: " + split.length);
        /*for (int i = 0; i < split.length; i++) {
            System.out.println(split[i]);
        }*/
        
        /*System.out.println("Split[0]: " + split[0]);
        System.out.println("Split[1]: " + split[1]);
        
        char simbolo = split[1].toCharArray()[0];
        
        System.out.println("Simbolo: " + simbolo);
        */
        
        /*String prueba2 = " q0 '0' q1";
        String[] split = prueba2.split(" ");
        
        for (int i = 0; i < split.length; i++) {
            System.out.println("[" + i + "]: " + split[i]);
        }*/
        
        AFND automataAFND = new AFND();
        
        AFND.construyeAFNDPorDefecto(automataAFND);
        
        ArrayList<String> inicio = new ArrayList<>();
        inicio.add("a");
        inicio.add("b");
        inicio.add("c");
        inicio.add("d");
        
        ArrayList<String> macroestado = new ArrayList<>();
        macroestado.add("b");
        macroestado.add("c");
        System.out.println(automataAFND.transicionLambda(macroestado));
        System.out.println(automataAFND);
        
        
        
    }
    
}
