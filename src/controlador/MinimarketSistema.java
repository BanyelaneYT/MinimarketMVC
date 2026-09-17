package controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import modelo.Cliente;
import controlador.ConexionBD; 
import modelo.Empleado;
import modelo.ItemVenta;
import modelo.Producto;
import modelo.Usuario;
import modelo.Venta;

public class MinimarketSistema {
private static final LocalTime HORA_LIMITE_ENTRADA = LocalTime.of(8, 30);
    public MinimarketSistema() {
    }
    /**
     * Intenta marcar la asistencia (Entrada o Salida) de un empleado.
     * @param codigoEmpleado El ID del empleado/usuario.
     * @return 1 (Entrada), 0 (Salida), -1 (Inválido/Error).
     */
public int registrarAsistencia(String codigoEmpleado) {
LocalDate hoy = LocalDate.now();
    LocalTime horaActual = LocalTime.now();
    String horaActualStr = horaActual.toString();
    
    // Asumiendo que HORA_LIMITE_ENTRADA está declarada como constante:
    // private static final LocalTime HORA_LIMITE_ENTRADA = LocalTime.of(8, 30);
    
    // **Validación 1:** Chequea si el código es un EMPLEADO válido.
    if (!esEmpleadoValido(codigoEmpleado)) {
        return -1; // Código no existe.
    }

    // Buscamos si ya existe el registro de SALIDA para HOY
    String SQL_BUSCAR_SALIDA_EXISTENTE = "SELECT id_empleado FROM asistencia WHERE id_empleado = ? AND fecha = ? AND tipo_entrada = 'SALIDA'";

    // Buscar registro de ENTRADA pendiente (Para saber si el empleado ya puede marcar Salida)
    String SQL_BUSCAR_ENTRADA_PENDIENTE = "SELECT id_empleado FROM asistencia WHERE id_empleado = ? AND fecha = ? AND tipo_entrada = 'ENTRADA'";

    // Insert para ENTRADA (Guarda ASISTIO/TARDANZA en estado)
    String SQL_ENTRADA = "INSERT INTO asistencia (id_empleado, fecha, hora_entrada, tipo_entrada, estado) VALUES (?, ?, ?, ?, ?)";
    
    // Insert para SALIDA (Guarda SALIDA en estado, como solicitaste)
    // ESTA SENTENCIA CREA EL NUEVO REGISTRO PARA LA SALIDA
    String SQL_SALIDA = "INSERT INTO asistencia (id_empleado, fecha, hora_entrada, tipo_entrada, estado) VALUES (?, ?, ?, 'SALIDA', 'SALIDA')";
    
    try (Connection conn = ConexionBD.conectar()) {
        
        // A. PRIMERO: **Verificamos si ya marcó SALIDA hoy** (Constraint: 1 sola salida al día)
        try (PreparedStatement psBuscarSalida = conn.prepareStatement(SQL_BUSCAR_SALIDA_EXISTENTE)) {
            psBuscarSalida.setString(1, codigoEmpleado);
            psBuscarSalida.setDate(2, Date.valueOf(hoy));
            if (psBuscarSalida.executeQuery().next()) {
                return 4; // Jornada Completa (Salida ya registrada)
            }
        }
        
        // B. SEGUNDO: ¿Tiene una ENTRADA pendiente? (Para saber si debe marcar Salida o Entrada)
        try (PreparedStatement psBuscarEntrada = conn.prepareStatement(SQL_BUSCAR_ENTRADA_PENDIENTE)) {
            psBuscarEntrada.setString(1, codigoEmpleado);
            psBuscarEntrada.setDate(2, Date.valueOf(hoy));
            
            if (psBuscarEntrada.executeQuery().next()) {
                // Caso 1: Tiene ENTRADA pendiente. Generar un NUEVO registro de SALIDA (INSERT)
                try (PreparedStatement psSalida = conn.prepareStatement(SQL_SALIDA)) {
                    psSalida.setString(1, codigoEmpleado); // 1. id_empleado
                    psSalida.setDate(2, Date.valueOf(hoy)); // 2. fecha
                    psSalida.setString(3, horaActualStr); // 3. hora_entrada (usamos el mismo campo)
                    psSalida.executeUpdate();
                    return 0; // SALIDA REGISTRADA
                }
            } else {
                // Caso 2: NO tiene ENTRADA pendiente. Generar registro de ENTRADA (INSERT)
                
                // Declaración e inicialización de variables de estado
                String estadoStr = null; 
                int estadoEntrada = 0;

                // Lógica de Puntualidad
                if (horaActual.isAfter(HORA_LIMITE_ENTRADA)) { 
                    estadoEntrada = 3; 
                    estadoStr = "TARDANZA";
                } else {
                    estadoEntrada = 2; 
                    estadoStr = "ASISTIO";
                }
                
                try (PreparedStatement psEntrada = conn.prepareStatement(SQL_ENTRADA)){
                    psEntrada.setString(1, codigoEmpleado);
                    psEntrada.setDate(2, Date.valueOf(hoy));
                    psEntrada.setString(3, horaActualStr); 
                    psEntrada.setString(4, "ENTRADA"); 
                    psEntrada.setString(5, estadoStr); 
                    psEntrada.executeUpdate();
                    
                    return estadoEntrada; 
                }  
            }
        }
    } catch (SQLException e) {
        System.err.println("Error de BD al marcar asistencia: " + e.getMessage());
        return 0; // Error SQL
    }
} 
    /**
     * Verifica si el código existe en la tabla 'logueo' y tiene el cargo de 'EMPLEADO'.
     */
    private boolean esEmpleadoValido(String codigo) {
        // Consulta la tabla 'logueo' con las columnas correctas ('id_usuario' y 'tipo_usuario')
        String SQL = "SELECT tipo_usuario FROM logueo WHERE id_usuario = ? AND tipo_usuario = 'EMPLEADO'";
        
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement ps = conn.prepareStatement(SQL)) {
            
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                // Devuelve true si encuentra al menos una fila.
                return rs.next(); 
            }
        } catch (SQLException e) {
            System.err.println("Error de BD al validar empleado: " + e.getMessage());
            return false;
        }
    }
    public void registrarProducto(Producto producto) {
    // 1. Modificar SQL: Incluir 'active' en la lista de columnas (columna 5)
    String sql = "INSERT INTO productos (codigo, nombre, precio, stock, active) VALUES (?, ?, ?, ?, ?)";
    
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
        conn = ConexionBD.conectar();
        if (conn == null) {
            System.err.println("Error: No se pudo establecer conexión con la base de datos.");
            return;
        }
        pstmt = conn.prepareStatement(sql);
        
        // 2. Establecer parámetros existentes
        pstmt.setString(1, producto.getCodigo());
        pstmt.setString(2, producto.getNombre());
        pstmt.setDouble(3, producto.getPrecio());
        pstmt.setInt(4, producto.getStock());
        
        // 3. AÑADIR PARÁMETRO PARA ACTIVE (fijo a 1)
        pstmt.setInt(5, 1); // <-- ¡Nuevo campo 'active' establecido a 1!
        
        pstmt.executeUpdate();
        System.out.println("Producto registrado con éxito en MySQL.");
    } catch (SQLException e) {
        System.err.println("Error al registrar producto en MySQL: " + e.getMessage());
    } finally {
        try {
            if (pstmt != null) pstmt.close();
            if (conn != null) ConexionBD.cerrarConexion(conn);
        } catch (SQLException e) {
            System.err.println("Error al cerrar recursos de BD: " + e.getMessage());
        }
    }
}
public List<Vector<Object>> obtenerHistorialAsistenciasSimple() {
    // Solo seleccionamos las columnas que existen en tu tabla de asistencia: id_empleado, fecha, hora_entrada
    String SQL = "SELECT id_empleado, fecha, hora_entrada, tipo_entrada, estado FROM asistencia ORDER BY fecha DESC, hora_entrada DESC";
    
    // Usamos Vector<Object> para representar la fila, que es compatible con JTable.
    List<Vector<Object>> historial = new ArrayList<>();

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement ps = conn.prepareStatement(SQL);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Vector<Object> fila = new Vector<>();
            // Las columnas deben coincidir con tu tabla: id_empleado, fecha, hora_entrada
            fila.add(rs.getString("id_empleado"));
            fila.add(rs.getString("fecha")); 
            fila.add(rs.getString("hora_entrada"));
            fila.add(rs.getString("tipo_entrada"));
            fila.add(rs.getString("estado"));
            historial.add(fila);
        }
    } catch (SQLException e) {
        System.err.println("Error al obtener historial de asistencias: " + e.getMessage());
    }
    return historial;
}
    public List<Producto> getProductos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT codigo, nombre, precio, stock, active FROM productos";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConexionBD.conectar();
            if (conn == null) {
                System.err.println("Error: No se pudo establecer conexión con la base de datos.");
                return productos;
            }
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Producto producto = new Producto(
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getInt("stock"),
                    rs.getInt("active")
                );
                productos.add(producto);
            }
            System.out.println("Productos cargados desde MySQL.");
        } catch (SQLException e) {
            System.err.println("Error al obtener productos de MySQL: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) ConexionBD.cerrarConexion(conn);
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos de BD: " + e.getMessage());
            }
        }
        return productos;
    }
    
