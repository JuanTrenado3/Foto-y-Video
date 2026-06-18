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

@WebServlet(name = "RegistrarUsuario", urlPatterns = {"/RegistrarUsuario"})
public class RegistrarUsuario extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");

        //recibimos los datos de tu formulario HTML
        String nombre = request.getParameter("nombre_completo");
        String telefono = request.getParameter("telefono");
        String password = request.getParameter("password");
        String idRolStr = request.getParameter("id_rol");

        Connection conn = null;
        PreparedStatement ps = null;

        try (PrintWriter out = response.getWriter()) {
            
            //validamos que no nos manden campos vacíos
            if (nombre == null || telefono == null || password == null || idRolStr == null || nombre.isEmpty()) {
                out.print("Faltan datos obligatorios para completar el registro.");
                return;
            }

            conn = ConexionPool.getInstancia().getConnection();
            
            //consulta para registrar usuario
            String sql = "INSERT INTO Empleados (nombre_completo, telefono, password, id_rol) VALUES (?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            
            ps.setString(1, nombre);
            ps.setString(2, telefono);
            ps.setString(3, password);
            ps.setInt(4, Integer.parseInt(idRolStr));
            
            int filasInsertadas = ps.executeUpdate();
            
            if (filasInsertadas > 0) {
                out.print("OK");
            } else {
                out.print("No se pudo guardar el registro en la base de datos.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            try (PrintWriter outError = response.getWriter()) {
                outError.print("Error interno del servidor al procesar tu solicitud.");
            } catch (Exception ex) {}
            
        } finally {
            if (ps != null) { try { ps.close(); } catch (Exception e) {} }
            if (conn != null) { ConexionPool.getInstancia().releaseConnection(conn); }
        }
    }
}