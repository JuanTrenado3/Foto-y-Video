package basedatos;

import modelos.Empleado;
import utilidades.ConexionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class VerificarUsuario {
    
    //funcion que busca al usuario en MySQL
    public Empleado buscar(String telefono, String password) {
        Empleado empleado = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConexionPool.getInstancia().getConnection();
            
            String sql = "SELECT * FROM Empleados WHERE telefono = ? AND password = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, telefono);
            ps.setString(2, password);
            
            rs = ps.executeQuery();
            
            if (rs.next()) {
                empleado = new Empleado(
                    rs.getInt("id_empleado"),
                    rs.getString("nombre_completo"),
                    rs.getString("telefono"),
                    rs.getString("password"),
                    rs.getInt("id_rol")
                );
            }
        } catch (Exception e) {
            System.out.println("Error al buscar usuario: " + e.getMessage());
        } finally {
            ConexionPool.getInstancia().releaseConnection(con);
        }
        
        return empleado;
    }
}