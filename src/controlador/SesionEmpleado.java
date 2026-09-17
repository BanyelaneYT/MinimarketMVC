
package controlador;

import modelo.Empleado;

public class SesionEmpleado {
    private static Empleado empleadoLogueado;

    public static void setEmpleado(Empleado usuario) {
        empleadoLogueado = usuario;
    }

    public static Empleado getEmpleado() {
        return empleadoLogueado;
    }

    public static void cerrarSesion() {
        empleadoLogueado = null;
    }
}
