package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Venta implements Serializable {
    private String id;
    private Date fecha;
    private Cliente cliente;
    private Empleado empleado;
    private String tipoComprobante;
    private double total; 
    private List<ItemVenta> items;

    // Constructor vacío
    public Venta() {
        this.id = UUID.randomUUID().toString();
        this.fecha = new Date();
        this.items = new ArrayList<>();
        this.total = 0.0; 
    }

    public Venta(String id, Date fecha, Cliente cliente, Empleado empleado, String tipoComprobante, double total) {
        this.id = id;
        this.fecha = fecha;
        this.cliente = cliente;
        this.empleado = empleado;
        this.tipoComprobante = tipoComprobante;
        this.total = total;
        this.items = new ArrayList<>();
    }

    public Venta(String id, Date fecha, Cliente cliente, Empleado empleado, String tipoComprobante, double total, List<ItemVenta> items) {
        this.id = id;
        this.fecha = fecha;
        this.cliente = cliente;
        this.empleado = empleado;
        this.tipoComprobante = tipoComprobante;
        this.total = total;
        this.items = items != null ? items : new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public Date getFecha() {
        return fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public String getTipoComprobante() {
        return tipoComprobante;
    }

    public double getTotal() {
        return total; 
    }

    public List<ItemVenta> getItems() {
        return items;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setItems(List<ItemVenta> items) {
        this.items = items;
    }

    public void agregarItem(ItemVenta item) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(item);

        calcularTotal();
    }
    
    public void calcularTotal() {
        double nuevoTotal = 0.0;
        if (this.items != null) {
            for (ItemVenta item : this.items) {
                nuevoTotal += item.getSubtotal(); 
            }
        }
        this.total = nuevoTotal;
    }

    @Override
    public String toString() {
        return "Venta [ID=" + id + ", Fecha=" + fecha + ", Cliente=" + (cliente != null ? cliente.getNombreCompleto() : "N/A") + ", Total=" + total + ", Items=" + items.size() + "]";
    }
}