public boolean eliminarProducto(String codigoProducto, int par) {
    // El nombre del método debería ser 'alternarEstadoProducto'
    String SQL_TOGGLE_ESTADO = "UPDATE productos SET active = ? WHERE codigo = ?";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement ps = conn.prepareStatement(SQL_TOGGLE_ESTADO)) {

        ps.setInt(1, par);               // CORREGIDO: Usamos 'par' que es el parámetro que entra
        ps.setString(2, codigoProducto); 

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        System.err.println("Error al alternar estado del producto: " + e.getMessage());
        return false;
    }
}
public boolean actualizarProducto(Producto p) {
    // Sentencia SQL para actualizar todos los campos editables
    // NOTA: Mantenemos el campo 'activo' sin modificarlo
    String SQL_UPDATE_PRODUCTO = "UPDATE productos SET nombre = ?, precio = ?, stock = ?, active = ? WHERE codigo = ?";

    try (Connection conn = ConexionBD.conectar();
         PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_PRODUCTO)) {

        // 1. Establecer los nuevos valores
        ps.setString(1, p.getNombre());
        ps.setDouble(2, p.getPrecio());
        ps.setInt(3, p.getStock());
        ps.setInt(4, p.getactive());
        
        // 2. Establecer la condición WHERE (el código del producto a actualizar)
        ps.setString(5, p.getCodigo()); 

        // 3. Ejecutar la actualización
        int filasAfectadas = ps.executeUpdate();

        return filasAfectadas > 0;

    } catch (SQLException e) {
        System.err.println("Error al actualizar producto en la BD: " + e.getMessage());
        // Puedes añadir un Logger aquí si lo usas
        return false;
    }
}    
    public void registrarEmpleado(Empleado empleado) {
        // 1. SQL para insertar en la tabla EMPLEADOS
    // CORREGIDO: Ahora hay 5 columnas y 5 placeholders (?)
    String sqlEmpleado = "INSERT INTO empleados (id, dni_empleado, nombre, password, cargo) VALUES (?, ?, ?, ?, ?)";
    
    // 2. SQL para insertar en la tabla LOGUEO
    String sqlLogueo = "INSERT INTO logueo (id_usuario, contraseña, tipo_usuario) VALUES (?, ?, ?)";
    
    Connection conn = null;
    PreparedStatement pstmtEmpleado = null;
    PreparedStatement pstmtLogueo = null;
    
    try {
        conn = ConexionBD.conectar();
        if (conn == null) {
            System.err.println("Error: No se pudo establecer conexión con la base de datos.");
            return;
        }

        // Recomendación: Iniciar Transacción
        // conn.setAutoCommit(false); 
        
        // --- 1. REGISTRAR EN TABLA EMPLEADOS ---
        pstmtEmpleado = conn.prepareStatement(sqlEmpleado);
        pstmtEmpleado.setString(1, empleado.getId());
        pstmtEmpleado.setString(2, empleado.getDNI());
        pstmtEmpleado.setString(3, empleado.getNombre());
        pstmtEmpleado.setString(4, empleado.getPassword());
        pstmtEmpleado.setString(5, empleado.getCargo());
        
        pstmtEmpleado.executeUpdate();
        
        System.out.println("Empleado registrado con éxito en la tabla 'empleados'.");
        
        // --- 2. REGISTRAR EN TABLA LOGUEO ---
        // El id_usuario es el ID del empleado, la contraseña es el password y el tipo es 'EMPLEADO'.
        pstmtLogueo = conn.prepareStatement(sqlLogueo);
        
        // Parámetro 1: id_usuario (ID del empleado)
        pstmtLogueo.setString(1, empleado.getId()); 
        
        // Parámetro 2: contraseña (Password del empleado)
        pstmtLogueo.setString(2, empleado.getPassword()); 
        
        // Parámetro 3: tipo_usuario (Debe ser 'EMPLEADO' para coincidir con el ENUM)
        pstmtLogueo.setString(3, "EMPLEADO"); 
        
        pstmtLogueo.executeUpdate();
        
        System.out.println("Usuario logueo (EMPLEADO) registrado con éxito en la tabla 'logueo'.");

        // Recomendación: Confirmar Transacción
        // conn.commit();

    } catch (SQLException e) {
        System.err.println("Error al registrar empleado/usuario en MySQL: " + e.getMessage());
        
        // Recomendación: Revertir Transacción en caso de error
        // if (conn != null) conn.rollback();
        
    } finally {
        // --- Cerrar ambos PreparedStatement y la conexión ---
        try {
            if (pstmtEmpleado != null) pstmtEmpleado.close();
            if (pstmtLogueo != null) pstmtLogueo.close();    
            if (conn != null) ConexionBD.cerrarConexion(conn);
        } catch (SQLException e) {
            System.err.println("Error al cerrar recursos de BD: " + e.getMessage());
        }
    }
}
    public Usuario SesionUsuario(String usuario, String contraseñaIngresada) {
        Usuario logueo = null;
        // La tabla MySQL usa 'id', no 'id_empleado'. Se corrigió el nombre de la columna 'passsword' a 'password'.
        String sql = "SELECT id_usuario , contraseña, tipo_usuario FROM logueo WHERE id_usuario = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.conectar();
            if (conn == null) {
                System.err.println("Error: No se pudo establecer conexión con la base de datos.");
                return null;
            }
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, usuario);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                String idusuario = rs.getString("id_usuario"); // Nombre de columna corregido a "id"
                String tipo = rs.getString("tipo_usuario");
                String contraseñaAlmacenada = rs.getString("contraseña"); // Nombre de columna corregido a "password"
                
                if (contraseñaAlmacenada != null && contraseñaAlmacenada.equals(contraseñaIngresada)) {
                    logueo = new Usuario(idusuario, contraseñaAlmacenada, tipo);
                    System.out.println("Autenticación exitosa para el usuario: " + idusuario);
                    return logueo;
                } else {
                    System.out.println("Contraseña incorrecta para el usuario: " + usuario);
                }
            } else {
                System.out.println("Usuario no encontrado: " + usuario);
            }
        } catch (SQLException e) {
            System.err.println("Error de base de datos al autenticar usuario: " + e.getMessage());
        } finally {

            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) ConexionBD.cerrarConexion(conn);
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos de BD: " + e.getMessage());
            }
        }
        return null; 
    }
    public Empleado autenticarEmpleado(String usuario, String contraseñaIngresada) {
        Empleado empleado = null;
        // La tabla MySQL usa 'id', no 'id_empleado'. Se corrigió el nombre de la columna 'passsword' a 'password'.
        String sql = "SELECT id,dni_empleado, nombre, cargo, password FROM empleados WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.conectar();
            if (conn == null) {
                System.err.println("Error: No se pudo establecer conexión con la base de datos.");
                return null;
            }
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, usuario);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                String idEmpleado = rs.getString("id"); // Nombre de columna corregido a "id"
                String dni = rs.getString("dni_empleado");
                String nombre = rs.getString("nombre");
                String cargo = rs.getString("cargo");
                String contraseñaAlmacenada = rs.getString("password"); // Nombre de columna corregido a "password"
                
                if (contraseñaAlmacenada != null && contraseñaAlmacenada.equals(contraseñaIngresada)) {
                    empleado = new Empleado(idEmpleado,dni, nombre, contraseñaAlmacenada, cargo);
                    System.out.println("Autenticación exitosa para el empleado: " + nombre);
                    return empleado;
                } else {
                    System.out.println("Contraseña incorrecta para el usuario: " + usuario);
                }
            } else {
                System.out.println("Usuario no encontrado: " + usuario);
            }
        } catch (SQLException e) {
            System.err.println("Error de base de datos al autenticar empleado: " + e.getMessage());
        } finally {

            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) ConexionBD.cerrarConexion(conn);
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos de BD: " + e.getMessage());
            }
        }
        return null; 
    }
    public List<Empleado> getEmpleados() {
        List<Empleado> empleados = new ArrayList<>();
        // SQL: Las columnas son 'id', 'nombre', 'password', 'cargo'. Ya estaba correcto.
        String sql = "SELECT id,dni_empleado, nombre, password, cargo FROM empleados";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionBD.conectar();
            if (conn == null) {
                System.err.println("Error: No se pudo establecer conexión con la base de datos.");
                return empleados;
            }
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Empleado emp = new Empleado(
                    rs.getString("id"),
                    rs.getString("dni_empleado"),
                    rs.getString("nombre"),
                    rs.getString("password"), // Se asume que el constructor de Empleado acepta 'password'
                    rs.getString("cargo")
                );
                empleados.add(emp);
            }
            System.out.println("Empleados cargados desde MySQL.");
        } catch (SQLException e) {
            System.err.println("Error al obtener empleados: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) ConexionBD.cerrarConexion(conn);
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
        return empleados;
    }
    public void eliminarEmpleado(String idEmpleado) {
        String sql = "DELETE FROM empleados WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConexionBD.conectar();
            if (conn == null) {
                System.err.println("Error: No se pudo establecer conexión con la base de datos.");
                return;
            }
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, idEmpleado);
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Empleado eliminado con éxito de MySQL.");
            } else {
                System.out.println("No se encontró el empleado con ID: " + idEmpleado);
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar empleado de MySQL: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) ConexionBD.cerrarConexion(conn);
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos de BD: " + e.getMessage());
            }
        }
    }
    public void registrarCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes (dni, nombre_completo, telefono) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConexionBD.conectar();
            if (conn == null) {
                System.err.println("Error: No se pudo establecer conexión con la base de datos.");
                return;
            }
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(2, cliente.getDni());
            pstmt.setString(1, cliente.getNombreCompleto());
            pstmt.setString(3, cliente.getTelefono());
            pstmt.executeUpdate();
            System.out.println("Cliente registrado con éxito en MySQL.");
        } catch (SQLException e) {
            System.err.println("Error al registrar cliente en MySQL: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) ConexionBD.cerrarConexion(conn);
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos de BD: " + e.getMessage());
            }
        }
    }
    public List<Cliente> getClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT dni, nombre_completo, telefono FROM clientes";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConexionBD.conectar();
            if (conn == null) {
                System.err.println("Error: No se pudo establecer conexión con la base de datos.");
                return clientes;
            }
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Cliente cliente = new Cliente(
                    rs.getString("nombre_completo"),
                    rs.getString("dni"),
                    rs.getString("telefono")
                );
                clientes.add(cliente);
            }
            System.out.println("Clientes cargados desde MySQL.");
        } catch (SQLException e) {
            System.err.println("Error al obtener clientes de MySQL: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) ConexionBD.cerrarConexion(conn);
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos de BD: " + e.getMessage());
            }
        }
        return clientes;
    }
    public void eliminarCliente(String dniCliente) {
        String sql = "DELETE FROM clientes WHERE dni = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConexionBD.conectar();
            if (conn == null) {
                System.err.println("Error: No se pudo establecer conexión con la base de datos.");
                return;
            }
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dniCliente);
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Cliente eliminado con éxito de MySQL.");
            } else {
                System.out.println("No se encontró el cliente con DNI: " + dniCliente);
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente de MySQL: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) ConexionBD.cerrarConexion(conn);
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos de BD: " + e.getMessage());
            }
        }
    }
