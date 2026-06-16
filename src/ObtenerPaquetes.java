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

@WebServlet(name = "ObtenerPaquetes", urlPatterns = {"/ObtenerPaquetes"})
public class ObtenerPaquetes extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        //pagina responsiva
        response.setContentType("text/html;charset=UTF-8");
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try (PrintWriter out = response.getWriter()) {
            conn = ConexionPool.getInstancia().getConnection();
            String sql = "SELECT * FROM Paquetes";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery(); 

            //Leemos los paquetes de la bd
            while (rs.next()) {
                out.print("<tr>");
                out.print("<td>" + rs.getString("categoria_evento") + "</td>");
                out.print("<td>" + rs.getString("nombre_paquete") + "</td>");
                out.print("<td>$" + rs.getDouble("precio_base") + "</td>");
                out.print("<td>");
                
                //bototn editar y datos 
                out.print("<button class='btn-edit' onclick='editarPaquete(" + 
                          rs.getInt("id_paquete") + ", \"" + 
                          rs.getString("categoria_evento") + "\", \"" + 
                          rs.getString("nombre_paquete") + "\", " + 
                          rs.getInt("num_fotos") + ", " + 
                          rs.getInt("incluye_video") + ", " + 
                          rs.getInt("incluye_sesion") + ", " + 
                          rs.getInt("incluye_foto_grande") + ", " +
                          rs.getDouble("precio_base") + 
                          ")'>Editar</button> ");
                          
                //btn eliminar
                out.print("<button class='btn-delete' onclick='eliminarPaquete(" + rs.getInt("id_paquete") + ")'>Eliminar</button>");
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