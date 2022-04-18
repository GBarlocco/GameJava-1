package sistema;

import criteriosDeOrden.CriterioXPuntaje;
import criteriosDeOrden.CriterioXNombre;
import criteriosDeOrden.CriterioXId;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author "Santiago Palacios - 248074 _ Gastón Barlocco - 241025"
 */

public class Sistema {
    // Variable de instancia.
    private ArrayList<Jugador> listaJugadores;
    private Tablero tablero;
    private boolean invertirPrimerRonda;
    private boolean notificoFRoja;
    private boolean notificoFAzul;
    private boolean notificoDRoja;
    private boolean notificoDAzul;

    // Constructor.
    public Sistema() {
        this.listaJugadores = new ArrayList(); // Se inicializa una lista de jugadores con 2 jugadores por defecto.
        Jugador j1 = new Jugador("Jugador1", "Invitado1", 18);
        listaJugadores.add(j1);
        Jugador j2 = new Jugador("Jugador2", "Invitado2", 18);
        listaJugadores.add(j2);
        // Sin tableroEnJuego y todos los booleanos en falso por defecto.
        this.tablero = null;
        this.setInvertirPrimerRonda(false);
        this.setNotificoFRoja(false);
        this.setNotificoFAzul(false);
        this.setNotificoDRoja(false);
        this.setNotificoDAzul(false);
    }

    // Gets.
    public ArrayList<Jugador> getListaJugadores() {
        return this.listaJugadores;
    }

    public Tablero getTablero() {
        return this.tablero;
    }

    public boolean getInvertirPrimerRonda() {
        return this.invertirPrimerRonda;
    }

    public boolean getNotificoFRoja() {
        return this.notificoFRoja;
    }

    public boolean getNotificoFAzul() {
        return this.notificoFAzul;
    }

    public boolean getNotificoDRoja() {
        return this.notificoDRoja;
    }

    public boolean getNotificoDAzul() {
        return this.notificoDAzul;
    }

