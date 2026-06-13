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

@WebServlet(name = "ProcesarPaquete", urlPatterns = {"/ProcesarPaquete"})
public class ProcesarPaquete extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");

        String idPaquete = request.getParameter("id_paquete");
        String categoriaEvento = request.getParameter("categoria_evento");
        String nombrePaquete = request.getParameter("nombre_paquete");
        int numFotos = Integer.parseInt(request.getParameter("num_fotos"));
        int incluyeVideo = Integer.parseInt(request.getParameter("incluye_video"));
        int incluyeSesion = Integer.parseInt(request.getParameter("incluye_sesion"));
        int incluyeFotoGrande = Integer.parseInt(request.getParameter("incluye_foto_grande")); // Atrapamos el nuevo dato
        double precioBase = Double.parseDouble(request.getParameter("precio_base"));

        Connection conn = null;
        PreparedStatement ps = null;

        try (PrintWriter out = response.getWriter()) {
            conn = ConexionPool.getInstancia().getConnection();
            String sql;

            if (idPaquete == null || idPaquete.trim().isEmpty()) {
                // Le agregamos incluye_foto_grande a la consulta
                sql = "INSERT INTO Paquetes (categoria_evento, nombre_paquete, num_fotos, incluye_video, incluye_sesion, incluye_foto_grande, precio_base) VALUES (?, ?, ?, ?, ?, ?, ?)";
                ps = conn.prepareStatement(sql);
                ps.setString(1, categoriaEvento);
                ps.setString(2, nombrePaquete);
                ps.setInt(3, numFotos);
                ps.setInt(4, incluyeVideo);
                ps.setInt(5, incluyeSesion);
                ps.setInt(6, incluyeFotoGrande);
                ps.setDouble(7, precioBase);     
            } else {
                sql = "UPDATE Paquetes SET categoria_evento=?, nombre_paquete=?, num_fotos=?, incluye_video=?, incluye_sesion=?, incluye_foto_grande=?, precio_base=? WHERE id_paquete=?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, categoriaEvento);
                ps.setString(2, nombrePaquete);
                ps.setInt(3, numFotos);
                ps.setInt(4, incluyeVideo);
                ps.setInt(5, incluyeSesion);
                ps.setInt(6, incluyeFotoGrande); 
                ps.setDouble(7, precioBase);    
                ps.setInt(8, Integer.parseInt(idPaquete)); 
            }

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                out.print("exito"); 
            } else {
                out.print("error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            if (ps != null) { try { ps.close(); } catch (Exception e) {} }
            if (conn != null) { ConexionPool.getInstancia().releaseConnection(conn); }
        }
    }
}