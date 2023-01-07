/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Saul Rodriguez Naranjo
 */
public class AFND implements Proceso, Cloneable {

    private String[] estadosFinales;
    private ArrayList<TransicionAFND> transiciones;
    private ArrayList<TransicionLambda> transicionesLambda;
    private String estadoInicial;

    /**
     * Constructor del Automata Finito No Determinista, inicializa dos ArrayList
     * de transiciones AFND y Lambda
     */
    public AFND() {
        this.transiciones = new ArrayList<>();
        this.transicionesLambda = new ArrayList<>();
    }

    /**
     * Agrega una transicion AFND al automata
     *
     * @param e1 Estado de Origen
     * @param simbolo Simbolo para moverse del Estado de Origen a los
     * respectivos estados finales
     * @param e2 Estados Finales
     */
    public void agregarTransicion(String e1, char simbolo, ArrayList<String> e2) {
        this.transiciones.add(new TransicionAFND(e1, simbolo, e2));
    }

    /**
     * Agrega una transicion Lambda al automata
     *
     * @param e1 Estado de Origen
     * @param e2 Estados Finales
     */
    public void agregarTransicionLambda(String e1, ArrayList<String> e2) {
        this.transicionesLambda.add(new TransicionLambda(e1, e2));
    }

    /**
     * Devuelve los Estados Finales de un macro estado
     *
     * @param estado El macro estado desde el que se transita
     * @param simbolo Simbolo para transitar de los Estados de Origen a los
     * Finales
     * @return El Conjunto de Estados Finales de los Estados de Origen
     * insertados
     */
    public ArrayList<String> transicionAFND(ArrayList<String> estado, char simbolo) {
        ArrayList<String> estadosTransitados = new ArrayList<>();

        for (int i = 0; i < estado.size(); i++) {
            ArrayList<String> aux = this.transicionIndividual(estado.get(i), simbolo);
            for (int j = 0; j < aux.size(); j++) {
                if (!estadosTransitados.contains(aux.get(j))) {
                    estadosTransitados.add(aux.get(j));
                }
            }
        }

        return estadosTransitados;
    }

    /**
     * Devuelve los Estados Finales de un unico Estado de Origen tras insertar
     * un simbolo.
     *
     * @param estadoOrigen Estado de Origen desde el que se parte
     * @param simbolo Simbolo para transitar a los Estados Finales
     * @return Los Estados Finales que dan lugar tras insertar el simbolo al
     * Estado de Origen
     */
    public ArrayList<String> transicionIndividual(String estadoOrigen, char simbolo) {

        ArrayList<String> estadosFinales = null;
        for (int i = 0; i < this.transiciones.size(); i++) {
            if (this.transiciones.get(i).getEstadoOrigen().equals(estadoOrigen)
                    && this.transiciones.get(i).getSimbolo() == simbolo) {
                estadosFinales = this.transiciones.get(i).getEstadosFinales();
            }
        }

        return estadosFinales;
    }

    /**
     * Devuelve los Estados Lambda Finales de un unico Estado de Origen
     *
     * @param estado Estado de Origen desde el que se transita
     * @return Estados Finales Lambda
     */
    public ArrayList<String> transicionLambdaIndividual(String estado) {
        boolean encontrado = false;
        int i = 0;

        while (i < this.transicionesLambda.size() && !encontrado) {
            if (this.transicionesLambda.get(i).getEstadoOrigen().equals(estado)) {
                encontrado = true;
            } else {
                i++;
            }
        }

        if (encontrado) {
            return this.transicionesLambda.get(i).getEstadosFinales();
        } else {
            ArrayList<String> transLambda = new ArrayList<>();
            transLambda.add(estado);
            return transLambda;
        }
    }

