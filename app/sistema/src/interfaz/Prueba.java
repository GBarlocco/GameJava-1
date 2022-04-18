package interfaz;

import sistema.*;
import java.util.*;

/**
 *
 * @author "Santiago Palacios - 248074 _ Gastón Barlocco - 241025"
 */

public class Prueba {
    // Variables de clase
    final public static String RESET_COLOR = "\u001B[0m";
    final public static String COLOR_ROJO = "\u001B[31m";
    final public static String COLOR_AZUL = "\u001B[34m";

    public static void main(String[] args) {
        Sistema sistema = new Sistema();
        mostrarMenu(sistema);
    }

    public static void mostrarMenu(Sistema sistema) {
        Scanner in = new Scanner(System.in);

        System.out.println( // Se muestra el menú.
                "- - - - - - - - - - - - - - - - - - - "
                        + "\nA- Registrar usuario "
                        + "\nB- Jugar"
                        + "\nC- Ver ranking "
                        + "\nD- Fin"
                        + "\n- - - - - - - - - - - - - - - - - - - ");

        // Se lee la opción seleccionada.
        String opcion = in.nextLine().toUpperCase();

        switch (opcion) { // Se ingresa la opción indicada.
            case "A" -> ingresarJugador(sistema);

            case "B" -> jugar(sistema);

            case "C" -> verRanking(sistema);

            case "D" -> {
                /* FIN */ }

            default -> {
                System.out.println("Dato incorrecto");
                mostrarMenu(sistema);
            }
        }
    }

    public static void ingresarJugador(Sistema sistema) {
        Scanner in = new Scanner(System.in);
        System.out.println("");
        boolean registrar = false;
        boolean confirmo = false;
        String nombre = "";
        String alias = "";
        int edad = 0;

        // Booleanos para iterar el ingreso de datos.
        boolean nombreValido = false;
        boolean aliasValido = false;
        boolean edadValida = false;

        while (!nombreValido) {// Se ingresa el nombre hasta que sea válido.
            // Preguntamos el nombre.
            System.out.print("Ingrese el nombre del jugador: ");
            nombre = in.nextLine();
            System.out.println("");

            // Lo validamos.
            if (Jugador.nombreValido(nombre)) {
                nombreValido = true;
            } else {
                System.out.println("Este nombre no es válido");
                System.out.println("(solo se aceptan letras y espacios)");
            }
        }

        while (!edadValida) { // Se ingresa la edad hasta que sea válida.
            try { // Pedimos la edad.
                System.out.print("La edad debe estar entre los 6 y 98 años escrito con números."
                        + "\nIngrese la edad: ");
                edad = in.nextInt();
                edadValida = Jugador.edadValida(edad);
                System.out.println("");
            } catch (InputMismatchException e) {
                System.out.println("");
            } finally {
                in.nextLine();
            }
        }

        while (!aliasValido) { // Se ingresa el alias hasta que sea válido.
            // Se solicita el alias.
            System.out.print("Ingrese alias: ");

            // Se ingresa el alias.
            alias = in.nextLine();

            // Se verifica el alias
            if (sistema.aliasYaExistente(alias)) { // El alias es válido.
                aliasValido = true;
            } else { // El alias no es válido.
                System.out.println("Este alias ya está en uso o puede que no sea válido.\n");
            }
        }

        // Se pide confirmar el ingreso del jugador.
        System.out.println("\n\n\n¿Está seguro de ingresar este jugador?");
        System.out.println("(SI/NO)");

        while (!confirmo) { // Se itera hasta confirmar el ingreso o no ingreso del jugador.
            System.out.println("\nNombre: " + nombre + "\nEdad: " + edad + "\nAlias: " + alias); // Se muestran los
                                                                                                 // datos del jugador
                                                                                                 // ingresados
                                                                                                 // previamente.
            String respuesta = in.nextLine();
            // Se verifican los casos de respuesta.
            if (respuesta.equalsIgnoreCase("SI")) { // Confirmó el registro.
                registrar = true;
                confirmo = true;
            } else if (respuesta.equalsIgnoreCase("NO")) { // Anuló el registro.
                confirmo = true;
            } else { // Ingresó dato incorrecto.
                System.out.println("\n\n\n¿Está seguro de ingresar este jugador?\nDebe ingresarse: 'SI' o 'NO'");
            }
        }

        if (registrar) { // Si se confirmó el registro, se registra el jugador al sistema.
            sistema.registrarJugador(nombre, alias, edad);
        }

        // Fin del proceso, se vuelve al menú.
        mostrarMenu(sistema);
    }

