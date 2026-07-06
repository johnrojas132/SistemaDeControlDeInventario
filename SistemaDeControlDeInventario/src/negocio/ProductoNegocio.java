package negocio;

import modelo.Producto;
import repositorio.ProductoRepositorio;
import excepciones.DatoInvalidoException;
import excepciones.ProductoDuplicadoException;

import java.util.*;


 //validaciones, reglas sobre productos.

public class ProductoNegocio {

    private final ProductoRepositorio repositorio;

    public ProductoNegocio(ProductoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    // Devuelve todos los productos
    public List<Producto> obtenerTodos() {
        return repositorio.listar();
    }

    //Agregar con validaciones
    public void agregar(Producto producto) throws DatoInvalidoException, ProductoDuplicadoException {
        validarProductoBasico(producto);

        String codigo = producto.getCodigo();
        if (codigo.isEmpty()) {
            throw new DatoInvalidoException("El codigo no puede estar vacio");
        }

        if (repositorio.existeCodigo(codigo)) {
            throw new ProductoDuplicadoException("El codigo ya existe: " + codigo);
        }

        boolean ok = repositorio.agregar(producto);
        if (!ok) {
            throw new ProductoDuplicadoException("No se pudo agregar el producto. codigo duplicado: " + codigo);
        }
    }

    // Editar producto
    public void editar(String codigoOriginal, Producto nuevo) throws DatoInvalidoException {
        if (codigoOriginal == null || codigoOriginal.trim().isEmpty()) {
            throw new DatoInvalidoException("Codigo original invalido");
        }
        validarProductoBasico(nuevo);

        boolean ok = repositorio.editar(codigoOriginal, nuevo);
        if (!ok) {
            throw new DatoInvalidoException("No se encontro el producto con codigo: " + codigoOriginal);
        }
    }

    // Eliminar producto
    public void eliminar(String codigo) throws DatoInvalidoException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new DatoInvalidoException("Codigo invalido para eliminar");
        }
        boolean ok = repositorio.eliminar(codigo);
        if (!ok) {
            throw new DatoInvalidoException("No se encontro el producto con codigo: " + codigo);
        }
    }

    // Buscar por codigo
    public Producto buscarPorCodigo(String codigo) {
        if (codigo == null) return null;
        return repositorio.buscarPorCodigo(codigo.trim());
    }

    //Buscar por nombre
    public List<Producto> buscarPorNombre(String nombre) {
        List<Producto> resultado = new ArrayList<>();
        if (nombre == null || nombre.trim().isEmpty()) return resultado;
        String buscado = nombre.trim().toLowerCase();
        for (Producto p : repositorio.listar()) {
            if (p.getNombre() != null && p.getNombre().toLowerCase().contains(buscado)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    //Filtrar por categoria
    public List<Producto> filtrarPorCategoria(String categoria) {
        List<Producto> resultado = new ArrayList<>();
        if (categoria == null || categoria.trim().isEmpty()) return resultado;
        String cat = categoria.trim().toLowerCase();
        for (Producto p : repositorio.listar()) {
            if (p.getCategoria() != null && p.getCategoria().toLowerCase().equals(cat)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    //ordenamientos usando Collections
    public List<Producto> ordenarPorNombre() {
        List<Producto> lista = new ArrayList<>(repositorio.listar());
        Collections.sort(lista, Comparator.comparing(p -> p.getNombre() == null ? "" : p.getNombre().toLowerCase()));
        return lista;
    }

    public List<Producto> ordenarPorPrecio() {
        List<Producto> lista = new ArrayList<>(repositorio.listar());
        Collections.sort(lista, Comparator.comparingDouble(Producto::getPrecio));
        return lista;
    }

    public List<Producto> ordenarPorCantidad() {
        List<Producto> lista = new ArrayList<>(repositorio.listar());
        Collections.sort(lista, Comparator.comparingInt(Producto::getCantidad));
        return lista;
    }

    //Estadisticas 
    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> stats = new HashMap<>();
        List<Producto> lista = repositorio.listar();

        int totalProductos = lista.size();
        int disponibles = 0;
        int totalUnidades = 0;
        double valorInventario = 0.0;

        Producto mayorPrecio = null;
        Producto menorPrecio = null;

        for (Producto p : lista) {
            if (p.isDisponible()) disponibles++;
            totalUnidades += p.getCantidad();
            valorInventario += p.getCantidad() * p.getPrecio();

            if (mayorPrecio == null || p.getPrecio() > mayorPrecio.getPrecio()) {
                mayorPrecio = p;
            }
            if (menorPrecio == null || p.getPrecio() < (menorPrecio == null ? Double.MAX_VALUE : menorPrecio.getPrecio())) {
                menorPrecio = p;
            }
        }

        int noDisponibles = totalProductos - disponibles;

        stats.put("totalProductos", totalProductos);
        stats.put("disponibles", disponibles);
        stats.put("noDisponibles", noDisponibles);
        stats.put("totalUnidades", totalUnidades);
        stats.put("mayorPrecio", mayorPrecio);
        stats.put("menorPrecio", menorPrecio);
        stats.put("productosPorCategoria", repositorio.getContadorCategorias());
        stats.put("valorInventario", valorInventario);

        return stats;
    }

    //obtener historial 
    public List<String> obtenerHistorial() {
      
        try {
            return repositorio.getHistorial();
        } catch (NoSuchMethodError | UnsupportedOperationException ex) {
            throw new UnsupportedOperationException("Historial no disponible");
        }
    }

    // Validaciones basicas
    private void validarProductoBasico(Producto p) throws DatoInvalidoException {
        if (p == null) {
            throw new DatoInvalidoException("Producto nulo");
        }
        String codigo = p.getCodigo() == null ? "" : p.getCodigo().trim();
        String nombre = p.getNombre() == null ? "" : p.getNombre().trim();

        if (codigo.isEmpty()) {
            throw new DatoInvalidoException("El codigo es obligatorio");
        }
        if (nombre.isEmpty()) {
            throw new DatoInvalidoException("El nombre es obligatorio");
        }
        if (p.getPrecio() <= 0) {
            throw new DatoInvalidoException("El precio debe ser mayor a 0");
        }
        if (p.getCantidad() < 0) {
            throw new DatoInvalidoException("La cantidad no puede ser negativa");
        }
    }
}