package calendario.dominio.excepciones;

public class IntegracionFestivosException extends RuntimeException {

    private final boolean tiempoAgotado;

    public IntegracionFestivosException(String mensaje) {
        this(mensaje, null, false);
    }

    public IntegracionFestivosException(String mensaje, Throwable causa) {
        this(mensaje, causa, false);
    }

    public IntegracionFestivosException(String mensaje, Throwable causa, boolean tiempoAgotado) {
        super(mensaje, causa);
        this.tiempoAgotado = tiempoAgotado;
    }

    public boolean isTiempoAgotado() {
        return tiempoAgotado;
    }
}