    public static void jugar(Sistema sistema) {
        boolean finalizo = false;

        // Creación del tablero.
        pedirDatosParaJugar(sistema);

        // Se muestra el tablero.
        mostrarTablero(sistema);

        while (!finalizo) { // Se solicitan datos hasta que se termine el juego.
            // Al final de cada lectura y efectuar los cambios necesarios, se muestra el
            // tablero.
            leerDatoIngresado(sistema);

            // Se analiza/n posible/s ganador/es.
            Jugador[] ganador = sistema.ganador();
            Jugador jRojo = ganador[0];
            Jugador jAzul = ganador[1];

            // Se verifica si hubo ganador.
            if (!Objects.isNull(jRojo) || !Objects.isNull(jAzul)) {
                // Se comprueban casos de ganador
                if (!Objects.isNull(jRojo) && !Objects.isNull(jAzul)) { // Si ninguno es null, es porque ambos son
                                                                        // ganadores y por tanto es empate.
                    System.out.println("EMPATE");
                    finalizo = true;
                } else if (!Objects.isNull(jRojo)) { // Ganó el rojo.
                    System.out.println(
                            COLOR_ROJO + "GANÓ " + jRojo.getNombre() + " (" + jRojo.getAlias() + ")" + RESET_COLOR);
                    finalizo = true;
                } else if (!Objects.isNull(jAzul)) { // Ganó el azul.
                    System.out.println(
                            COLOR_AZUL + "GANÓ " + jAzul.getNombre() + " (" + jAzul.getAlias() + ")" + RESET_COLOR);
                    finalizo = true;
                }
                sistema.aplausos();
            }
        }

        // Se restablecen los datos y se vuelve al menú.
        sistema.reset();
        mostrarMenu(sistema);
    }

    public static void pedirDatosParaJugar(Sistema sistema) {
        // Ordenamos los jugadores por ID y los mostramos en una lista.
        sistema.ordenarPorId();
        listaJugadores(sistema);

        // Se solicita el ID del jugador rojo.
        System.out.print("Ingrese ID del jugador rojo: ");
        Jugador jugadorRojo = solicitarJugador(sistema, null);
        System.out.println("");

        // Se solicita el ID del jugador azul.
        System.out.print("Ingrese ID del jugador azul: ");
        Jugador jugadorAzul = solicitarJugador(sistema, jugadorRojo);
        System.out.println("");

        // Pedimos la carga de tablero.
        System.out.print("Carga de tablero:\n");
        System.out.print("1- Al azar\n");
        System.out.print("2- Precargado\n");
        boolean carga = opcionCarga();
        System.out.print("\n");

        // Pedimos el tamaño.
        System.out.print("Tamaño del tablero:\n");
        System.out.print("1- Grande\n");
        System.out.print("2- Chico\n");
        boolean tamanio = opcionTamanio();
        System.out.print("\n");

        // Se inicializa un nuevo tablero con los datos recibidos de la parte anterior.
        Tablero tablero = new Tablero(jugadorRojo, jugadorAzul, tamanio, carga);

        // Y se asigna el tablero al sistema.
        sistema.setTablero(tablero);
    }

