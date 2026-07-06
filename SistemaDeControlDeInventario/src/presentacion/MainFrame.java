package presentacion;

import negocio.ProductoNegocio;
import repositorio.ProductoRepositorio;
import excepciones.ArchivoException;
import modelo.Producto;
import util.ArchivoUtil;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {

    private ProductoNegocio negocio;
    private ProductoPanel productoPanel;
    private EstadisticaPanel estadisticaPanel;
    private registroPanel registro;

    public MainFrame() {
        //crear repo y negocio
        ProductoRepositorio repo = new ProductoRepositorio();
        negocio = new ProductoNegocio(repo);

        initComponents();
        setTitle("Sistema de Inventario - UISIL");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //crear paneles con negocio
        registro = new registroPanel(negocio);
        productoPanel = new ProductoPanel(negocio);
        estadisticaPanel = new EstadisticaPanel(negocio);

        //conectar paneles entre si
        productoPanel.setRegistroPanel(registro);
        productoPanel.setEstadisticaPanel(estadisticaPanel);
        registro.setProductoPanel(productoPanel);
        registro.setEstadisticaPanel(estadisticaPanel);

        //tabs principales
        tabbedPane.removeAll();
        tabbedPane.addTab("Registro", registro);
        tabbedPane.addTab("Productos", productoPanel);
        tabbedPane.addTab("Estadisticas", estadisticaPanel);

        //refrescar 
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int idx = tabbedPane.getSelectedIndex();
                if (idx >= 0) {
                    String title = tabbedPane.getTitleAt(idx);
                    if ("Estadisticas".equalsIgnoreCase(title) && estadisticaPanel != null) {
                        estadisticaPanel.actualizarEstadisticas();
                    }
                }
            }
        });

        //menu archivo
        menuArchivoNuevo.addActionListener(e -> {
            if (registro != null) {
                tabbedPane.setSelectedComponent(registro);
            }
        });

        exportarItem.addActionListener(e -> {
            try {
                ArchivoUtil.exportarInventario(negocio.obtenerTodos(), this);
                JOptionPane.showMessageDialog(this, "Exportacion completada.", "Exportar", JOptionPane.INFORMATION_MESSAGE);
            } catch (ArchivoException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al exportar", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        salirItem.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Desea salir de la aplicacion?", "Salir", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                dispose();
                System.exit(0);
            }
        });

        // menu herramientas
        menuHerrOrdenar.addActionListener(e -> mostrarDialogoOrdenar());
        menuHerrEstadisticas.addActionListener(e -> {
            tabbedPane.setSelectedComponent(estadisticaPanel);
            estadisticaPanel.actualizarEstadisticas();
        });
        menuHerrHistorial.addActionListener(e -> mostrarHistorial());

        // menu ayuda
        menuAyudaAcerca.addActionListener(e -> {
            String msg = "Sistema de Inventario\n"
                    + "Universidad Internacional San Isidro Labrador\n"
                    + "Escuela de Ingenieria de Sistemas\n"
                    + "Programacion IV\n"
                    + "Profesor: Jose Andres Jimenez Zamora\n\n"
                    + "Autor: John Rojas Alvarado\nVersion: 1.0";
            JOptionPane.showMessageDialog(this, msg, "Acerca del sistema", JOptionPane.INFORMATION_MESSAGE);
        });

        //toolbar
        btnToolbarNuevo.addActionListener(e -> {
            if (registro != null) {
                tabbedPane.setSelectedComponent(registro);
            }
        });

        btnToolbarGuardar.addActionListener(e -> {
            if (registro != null) {
                tabbedPane.setSelectedComponent(registro);
                try {
                    registro.guardarDesdeToolbar();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnToolbarEditar.addActionListener(e -> {
            if (productoPanel != null) {
                tabbedPane.setSelectedComponent(productoPanel);
                try {
                    productoPanel.editarSeleccionado();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al editar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnToolbarEliminar.addActionListener(e -> {
            if (productoPanel != null) {
                tabbedPane.setSelectedComponent(productoPanel);
                try {
                    productoPanel.eliminarSeleccionado();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnToolbarOrdenar.addActionListener(e -> mostrarDialogoOrdenar());
        btnToolbarExportar.addActionListener(e -> {
            if (exportarItem != null) {
                exportarItem.doClick();
            }
        });

        setJMenuBar(menuBar);
    }

    //registro con producto seleccionado
    public void abrirRegistroConProducto(Producto p) {
        if (registro == null) {
            return;
        }
        registro.cargarProductoEnFormulario(p);
        tabbedPane.setSelectedComponent(registro);
    }

    // dialogo para ordenar productos
    private void mostrarDialogoOrdenar() {
        String[] opciones = {"Nombre", "Precio", "Cantidad"};
        String sel = (String) JOptionPane.showInputDialog(this, "Ordenar por:", "Ordenar productos",
                JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
        if (sel == null) {
            return;
        }
        switch (sel) {
            case "Nombre":
                productoPanel.refreshTable(negocio.ordenarPorNombre());
                break;
            case "Precio":
                productoPanel.refreshTable(negocio.ordenarPorPrecio());
                break;
            case "Cantidad":
                productoPanel.refreshTable(negocio.ordenarPorCantidad());
                break;
            default:
                productoPanel.refreshTable();
        }
    }

    // muestra historial de acciones
    private void mostrarHistorial() {
        try {
            List<String> historial = negocio.obtenerHistorial();
            if (historial == null || historial.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay historial disponible.", "Historial", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            JTextArea area = new JTextArea(String.join("\n", historial));
            area.setEditable(false);
            JScrollPane sp = new JScrollPane(area);
            sp.setPreferredSize(new Dimension(500, 300));
            JOptionPane.showMessageDialog(this, sp, "Historial de acciones", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener historial: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        tabbedPane = new javax.swing.JTabbedPane();
        jToolBar1 = new javax.swing.JToolBar();
        btnToolbarNuevo = new javax.swing.JButton();
        btnToolbarGuardar = new javax.swing.JButton();
        btnToolbarEditar = new javax.swing.JButton();
        btnToolbarEliminar = new javax.swing.JButton();
        btnToolbarOrdenar = new javax.swing.JButton();
        btnToolbarExportar = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        exportarItem = new javax.swing.JMenuItem();
        menuArchivoNuevo = new javax.swing.JMenuItem();
        salirItem = new javax.swing.JMenuItem();
        menuArchivo = new javax.swing.JMenu();
        menuHerrOrdenar = new javax.swing.JMenuItem();
        menuHerrEstadisticas = new javax.swing.JMenuItem();
        menuHerrHistorial = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        menuAyudaAcerca = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        jMenuItem2.setText("jMenuItem2");

        jMenuItem3.setText("jMenuItem3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jToolBar1.setRollover(true);

        btnToolbarNuevo.setText("Nuevo");
        btnToolbarNuevo.setFocusable(false);
        btnToolbarNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnToolbarNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnToolbarNuevo);

        btnToolbarGuardar.setText("Guardar");
        btnToolbarGuardar.setToolTipText("");
        btnToolbarGuardar.setFocusable(false);
        btnToolbarGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnToolbarGuardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnToolbarGuardar);

        btnToolbarEditar.setText("Editar");
        btnToolbarEditar.setFocusable(false);
        btnToolbarEditar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnToolbarEditar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnToolbarEditar);

        btnToolbarEliminar.setText("Eliminar");
        btnToolbarEliminar.setFocusable(false);
        btnToolbarEliminar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnToolbarEliminar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnToolbarEliminar);

        btnToolbarOrdenar.setText("Ordenar");
        btnToolbarOrdenar.setFocusable(false);
        btnToolbarOrdenar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnToolbarOrdenar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnToolbarOrdenar);

        btnToolbarExportar.setText("Exportar");
        btnToolbarExportar.setFocusable(false);
        btnToolbarExportar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnToolbarExportar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnToolbarExportar);

        jMenu1.setText("Archivo");

        exportarItem.setText("Exportar");
        jMenu1.add(exportarItem);

        menuArchivoNuevo.setText("Nuevo producto");
        jMenu1.add(menuArchivoNuevo);

        salirItem.setText("Salir");
        jMenu1.add(salirItem);

        menuBar.add(jMenu1);

        menuArchivo.setText("Herramientas");

        menuHerrOrdenar.setText("Ordenar productos");
        menuArchivo.add(menuHerrOrdenar);

        menuHerrEstadisticas.setText("Ver estadisticas");
        menuArchivo.add(menuHerrEstadisticas);

        menuHerrHistorial.setText("Ver historial");
        menuArchivo.add(menuHerrHistorial);

        menuBar.add(menuArchivo);

        jMenu2.setText("Ayuda");

        menuAyudaAcerca.setText("Acerca del sistema");
        jMenu2.add(menuAyudaAcerca);

        menuBar.add(jMenu2);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 787, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(143, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnToolbarEditar;
    private javax.swing.JButton btnToolbarEliminar;
    private javax.swing.JButton btnToolbarExportar;
    private javax.swing.JButton btnToolbarGuardar;
    private javax.swing.JButton btnToolbarNuevo;
    private javax.swing.JButton btnToolbarOrdenar;
    private javax.swing.JMenuItem exportarItem;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JMenu menuArchivo;
    private javax.swing.JMenuItem menuArchivoNuevo;
    private javax.swing.JMenuItem menuAyudaAcerca;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem menuHerrEstadisticas;
    private javax.swing.JMenuItem menuHerrHistorial;
    private javax.swing.JMenuItem menuHerrOrdenar;
    private javax.swing.JMenuItem salirItem;
    private javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables
}
