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

@WebServlet(name = "ObtenerContratosParaAsignar", urlPatterns = {"/ObtenerContratosParaAsignar"})
public class ObtenerContratosParaAsignar extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try (PrintWriter out = response.getWriter()) {
            conn = ConexionPool.getInstancia().getConnection();
            
            //Contratos ordenados por los mas cercanos
            String sql = "SELECT c.id_contrato, c.fecha_evento, c.nombre_cliente, p.nombre_paquete " +
                         "FROM Contratos c " +
                         "INNER JOIN Paquetes p ON c.id_paquete = p.id_paquete " +
                         "ORDER BY c.fecha_evento ASC";
                         
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            boolean hayDatos = false;

            while (rs.next()) {
                hayDatos = true;
                out.print("<tr>");
                out.print("<td>" + rs.getString("fecha_evento") + "</td>");
                out.print("<td>" + rs.getString("nombre_cliente") + "</td>");
                out.print("<td>" + rs.getString("nombre_paquete") + "</td>");
                
                // Por ahora dejamos el estado como "Pendiente" fijo, después lo haremos dinámico
                out.print("<td><span style='color: #dc3545; font-weight: bold;'>Pendiente</span></td>");
                
                out.print("<td>");
                // Botón que gatilla la función de JavaScript para abrir el panel derecho
                out.print("<button class='btn-action' onclick='prepararAsignacion(" + 
                          rs.getInt("id_contrato") + ", \"" + 
                          rs.getString("nombre_cliente") + "\", \"" + 
                          rs.getString("fecha_evento") + "\", \"" + 
                          rs.getString("nombre_paquete") + "\")'>Asignar</button>");
                out.print("</td>");
                out.print("</tr>");
            }

           //no hay datos
            if (!hayDatos) {
                out.print("<tr><td colspan='5' style='text-align:center;'>No hay eventos pendientes.</td></tr>");
            }

        } catch (Exception e) {
            //Prueba de errores mientras se programaba
            try (PrintWriter out = response.getWriter()) {
                out.print("<tr><td colspan='5' style='color: red; font-weight: bold;'>Error interno: " + e.getMessage() + "</td></tr>");
            } catch (Exception ex) {}
            e.printStackTrace();
            
        } finally {
            if (rs != null) { try { rs.close(); } catch (Exception e) {} }
            if (ps != null) { try { ps.close(); } catch (Exception e) {} }
            if (conn != null) { ConexionPool.getInstancia().releaseConnection(conn); }
        }
    }
}