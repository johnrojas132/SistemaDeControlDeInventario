package repositorio;

import modelo.Producto;
import java.util.*;

public class ProductoRepositorio {

    // lista de productos guardados, set para duplicidad, contar por categoria,historial y id 
    private final List<Producto> productos = new ArrayList<>();

    private final Set<String> codigos = new HashSet<>();

    private final Map<String, Integer> contadorCategorias = new HashMap<>();

    private final Stack<String> historial = new Stack<>();

    private int siguienteId = 1;

    //quita espacios y evita null
    private String normalize(String s) {
        return s == null ? "" : s.trim();
    }

    //suma uno al contador de categoria
    private void incrementarCategoria(String categoria) {
        if (categoria == null) {
            return;
        }
        String key = categoria.trim();
        if (key.isEmpty()) {
            return;
        }
        contadorCategorias.put(key, contadorCategorias.getOrDefault(key, 0) + 1);
    }

    //resta uno al contador de categoria
    private void decrementarCategoria(String categoria) {
        if (categoria == null) {
            return;
        }
        String key = categoria.trim();
        if (key.isEmpty()) {
            return;
        }
        Integer v = contadorCategorias.get(key);
        if (v == null) {
            return;
        }
        if (v <= 1) {
            contadorCategorias.remove(key);
        } else {
            contadorCategorias.put(key, v - 1);
        }
    }

    // agrega producto, valida codigo unico y asigna id
    public synchronized boolean agregar(Producto p) {
        String c = normalize(p.getCodigo());
        if (c.isEmpty() || codigos.contains(c)) {
            return false;
        }
        if (p.getId() <= 0) {
            p.setId(siguienteId++);
        } else if (p.getId() >= siguienteId) {
            siguienteId = p.getId() + 1;
        }
        productos.add(p);
        codigos.add(c);
        incrementarCategoria(p.getCategoria());
        historial.push("Agregado: " + p.getNombre());
        return true;
    }

    //devuelve lista de productos
    public synchronized List<Producto> listar() {
        return new ArrayList<>(productos);
    }

    //busca producto por codigo
    public synchronized Producto buscarPorCodigo(String codigo) {
        if (codigo == null) {
            return null;
        }
        String c = normalize(codigo);
        for (Producto p : productos) {
            if (c.equals(p.getCodigo())) {
                return p;
            }
        }
        return null;
    }

    //revisa si existe codigo
    public synchronized boolean existeCodigo(String codigo) {
        if (codigo == null) {
            return false;
        }
        return codigos.contains(normalize(codigo));
    }

    // edita producto, cambia datos y actualiza categoria
    public synchronized boolean editar(String codigoOriginal, Producto nuevo) {
        if (codigoOriginal == null) {
            return false;
        }
        String orig = normalize(codigoOriginal);
        for (int i = 0; i < productos.size(); i++) {
            Producto p = productos.get(i);
            if (orig.equals(p.getCodigo())) {
                String nuevoCodigo = normalize(nuevo.getCodigo());
                if (!orig.equals(nuevoCodigo) && codigos.contains(nuevoCodigo)) {
                    return false;
                }
                codigos.remove(orig);
                codigos.add(nuevoCodigo);
                decrementarCategoria(p.getCategoria());
                incrementarCategoria(nuevo.getCategoria());
                nuevo.setId(p.getId());
                productos.set(i, nuevo);
                historial.push("Editado: " + nuevo.getNombre());
                return true;
            }
        }
        return false;
    }

    //elimina producto por codigo
    public synchronized boolean eliminar(String codigo) {
        if (codigo == null) {
            return false;
        }
        String c = normalize(codigo);
        Iterator<Producto> it = productos.iterator();
        while (it.hasNext()) {
            Producto p = it.next();
            if (c.equals(p.getCodigo())) {
                it.remove();
                codigos.remove(c);
                decrementarCategoria(p.getCategoria());
                historial.push("Eliminado: " + p.getNombre());
                return true;
            }
        }
        return false;
    }

    // devuelve copia del contador 
    public synchronized Map<String, Integer> getContadorCategorias() {
        return new HashMap<>(contadorCategorias);
    }

    //devuelve historial 
    public synchronized List<String> getHistorial() {
        return new ArrayList<>(historial);
    }

    //limpia todo el repo
    public synchronized void limpiar() {
        productos.clear();
        codigos.clear();
        contadorCategorias.clear();
        historial.clear();
        siguienteId = 1;
    }
}