    public static Jugador solicitarJugador(Sistema sistema, Jugador jugadorRojo) {
        Scanner in = new Scanner(System.in);
        Jugador jugador = null;
        boolean datoCorrecto = false;

        while (!datoCorrecto) { // Se solicitan datos hasta ser correctos.
            try {
                // Se ingresa el ID del jugador.
                int idJugador = in.nextInt();

                // Si es nulo, significa que se está ingresando al jugador Rojo, de lo contrario
                // se ingresa al azul.
                if (Objects.isNull(jugadorRojo)) { // Se está ingresando al jugador Rojo.

                    if (idJugador > 0 && idJugador < Jugador.getId()) {
                        jugador = sistema.buscarJugador(idJugador);
                        datoCorrecto = true;
                    } else {
                        listaJugadores(sistema);
                        System.out.println("Por favor, reingrese el número del jugador.\nNúmero entre: 1 y "
                                + (Jugador.getId() - 1));
                    }

                } else { // Se está ingresando al jugador Azul.

                    if ((idJugador > 0 && idJugador < Jugador.getId()) && idJugador != jugadorRojo.getIdJugador()) {
                        jugador = sistema.buscarJugador(idJugador);
                        datoCorrecto = true;
                    } else {
                        listaJugadores(sistema);
                        System.out.println("Por favor, reingrese el número del jugador.\nNúmero entre: 1 y "
                                + (Jugador.getId() - 1) + " a excepción del " + jugadorRojo.getIdJugador());
                    }

                }
            } catch (InputMismatchException e) {
                // Entró en error y vuelve a mostrar la lista y solicitar datos.
                listaJugadores(sistema);

                // Si es nulo, significa que se está ingresando al jugador Rojo, de lo contrario
                // se ingresa al azul.
                if (Objects.isNull(jugadorRojo)) { // Se está ingresando al jugador Rojo.
                    System.out.println(
                            "Por favor, reingrese el número del jugador.\nNúmero entre: 1 y " + (Jugador.getId() - 1));
                } else { // Se está ingresando al jugador Azul.
                    System.out.println("Por favor, reingrese el número del jugador.\nNúmero entre: 1 y "
                            + (Jugador.getId() - 1) + " a excepción del " + jugadorRojo.getIdJugador());
                }

            } finally {
                in.nextLine(); // Esto se hace por la solicitud de datos int.
            }
        }
        return jugador;
    }

