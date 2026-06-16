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

@WebServlet(name = "ObtenerOpcionesPaquetes", urlPatterns = {"/ObtenerOpcionesPaquetes"})
public class ObtenerOpcionesPaquetes extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try (PrintWriter out = response.getWriter()) {
            conn = ConexionPool.getInstancia().getConnection();
            String sql = "SELECT id_paquete, nombre_paquete, precio_base FROM Paquetes";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            //se imprime la primera opcion por defecto
            out.print("<option value=''>Seleccione un paquete...</option>");
            
            //se lee la base de datos
            while (rs.next()) {
                out.print("<option value='" + rs.getInt("id_paquete") + "'>" 
                        + rs.getString("nombre_paquete") + " ($" + rs.getDouble("precio_base") + ")" 
                        + "</option>");
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