package excepciones;

//para errores relacionados con operaciones de archivo
public class ArchivoException extends Exception {
    public ArchivoException(String mensaje) {
        super(mensaje);
    }

    public ArchivoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}