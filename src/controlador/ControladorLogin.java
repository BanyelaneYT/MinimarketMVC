
package controlador;

import javax.swing.*;
import java.sql.*;
import vista.MinimarketGUI;
import controlador.ConexionBD;
import modelo.Empleado;


public class ControladorLogin {
    public static void login(String id, String password, JFrame loginFrame) {
        try (Connection conn = ConexionBD.obtenerConexion()) {
            String query = "SELECT * FROM empleados WHERE id = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, id);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Empleado emp = new Empleado(rs.getString("nombre"), rs.getString("nombre"), id, id);
                SesionEmpleado.setEmpleado(emp);
                JOptionPane.showMessageDialog(null, "Bienvenido " + emp.getNombre());

                MinimarketGUI gui = new MinimarketGUI();
                gui.setVisible(true);
                loginFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "DNI o contraseña incorrectos");
            }
        } catch (SQLException e) {
        }
    }
}