    // Sets.
    public void setListaJugadores(ArrayList<Jugador> listaJugadores) {
        this.listaJugadores = listaJugadores;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    public void setInvertirPrimerRonda(boolean unaInvertida) {
        this.invertirPrimerRonda = unaInvertida;
    }

    public void setNotificoFRoja(boolean notificacion) {
        this.notificoFRoja = notificacion;
    }

    public void setNotificoFAzul(boolean notificacion) {
        this.notificoFAzul = notificacion;
    }

    public void setNotificoDRoja(boolean notificacion) {
        this.notificoDRoja = notificacion;
    }

    public void setNotificoDAzul(boolean notificacion) {
        this.notificoDAzul = notificacion;
    }

    // Metodos generales.
    public void registrarJugador(String nombre, String alias, int edad) {
        // Se crea un nuevo Jugador.
        Jugador nuevoJugador = new Jugador(nombre, alias, edad);
        // Y se añade a la lista de jugadores.
        this.getListaJugadores().add(nuevoJugador);
    }

    public boolean aliasYaExistente(String alias) {
        // En un principio se asume alias no repetido.
        boolean estaRepetido = false;

        // Comprobamos que no sea un String vacío y que comience con una letra
        if (alias.equals("") || alias.charAt(0) == ' ') {
            estaRepetido = true;
        }

        for (int i = 0; i < this.getListaJugadores().size() && !estaRepetido; i++) { // Se recorre la lista de jugadores
                                                                                     // y se da fin
                                                                                     // si se encuentra uno con el mismo
                                                                                     // alias o finaliza el recorrido.
            String aliasJ = this.getListaJugadores().get(i).getAlias(); // Se toma el alias de la posición actual de la
                                                                        // lista.

            // Se comparan los alias y si está repetido termina el recorrido.
            if (alias.equals(aliasJ)) {
                estaRepetido = true;
            }

        }
        return !estaRepetido;
    }

    public Jugador buscarJugador(int idJugador) {
        Jugador jugador = null;
        for (Jugador j : this.getListaJugadores()) { // Buscamos al jugador comparando su id con el ingresado.
            // Si se encuentra un jugador con mismo ID se guarda en la variable jugador para
            // devolverlo después.
            if (j.getIdJugador() == idJugador) {
                jugador = j;
            }
        }
        return jugador;
    }

    public boolean interpretarDato(String jugadaIngresada) {
        String[] jugada = jugadaIngresada.split(" "); // Se convierte la jugada que era un String a un array.
        boolean jugadaValida = false;
        Tablero tableroEnJuego = this.getTablero();

        /*
         * Según el largo del array se induce el tipo de jugada.
         * El primer valor (jugada[0]) nos indica si va a ser un movimiento o si va a
         * invertir.
         * El segundo valor (jugada[1]) las coordenadas de la ficha a mover o invertir.
         * El tercer valor (jugada[2]) las coordenadas adonde la ficha se va a mover.
         */
        if (jugada.length == 2) { // Largo 2 es un posible invertir.

            if (jugada[0].equalsIgnoreCase("I") && jugada[1].length() == 2) { // Se verifica que efectivamente sea "I".
                                                                              // Se verifica que las coordenadas tengan
                                                                              // largo 2.
                // Convertimos las coordenadas a posiciones de matriz.
                int fila = Character.getNumericValue(jugada[1].charAt(0)) - 10;
                int columna = Character.getNumericValue(jugada[1].charAt(1)) - 1;

                if (validarJugadaI(fila, columna)) { // Se checkea que sea un invertir válido.
                    jugadaValida = true;
                    // Invertimos la ficha.
                    tableroEnJuego.invertir(fila, columna);
                }
            }
        } else if (jugada.length == 3) { // Largo 3 es un posible mover.

            if (jugada[0].equalsIgnoreCase("M") && (jugada[1].length() == 2 && jugada[2].length() == 2)) { // Se
                                                                                                           // verifica
                                                                                                           // que
                                                                                                           // efectivamente
                                                                                                           // sea "M".
                                                                                                           // Se
                                                                                                           // verifica
                                                                                                           // que ambas
                                                                                                           // coordenadas
                                                                                                           // tengan
                                                                                                           // largo 2.
                // Convertimos las coordenadas a posiciones de matriz.
                int filaOrigen = Character.getNumericValue(jugada[1].charAt(0)) - 10;
                int columnaOrigen = Character.getNumericValue(jugada[1].charAt(1)) - 1;
                int filaDestino = Character.getNumericValue(jugada[2].charAt(0)) - 10;
                int columnaDestino = Character.getNumericValue(jugada[2].charAt(1)) - 1;

                if (validarJugadaM(filaOrigen, columnaOrigen, filaDestino, columnaDestino)) { // Se checkea que sea un
                                                                                              // mover válido.
                    jugadaValida = true;
                    // Movemos la ficha.
                    tableroEnJuego.mover(filaOrigen, columnaOrigen, filaDestino, columnaDestino);
                }
            }
        }

        if (jugadaValida) { // Si se ingresó una jugada válida:
            if (!tableroEnJuego.getPrimerTurno()) { // Y es el segundo turno:
                verificarFiguras(); // Se comprueban figuras,
                setInvertirPrimerRonda(false); // Reestablece dato,
                tableroEnJuego.setTurnoRojo(!tableroEnJuego.getTurnoRojo());// Cambia de turno.
            } // Y
            tableroEnJuego.setPrimerTurno(!tableroEnJuego.getPrimerTurno());// Cambia de ronda.
        }

        return jugadaValida;
    }

    private boolean validarJugadaI(int fila, int columna) {
        Tablero tableroEnJuego = this.getTablero();
        boolean jugadaValida = false;

        // Validamos que la jugada contenga una letra A, B, C o D. Y números: 1, 2, 3,
        // 4.
        if ((0 <= fila && fila < 4) && (0 <= columna && columna < 4)) {

            // Accedemos al contenido de la celda del tableroEnJuego mediante la fila y
            // columna.
            int contenidoCelda = tableroEnJuego.getTableroJuego()[fila][columna];

            if (tableroEnJuego.getPrimerTurno()) { // Caso primer turno.

                // Comparamos el contenido de la celda con el color del jugador actual.
                if ((tableroEnJuego.getTurnoRojo() && contenidoCelda > 0)
                        || (!(tableroEnJuego.getTurnoRojo()) && contenidoCelda < 0)) {
                    jugadaValida = true;
                    setInvertirPrimerRonda(true); // Se invirtió en la primer ronda.
                }

            } else { // Caso segundo turno.

                // Verificamos que haya una ficha en la celda.
                if (contenidoCelda != 0) {
                    jugadaValida = true;
                }
            }
        }
        return jugadaValida;
    }

    private boolean validarJugadaM(int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
        Tablero tableroEnJuego = this.getTablero();
        boolean jugadaValida = false;

        // Validamos que la jugada contenga una letra A, B, C o D. Y números: 1, 2, 3,
        // 4.
        if ((0 <= filaOrigen && filaOrigen < 4) && (0 <= columnaOrigen && columnaOrigen < 4)) { // Verificamos las
                                                                                                // primeras coordenadas.
            if ((0 <= filaDestino && filaDestino < 4) && (0 <= columnaDestino && columnaDestino < 4)) { // Verificamos
                                                                                                        // las segundas
                                                                                                        // coordenadas.

                // Accedemos al contenido de la celda del tableroEnJuego mediante la fila y
                // columna.
                int contenidoCelda = tableroEnJuego.getTableroJuego()[filaOrigen][columnaOrigen];

                // Calculamos las distancias de las casillas origen y destino.
                int distanciaFilas = Math.abs(filaOrigen - filaDestino);
                int distanciaColumnas = Math.abs(columnaOrigen - columnaDestino);
                // Verificamos que se mueva la ficha dentro de una posición adyacente.
                if ((distanciaFilas == 1 && (distanciaColumnas == 0 || distanciaColumnas == 1))
                        || (distanciaColumnas == 1 && (distanciaFilas == 0 || distanciaFilas == 1))) {

                    // Según el turno ejecutamos if o else.
                    if (tableroEnJuego.getPrimerTurno()) { // Caso primer turno.

                        // Comparamos el contenido de la celda con el color del jugador actual.
                        if ((tableroEnJuego.getTurnoRojo() && contenidoCelda > 0)
                                || (!(tableroEnJuego.getTurnoRojo()) && contenidoCelda < 0)) {
                            jugadaValida = true;
                        }

                    } else { // Caso segundo turno.

                        // Comprobamos que no se haya invertido en la primer ronda, y que haya una ficha
                        // para mover.
                        if (!this.getInvertirPrimerRonda() && contenidoCelda != 0) {
                            jugadaValida = true;
                        }

                    }
                }
            }
        }
        return jugadaValida;
    }

    private void verificarFiguras() {
        // Comprobamos si se realizó alguna figura.
        this.getTablero().figuraF();
        this.getTablero().figuraD();
    }

    public Jugador[] ganador() {
        Jugador[] jugador = new Jugador[2];

        // Se asignan "alias" a las variables que nos facilitan la nomenclatura.
        Jugador jRojo = jugador[0];
        Jugador jAzul = jugador[1];
        boolean FRojo = this.getTablero().getJRojo().getFiguraF();
        boolean DRojo = this.getTablero().getJRojo().getFiguraD();
        boolean FAzul = this.getTablero().getJAzul().getFiguraF();
        boolean DAzul = this.getTablero().getJAzul().getFiguraD();

        // Comprueba que algun jugador haya logrado ambas figuras.
        if (FRojo && DRojo) { // Caso jugador rojo logra ambas figuras.
            jRojo = this.getTablero().getJRojo();
        }
        if (FAzul && DAzul) { // Caso jugador azul logra ambas figuras.
            jAzul = this.getTablero().getJAzul();
        }

        if (!Objects.isNull(jRojo) && !Objects.isNull(jAzul)) { // Si ninguno es nullo, es porque ambos son ganadores y
                                                                // por tanto es empate.
            // No suma puntajes, y en adhesión, se omite sumar puntaje y verificar si hubo
            // ganador.
        } else if (!Objects.isNull(jRojo)) { // Ganó el rojo.
            jRojo.setVictorias(jRojo.getVictorias() + 1);

        } else if (!Objects.isNull(jAzul)) { // Ganó el azul.
            jAzul.setVictorias(jAzul.getVictorias() + 1);
        }

        jugador[0] = jRojo; // Asigna al jugador rojo al primer Jugador de la lista.
        jugador[1] = jAzul; // Asigna al jugador azul al segundo Jugador de la lista.

        return jugador;
    }

    public void reset() {
        // Se resetea el sistema.
        this.setInvertirPrimerRonda(false);
        this.setNotificoFRoja(false);
        this.setNotificoFAzul(false);
        this.setNotificoDRoja(false);
        this.setNotificoDAzul(false);

        // Se resetean los logros de los jugadores
        tablero.getJRojo().setFiguraF(false);
        tablero.getJRojo().setFiguraD(false);
        tablero.getJAzul().setFiguraF(false);
        tablero.getJAzul().setFiguraD(false);

        // Se resetea el tableroEnJuego.
        this.setTablero(null);
    }

    // Ordenar lista de jugadores
    public void ordenarPorNombre() {
        Collections.sort(this.getListaJugadores(), new CriterioXNombre()); // Ordena la lista de jugadores por nombre.
    }

    public void ordenarPorPuntaje() {
        Collections.sort(this.getListaJugadores(), new CriterioXPuntaje()); // Ordena la lista de jugadores por puntaje.
    }

    public void ordenarPorId() {
        Collections.sort(this.getListaJugadores(), new CriterioXId()); // Ordena la lista de jugadores por ID de
                                                                       // jugador.
    }

    // Sonido
    public void aplausos() {
        File ubicacionSonido = new File("aplausos.wav");
        try {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(ubicacionSonido);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
            TimeUnit.SECONDS.sleep(3);
            clip.stop();
        } catch (Exception ex) {
            System.out.println("*APLAUSOS*");
        }
    }

}
