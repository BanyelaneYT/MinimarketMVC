
package modelo;
import java.io.Serializable;
import java.util.List;

public class Producto implements Serializable {
    private String codigo;
    private String nombre;
    private double precio;
    private int stock;
    private int active;

    public Producto(String codigo, String nombre, double precio, int stock, int active) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.active = active;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    public int getactive() {
        return active;
    }

    public void setactive(int active) {
        this.active = active;
    }

    public void actualizarStock(int cantidadVendida) {
        this.stock -= cantidadVendida;
    }

    // ... (Getters y Setters se mantienen) ...

    @Override
    public String toString() {
        return "Código: " + codigo + ", Nombre: " + nombre + ", Precio: " + precio + ", Stock: " + stock+ ", Active: " + active;
    }
}