    /**
     * Devuelve el conjunto de transiciones lambda de un macroestado
     *
     * @param estado Macroestado
     * @return El conjunto de transiciones lambda
     */
    public ArrayList<String> transicionLambda(ArrayList<String> estados) {
        ArrayList<String> resultado = new ArrayList<>();

        for (int i = 0; i < estados.size(); i++) {
            if (!resultado.contains(estados.get(i))) {
                resultado.add(estados.get(i));
                this.lambdaRecursivo(resultado, estados.get(i));
            }
        }

        return resultado;
    }

    /**
     * De forma recursiva busca todas las pòsibles transiciones lambda a partir
     * de un estado y las deposita en un array
     *
     * @param lambda Array de estados de destino lambda
     * @param estadoActual Estado lambda actual
     */
    public void lambdaRecursivo(ArrayList<String> lambda, String estadoActual) {
        ArrayList<String> aux = this.transicionLambdaIndividual(estadoActual);
        //System.out.println("length: " + aux.size());
        if (aux.size() == 2) {
            if (!lambda.contains(aux.get(1))) {
                lambda.add(aux.get(1));
                this.lambdaRecursivo(lambda, aux.get(1));
            }
        }

    }

    /**
     * Devuelve una transicion completa del Automata AFND
     *
     * @param estado Estado de Origen
     * @param simbolo Simbolo para la transicion
     * @return La transicion completa
     */
    public ArrayList<String> transicion(ArrayList<String> estado, char simbolo) {

        ArrayList<String> AFNDTransitions = this.transicionAFND(estado, simbolo);

        return this.lambdaClausura(AFNDTransitions);

    }

    /**
     * Devuelve la lambda clausura de un macroestado
     *
     * @param macroestado Macroestado al que aplicar la Lambda Clausura
     * @return Lambda Clausura del Macroestado
     */
    public ArrayList<String> lambdaClausura(ArrayList<String> macroestado) {
        return this.transicionLambda(macroestado);
    }

