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

@WebServlet(name = "GuardarAsignacion", urlPatterns = {"/GuardarAsignacion"})
public class GuardarAsignacion extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");

        String idContratoStr = request.getParameter("id_contrato");
        String idFotografoStr = request.getParameter("id_fotografo");
        String idCamarografoStr = request.getParameter("id_camarografo");

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConexionPool.getInstancia().getConnection();
            conn.setAutoCommit(false);
            boolean exito = false;

            //consulta lista con la columna labor_asignada
            String sql = "INSERT INTO Asignaciones (id_contrato, id_empleado, labor_asignada) VALUES (?, ?, ?)";
            
            //se guarda al fotógrafo
            if (idFotografoStr != null && !idFotografoStr.isEmpty()) {
                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(idContratoStr));
                ps.setInt(2, Integer.parseInt(idFotografoStr));
                ps.setString(3, "Fotografía");
                ps.executeUpdate();
                ps.close();
                exito = true;
            }

            //se guarda al camarógrafo
            if (idCamarografoStr != null && !idCamarografoStr.isEmpty()) {
                ps = conn.prepareStatement(sql);
                ps.setInt(1, Integer.parseInt(idContratoStr));
                ps.setInt(2, Integer.parseInt(idCamarografoStr));
                ps.setString(3, "Video");
                ps.executeUpdate();
                ps.close();
                exito = true;
            }

            //respondemos según el resultado
            try (PrintWriter out = response.getWriter()) {
                if (exito) {
                    conn.commit();
                    out.print("OK");
                } else {
                    out.print("Debes seleccionar al menos un empleado.");
                }
            }

        } catch (Exception e) {
            //Si hay error deshacemos los cambios y enviamos un mensaje genérico
            if (conn != null) { try { conn.rollback(); } catch (Exception ex) {} }
            e.printStackTrace(); // El error real solo se queda en la consola de NetBeans
            
            try (PrintWriter outError = response.getWriter()) {
                outError.print("Ocurrió un error interno al guardar la asignación.");
            } catch (Exception ex) {}
            
        } finally {
            if (ps != null) { try { ps.close(); } catch (Exception e) {} }
            if (conn != null) { 
                try { conn.setAutoCommit(true); } catch (Exception e) {}
                ConexionPool.getInstancia().releaseConnection(conn); 
            }
        }
    }
}