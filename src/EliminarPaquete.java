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

@WebServlet(name = "EliminarPaquete", urlPatterns = {"/EliminarPaquete"})
public class EliminarPaquete extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/plain;charset=UTF-8");
        String idPaquete = request.getParameter("id");

        Connection conn = null;
        PreparedStatement ps = null;

        try (PrintWriter out = response.getWriter()) {
            conn = ConexionPool.getInstancia().getConnection();
            
            String sql = "DELETE FROM Paquetes WHERE id_paquete = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(idPaquete));

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                out.print("exito");
            } else {
                out.print("error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Si hay un error, lo imprimimos 
        } finally {
            if (ps != null) { try { ps.close(); } catch (Exception e) {} }
            if (conn != null) { ConexionPool.getInstancia().releaseConnection(conn); }
        }
    }
}