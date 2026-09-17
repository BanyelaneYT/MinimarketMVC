package modelo;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String id_usuario;
    private String cargo;
    private String contraseña;
    
public Usuario(String id_usuario , String contraseña, String tipo_usuario) {
        this.id_usuario = id_usuario;
        this.contraseña = contraseña; 
        this.cargo = cargo; 
    }    
public String getPassword() {
        return contraseña;
    }

    public void setPassword(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getId() {
        return id_usuario;
    }

    public void setId(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    @Override
    public String toString() {
        return "ID: " + id_usuario + ", Cargo: " + cargo;
    }

    public String getUsuario() {
        return id_usuario; 
    }  
}

    