    public static boolean opcionCarga() {
        Scanner in = new Scanner(System.in);

        boolean opcionCarga = false;
        boolean datoCorrecto = false;

        while (!datoCorrecto) { // Se solicitan datos hasta que se ingrese '1' o '2'.
            try {
                int carga = in.nextInt();

                datoCorrecto = true;
                switch (carga) {
                    case 1 -> opcionCarga = false;
                    case 2 -> opcionCarga = true;
                    default -> {
                        datoCorrecto = false;
                        System.out.println("Dato incorrecto.\nPor favor ingrese: '1' o '2'.");
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Dato incorrecto.\nPor favor ingrese: '1' o '2'.");
                in.nextLine();
            }
        }

        return opcionCarga;
    }

    public static boolean opcionTamanio() {
        Scanner in = new Scanner(System.in);

        boolean opcionTamanio = false;
        boolean datoCorrecto = false;

        while (!datoCorrecto) { // Se solicitan datos hasta que se ingrese '1' o '2'.
            try {
                int carga = in.nextInt();

                datoCorrecto = true;
                switch (carga) {
                    case 1 -> opcionTamanio = true;
                    case 2 -> opcionTamanio = false;
                    default -> {
                        datoCorrecto = false;
                        System.out.println("Dato incorrecto.\nPor favor ingrese: '1' o '2'.");
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Dato incorrecto.\nPor favor ingrese: '1' o '2'.");
                in.nextLine();
            }
        }

        return opcionTamanio;
    }

    public static void verRanking(Sistema sistema) {
        Scanner in = new Scanner(System.in);
        boolean datoCorrecto = false;

        while (!datoCorrecto) { // Se muestra el menú hasta que se ingrese '1' o '2'.
            System.out.println("- - - - - - - - - - - - - - - - - - - ");
            System.out.println("1 - Ordenar por nombre");
            System.out.println("2 - Ordenar por puntaje");
            System.out.println("- - - - - - - - - - - - - - - - - - - ");

            try {
                int opcion = in.nextInt();

                switch (opcion) {
                    case 1 -> {
                        datoCorrecto = true;
                        sistema.ordenarPorNombre();
                        listaJugadores(sistema);
                    }

                    case 2 -> {
                        datoCorrecto = true;
                        sistema.ordenarPorPuntaje();
                        listaJugadores(sistema);
                    }

                    default -> {
                        System.out.println("\nDato incorrecto.\nPor favor ingrese: '1' o '2'.");
                        verRanking(sistema);
                    }
                }

                // finaliza el proceso y se regresa al menú.
                mostrarMenu(sistema);

            } catch (InputMismatchException e) {
                System.out.println("\nDato incorrecto.\nPor favor ingrese: '1' o '2'.");
            } finally {
                in.nextLine();
            }
        }
    }

    public static void listaJugadores(Sistema sistema) {
        ArrayList<Jugador> listaJugadores = sistema.getListaJugadores();

        System.out.println("\nID | NOMBRE - ALIAS - EDAD - VICTORIAS");
        // Carga cada jugador en la lista con el toString de Jugador.
        for (int i = 0; i < listaJugadores.size(); i++) {
            System.out.println(listaJugadores.get(i));
        }
        System.out.println("");
    }

    public static void leerDatoIngresado(Sistema sistema) {
        Scanner ok = new Scanner(System.in);
        Tablero tablero = sistema.getTablero();
        boolean datoCorrecto = false;
        boolean seRindio = false;

        while (!datoCorrecto) { // Se repite hasta ingresar un dato válido.
            // Se solicita jugada.
            if (tablero.getTurnoRojo()) {
                if (tablero.getPrimerTurno()) {
                    System.out.print(COLOR_ROJO + "Jugador rojo ingrese la primer jugada:");
                } else {
                    System.out.print(COLOR_ROJO + "Jugador rojo ingrese la segunda jugada: ");
                }
            } else {
                if (tablero.getPrimerTurno()) {
                    System.out.print(COLOR_AZUL + "Jugador azul ingrese la primer jugada: ");
                } else {
                    System.out.print(COLOR_AZUL + "Jugador azul ingrese la segunda jugada: ");
                }
            }
            System.out.print(RESET_COLOR + " ");

            // Se ingresa jugada.
            String jugada = ok.nextLine();
            System.out.println("");

            // Se interpreta lo ingresado.
            if (!(jugada.equalsIgnoreCase("x"))) { // Curso natural del programa, se ingresa posible jugada.
                datoCorrecto = sistema.interpretarDato(jugada);
                mostrarTablero(sistema);
                if (!datoCorrecto) {
                    System.out.println("Jugada incorrecta");
                }
            } else { // Curso alternativo, el jugador ingresa X.
                if (confirmarRendirse(sistema)) {
                    seRindio = true;
                    tablero.rendirse();
                }
                datoCorrecto = true;
            }
        }

        // Se notifica si algún jugador consigue alguna figura por primera vez.
        if (!seRindio) {
            if (!sistema.getNotificoFRoja() && tablero.getJRojo().getFiguraF()) {
                System.out.println(COLOR_ROJO + "Jugador rojo logró figura F" + RESET_COLOR);
                sistema.setNotificoFRoja(true);
            } else if (!sistema.getNotificoDRoja() && tablero.getJRojo().getFiguraD()) {
                System.out.println(COLOR_ROJO + "Jugador rojo logró figura D" + RESET_COLOR);
                sistema.setNotificoDRoja(true);
            }
            if (!sistema.getNotificoFAzul() && tablero.getJAzul().getFiguraF()) {
                System.out.println(COLOR_AZUL + "Jugador azul logró figura F" + RESET_COLOR);
                sistema.setNotificoFAzul(true);
            } else if (!sistema.getNotificoDAzul() && tablero.getJAzul().getFiguraD()) {
                System.out.println(COLOR_AZUL + "Jugador azul logró figura D" + RESET_COLOR);
                sistema.setNotificoDAzul(true);
            }
        }
    }

    public static boolean confirmarRendirse(Sistema sistema) {
        Scanner in = new Scanner(System.in);
        boolean turnoRojo = sistema.getTablero().getTurnoRojo();
        boolean confirmacion = false;
        boolean seRinido = false;
        String input;
        String color;

        // Dependiendo del turno, seteamos el color a mostrar en la consola.
        if (turnoRojo) {
            color = COLOR_ROJO;
        } else {
            color = COLOR_AZUL;
        }

        while (!confirmacion) { // Se repite el proceso hasta que el jugador ingrese "SI" o "NO".
            mostrarTablero(sistema);
            System.out.println(color + "Seguro que deseas rendirte?\n" + RESET_COLOR + "(SI / NO)");
            input = in.nextLine();
            if (input.equalsIgnoreCase("SI")) {
                confirmacion = true;
                seRinido = true;
            } else if (input.equalsIgnoreCase("NO")) {
                confirmacion = true;
            }
        }
        return seRinido;
    }

    public static void mostrarTablero(Sistema sistema) {
        // Muestra tablero según cómo se haya seteado el formato.
        boolean grande = sistema.getTablero().getGrande();
        if (grande) {
            tableroG(sistema);
        } else {
            tableroC(sistema);
        }
    }

    public static void tableroC(Sistema sistema) {
        int[][] tableroJuego = sistema.getTablero().getTableroJuego();
        String tablero = sistema.getTablero().toString();

        // Creación del borde superior.
        String retorno = "   1 2 3 4 \n  +-+-+-+-+\n";

        // Recorremos el contenido de las celdas como String de "F", "D" y " ".
        for (int i = 0; i < tablero.length(); i++) {

            // Creación del lateral izquierdo.
            switch (i) {
                case 0 -> retorno += "A |";

                case 4 -> retorno += "B |";

                case 8 -> retorno += "C |";

                case 12 -> retorno += "D |";
            }

            // Colocación de fichas con colores: La letra la contiene el toString de Tablero
            // y los colores se guían según el valor de la posición tablero.
            if (tableroJuego[(int) (i / 4)][i % 4] > 0) {
                retorno += COLOR_ROJO + tablero.charAt(i) + RESET_COLOR;
            } else if (tableroJuego[(int) (i / 4)][i % 4] < 0) {
                retorno += COLOR_AZUL + tablero.charAt(i) + RESET_COLOR;
            } else {
                retorno += " ";
            }

            // Separador de columnas.
            retorno += "|";

            // Separador de filas.
            if (i == 3 || i == 7 || i == 11 || i == 15) {
                retorno += "\n  +-+-+-+-+\n";
            }
        }

        // Finalmente mostramos el tablero.
        System.out.println(retorno);

    }

    public static void tableroG(Sistema sistema) {
        int[][] tableroJuego = sistema.getTablero().getTableroJuego();
        // Creamos una matriz para almacenar cada contenido de celda como un array de
        // String.
        String[][] celdas = new String[16][5];

        // Colocamos cada letra en grande como array de String dentro de cada celda.
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 5; j++) {
                switch (tableroJuego[(int) (i / 4)][i % 4]) {
                    case 2 -> celdas[i][j] = COLOR_ROJO + DGrande()[j] + RESET_COLOR;

                    case 1 -> celdas[i][j] = COLOR_ROJO + FGrande()[j] + RESET_COLOR;

                    case 0 -> celdas[i][j] = vacioGrande()[j];

                    case -1 -> celdas[i][j] = COLOR_AZUL + FGrande()[j] + RESET_COLOR;

                    case -2 -> celdas[i][j] = COLOR_AZUL + DGrande()[j] + RESET_COLOR;
                }
            }
        }

        // Generamos la cabecera del tablero.
        System.out.println("      1     2     3     4   \n   +-----+-----+-----+-----+");

        for (int fila = 0; fila < 4; fila++) { // Recorremos las filas
            for (int j = 0; j < 5; j++) { // Recorremos cada fila en cinco partes.

                // Generamos el lateral izquierdo.
                if (j == 2) {
                    // Cuando nos encontramos en el medio de la fila agregamos al comienzo la letra
                    // que representa la fila.
                    switch (fila) {
                        case 0 -> System.out.print(" A |");
                        case 1 -> System.out.print(" B |");
                        case 2 -> System.out.print(" C |");
                        case 3 -> System.out.print(" D |");
                    }

                } else {
                    System.out.print("   |");
                }

                // Insertamos las 4 letras de la primer fila por partes.
                for (int i = 0; i < 4; i++) {
                    System.out.print(celdas[fila * 4 + i][j] + "|");

                }

                // Se imprime la siguiente línea de la fila.
                System.out.println("");
            }

            // Se imprime el separador de filas.
            System.out.println("   +-----+-----+-----+-----+");
        }
    }

    public static String[] vacioGrande() {
        String[] vacio = { "     ", "     ", "     ", "     ", "     " };
        return vacio;
    }

    public static String[] FGrande() {
        String[] F = { "FFFFF", "F    ", "FFF  ", "F    ", "F    " };
        return F;
    }

    public static String[] DGrande() {
        String[] D = { "DDDD ", "D   D", "D   D", "D   D", "DDDD " };
        return D;
    }
}