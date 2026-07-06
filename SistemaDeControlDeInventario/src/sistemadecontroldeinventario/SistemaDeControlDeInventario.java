package sistemadecontroldeinventario;

import presentacion.MainFrame;

public class SistemaDeControlDeInventario {

    public static void main(String[] args) {
        try {
            // set si existe
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            //si falla imprime error en consola
            ex.printStackTrace();
        }

        // abrir ventana principal 
        java.awt.EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
    }
}