public void actualizarStockProducto(Connection conn, String codigoProducto, int cantidadVendida) throws SQLException {

        String sql = "UPDATE productos SET stock = stock - ? WHERE codigo = ?";

        PreparedStatement pstmt = null;

        try {

            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, cantidadVendida);

            pstmt.setString(2, codigoProducto);

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas == 0) {

                System.err.println("Advertencia: No se pudo actualizar el stock del producto " + codigoProducto + ". Podría no existir o la cantidad vendida era mayor al stock disponible.");

            } else {

                System.out.println("Stock del producto " + codigoProducto + " actualizado correctamente.");

            }

        } catch (SQLException e) {

            System.err.println("Error al actualizar stock del producto en MySQL: " + e.getMessage());

            throw e; 

        } finally {

            try {

                if (pstmt != null) pstmt.close();

            } catch (SQLException e) {

                System.err.println("Error al cerrar PreparedStatement en actualizarStockProducto: " + e.getMessage());

            }

        }

    }
    public boolean registrarVenta(Venta venta) throws Exception {
        Connection conn = null;
        PreparedStatement pstmtVenta = null;
        PreparedStatement pstmtItem = null;
        try {
            conn = ConexionBD.conectar();
            if (conn == null) {
                throw new Exception("Error: No se pudo establecer conexión con la base de datos para registrar la venta.");
            }
            conn.setAutoCommit(false);

            String sqlVenta = "INSERT INTO ventas (id_venta, dni_cliente, id_empleado, fecha_venta, total) VALUES (?, ?, ?, ?, ?)";
            pstmtVenta = conn.prepareStatement(sqlVenta); 

            pstmtVenta.setString(1, venta.getId()); 
            pstmtVenta.setString(2, venta.getCliente().getDni());
            pstmtVenta.setString(3, venta.getEmpleado().getId());
            pstmtVenta.setTimestamp(4, new Timestamp(venta.getFecha().getTime()));
            pstmtVenta.setDouble(5, venta.getTotal()); 
            //pstmtVenta.setString(6, venta.getTipoComprobante());
            pstmtVenta.executeUpdate();

            String sqlItem = "INSERT INTO item_ventas (id_venta, codigo_producto, cantidad, subtotal) VALUES (?, ?, ?, ?)"; 
            pstmtItem = conn.prepareStatement(sqlItem);
            
            for (ItemVenta item : venta.getItems()) { 
                pstmtItem.setString(1, venta.getId()); 
                pstmtItem.setString(2, item.getProducto().getCodigo());
                pstmtItem.setInt(3, item.getCantidad());
                pstmtItem.setDouble(4, item.getSubtotal()); 
                pstmtItem.addBatch();
            }
            pstmtItem.executeBatch();

            for (ItemVenta item : venta.getItems()) {
                actualizarStockProducto(conn, item.getProducto().getCodigo(), item.getCantidad());
            }

            conn.commit(); 
            System.out.println("Venta registrada con éxito en MySQL. ID: " + venta.getId());
            return true; 
        } catch (SQLException e) {
            System.err.println("Error al registrar venta en MySQL: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); 
                    System.err.println("Transacción de venta revertida.");
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            throw new Exception("Error al registrar la venta: " + e.getMessage(), e); 
        } finally {

            try {
                if (pstmtItem != null) pstmtItem.close();
                if (pstmtVenta != null) pstmtVenta.close();
                if (conn != null) {
                    conn.setAutoCommit(true); 
                    ConexionBD.cerrarConexion(conn);
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos de BD: " + e.getMessage());
            }
        }
    }  
  public List<Venta> getHistorialVentas() {
        List<Venta> historialVentas = new ArrayList<>();
        // Se corrigieron los nombres de las columnas para que coincidan con el DDL y el modelo.
        String sqlVentas = "SELECT v.id_venta, v.fecha_venta, v.total, " + 
                            "c.dni AS dni_cliente, c.nombre_completo AS nombre_cliente, c.telefono AS tel_cliente, " +
                            "e.id AS id_empleado, e.nombre AS nombre_empleado, e.cargo AS cargo_empleado, e.password AS contrasena_empleado " + // Cambio 'id_empleado' por 'id' y 'contraseña' por 'password'
                            "FROM ventas v " +
                            "JOIN clientes c ON v.dni_cliente = c.dni " +
                            "JOIN empleados e ON v.id_empleado = e.id " + // Cambio 'e.id_empleado' por 'e.id'
                            "ORDER BY v.fecha_venta DESC";
        
        // Se corrigió el nombre de la columna 'subtotal_item' por 'subtotal' para item_ventas
        String sqlItems = "SELECT iv.cantidad, iv.subtotal, p.codigo, p.nombre, p.precio, p.stock " + 
                            "FROM item_ventas iv JOIN productos p ON iv.codigo_producto = p.codigo " +
                            "WHERE iv.id_venta = ?";

        Connection conn = null;
        Statement stmtVentas = null;
        ResultSet rsVentas = null;

        try {
            conn = ConexionBD.conectar();
            if (conn == null) {
                System.err.println("Error: No se pudo establecer conexión con la base de datos.");
                return historialVentas;
            }
            stmtVentas = conn.createStatement();
            rsVentas = stmtVentas.executeQuery(sqlVentas);

            while (rsVentas.next()) {

                Cliente cliente = new Cliente(
                    rsVentas.getString("nombre_cliente"),
                    rsVentas.getString("dni_cliente"),
                    rsVentas.getString("tel_cliente")
                );

                Empleado empleado = new Empleado(
                    rsVentas.getString("id_empleado"),
                    rsVentas.getString("dni_empleado"), // Alias correcto 'nombre_empleado'// Alias correcto 'id_empleado'
                    rsVentas.getString("nombre_empleado"), // Alias correcto 'nombre_empleado'
                    rsVentas.getString("contrasena_empleado"), // Alias correcto 'contrasena_empleado'
                    rsVentas.getString("cargo_empleado") // Alias correcto 'cargo_empleado'
                );

                List<ItemVenta> itemsVenta = new ArrayList<>();
                PreparedStatement pstmtItems = null; 
                ResultSet rsItems = null; 
                try {
                    pstmtItems = conn.prepareStatement(sqlItems);
                    pstmtItems.setString(1, rsVentas.getString("id_venta"));
                    rsItems = pstmtItems.executeQuery();
                    while (rsItems.next()) {
                        Producto producto = new Producto(
                            rsItems.getString("codigo"),
                            rsItems.getString("nombre"),
                            rsItems.getDouble("precio"),
                            rsItems.getInt("stock"),
                            rsItems.getInt("active")
    );
                        ItemVenta item = new ItemVenta(
                            producto,
                            rsItems.getInt("cantidad")
                        );
                        item.setSubtotal(rsItems.getDouble("subtotal")); // Corrección: "subtotal"
                        itemsVenta.add(item);
                    }
                } finally {

                    if (rsItems != null) { try { rsItems.close(); } catch (SQLException e) { System.err.println("Error al cerrar rsItems: " + e.getMessage()); } }
                    if (pstmtItems != null) { try { pstmtItems.close(); } catch (SQLException e) { System.err.println("Error al cerrar pstmtItems: " + e.getMessage()); } }
                }

                Venta venta = new Venta(
                    rsVentas.getString("id_venta"), 
                    new java.util.Date(rsVentas.getTimestamp("fecha_venta").getTime()),
                    cliente,
                    empleado,
                    null, // Se eliminó tipo_comprobante del DDL de ventas, se envía null o se elimina del constructor
                    rsVentas.getDouble("total") 
                );
                venta.setItems(itemsVenta); 
                
                historialVentas.add(venta);
            }
            System.out.println("Historial de ventas cargado desde MySQL.");
        } catch (SQLException e) {
            System.err.println("Error al obtener historial de ventas de MySQL: " + e.getMessage());
        } finally {
            try {
                if (rsVentas != null) rsVentas.close();
                if (stmtVentas != null) stmtVentas.close();
                if (conn != null) ConexionBD.cerrarConexion(conn);
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos de BD: " + e.getMessage());
            }
        }
        return historialVentas;
    }
    public void eliminarReporteGenerado(String idReporte) {
        // Se usa 'id' en lugar de 'id_reporte' para coincidir con la tabla 'reportes' (DDL)
        String sql = "DELETE FROM reportes WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConexionBD.conectar();
            if (conn == null) {
                System.err.println("Error: No se pudo establecer conexión con la base de datos.");
                return;
            }
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, idReporte);
            int filasAfectadas = pstmt.executeUpdate(); 
            if (filasAfectadas > 0) {
                System.out.println("Reporte eliminado con éxito de MySQL.");
            } else {
                System.out.println("No se encontró el reporte con ID: " + idReporte);
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar reporte de MySQL: " + e.getMessage());
        } finally {

            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) ConexionBD.cerrarConexion(conn);
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos de BD: " + e.getMessage());
            }
        }
    }
    public List<Venta> getVentasPorRangoDeFecha(Date fechaInicio, Date fechaFin) {

        throw new UnsupportedOperationException("El método getVentasPorRangoDeFecha aún no está implementado.");
    }
    public List<Venta> getVentasPorRangoDeFecha(java.util.Date fechaInicio, java.util.Date fechaFin) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public List<Empleado> getId() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
