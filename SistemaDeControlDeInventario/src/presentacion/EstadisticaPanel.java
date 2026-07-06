package presentacion;

import negocio.ProductoNegocio;
import modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Map;

public class EstadisticaPanel extends javax.swing.JPanel {

    private final ProductoNegocio negocio;
    private DefaultTableModel modeloCategorias;

    public EstadisticaPanel(ProductoNegocio negocio) {
        this.negocio = negocio;
        initComponents();

        //tabla de categorias, no editable
        modeloCategorias = new DefaultTableModel(new Object[]{"Categoria", "Cantidad"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblCategorias.setModel(modeloCategorias);

        // boton refrescar
        btnRefrescar.addActionListener(e -> actualizarEstadisticas());

        // boton historial muestra lista de acciones 
        btnHistorial.addActionListener(e -> {
            try {
                List<String> historial = negocio.obtenerHistorial();
                if (historial == null || historial.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No hay historial disponible.");
                } else {
                    JTextArea area = new JTextArea(String.join("\n", historial));
                    area.setEditable(false);
                    JScrollPane sp = new JScrollPane(area);
                    sp.setPreferredSize(new java.awt.Dimension(500, 300));
                    JOptionPane.showMessageDialog(this, sp, "Historial de acciones", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (UnsupportedOperationException ex) {
                JOptionPane.showMessageDialog(this, "Historial no disponible en negocio.", "Info", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al obtener historial: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        actualizarEstadisticas();
    }

    // actualiza labels y tabla con datos del negocio
    public void actualizarEstadisticas() {
        Map<String, Object> stats = negocio.obtenerEstadisticas();

        int totalProductos = safeInt(stats.get("totalProductos"));
        int disponibles = safeInt(stats.get("disponibles"));
        int noDisponibles = safeInt(stats.get("noDisponibles"));
        int totalUnidades = safeInt(stats.get("totalUnidades"));
        double valorInventario = safeDouble(stats.get("valorInventario"));

        Producto mayor = (Producto) stats.get("mayorPrecio");
        Producto menor = (Producto) stats.get("menorPrecio");

        lblTotalProductos.setText("Total de productos: " + totalProductos);
        lblDisponibles.setText("Disponibles: " + disponibles);
        lblNoDisponibles.setText("No disponibles: " + noDisponibles);
        lblTotalUnidades.setText("Total de unidades: " + totalUnidades);
        lblValorInventario.setText(String.format("Valor total inventario: %.2f", valorInventario));

        lblMayorPrecio.setText(mayor == null ? "Producto mayor precio: N/A"
                : String.format("Producto mayor precio: %s (%.2f)", mayor.getNombre(), mayor.getPrecio()));
        lblMenorPrecio.setText(menor == null ? "Producto menor precio: N/A"
                : String.format("Producto menor precio: %s (%.2f)", menor.getNombre(), menor.getPrecio()));

        //llenar tabla de categorias
        modeloCategorias.setRowCount(0);
        Map<String, Integer> categorias = (Map<String, Integer>) stats.get("productosPorCategoria");
        if (categorias != null && !categorias.isEmpty()) {
            for (Map.Entry<String, Integer> e : categorias.entrySet()) {
                modeloCategorias.addRow(new Object[]{e.getKey(), e.getValue()});
            }
        }
    }

    private int safeInt(Object o) {
        if (o instanceof Integer) {
            return (Integer) o;
        }
        if (o instanceof Number) {
            return ((Number) o).intValue();
        }
        return 0;
    }

    //convertir a double sin explotar
    private double safeDouble(Object o) {
        if (o instanceof Double) {
            return (Double) o;
        }
        if (o instanceof Number) {
            return ((Number) o).doubleValue();
        }
        return 0.0;
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTotalProductos = new javax.swing.JLabel();
        lblDisponibles = new javax.swing.JLabel();
        lblNoDisponibles = new javax.swing.JLabel();
        lblTotalUnidades = new javax.swing.JLabel();
        lblValorInventario = new javax.swing.JLabel();
        lblMayorPrecio = new javax.swing.JLabel();
        lblMenorPrecio = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCategorias = new javax.swing.JTable();
        btnRefrescar = new javax.swing.JButton();
        btnHistorial = new javax.swing.JButton();

        lblTotalProductos.setText("Total Productos");

        lblDisponibles.setText("Disponibles");

        lblNoDisponibles.setText("No Disponibles");

        lblTotalUnidades.setText("Total Unidades");

        lblValorInventario.setText("Valor Inventario");

        lblMayorPrecio.setText("Mayor Precio");

        lblMenorPrecio.setText("Menor Precio");

        tblCategorias.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblCategorias);

        btnRefrescar.setText("Refrescar");

        btnHistorial.setText("Historial");
        btnHistorial.setToolTipText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblValorInventario, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnRefrescar)
                                .addGap(18, 18, 18)
                                .addComponent(btnHistorial))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 608, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(163, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDisponibles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblTotalProductos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblMenorPrecio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(lblTotalUnidades, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblNoDisponibles, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblMayorPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(86, 86, 86))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalProductos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTotalUnidades, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMenorPrecio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblNoDisponibles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDisponibles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblMayorPrecio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblValorInventario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRefrescar)
                    .addComponent(btnHistorial))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(71, 71, 71))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHistorial;
    private javax.swing.JButton btnRefrescar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDisponibles;
    private javax.swing.JLabel lblMayorPrecio;
    private javax.swing.JLabel lblMenorPrecio;
    private javax.swing.JLabel lblNoDisponibles;
    private javax.swing.JLabel lblTotalProductos;
    private javax.swing.JLabel lblTotalUnidades;
    private javax.swing.JLabel lblValorInventario;
    private javax.swing.JTable tblCategorias;
    // End of variables declaration//GEN-END:variables
}
