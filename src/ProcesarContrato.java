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

@WebServlet(name = "ProcesarContrato", urlPatterns = {"/ProcesarContrato"})
public class ProcesarContrato extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        //datos para el contrato
        String nombreCliente = request.getParameter("nombre_cliente");
        String telCliente = request.getParameter("tel_cliente");
        String nombreFestejado = request.getParameter("nombre_festejado");
        String fechaEvento = request.getParameter("fecha_evento");
        int idPaquete = Integer.parseInt(request.getParameter("id_paquete"));
        String dirMisa = request.getParameter("dir_misa");
        String horaMisa = request.getParameter("hora_misa");
        String dirFiesta = request.getParameter("dir_fiesta");
        String horaFiesta = request.getParameter("hora_fiesta");
        
        //convertimos a decimales por double
        double costoTotal = Double.parseDouble(request.getParameter("costo_total"));
        double anticipoPagado = Double.parseDouble(request.getParameter("anticipo_pagado"));

        //aqui es la conexion y el ql
        Connection conn = null;
        PreparedStatement ps = null;

        try (PrintWriter out = response.getWriter()) {
            //usamos el archivo de la conexion 
            conn = ConexionPool.getInstancia().getConnection();
            
            //aqui dejamos que MySQL calcule el saldo_restante del contrato 
            String sql = "INSERT INTO Contratos (nombre_cliente, nombre_festejado, tel_cliente, dir_misa, dir_fiesta, fecha_evento, hora_misa, hora_fiesta, id_paquete, costo_total, anticipo_pagado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            ps = conn.prepareStatement(sql);
            ps.setString(1, nombreCliente);
            ps.setString(2, nombreFestejado);
            ps.setString(3, telCliente);
            ps.setString(4, dirMisa);
            ps.setString(5, dirFiesta);
            ps.setString(6, fechaEvento);
            
            //si no se pone hora se pone un null para la bd
            if(horaMisa == null || horaMisa.isEmpty()) { ps.setNull(7, java.sql.Types.TIME); } else { ps.setString(7, horaMisa); }
            if(horaFiesta == null || horaFiesta.isEmpty()) { ps.setNull(8, java.sql.Types.TIME); } else { ps.setString(8, horaFiesta); }
            
            ps.setInt(9, idPaquete);
            ps.setDouble(10, costoTotal);
            ps.setDouble(11, anticipoPagado);

            //se guarda
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                //mensaje de que se guardo o no se pudo guardar
                out.println("<script>alert('Contrato registrado con éxito'); window.location.href='index.html';</script>");
            } else {
                out.println("<script>alert('Error: No se pudo guardar el contrato.'); window.history.back();</script>");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //siempre liberamos la conexión
            if (ps != null) { try { ps.close(); } catch (Exception e) {} }
            if (conn != null) { ConexionPool.getInstancia().releaseConnection(conn); }
        }
    }
}