    /**
     * Devuelve si alguno de los Estados del macroestado es un Estado Final
     *
     * @param macroestado Conjunto de Estados de Origen
     * @return Verdadero o Falso en funcion de si hay un Estado Final o no
     */
    public boolean esFinal(ArrayList<String> macroestado) {
        for (int i = 0; i < macroestado.size(); i++) {
            if (esFinal(macroestado.get(i))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Devuelve si un unico Estado es un Estado Final
     *
     * @param estado
     * @return Verdero o falso en funcion de si el Estado es Final o no
     */
    @Override
    public boolean esFinal(String estado) {
        for (int i = 0; i < this.estadosFinales.length; i++) {
            if (estado.equals(this.estadosFinales[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * Devuelve si una determinada cadena de simbolos es aceptada o no por el
     * automata.
     *
     * @param cadena Cadena de Simbolos para el automata
     * @return Si se reconoce la cadena
     */
    @Override
    public boolean reconocer(String cadena) {
        char[] simbolo = cadena.toCharArray();
        ArrayList<String> estado = new ArrayList<>();
        estado.add(this.estadoInicial);
        ArrayList<String> macroestado = this.lambdaClausura(estado);
        System.out.println(macroestado);
        for (int i = 0; i < simbolo.length; i++) {
            macroestado = transicion(macroestado, simbolo[i]);
            System.out.println(macroestado);
        }

        return esFinal(macroestado);
    }

    /**
     * Convierte a String un automata AFND
     *
     * @return Cadena de caracteres que describe al automata
     */
    @Override
    public String toString() {
        String devolver = "";

        devolver += "Estado Inicial: " + this.estadoInicial + "\n";
        devolver += "Estados Finales: ";

        for (int i = 0; i < this.estadosFinales.length; i++) {
            devolver += this.estadosFinales[i] + " ";
        }

        devolver += "\nTRANSICIONES:\n";

        for (int i = 0; i < this.transiciones.size(); i++) {
            devolver += this.transiciones.get(i).toString() + "\n";
        }

        devolver += "TRANSICIONES LAMBDA:\n";

        for (int i = 0; i < this.transicionesLambda.size(); i++) {
            devolver += this.transicionesLambda.get(i).toString() + "\n";
        }

        return devolver;
    }

    /**
     * Dada una ruta a un fichero con un Automata AFND definido, se construira
     * el mismo dentro del programa.
     *
     * @param ruta
     * @param automata
     */
    public static void leeFichero(String ruta, AFND automata) {
        FileReader entrada = null;
        try {
            File archivo = new File(ruta);
            entrada = new FileReader(archivo);

            BufferedReader buffer = new BufferedReader(entrada);
            String linea = "";
            String split_linea[] = null;

            //Leemos las dos primeras lineas, "ESTADOS:" e "INICIAL:" 
            //que no nos interesan
            for (int i = 0; i < 2; i++) {
                linea = buffer.readLine();
                if (i == 1) {
                    String[] split = linea.split(" ");
                    automata.estadoInicial = split[1];
                }
            }

            //Leemos los estados finales y los añadimos a nuestro automata
            linea = buffer.readLine();
            split_linea = linea.split(" ");
            String[] estadosFinales = new String[split_linea.length - 1];
            for (int i = 0; i < estadosFinales.length; i++) {
                estadosFinales[i] = split_linea[i + 1];
            }
            automata.estadosFinales = estadosFinales;
            //SE LEERA LA CADENA "TRANSICIONES:" y se desecha
            linea = buffer.readLine();

            //Comenzamos a leer las transiciones e insertarlas en el automata
            while (!linea.equals("FIN")) {
                linea = buffer.readLine();
                if (!linea.equals("FIN")) {
                    split_linea = linea.split(" ");

                    String simbolo_array[] = split_linea[2].split("'");
                    char simbolo = simbolo_array[1].toCharArray()[0];
                    ArrayList<String> estadosDestino = new ArrayList<>();
                    for (int i = 3; i < split_linea.length; i++) {
                        estadosDestino.add(split_linea[i]);
                    }

                    //EN FUNCION DE SI TIENE UNA L (TRANSICION LAMBDA) O NO, SE 
                    //DEFINIRA UN TIPO DE TRANSICION U OTRA
                    if (simbolo != 'L') {
                        automata.agregarTransicion(split_linea[1], simbolo, estadosDestino);
                    } else {
                        automata.agregarTransicionLambda(split_linea[1], estadosDestino);
                    }

                }

            }

            entrada.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(AFD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AFD.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                entrada.close();
            } catch (IOException ex) {
                Logger.getLogger(AFD.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Construye el automataAFND de ejemplo utilizado en las clases de practicas
     *
     * @param automataAFND Un automata AFND para insertarle los estados
     */
    public static void construyeAFNDPorDefecto(AFND automataAFND) {

        automataAFND.estadoInicial = "a";
        automataAFND.estadosFinales = new String[2];
        automataAFND.estadosFinales[0] = "e";
        automataAFND.estadosFinales[1] = null;

        //TRANSICIONES NORMALES AFND
        ArrayList<String> transicionA0 = new ArrayList<>();
        transicionA0.add("a");
        transicionA0.add("d");
        automataAFND.agregarTransicion("a", '0', transicionA0);

        ArrayList<String> transicionA1 = new ArrayList<>();
        transicionA1.add("a");
        transicionA1.add("d");
        automataAFND.agregarTransicion("a", '1', transicionA1);

        ArrayList<String> transicionB0 = new ArrayList<>();
        transicionB0.add("d");
        automataAFND.agregarTransicion("b", '0', transicionB0);

        ArrayList<String> transicionB1 = new ArrayList<>();
        transicionB1.add("b");
        automataAFND.agregarTransicion("b", '1', transicionB1);

        ArrayList<String> transicionC0 = new ArrayList<>();
        transicionC0.add("M");
        automataAFND.agregarTransicion("c", '0', transicionC0);

        ArrayList<String> transicionC1 = new ArrayList<>();
        transicionC1.add("e");
        automataAFND.agregarTransicion("c", '1', transicionC1);

        ArrayList<String> transicionD0 = new ArrayList<>();
        transicionD0.add("e");
        automataAFND.agregarTransicion("d", '0', transicionD0);

        ArrayList<String> transicionD1 = new ArrayList<>();
        transicionD1.add("M");
        automataAFND.agregarTransicion("d", '1', transicionD1);

        ArrayList<String> transicionE0 = new ArrayList<>();
        transicionE0.add("M");
        automataAFND.agregarTransicion("e", '0', transicionE0);

        ArrayList<String> transicionE1 = new ArrayList<>();
        transicionE1.add("M");
        automataAFND.agregarTransicion("e", '1', transicionE1);

        ArrayList<String> transicionM0 = new ArrayList<>();
        transicionM0.add("M");
        automataAFND.agregarTransicion("M", '0', transicionM0);

        ArrayList<String> transicionM1 = new ArrayList<>();
        transicionM1.add("M");
        automataAFND.agregarTransicion("M", '1', transicionM1);

        //TRANSICIONES LAMBDA
        ArrayList<String> transicionAL = new ArrayList<>();
        transicionAL.add("a");
        transicionAL.add("b");
        automataAFND.agregarTransicionLambda("a", transicionAL);

        ArrayList<String> transicionBL = new ArrayList<>();
        transicionBL.add("b");
        transicionBL.add("c");
        automataAFND.agregarTransicionLambda("b", transicionBL);

        ArrayList<String> transicionCL = new ArrayList<>();
        transicionCL.add("c");
        transicionCL.add("d");
        automataAFND.agregarTransicionLambda("c", transicionCL);

        ArrayList<String> transicionDL = new ArrayList<>();
        transicionDL.add("d");
        automataAFND.agregarTransicionLambda("d", transicionDL);

        ArrayList<String> transicionEL = new ArrayList<>();
        transicionEL.add("e");
        automataAFND.agregarTransicionLambda("e", transicionEL);

        ArrayList<String> transicionML = new ArrayList<>();
        transicionML.add("M");
        automataAFND.agregarTransicionLambda("M", transicionML);
    }

    /**
     * Devuelve el Estado Inicila del Automata AFND
     *
     * @return El Estado Inicial
     */
    public ArrayList<String> getEstadoInicial() {
        ArrayList<String> inicial = new ArrayList<>();
        inicial.add(estadoInicial);
        return inicial;
    }

    /**
     * Devuelve una copia del Automata AFND
     *
     * @return Una Copia del Automata AFND
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        AFND automataCopia = new AFND();
        automataCopia.setEstadoInicial(this.estadoInicial);
        String[] estadosFinalesCopia = new String[this.estadosFinales.length - 1];
        for (int i = 0; i < this.estadosFinales.length; i++) {
            estadosFinalesCopia[i] = this.estadosFinales[i];
        }
        automataCopia.setEstadosFinales(estadosFinalesCopia);

        for (int i = 0; i < this.transiciones.size(); i++) {
            TransicionAFND transicionCopia = (TransicionAFND) this.transiciones.get(i).clone();
            automataCopia.agregarTransicion(transicionCopia.getEstadoOrigen(),
                    transicionCopia.getSimbolo(), transicionCopia.getEstadosFinales());
        }

        for (int i = 0; i < this.transicionesLambda.size(); i++) {
            TransicionLambda transicionesLambdaCopia = (TransicionLambda) this.transicionesLambda.get(i).clone();
            automataCopia.agregarTransicionLambda(transicionesLambdaCopia.getEstadoOrigen(),
                    transicionesLambdaCopia.getEstadosFinales());
        }

        return (Object) automataCopia;
    }

    /**
     * Asigna el Estado Inicial del Automata AFND
     *
     * @param estadoInicial El Estado Inicial
     */
    private void setEstadoInicial(String estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    /**
     * Asigna los Estados Finales del Automata AFND
     *
     * @param estadosFinalesCopia
     */
    private void setEstadosFinales(String[] estadosFinalesCopia) {
        this.estadosFinales = estadosFinalesCopia;
    }

}
