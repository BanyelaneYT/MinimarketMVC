package vista;

import java.util.List;
import java.text.SimpleDateFormat;
import modelo.ItemVenta;
import modelo.Producto;
import modelo.Venta;

public class Reporte {
    public String generarReporteVentas(List<Venta> ventas) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- REPORTE DE VENTAS ---\n");
        if (ventas.isEmpty()) {
            sb.append("No hay ventas registradas.\n");
            return sb.toString();
        }

        double totalGeneralVentas = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        for (Venta venta : ventas) {
            sb.append("-------------------------\n");
            sb.append("ID Venta: ").append(venta.getId()).append("\n");
            sb.append("Fecha: ").append(venta.getFecha() != null ? sdf.format(venta.getFecha()) : "N/A").append("\n");
            sb.append("Cliente: ").append(venta.getCliente() != null ? venta.getCliente().getNombreCompleto() : "N/A").append("\n");
            sb.append("Empleado: ").append(venta.getEmpleado() != null ? venta.getEmpleado().getNombre() : "N/A").append("\n");
            sb.append("Total: S/ ").append(String.format("%.2f", venta.getTotal())).append("\n");
            sb.append("--- Items de Venta ---\n");
            for (ItemVenta item : venta.getItems()) {
                sb.append("  - Producto: ").append(item.getProducto().getNombre())
                  .append(", Cantidad: ").append(item.getCantidad())
                  .append(", Subtotal: S/ ").append(String.format("%.2f", item.getSubtotal())).append("\n");
            }
            totalGeneralVentas += venta.getTotal();
        }
        sb.append("-------------------------\n");
        sb.append("TOTAL GENERAL DE VENTAS: S/ ").append(String.format("%.2f", totalGeneralVentas)).append("\n");
        sb.append("-------------------------\n");
        return sb.toString();
    }

    public String generarReporteInventario(List<Producto> productos) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- REPORTE DE INVENTARIO ---\n");
        if (productos.isEmpty()) {
            sb.append("No hay productos en el inventario.\n");
            return sb.toString();
        }

        sb.append("--------------------------------------------------\n");
        sb.append(String.format("%-10s %-20s %-10s %-10s%n", "Código", "Nombre", "Precio", "Stock"));
        sb.append("--------------------------------------------------\n");
        for (Producto producto : productos) {
            sb.append(String.format("%-10s %-20s %-10.2f %-10d%n",
                                      producto.getCodigo(),
                                      producto.getNombre(),
                                      producto.getPrecio(),
                                      producto.getStock()));
        }
        sb.append("--------------------------------------------------\n");
        return sb.toString();
    }
}