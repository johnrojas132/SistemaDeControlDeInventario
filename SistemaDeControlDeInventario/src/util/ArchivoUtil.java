package util;

import modelo.Producto;
import excepciones.ArchivoException;

import javax.swing.*;
import java.io.*;
import java.util.List;

public class ArchivoUtil {
    
    // exporta lista de productos a un archivo csv
    public static void exportarInventario(List<Producto> productos, java.awt.Component parent) throws ArchivoException {
        if (productos == null) productos = java.util.Collections.emptyList();

        // abre dialogo para elegir donde guardar
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar inventario como");
        chooser.setSelectedFile(new File("inventario.csv"));
        int opcion = chooser.showSaveDialog(parent);
        if (opcion != JFileChooser.APPROVE_OPTION) {
            //si el usuario cancela no pasa nada
            return;
        }

        File archivo = chooser.getSelectedFile();
        //asegura que termine en .csv
        if (!archivo.getName().toLowerCase().endsWith(".csv")) {
            archivo = new File(archivo.getParentFile(), archivo.getName() + ".csv");
        }

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(archivo), "UTF-8"))) {
            // escribe encabezado
            bw.write("ID,Codigo,Nombre,Categoria,Cantidad,Precio,Disponible");
            bw.newLine();

            int id = 1;
            for (Producto p : productos) {
                //saca datos del producto
                String codigo = safe(p.getCodigo());
                String nombre = safe(p.getNombre());
                String categoria = safe(p.getCategoria());
                int cantidad = p.getCantidad();
                double precio = p.getPrecio();
                boolean disponible = p.isDisponible();

                //reemplaza comas y saltos por espacios
                codigo = escape(codigo);
                nombre = escape(nombre);
                categoria = escape(categoria);

                // linea del csv
                String linea = String.format("%d,%s,%s,%s,%d,%.2f,%b",
                        id, codigo, nombre, categoria, cantidad, precio, disponible);
                bw.write(linea);
                bw.newLine();
                id++;
            }
            bw.flush();
            //mensaje de exito
            JOptionPane.showMessageDialog(parent, "Inventario exportado:\n" + archivo.getAbsolutePath(),
                    "Exportacion completa", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            // si falla lanza excepcion 
            throw new ArchivoException("Error al exportar: " + ex.getMessage(), ex);
        }
    }

    // evita null y quita espacios
    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }

    //reemplaza comas y saltos por espacios
    private static String escape(String s) {
        return s.replace(",", " ").replace("\n", " ").replace("\r", " ");
    }
}