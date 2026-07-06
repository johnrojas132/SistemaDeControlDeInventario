package modelo;

//Atributos principales
import java.util.Objects;

public class Producto {

    private int id;
    private String codigo;
    private String nombre;
    private double precio;
    private String categoria;
    private int cantidad;
    private boolean disponible;
    private String descripcion;  

    //Constructor 
    public Producto(String codigo, String nombre, double precio, String categoria, int cantidad, boolean disponible) {
        this(0, codigo, nombre, precio, categoria, cantidad, disponible, "");
    }

    //Constructor con descripcion
    public Producto(String codigo, String nombre, double precio, String categoria, int cantidad, boolean disponible, String descripcion) {
        this(0, codigo, nombre, precio, categoria, cantidad, disponible, descripcion);
    }

    public Producto(int id, String codigo, String nombre, double precio, String categoria, int cantidad, boolean disponible, String descripcion) {
        this.id = id;
        this.codigo = normalize(codigo);
        this.nombre = normalize(nombre);
        this.precio = precio;
        this.categoria = normalize(categoria);
        this.cantidad = cantidad;
        this.disponible = disponible;
        this.descripcion = normalize(descripcion);
    }

    private String normalize(String s) { return s == null ? "" : s.trim(); }

    //Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = normalize(codigo); }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = normalize(nombre); }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = normalize(categoria); }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = normalize(descripcion); }

    
    //evita diplicidad
    @Override
    public String toString() {
        return String.format("%d - %s - %s", id, codigo, nombre);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto)) return false;
        Producto p = (Producto) o;
        return Objects.equals(codigo, p.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}