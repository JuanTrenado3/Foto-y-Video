package utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConexionPool {
    /*
    //CONEXION A BASE DE DATOS LOCAL
    private static final String URL = "jdbc:mysql://localhost:3306/estudio_fotografico";*/
    //CONEXIOPN A BASE DE DATOS ESCUELA
    /*
    private static final String URL = "jdbc:mysql://localhost:3388/estudio_fotografico?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";
    */
    //CONEXION A BASE DE DATOS EN LA NUBE
    private static final String URL = "jdbc:mysql://thomas.proxy.rlwy.net:13861/railway?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "SHelaMMzSLEVryKSpTbegKgsIGhdVxzQ";
    
    // TUNEL SSH PARA SERVIDOR FIE
    /*String url = "jdbc:mysql://localhost:3388/estudio_fotografico?useSSL=false&serverTimezone=UTC";*/
    
    
    //Tamaño de pool osea conexiones abiertas al mismo tiempo
    private static final int MAX_CONEXIONES = 5;
    
    // Instancia única 
    private static ConexionPool instancia = null;
    private List<Connection> conexionesDisponibles = new ArrayList<>();

    // Constructor privado para inicializar las conexiones
    private ConexionPool() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            for (int i = 0; i < MAX_CONEXIONES; i++) {
                conexionesDisponibles.add(crearConexion());
            }
        } catch (Exception e) {
            System.out.println("Error al inicializar el Pool de Conexiones: " + e.getMessage());
        }
    }

    // Método para obtener la instancia del Pool
    public static synchronized ConexionPool getInstancia() {
        if (instancia == null) {
            instancia = new ConexionPool();
        }
        return instancia;
    }

    // Método interno para crear una conexión nueva
    private Connection crearConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    // Método que usarán tus DAOs para pedir una conexión prestada
    public synchronized Connection getConnection() {
        if (conexionesDisponibles.isEmpty()) {
            System.out.println("Todas las conexiones están en uso. Espera un momento.");
            return null; 
        }
        // Saca la última conexión de la lista y la entrega
        return conexionesDisponibles.remove(conexionesDisponibles.size() - 1);
    }

    // Método que usarán tus DAOs para devolver la conexión cuando terminen
    public synchronized void releaseConnection(Connection conexion) {
        if (conexion != null) {
            conexionesDisponibles.add(conexion); 
        }
    }
}