package sistema;

/**
 *
 * @author "Santiago Palacios - 248074 _ Gastón Barlocco - 241025"
 */

public class Jugador {
    // Variable de clase
    private static int id = 1;

    // Variables de instanica
    final private int ID_JUGADOR;
    final private String NOMBRE;
    final private String ALIAS;
    final private int EDAD;
    private int victorias;
    private boolean figuraF;
    private boolean figuraD;

    // Constructor
    public Jugador(String unNombre, String unAlias, int unaEdad) {
        ID_JUGADOR = id;
        setId(getId() + 1);
        NOMBRE = unNombre;
        ALIAS = unAlias;
        EDAD = unaEdad;
        this.victorias = 0;
        this.figuraF = false;
        this.figuraD = false;
    }

    // Get
    public static int getId() {
        return Jugador.id;
    }

    public int getIdJugador() {
        return this.ID_JUGADOR;
    }

    public String getNombre() {
        return this.NOMBRE;
    }

    public String getAlias() {
        return this.ALIAS;
    }

    public int getEdad() {
        return this.EDAD;
    }

    public int getVictorias() {
        return this.victorias;
    }

    public boolean getFiguraF() {
        return this.figuraF;
    }

    public boolean getFiguraD() {
        return this.figuraD;
    }

    // Set
    public static void setId(int unId) {
        Jugador.id = unId;
    }

    public void setVictorias(int cantVictorias) {
        this.victorias = cantVictorias;
    }

    public void setFiguraF(boolean figura) {
        this.figuraF = figura;
    }

    public void setFiguraD(boolean figura) {
        this.figuraD = figura;
    }

    // Metodos generales

    public static boolean nombreValido(String nombre) {
        boolean esValido = true;
        // Comprobamos que no sea un String vacío y que comience con una letra
        if (nombre.equals("") || nombre.charAt(0) == ' ') {
            esValido = false;
        }
        for (int i = 0; i < nombre.length() && esValido; i++) {
            if (!(nombre.charAt(i) == 32 // Si no es un espacio
                    || ((nombre.charAt(i) > 64 && nombre.charAt(i) < 91) // , una letra en mayúscula
                            || (nombre.charAt(i) > 96 && nombre.charAt(i) < 123)))) { // o una letra minúscula
                esValido = false;
            }
        }
        return esValido;
    }

    public static boolean edadValida(int edad) {
        boolean esValido = true;
        if (!(edad > 5 && edad < 99)) {
            esValido = false;
        }
        return esValido;
    }

    // toSring
    @Override
    public String toString() {
        return "" + this.getIdJugador() + "  | " + this.getNombre() + " " + this.getAlias() + " " + this.getEdad() + " "
                + this.getVictorias();
    }

}