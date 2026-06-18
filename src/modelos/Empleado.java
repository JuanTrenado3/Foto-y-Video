package modelos;

public class Empleado {
    //variables privadas de la base de datos
    private int idEmpleado;
    private String nombreCompleto;
    private String telefono;
    private String password;
    private int idRol;

    //constructor vacío (Obligatorio para que Java lo pueda instanciar fácilmente)
    public Empleado() {
    }

    //constructor con todos los parámetros 
    public Empleado(int idEmpleado, String nombreCompleto, String telefono, String password, int idRol) {
        this.idEmpleado = idEmpleado;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.password = password;
        this.idRol = idRol;
    }

    //getters y Setters para acceder y modificar los datos de forma segura
    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }
}