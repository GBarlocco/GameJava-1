package criteriosDeOrden;

import java.util.Comparator;
import sistema.Jugador;

/**
 *
 * @author "Santiago Palacios - 248074 _ Gastón Barlocco - 241025"
 */

public class CriterioXNombre implements Comparator<Jugador> {
    // Ordena por nombre y después por alias.
    @Override
    public int compare(Jugador j1, Jugador j2) {
        int retorno = j1.getNombre().compareToIgnoreCase(j2.getNombre());
        if (retorno == 0) {
            retorno = j1.getAlias().compareToIgnoreCase(j2.getAlias());
        }
        return retorno;
    }
}