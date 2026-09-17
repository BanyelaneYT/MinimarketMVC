
package controlador;

import modelo.Usuario;

public class SesionUsuario {
    private static Usuario empleadoLogueado;

    public static void setUsuario(Usuario usuario) {
        empleadoLogueado = usuario;
    }

    public static Usuario getEmpleado() {
        return empleadoLogueado;
    }

    public static void cerrarSesion() {
        empleadoLogueado = null;
    }
}