package criteriosDeOrden;

import java.util.Comparator;
import sistema.Jugador;

/**
 *
 * @author "Santiago Palacios - 248074 _ Gastón Barlocco - 241025"
 */

public class CriterioXId implements Comparator<Jugador> {
    @Override
    public int compare(Jugador j1, Jugador j2) {
        return j1.getIdJugador() - j2.getIdJugador();
    }
}