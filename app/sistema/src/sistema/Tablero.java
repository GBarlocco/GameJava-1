package sistema;

/**
 *
 * @author "Santiago Palacios - 248074 _ Gastón Barlocco - 241025"
 */

/*
 * Interpretación de la jugada:
 * Luego de leer y validar la jugada, cargamos al tablero con un número entero,
 * el cual puede adquirir uno de los
 * siguientes valores:
 * 
 * 0 -> Espacio Vacío.
 * 1 -> Rojo F.
 * 2 -> Rojo D.
 * -1 -> Azul F.
 * -2 -> Azul D.
 * 
 * Por letra: jugador 1 -> Rojo, jugador 2 -> Azul
 * 
 * Ejemplo de Jugada1 = "M A1 A2". --> ["M", "A1", "A2"]
 * Ejemplo de Jugada2 = "I B3". --> ["I", "B3"]
 */

public class Tablero {
    // Variable de clase
    private static boolean turnoRojo;
    private static boolean primerTurno;

    // Variable de instancia.
    private int[][] tableroJuego = { { -1, 0, 2, -1 }, { 0, 0, 0, 0 }, { 0, -2, -1, 0 }, { 1, 2, 1, 0 } };
    private Jugador jRojo;
    private Jugador jAzul;
    private boolean grande;

    // Constructores

    public Tablero(Jugador jugadorRojo, Jugador jugadorAzul, boolean tamanio, boolean preCarga) {
        turnoRojo = true;
        primerTurno = true;
        if (!preCarga) {
            tableroRandom(tableroJuego);
        }
        jRojo = jugadorRojo;
        jAzul = jugadorAzul;
        grande = tamanio;
    }

    // Get.
    public boolean getTurnoRojo() {
        return turnoRojo;
    }

    public boolean getPrimerTurno() {
        return primerTurno;
    }

    public int[][] getTableroJuego() {
        return this.tableroJuego;
    }

    public Jugador getJRojo() {
        return this.jRojo;
    }

    public Jugador getJAzul() {
        return this.jAzul;
    }

    public boolean getGrande() {
        return this.grande;
    }

    // Set.
    public void setTurnoRojo(boolean turno) {
        Tablero.turnoRojo = turno;
    }

    public void setPrimerTurno(boolean turno) {
        Tablero.primerTurno = turno;
    }

    public void setTableroJuego(int[][] unTablero) {
        this.tableroJuego = unTablero;
    }

    public void setJRojo(Jugador unJugador) {
        this.jRojo = unJugador;
    }

    public void setJAzul(Jugador unJugador) {
        this.jAzul = unJugador;
    }

    public void setGrande(boolean tamanio) {
        this.grande = tamanio;
    }

    /*
     * Interpretación de la jugada:
     * Luego de leer y validar la jugada, cargamos al tablero con un número entero,
     * el cual puede adquirir uno de los
     * siguientes valores:
     * 
     * 0 -> Espacio Vacío.
     * 1 -> Rojo F.
     * 2 -> Rojo D.
     * -1 -> Azul F.
     * -2 -> Azul D.
     * 
     * Por letra: jugador 1 -> Rojo, jugador 2 -> Azul
     * 
     * Ejemplo de Jugada1 = "M A1 A2". --> ["M", "A1", "A2"]
     * Ejemplo de Jugada2 = "I B3". --> ["I", "B3"]
     */
    // Metodos generales

    public void invertir(int fila, int columna) {
        int ficha = this.getTableroJuego()[fila][columna];

        // Se invierte la ficha.
        switch (ficha) {
            case 1 -> ficha = 2;

            case 2 -> ficha = 1;

            case -1 -> ficha = -2;

            case -2 -> ficha = -1;

            default -> System.out.println("Algo funcionó mal.");
        }

        // Y seteamos el nuevo valor en el tablero.
        this.getTableroJuego()[fila][columna] = ficha;

    }

    public void mover(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
        // Intercambiamos contenidos de la matriz de donde está la ficha a mover y a
        // dónde se moverá, con un auxiliar.
        int datoDestino = this.getTableroJuego()[filaDestino][columnaDestino];

        this.getTableroJuego()[filaDestino][columnaDestino] = this.getTableroJuego()[filaOrigen][columnaOrigen];

        this.getTableroJuego()[filaOrigen][columnaOrigen] = datoDestino;

    }

    public void rendirse() {
        if (turnoRojo) {
            getJAzul().setFiguraF(true);
            getJAzul().setFiguraD(true);
        } else {
            getJRojo().setFiguraF(true);
            getJRojo().setFiguraD(true);
        }
    }

