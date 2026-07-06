package excepciones;
//indicar que ya existe un producto
public class ProductoDuplicadoException extends Exception {
    public ProductoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
