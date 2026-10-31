package calendario.dominio.excepciones;

public class AnioInvalidoException extends RuntimeException {

    public AnioInvalidoException(int anio) {
        super("El año debe ser un número entero entre 1900 y 9999. Valor recibido: " + anio);
    }
}
