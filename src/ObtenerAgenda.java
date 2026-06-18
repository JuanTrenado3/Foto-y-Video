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
import javax.servlet.http.HttpSession;
import utilidades.ConexionPool;

@WebServlet(name = "ObtenerAgenda", urlPatterns = {"/ObtenerAgenda"})
public class ObtenerAgenda extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession();
        
        int idRol = 1; 
        int idEmpleado = 1;

        try {
            if (session.getAttribute("id_rol") != null) {
                idRol = Integer.parseInt(session.getAttribute("id_rol").toString());
            }
            if (session.getAttribute("id_empleado") != null) {
                idEmpleado = Integer.parseInt(session.getAttribute("id_empleado").toString());
            }
        } catch (Exception e) {}

        String buscar = request.getParameter("buscar");
        if (buscar == null) { buscar = ""; }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try (PrintWriter out = response.getWriter()) {
            conn = ConexionPool.getInstancia().getConnection();
            String sql = "";
            
            //rol 1 y 2 Añadimos horas y direcciones al SELECT
            if (idRol == 1 || idRol == 2) {
                sql = "SELECT c.fecha_evento, c.nombre_cliente, c.nombre_festejado, p.nombre_paquete, " +
                      "c.hora_misa, c.hora_fiesta, c.dir_misa, c.dir_fiesta " +
                      "FROM Contratos c " +
                      "INNER JOIN Paquetes p ON c.id_paquete = p.id_paquete " +
                      "WHERE c.nombre_cliente LIKE ? OR c.nombre_festejado LIKE ? OR c.fecha_evento LIKE ? " +
                      "ORDER BY c.fecha_evento DESC";
                
                ps = conn.prepareStatement(sql);
                String queryFilter = "%" + buscar + "%";
                ps.setString(1, queryFilter);
                ps.setString(2, queryFilter);
                ps.setString(3, queryFilter);
            } 
            //rol 1, 2, 3 añadimos horas y direcciones al SELECT
            else {
                sql = "SELECT c.fecha_evento, c.nombre_cliente, c.nombre_festejado, p.nombre_paquete, a.labor_asignada, " +
                      "c.hora_misa, c.hora_fiesta, c.dir_misa, c.dir_fiesta " +
                      "FROM Asignaciones a " +
                      "INNER JOIN Contratos c ON a.id_contrato = c.id_contrato " +
                      "INNER JOIN Paquetes p ON c.id_paquete = p.id_paquete " +
                      "WHERE a.id_empleado = ? AND c.fecha_evento >= CURDATE() " +
                      "ORDER BY c.fecha_evento ASC";
                
                ps = conn.prepareStatement(sql);
                ps.setInt(1, idEmpleado);
            }

            rs = ps.executeQuery();
            boolean hayDatos = false;

            while (rs.next()) {
                hayDatos = true;
                
                //formateamos las horas (quitar los segundos si no son nulos)
                String hMisa = rs.getString("hora_misa") != null ? rs.getString("hora_misa").substring(0, 5) : "N/A";
                String hFiesta = rs.getString("hora_fiesta") != null ? rs.getString("hora_fiesta").substring(0, 5) : "N/A";
                
                //validamos direcciones nulas
                String dMisa = rs.getString("dir_misa") != null ? rs.getString("dir_misa") : "No registrada";
                String dFiesta = rs.getString("dir_fiesta") != null ? rs.getString("dir_fiesta") : "No registrada";

                out.print("<tr>");
                out.print("<td><strong>" + rs.getString("fecha_evento") + "</strong></td>");
                out.print("<td>" + rs.getString("nombre_cliente") + "</td>");
                out.print("<td>" + (rs.getString("nombre_festejado") != null ? rs.getString("nombre_festejado") : "N/A") + "</td>");
                
                //horarios
                out.print("<td><span class='txt-sub'>Misa:</span> " + hMisa + "<br><span class='txt-sub'>Fiesta:</span> " + hFiesta + "</td>");
                
                //direcciones
                out.print("<td><span class='txt-sub'>Misa:</span> " + dMisa + "<br><span class='txt-sub'>Fiesta:</span> " + dFiesta + "</td>");
                
                out.print("<td>" + rs.getString("nombre_paquete") + "</td>");
                
                if (idRol == 1 || idRol == 2) {
                    out.print("<td><span style='color: #28a745; font-weight: bold;'>Registrado</span></td>");
                } else {
                    out.print("<td><span class='badge-labor'>" + rs.getString("labor_asignada") + "</span></td>");
                }
                out.print("</tr>");
            }

            if (!hayDatos) {
                //cambiado a colspan='7' porque ahora la tabla es más ancha
                out.print("<tr><td colspan='7' style='text-align:center; color:#777;'>No se encontraron eventos en la agenda.</td></tr>");
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