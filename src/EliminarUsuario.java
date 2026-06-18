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

@WebServlet(name = "EliminarUsuario", urlPatterns = {"/EliminarUsuario"})
public class EliminarUsuario extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/plain;charset=UTF-8");
        String idStr = request.getParameter("id_empleado");

        Connection conn = null;
        PreparedStatement psAsig = null;
        PreparedStatement psEmp = null;

        try (PrintWriter out = response.getWriter()) {
            int idEmpleado = Integer.parseInt(idStr);
            conn = ConexionPool.getInstancia().getConnection();
            
            //limpieza preventiva de asignaciones ligadas a este id
            String sqlAsig = "DELETE FROM Asignaciones WHERE id_empleado = ?";
            psAsig = conn.prepareStatement(sqlAsig);
            psAsig.setInt(1, idEmpleado);
            psAsig.executeUpdate();
            
            //baja del empleado
            String sqlEmp = "DELETE FROM Empleados WHERE id_empleado = ?";
            psEmp = conn.prepareStatement(sqlEmp);
            psEmp.setInt(1, idEmpleado);
            
            if (psEmp.executeUpdate() > 0) {
                out.print("OK");
            } else {
                out.print("No se pudo eliminar al usuario.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try (PrintWriter outError = response.getWriter()) { outError.print("Error al dar de baja."); } catch (Exception ex) {}
        } finally {
            if (psAsig != null) { try { psAsig.close(); } catch (Exception e) {} }
            if (psEmp != null) { try { psEmp.close(); } catch (Exception e) {} }
            if (conn != null) { ConexionPool.getInstancia().releaseConnection(conn); }
        }
    }
}