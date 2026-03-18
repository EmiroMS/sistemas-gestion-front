package sistemaGestionFront.session;

public class Sesion {

    private static String nombre;
    private static String rol;

    public static String getNombre() {
        return nombre;
    }

    public static void setNombre(String nombre) {
        Sesion.nombre = nombre;
    }

    public static String getRol() {
        return rol;
    }

    public static void setRol(String rol) {
        Sesion.rol = rol;
    }
}