import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class main {

    public static class Matriz {
        // Atributos
        int filas;
        int columnas;
        int tamano;
        char[][] celdas;
        List<String> soluciones = new ArrayList<>();

        // Constructor
        public Matriz(int filas, int columnas) {
            this.filas = filas;
            this.columnas = columnas;
            tamano = filas * columnas;
            celdas = new char[filas][columnas];
        }
    }

    // Variables globales
    static List<String> palabras = new ArrayList<>(Arrays.asList("JUAN", "PABLO", "ISAZA", "MARIN"));
    static char[] abecedario = {'A', 'B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R',
            'S','T','U','V','W','X','Y','Z'};
    public static final int[][] DIRECCIONES = {{1,0}, {0,1}, {1,1}, {1,-1}, {-1,0}, {0,-1}, {-1,-1}, {-1,1}};
    public static final Random RANDOM = new Random();

    public static void main(String[] args) {
        // Inicializamos variables
        Matriz matriz = new Matriz(10, 5);
        // Creamos la sopa de letras
        matriz = sopaDeLetras(matriz.filas, matriz.columnas, palabras);

        // Llenamos vacíos en la matriz
        for (int i = 0; i < matriz.filas; i++) {
            for (int j = 0; j < matriz.columnas; j++) {
                if(matriz.celdas[i][j] == 0)	matriz.celdas[i][j] = abecedario[RANDOM.nextInt(abecedario.length)];
            }
        }

        // Imprimimos la matriz en consola y en un archivo
        imprimir(matriz);
        try {
            imprimirArchivo(matriz, "output.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Matriz sopaDeLetras(int filas, int columnas, List<String> palabras) {
        /*
         * FUNCIÓN QUE RECIBE COMO PARÁMETROS EL NÚMERO DE FILAS, NÚMERO DE COLUMNAS
         * Y UN ARREGLO CON LAS PALABRAS QUE QUEREMOS AGREGAR A LA SOPA DE LETRAS.
         * DEVUELVE UNA MATRIZ QUE CONTIENE LA SOPA DE LETRAS RESULTANTE.
         */

        // inicializamos variables
        Matriz matriz = null;
        Collections.shuffle(palabras); // revolvemos las palabras del arreglo
        matriz = new Matriz(filas, columnas);

        // recorremos todas las palabras en al arreglo e intentamos agregarlas
        for (String palabra : palabras) {
            ponerPalabra(matriz, palabra);
        }

        return matriz;
    }

    public static int ponerPalabra(Matriz matriz, String palabra) {
        /*
         * Función que recibe como parámetros una matriz, y una palabra que
         * se quiere poner en la sopa de letras.
         * Devuelve un entero, que es la posición de la primera letra, en caso
         * de que se pueda poner, y cero en caso de que no se pueda poner la palabra.
         */

        // inicialización variables
        int randDir = RANDOM.nextInt(DIRECCIONES.length);
        int randPos = RANDOM.nextInt(matriz.tamano);

        // ciclo para recorrer las posibles direcciones en la matriz
        for (int dir = 0; dir < DIRECCIONES.length; dir++) {
            dir = (dir + randDir) % DIRECCIONES.length;

            // ciclo para recorrer la matriz
            for (int pos = 0; pos < matriz.tamano; pos++) {
                pos = (pos + randPos) % matriz.tamano;
                int lugaresLetra = intentarUbicacion(matriz, palabra, dir, pos);

                if (lugaresLetra > 0)
                    return lugaresLetra;
            }
        }

        return 0;
    }

    public static int intentarUbicacion(Matriz matriz, String s, int dir, int pos) {
        /*
         * Función que recibe como parámetros una matriz, una cadena, una dirección,
         * y una posición, para intentar ubicar una palabra en la sopa de letras.
         * Devuelve el lugar de la letra en la matriz, en caso de que logre ubicarla,
         * devuelve cero en caso contrario.
         */

        // inicialización de variables
        int r = pos / matriz.columnas;
        int c = pos % matriz.columnas;
        int length = s.length();

        // comprobaciones iniciales
        if ((DIRECCIONES[dir][0] == 1 && (length + c) > matriz.columnas)
                || (DIRECCIONES[dir][0] == -1 && (length - 1) > c)
                || (DIRECCIONES[dir][1] == 1 && (length + r) > matriz.filas)
                || (DIRECCIONES[dir][1] == -1 && (length - 1) > r))
            return 0;

        // inicialización variables auxiliares
        int i, rr, cc, cruzan = 0;

        // ciclo para probar las posiciones en la matriz
        for (i = 0, rr = r, cc = c; i < length; i++) {
            if (matriz.celdas[rr][cc] != 0 && matriz.celdas[rr][cc] != s.charAt(i))
                return 0;

            cc += DIRECCIONES[dir][0];
            rr += DIRECCIONES[dir][1];
        }

        for (i = 0, rr = r, cc = c; i < length; i++) {
            if (matriz.celdas[rr][cc] == s.charAt(i))
                cruzan++;
            else matriz.celdas[rr][cc] = s.charAt(i);

            if (i < length - 1) {
                cc += DIRECCIONES[dir][0];
                rr += DIRECCIONES[dir][1];
            }
        }

        int lugaresLetra = length - cruzan;

        if (lugaresLetra > 0)
            matriz.soluciones.add(String.format("%-10s (%d, %d)(%d,%d)", s, c, r, cc, rr));

        return lugaresLetra;
    }

    public static void imprimir(Matriz matriz) {

        int size = matriz.soluciones.size();
        System.out.print("   ");

        for (int i = 0; i < matriz.columnas; i++) {
            System.out.print(i + "  ");
        }

        System.out.println();

        for (int r = 0; r < matriz.filas; r++) {
            System.out.printf("%n%d ", r);

            for(int c = 0; c < matriz.columnas; c++ ) {
                System.out.printf(" %c ", matriz.celdas[r][c]);
            }
        }

        System.out.println("\n");

        for (int i = 0; i < size - 1; i += 2) {
            System.out.printf("%s %s%n", matriz.soluciones.get(i), matriz.soluciones.get(i + 1));
        }

        if (size % 2 == 1) {
            System.out.println(matriz.soluciones.get(size - 1));
        }
    }

    public static void imprimirArchivo(Matriz matriz, String filename) throws IOException {
        FileWriter fileWriter = new FileWriter(filename);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        int size = matriz.soluciones.size();

        printWriter.print("   ");

        for (int i = 0; i < matriz.columnas; i++) {
            printWriter.print(i + "  ");
        }

        printWriter.println();

        for (int r = 0; r < matriz.filas; r++) {
            printWriter.printf("%n%d ", r);

            for(int c = 0; c < matriz.columnas; c++ ) {
                printWriter.printf(" %c ", matriz.celdas[r][c]);
            }
        }

        printWriter.println("\n");

        for (int i = 0; i < size - 1; i += 2) {
            printWriter.printf("%s %s%n", matriz.soluciones.get(i), matriz.soluciones.get(i + 1));
        }

        if (size % 2 == 1) {
            printWriter.println(matriz.soluciones.get(size - 1));
        }

        printWriter.close();
    }

    public static void imprimirMatriz(Matriz matriz) {
        for(int i = 0; i < matriz.filas; i++) {
            for (int j = 0; j < matriz.columnas; j++)
                System.out.print(matriz.celdas[i][j] + " ");
            System.out.println();
        }
    }

}
