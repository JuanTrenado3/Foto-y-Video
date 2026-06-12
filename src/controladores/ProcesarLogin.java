package controladores;

import basedatos.VerificarUsuario;
import modelos.Empleado;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// Este es el nombre que usará el HTML para encontrar este archivo
@WebServlet("/ProcesarLogin")
public class ProcesarLogin extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String telefono = request.getParameter("telefono");
        String password = request.getParameter("password");
        
        // Usamos el archivo que acabamos de crear arriba
        VerificarUsuario validador = new VerificarUsuario();
        Empleado usuario = validador.buscar(telefono, password);
        
        if (usuario != null) {
            HttpSession sesion = request.getSession();
            sesion.setAttribute("idEmpleado", usuario.getIdEmpleado());
            sesion.setAttribute("nombre", usuario.getNombreCompleto());
            sesion.setAttribute("idRol", usuario.getIdRol());
            
            out.print("{\"status\":\"success\", \"rol\":" + usuario.getIdRol() + "}");
        } else {
            out.print("{\"status\":\"error\", \"mensaje\":\"Teléfono o contraseña incorrectos\"}");
        }
        out.flush();
    }
}