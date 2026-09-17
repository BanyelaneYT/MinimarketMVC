package modelo;

import java.io.Serializable;
public class Empleado implements Serializable {
    private String id;
    private String dni_empleado;
    private String nombre;
    private String cargo;
    private String password;

 
    public Empleado(String id,String dni, String nombre, String contraseña, String cargo) {
        this.id = id;
        this.dni_empleado = dni;
        this.nombre = nombre;
        this.password = contraseña; 
        this.cargo = cargo; 
    }

    public Empleado(String string, String string0, String id, String id0) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
   
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getDNI() {
        return dni_empleado;
    }

    public void setDNI(String dni) {
        this.dni_empleado = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", DNI: " + dni_empleado +", Nombre: " + nombre + ", Cargo: " + cargo;
    }

    public String getUsuario() {
        return id; 
    }  
}

    