package presentacion;

import modelo.Producto;
import negocio.ProductoNegocio;
import excepciones.DatoInvalidoException;
import excepciones.ProductoDuplicadoException;

import javax.swing.*;
import java.awt.*;

public class registroPanel extends javax.swing.JPanel {

    private ProductoNegocio negocio; 
    private EstadisticaPanel estadisticaPanel;
    private ProductoPanel productoPanel; 

    private String codigoOriginalEnEdicion = null;

    public registroPanel() {
        initComponents();
        inicializar();
    }

    //constructor con negocio
    public registroPanel(ProductoNegocio negocio) {
        this.negocio = negocio;
        initComponents();
        inicializar();
    }

    public void setEstadisticaPanel(EstadisticaPanel estadisticaPanel) {
        this.estadisticaPanel = estadisticaPanel;
    }

    public void setProductoPanel(ProductoPanel productoPanel) {
        this.productoPanel = productoPanel;
    }

    private void inicializar() {
        //valores por defecto
        txtCantidad.setText("1");
        lblId.setText("ID: -");

        //poblar combo si esta vacio
        if (cmbCategoria.getItemCount() == 0) {
            String[] cats = new String[]{
                    "Electronica", "Oficina", "Mobiliario", "Redes",
                     "Mantenimiento", "Seguridad", "Software y Licencias"
            };
            for (String c : cats) cmbCategoria.addItem(c);
        }

        // boton limpiar
        btnLimpiar.addActionListener(e -> {
            limpiarCampos();
            codigoOriginalEnEdicion = null;
            btnGuardar.setText("Guardar");
        });

        // boton guardar
        btnGuardar.addActionListener(e -> {
            try {
                Producto p = leerProductoDesdeCampos();
                if (negocio != null) {
                    if (codigoOriginalEnEdicion == null) {
                        negocio.agregar(p);
                        JOptionPane.showMessageDialog(this, "Producto agregado", "Info", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        negocio.editar(codigoOriginalEnEdicion, p);
                        JOptionPane.showMessageDialog(this, "Producto actualizado", "Info", JOptionPane.INFORMATION_MESSAGE);
                    }

                    if (productoPanel != null) productoPanel.refreshTable();
                    if (estadisticaPanel != null) estadisticaPanel.actualizarEstadisticas();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Producto listo para guardar:\n" +
                                    "Codigo: " + p.getCodigo() + "\n" +
                                    "Nombre: " + p.getNombre(),
                            "Info", JOptionPane.INFORMATION_MESSAGE);
                }

                limpiarCampos();
                codigoOriginalEnEdicion = null;
                btnGuardar.setText("Guardar");

            } catch (DatoInvalidoException | ProductoDuplicadoException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Precio o cantidad invalidos", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error : " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    //lee campos y valida
    private Producto leerProductoDesdeCampos() throws DatoInvalidoException {
        String codigo = txtCodigo.getText() == null ? "" : txtCodigo.getText().trim();
        String nombre = txtNombre.getText() == null ? "" : txtNombre.getText().trim();
        String precioStr = txtPrecio.getText() == null ? "" : txtPrecio.getText().trim();
        String categoria = (cmbCategoria.getSelectedItem() == null) ? "" : cmbCategoria.getSelectedItem().toString().trim();
        String cantidadStr = txtCantidad.getText() == null ? "" : txtCantidad.getText().trim();
        boolean disponible = chkDisponible.isSelected();
        String descripcion = txtDescripcion.getText() == null ? "" : txtDescripcion.getText().trim();

        if (codigo.isEmpty()) throw new DatoInvalidoException("El codigo es obligatorio");
        if (nombre.isEmpty()) throw new DatoInvalidoException("El nombre es obligatorio");
        if (precioStr.isEmpty()) throw new DatoInvalidoException("El precio es obligatorio");
        if (cantidadStr.isEmpty()) throw new DatoInvalidoException("La cantidad es obligatoria");

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
            if (cantidad < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            throw new DatoInvalidoException("La cantidad debe ser un entero no negativo");
        }

        double precio = Double.parseDouble(precioStr);
        if (precio <= 0) throw new DatoInvalidoException("El precio debe ser mayor a 0");

        return new Producto(codigo, nombre, precio, categoria, cantidad, disponible, descripcion);
    }

    // limpia campos
    private void limpiarCampos() {
        lblId.setText("ID: -");
        txtCodigo.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("1");
        if (cmbCategoria.getItemCount() > 0) cmbCategoria.setSelectedIndex(0);
        chkDisponible.setSelected(false);
        txtDescripcion.setText("");
    }

    //carga producto en formulario para editar
    public void cargarProductoEnFormulario(Producto p) {
        if (p == null) return;
        lblId.setText("ID: " + p.getId());
        txtCodigo.setText(p.getCodigo());
        txtNombre.setText(p.getNombre());
        txtPrecio.setText(String.format("%.2f", p.getPrecio()));
        txtCantidad.setText(String.valueOf(p.getCantidad()));
        cmbCategoria.setSelectedItem(p.getCategoria());
        chkDisponible.setSelected(p.isDisponible());
        txtDescripcion.setText(p.getDescripcion() == null ? "" : p.getDescripcion());

        codigoOriginalEnEdicion = p.getCodigo();
        btnGuardar.setText("Actualizar");
    }

    //guardar desde toolbar
    public void guardarDesdeToolbar() {
        btnGuardar.doClick();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblId = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        cmbCategoria = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        chkDisponible = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        spDescripcion = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();

        lblId.setText("ID: ");

        jLabel1.setText("Codigo:");

        jLabel2.setText("Nombre:");

        cmbCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mobilario", "Perifericos", "Mantenimienro", "Pc´s", "Latops", " " }));

        jLabel3.setText("Categoria:");

        jLabel4.setText("Precio:");

        chkDisponible.setText("Disponible");

        jLabel5.setText("Estado:");

        jLabel6.setText("Cantidad:");

        jLabel7.setText("Descripcion:");

        btnGuardar.setText("Guardar");

        btnLimpiar.setText("Limpiar");

        txtDescripcion.setColumns(30);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setRows(4);
        txtDescripcion.setWrapStyleWord(true);
        spDescripcion.setViewportView(txtDescripcion);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblId)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                                    .addComponent(txtCodigo))
                                .addGap(27, 27, 27)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                                    .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(chkDisponible, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtPrecio, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtCantidad, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(cmbCategoria, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addComponent(jLabel7)
                    .addComponent(spDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblId)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardar))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpiar))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkDisponible)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(spDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JCheckBox chkDisponible;
    private javax.swing.JComboBox<String> cmbCategoria;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel lblId;
    private javax.swing.JScrollPane spDescripcion;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextArea txtDescripcion;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecio;
    // End of variables declaration//GEN-END:variables
}