    // Vefiricar Figuras
    public void figuraF() {
        for (int i = 0; i < 4; i++) {
            // Verifiación de F Rojas por filas
            for (int j = 0; j < 4 && this.getTableroJuego()[i][j] == 1; j++) {
                if (j == 3) {
                    this.getJRojo().setFiguraF(true);
                }
            }
            // Verifiación de F Azules por filas
            for (int j = 0; j < 4 && this.getTableroJuego()[i][j] == -1; j++) {
                if (j == 3) {
                    this.getJAzul().setFiguraF(true);
                }
            }
            // Verifiación de F Rojas por columnas
            for (int j = 0; j < 4 && this.getTableroJuego()[j][i] == 1; j++) {
                if (j == 3) {
                    this.getJRojo().setFiguraF(true);
                }
            }
            // Verifiación de F Azules por columnas
            for (int j = 0; j < 4 && this.getTableroJuego()[j][i] == -1; j++) {
                if (j == 3) {
                    this.getJAzul().setFiguraF(true);
                }
            }
        }
    }

    public void figuraD() {
        // Recorremos las posiciones de la matriz a excepción de la última fila y última
        // columna.
        for (int i = 0; i < this.getTableroJuego().length - 1; i++) {
            for (int j = 0; j < this.getTableroJuego().length - 1; j++) {
                // Verificamos si hay un cuadrado de D rojas, incluyendo la posiciòn evaluada,
                // la de la derecha, la de abajo y la de abajo a la derecha.
                if ((this.getTableroJuego()[i][j] == 2 && this.getTableroJuego()[i + 1][j + 1] == 2)
                        && (this.getTableroJuego()[i][j + 1] == 2 && this.getTableroJuego()[i + 1][j] == 2)) {
                    this.getJRojo().setFiguraD(true);
                }
                // Verificamos si hay un cuadrado de D azules, incluyendo la posiciòn evaluada,
                // la de la derecha, la de abajo y la de abajo a la derecha.
                if ((this.getTableroJuego()[i][j] == -2 && this.getTableroJuego()[i + 1][j + 1] == -2)
                        && (this.getTableroJuego()[i][j + 1] == -2 && this.getTableroJuego()[i + 1][j] == -2)) {
                    this.getJAzul().setFiguraD(true);
                }
            }
        }
    }

    // Seteo tablero random.
    private static void resetearTablero(int[][] tablero) {
        // Se utiliza para que al randomizar el tablero, la matriz esté vacía.
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero.length; j++) {
                tablero[i][j] = 0;
            }
        }
    }

    private static void tableroRandom(int[][] tablero) {
        resetearTablero(tablero);
        int fichasRojas = 0;
        int fichasAzules = 0;
        // Se recorren las matrices hasta que las fichas rojas y las fichas azules sean
        // 4.
        for (int i = 0; i < tablero.length && !(fichasRojas == 4 && fichasAzules == 4); i++) {
            for (int j = 0; j < tablero[i].length && !(fichasRojas == 4 && fichasAzules == 4); j++) {
                int random = (int) (Math.random() * 2);
                // Evaluar este "random" disminuye las posibilidades de que las fichas queden
                // una tras otra y hace que se vea más aleatoria la carga.
                // Si no hay nada todavía en la casilla en la que está parado el for y "random"
                // != 0, se decide colocar una ficha cualquiera o vacío nuevamente.
                if (tablero[i][j] == 0 && random != 0) {
                    random = ((int) (Math.random() * 5) - 2);
                    // El resultado del "random" es la ficha a colocar.
                    // Según la ficha pone roja o azul y consulta que no hayan ya 4 fichas de ese
                    // color.
                    // Si "random" diera cero no se mete ficha y se sigue el recorrido.
                    if (random > 0 && fichasRojas < 4) {
                        fichasRojas++;
                        tablero[i][j] = random;
                    } else if (random < 0 && fichasAzules < 4) {
                        fichasAzules++;
                        tablero[i][j] = random;
                    }
                }
            }
            // Si se terminó el recorrido de la matriz y no se pusieron las fichas
            // suficientes, retrocede a la fila 0 para volver a meter las fichas.
            // Se asigna i = -1, porque al consultar en el for y dar true, suma i++ y
            // después vuelve a ejecutar el código. Ahora con i = 0 y no i = -1.
            if (i == 3 && (fichasRojas < 4 || fichasAzules < 4)) {
                i = -1;
            }
        }
    }

    // Override
    @Override
    public String toString() {
        // Devuelve los contenidos de cada celda como letra "F", "D" o " "(un espacio
        // vacío) si no contiene nada. Recorre la matriz de manera tradicional.
        String retorno = "";
        for (int i = 0; i < tableroJuego.length; i++) {
            for (int j = 0; j < tableroJuego[i].length; j++) {
                switch (Math.abs(tableroJuego[i][j])) {
                    case 1 -> retorno += "F";

                    case 2 -> retorno += "D";

                    default -> retorno += " ";
                }
            }
        }
        return retorno;
    }

}