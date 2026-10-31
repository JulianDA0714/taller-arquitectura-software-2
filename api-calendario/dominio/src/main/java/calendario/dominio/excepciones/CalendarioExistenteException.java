package calendario.dominio.excepciones;

public class CalendarioExistenteException extends RuntimeException {

    public CalendarioExistenteException(int anio) {
        super("El año " + anio + " ya tiene días almacenados. Su calendario no fue modificado.");
    }
}
