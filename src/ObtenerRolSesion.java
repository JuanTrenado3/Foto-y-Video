import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ObtenerRolSesion", urlPatterns = {"/ObtenerRolSesion"})
public class ObtenerRolSesion extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/plain;charset=UTF-8");
        HttpSession session = request.getSession();
        
        int idRol = 1; //rol admin
        
        try {
            if (session.getAttribute("id_rol") != null) {
                idRol = Integer.parseInt(session.getAttribute("id_rol").toString());
            }
        } catch (Exception e) {}
        
        try (PrintWriter out = response.getWriter()) {
            out.print(idRol);
        }
    }
}