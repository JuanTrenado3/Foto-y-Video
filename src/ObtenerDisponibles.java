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

@WebServlet(name = "ObtenerDisponibles", urlPatterns = {"/ObtenerDisponibles"})
public class ObtenerDisponibles extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        //fecha del contrato y si buscamos "foto" o "video"
        String fecha = request.getParameter("fecha");
        String tipo = request.getParameter("tipo");
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try (PrintWriter out = response.getWriter()) {
            conn = ConexionPool.getInstancia().getConnection();
            
            String sql = "";
            
            //si es fotografo tenemos los Rol 2 (Fotógrafo) o Rol 5 (Híbrido)
            if ("foto".equals(tipo)) {
                sql = "SELECT id_empleado, nombre_completo FROM Empleados " +
                      "WHERE (id_rol = 2 OR id_rol = 5) " +
                      "AND id_empleado NOT IN (" +
                      "   SELECT a.id_empleado FROM Asignaciones a " +
                      "   INNER JOIN Contratos c ON a.id_contrato = c.id_contrato " +
                      "   WHERE c.fecha_evento = ?" +
                      ")";
            } 
            //si es video tenemos Rol 3 (Camarógrafo) o Rol 5 (Híbrido)
            else if ("video".equals(tipo)) {
                sql = "SELECT id_empleado, nombre_completo FROM Empleados " +
                      "WHERE (id_rol = 3 OR id_rol = 5) " +
                      "AND id_empleado NOT IN (" +
                      "   SELECT a.id_empleado FROM Asignaciones a " +
                      "   INNER JOIN Contratos c ON a.id_contrato = c.id_contrato " +
                      "   WHERE c.fecha_evento = ?" +
                      ")";
            }

            ps = conn.prepareStatement(sql);
            ps.setString(1, fecha);
            rs = ps.executeQuery();

            //El paquete no tiene servicio
            out.print("<option value=''>-- Seleccionar o No Aplica --</option>");
            
            boolean hayDisponibles = false;
            while (rs.next()) {
                hayDisponibles = true;
                out.print("<option value='" + rs.getInt("id_empleado") + "'>" + 
                          rs.getString("nombre_completo") + "</option>");
            }
            
            if (!hayDisponibles) {
                out.print("<option value='' disabled>No hay personal libre este día</option>");
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