import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utilidades.ConexionPool;

@WebServlet(name = "ListarUsuarios", urlPatterns = {"/ListarUsuarios"})
public class ListarUsuarios extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try (PrintWriter out = response.getWriter()) {
            conn = ConexionPool.getInstancia().getConnection();
            
            String sql = "SELECT e.id_empleado, e.nombre_completo, e.telefono, e.password, e.id_rol, r.nombre_rol " +
                         "FROM Empleados e " +
                         "INNER JOIN Roles r ON e.id_rol = r.id_rol " +
                         "ORDER BY e.id_empleado ASC";
            
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_empleado");
                String nombre = rs.getString("nombre_completo");
                String tel = rs.getString("telefono");
                String pass = rs.getString("password");
                int idRol = rs.getInt("id_rol");
                String nombreRol = rs.getString("nombre_rol");

                out.print("<tr>");
                out.print("<td>" + nombre + "</td>");
                out.print("<td>" + tel + "</td>");
                out.print("<td>" + nombreRol + "</td>");
                out.print("<td>");
                
                //btn editar este pasa todos los datos a la función JS
                out.print("<button class='btn-edit' onclick=\"prepararEdicion(" + 
                          id + ", '" + nombre + "', '" + tel + "', '" + pass + "', " + idRol + ")\">Editar</button>");
                
                //btn eliminar
                out.print("<button class='btn-delete' onclick=\"darDeBaja(" + id + ", '" + nombre + "')\">Baja</button>");
                
                out.print("</td>");
                out.print("</tr>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) { try { rs.close(); } catch (Exception e) {} }
            if (ps != null) { try { ps.close(); } catch (Exception e) {} }
            if (conn != null) { ConexionPool.getInstancia().releaseConnection(conn); }
        }
    }
}