package presentacion;

import modelo.Producto;
import negocio.ProductoNegocio;
import excepciones.DatoInvalidoException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductoPanel extends javax.swing.JPanel {

    private final ProductoNegocio negocio;
    private DefaultTableModel modelo;
    private registroPanel registroPanel;
    private EstadisticaPanel estadisticaPanel;

    public ProductoPanel(ProductoNegocio negocio) {
        this.negocio = negocio;
        initComponents();
        inicializar();
    }

    private void inicializar() {
        //tabla de productos, no editable
        modelo = new DefaultTableModel(
                new Object[]{"ID", "Codigo", "Nombre", "Precio", "Categoria", "Cantidad", "Disponible"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblProductos.setModel(modelo);
        tblProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblProductos.setDefaultEditor(Object.class, null);

        refreshTable();

        //botones buscar y filtrar
        btnBuscarCodigo.addActionListener(e -> {
            String codigo = JOptionPane.showInputDialog(this, "Ingrese codigo a buscar:");
            if (codigo == null) return;
            codigo = codigo.trim();
            if (codigo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Codigo vacio");
                return;
            }
            Producto p = negocio.buscarPorCodigo(codigo);
            if (p != null) {
                refreshTable(List.of(p));
            } else {
                JOptionPane.showMessageDialog(this, "No se encontro producto con ese codigo");
            }
        });

        btnBuscarNombre.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(this, "Ingrese nombre a buscar:");
            if (nombre == null) return;
            nombre = nombre.trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre vacio");
                return;
            }
            refreshTable(negocio.buscarPorNombre(nombre));
        });

        btnFiltrarCategoria.addActionListener(e -> {
            String categoria = JOptionPane.showInputDialog(this, "Ingrese categoria:");
            if (categoria == null) return;
            categoria = categoria.trim();
            if (categoria.isEmpty()) {
                refreshTable();
            } else {
                refreshTable(negocio.filtrarPorCategoria(categoria));
            }
        });

        // ordenar
        btnOrdenarNombre.addActionListener(e -> refreshTable(negocio.ordenarPorNombre()));
        btnOrdenarPrecio.addActionListener(e -> refreshTable(negocio.ordenarPorPrecio()));
        btnOrdenarCantidad.addActionListener(e -> refreshTable(negocio.ordenarPorCantidad()));

        //botones editar y eliminar
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        btnEditar.addActionListener(e -> editarSeleccionado());

        //doble clic en tabla edita
        tblProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    editarSeleccionado();
                }
            }
        });
    }

    //refresca tabla con lista
    public void refreshTable(List<Producto> lista) {
        modelo.setRowCount(0);
        if (lista == null) return;
        for (Producto p : lista) {
            modelo.addRow(new Object[]{
                    p.getId(),
                    p.getCodigo(),
                    p.getNombre(),
                    String.format("%.2f", p.getPrecio()),
                    p.getCategoria(),
                    p.getCantidad(),
                    p.isDisponible()
            });
        }
    }

    // refresca tabla con todos los productos
    public void refreshTable() {
        if (negocio != null) {
            refreshTable(negocio.obtenerTodos());
        }
    }

    // edita producto seleccionado y abre registro
    public void editarSeleccionado() {
        int fila = tblProductos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila para editar");
            return;
        }
        String codigo = modelo.getValueAt(fila, 1).toString();
        Producto p = negocio.buscarPorCodigo(codigo);
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Producto no encontrado");
            return;
        }
        if (registroPanel != null) {
            registroPanel.cargarProductoEnFormulario(p);
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w instanceof MainFrame) {
                ((MainFrame) w).abrirRegistroConProducto(p);
            }
        } else {
            JOptionPane.showMessageDialog(this, "RegistroPanel no conectado");
        }
    }

    //elimina producto seleccionado 
    public void eliminarSeleccionado() {
        int fila = tblProductos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila para eliminar");
            return;
        }
        String codigo = modelo.getValueAt(fila, 1).toString();
        int opcion = JOptionPane.showConfirmDialog(this,
                "Eliminar producto con codigo: " + codigo + " ?",
                "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) return;
        try {
            negocio.eliminar(codigo);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Producto eliminado");
            if (estadisticaPanel != null) estadisticaPanel.actualizarEstadisticas();
        } catch (DatoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // setters para conectar con otros paneles
    public void setRegistroPanel(registroPanel r) {
        this.registroPanel = r;
    }

    public void setEstadisticaPanel(EstadisticaPanel e) {
        this.estadisticaPanel = e;
    } 

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnBuscarCodigo = new javax.swing.JButton();
        btnFiltrarCategoria = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnOrdenarPrecio = new javax.swing.JButton();
        btnBuscarNombre = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnOrdenarNombre = new javax.swing.JButton();
        btnOrdenarCantidad = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblProductos = new javax.swing.JTable();

        btnBuscarCodigo.setText("Buscar Codigo");

        btnFiltrarCategoria.setText("Filtrar Categoria");

        btnEliminar.setText("Eliminar");

        btnOrdenarPrecio.setText("Ordenar Precio");

        btnBuscarNombre.setText("Buscar Nombre");

        btnEditar.setText("Editar");

        btnOrdenarNombre.setText("Ordenar Nombre");

        btnOrdenarCantidad.setText("Ordenar Cantidad");

        tblProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tblProductos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 686, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnOrdenarNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFiltrarCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnOrdenarPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(110, 110, 110)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnBuscarNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnBuscarCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnOrdenarCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOrdenarNombre)
                    .addComponent(btnBuscarCodigo)
                    .addComponent(btnEditar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFiltrarCategoria)
                    .addComponent(btnBuscarNombre))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOrdenarCantidad)
                    .addComponent(btnOrdenarPrecio)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(169, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarCodigo;
    private javax.swing.JButton btnBuscarNombre;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnFiltrarCategoria;
    private javax.swing.JButton btnOrdenarCantidad;
    private javax.swing.JButton btnOrdenarNombre;
    private javax.swing.JButton btnOrdenarPrecio;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblProductos;
    // End of variables declaration//GEN-END:variables
}
