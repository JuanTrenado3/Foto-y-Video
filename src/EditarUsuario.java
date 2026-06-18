import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utilidades.ConexionPool;

@WebServlet(name = "EditarUsuario", urlPatterns = {"/EditarUsuario"})
public class EditarUsuario extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");

        String idStr = request.getParameter("id_empleado");
        String nombre = request.getParameter("nombre_completo");
        String telefono = request.getParameter("telefono");
        String password = request.getParameter("password");
        String idRolStr = request.getParameter("id_rol");

        Connection conn = null;
        PreparedStatement ps = null;

        try (PrintWriter out = response.getWriter()) {
            conn = ConexionPool.getInstancia().getConnection();
            
            String sql = "UPDATE Empleados SET nombre_completo=?, telefono=?, password=?, id_rol=? WHERE id_empleado=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, telefono);
            ps.setString(3, password);
            ps.setInt(4, Integer.parseInt(idRolStr));
            ps.setInt(5, Integer.parseInt(idStr));

            if (ps.executeUpdate() > 0) {
                out.print("OK");
            } else {
                out.print("No se encontraron cambios para guardar.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try (PrintWriter outError = response.getWriter()) { outError.print("Error al editar."); } catch (Exception ex) {}
        } finally {
            if (ps != null) { try { ps.close(); } catch (Exception e) {} }
            if (conn != null) { ConexionPool.getInstancia().releaseConnection(conn); }
        }
    }
}