package vista; 

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import controlador.MinimarketSistema;
import controlador.SesionEmpleado;
import javax.swing.text.AbstractDocument;
import modelo.Cliente;
import modelo.Empleado;
import modelo.ItemVenta;
import modelo.Producto;
import modelo.Venta;

public class MinimarketGUI extends JFrame {
    private MinimarketSistema sistema;
    private Venta ventaEnProgreso;
    private JDialog comprobanteDialog;

    private JComboBox<Cliente> cmbClientes;
    private JComboBox<Producto> cmbProductos;
    private JTextField txtCantidadProducto;
    private JButton btnAgregarProducto;
    private JButton btnFinalizarVenta;
    private JButton btnCancelarVentaComprobante;
    private JLabel lblTotalVenta;

    private JComboBox<String> cmbTipoComprobante;

    private JTable tblDetalleComprobante;
    private DefaultTableModel modeloTablaDetalle;

    private static final Color COLOR_FONDO_MORADO = new Color(85, 30, 110);
    private static final Color COLOR_BOTON_AMARILLO = new Color(255, 204, 0); // Amarillo Dorado
    private static final Color COLOR_TEXTO_MORADO = new Color(85, 30, 110);      // Texto Morado
    // Texto Morado
public MinimarketGUI() {
    // --- 1. Definición de Colores Tambo ---
    // Fondo y Letras: Morado Oscuro
    

    // Inicialización del Sistema (manteniendo tu lógica)
    sistema = new MinimarketSistema();
    setTitle("Sistema de Minimarket - Estilo Tambo+");
    setSize(750, 600); // Ajustamos el ancho para acomodar la imagen y botones
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    
    // Cambiamos el Layout principal a BorderLayout
    setLayout(new BorderLayout()); 
    // Fondo de la ventana principal
    getContentPane().setBackground(COLOR_FONDO_MORADO);

    // --- 2. Panel de Botones (Columna Derecha) ---
    JPanel panelBotones = new JPanel();
    // Usamos el GridLayout original para los botones dentro de este panel
    panelBotones.setLayout(new GridLayout(9, 1, 10, 10)); // 10px de espaciado entre botones
    panelBotones.setBackground(COLOR_FONDO_MORADO);
    panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Margen
    
    // --- 3. Inicialización y Estilo de Botones --- 
    JButton btnVerProductos = crearBotonEstiloTambo("Productos", COLOR_BOTON_AMARILLO, COLOR_FONDO_MORADO);
    JButton btnVerClientes = crearBotonEstiloTambo("Clientes", COLOR_BOTON_AMARILLO, COLOR_FONDO_MORADO);
    JButton btnVerEmpleados = crearBotonEstiloTambo("Empleados", COLOR_BOTON_AMARILLO, COLOR_FONDO_MORADO);
    JButton btnAsistencia = crearBotonEstiloTambo("Asistencia", COLOR_BOTON_AMARILLO, COLOR_FONDO_MORADO);
    JButton btnSalirG = crearBotonEstiloTambo("Cerrar", COLOR_BOTON_AMARILLO, COLOR_FONDO_MORADO);

    // Agregar botones al panel de botones
    JLabel lblImagen2 = new JLabel();
    int anchoDeseado = 120; // Define el ancho que deseas (ejemplo: 150 píxeles)
    int altoDeseado = 40;  // Define el alto que deseas (ejemplo: 150 píxeles)
    try {
        // 1. Cargar la imagen del recurso como URL
        java.net.URL url = getClass().getResource("/images/logo.png");
        if (url == null) {
             throw new NullPointerException("El recurso /images/logo.png no fue encontrado.");
        }
        ImageIcon iconOriginal = new ImageIcon(url);
        Image imagenOriginal = iconOriginal.getImage(); 
        Image imagenEscalada = imagenOriginal.getScaledInstance(
            anchoDeseado, 
            altoDeseado, 
            Image.SCALE_SMOOTH
        );
        ImageIcon iconEscalado = new ImageIcon(imagenEscalada);
        lblImagen2.setIcon(iconEscalado); 
    } catch (Exception e) {
        // El bloque catch se ejecuta si el archivo no se encuentra o hay error al cargar/escalar
        System.err.println("Error al cargar o escalar la imagen: " + e.getMessage());
        lblImagen2.setText("AQUÍ VA LA IMAGEN DE TAMBO");
        lblImagen2.setForeground(Color.WHITE);
        lblImagen2.setFont(new Font("Arial", Font.BOLD, 18));
        lblImagen2.setHorizontalAlignment(SwingConstants.CENTER);
    }
    
    panelBotones.add(lblImagen2);
    panelBotones.add(btnVerProductos);
    panelBotones.add(btnVerClientes);
    panelBotones.add(btnVerEmpleados);
    panelBotones.add(btnAsistencia);
    panelBotones.add(btnSalirG);
    JPanel panelImagen = new JPanel();
    panelImagen.setBackground(COLOR_FONDO_MORADO);
    
    // *** Sección para tu imagen (debes adaptarla) ***
    JLabel lblImagen = new JLabel();
    // Intenta cargar la imagen (asumiendo que está en una carpeta 'images' dentro de 'src')
    try {
        ImageIcon icono = new ImageIcon(getClass().getResource("/images/tambo_logo.png"));
        lblImagen.setIcon(icono); // Si la imagen ya tiene un buen tamaño
    } catch (Exception e) {
        lblImagen.setText("AQUÍ VA LA IMAGEN DE TAMBO");
        lblImagen.setForeground(Color.WHITE);
        lblImagen.setFont(new Font("Arial", Font.BOLD, 16));
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
    }
    panelImagen.add(lblImagen);
    add(panelImagen, BorderLayout.CENTER); // La imagen ocupa la mayor parte
    add(panelBotones, BorderLayout.EAST); // Los botones ocupan el lado derecho
    // --- 6. Event Listeners (Manteniendo tu lógica) ---
    btnSalirG.addActionListener(e -> Salir());
    btnAsistencia.addActionListener(e -> VerAsistencia());
    btnVerProductos.addActionListener(e -> {
    try {
        List<Producto> productos = sistema.getProductos();
        verListaProductosEnTabla(productos, "Productos en Stock");
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error al cargar la lista de productos: " + ex.getMessage(), "Error de BD", JOptionPane.ERROR_MESSAGE);
    }
});
    btnVerClientes.addActionListener(e -> verListaClientes(sistema.getClientes(), "Clientes"));
    btnVerEmpleados.addActionListener(e -> verListaEmpleados(sistema.getEmpleados(), "Empleados"));
    // --- 7. Lógica de Sesión (Manteniendo tu lógica) ---
    if (SesionEmpleado.getEmpleado() == null) {
        try {
            List<Empleado> empleados = sistema.getEmpleados();
            if (!empleados.isEmpty()) {
                SesionEmpleado.setEmpleado(empleados.get(0));
                System.out.println("Empleado '" + empleados.get(0).getNombre() + "' logueado automáticamente para pruebas.");
            } else {
                Empleado emp = new Empleado("EMP001","00000000", "Empleado Demo", "pass123", "Vendedor");
                sistema.registrarEmpleado(emp);
                SesionEmpleado.setEmpleado(emp);
                System.out.println("Empleado Demo creado y logueado automáticamente.");
            }
        } catch (Exception ex) {
            Logger.getLogger(MinimarketGUI.class.getName()).log(Level.SEVERE, "Error al configurar empleado de sesión", ex);
            JOptionPane.showMessageDialog(this, "Error al cargar empleado para sesión: " + ex.getMessage(), "Error de Inicio", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Finalización del Frame
    pack();
    setLocationRelativeTo(null); // Centrar
    setVisible(true);
}
public void VerAsistencia() {
      VerAsistencia VerAsistenciaFrame = new VerAsistencia();
        VerAsistenciaFrame.setVisible(true);
        VerAsistenciaFrame.pack();
        VerAsistenciaFrame.setLocationRelativeTo(null); 
    }
public void verListaProductosEnTabla(List<Producto> listaProductos, String titulo) {
    String[] nombresColumnas = {"Código", "Nombre", "Precio", "Stock", "active"};
    
    DefaultTableModel modeloTablaProductos = new DefaultTableModel(nombresColumnas, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    for (Producto producto : listaProductos) {
        String estadoActivoStr = (producto.getactive() == 1) ? "Sí" : "No";
        Object[] fila = {
            producto.getCodigo(),
            producto.getNombre(),
            String.format("%.2f", producto.getPrecio()),
            producto.getStock(),
            estadoActivoStr
        };
        modeloTablaProductos.addRow(fila);
    }

    JTable tblProductos = new JTable(modeloTablaProductos);
    tblProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tblProductos.setRowHeight(25);
    tblProductos.setBackground(COLOR_FONDO_MORADO);
    tblProductos.setForeground(COLOR_BOTON_AMARILLO);

    tblProductos.getTableHeader().setBackground(COLOR_BOTON_AMARILLO);
    tblProductos.getTableHeader().setForeground(COLOR_FONDO_MORADO);
    tblProductos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

    JScrollPane scrollPane = new JScrollPane(tblProductos);
    scrollPane.setPreferredSize(new Dimension(650, 400));
    scrollPane.getViewport().setBackground(COLOR_FONDO_MORADO);
    JPanel panelContenido = new JPanel(new BorderLayout(5, 5));
    panelContenido.add(scrollPane, BorderLayout.CENTER); 
    panelContenido.setBackground(COLOR_FONDO_MORADO); // Fondo principal Morado (¡Ya lo tenías!)
    
    JPanel buttonPanel = new JPanel();
    JButton deleteButton = crearBotonEstiloTambo("Desactivar Producto", COLOR_BOTON_AMARILLO, COLOR_FONDO_MORADO);
    deleteButton.setBackground(COLOR_BOTON_AMARILLO);
    JButton actualButton = crearBotonEstiloTambo("Actualizar Producto", COLOR_BOTON_AMARILLO, COLOR_FONDO_MORADO);
    actualButton.setBackground(COLOR_BOTON_AMARILLO);
    JButton btnProducto = crearBotonEstiloTambo("Registrar Producto", COLOR_BOTON_AMARILLO, COLOR_FONDO_MORADO);
    btnProducto.setForeground(COLOR_FONDO_MORADO);
    JButton btnGenerarComprobante = crearBotonEstiloTambo("Generar Comprobante", COLOR_BOTON_AMARILLO, COLOR_FONDO_MORADO);
    btnGenerarComprobante.setForeground(COLOR_FONDO_MORADO);
    
    btnGenerarComprobante.addActionListener(e -> generarComprobante());
    buttonPanel.add(btnGenerarComprobante);
    btnProducto.addActionListener(e -> registrarProducto());
    buttonPanel.add(btnProducto);
    buttonPanel.add(deleteButton);
    buttonPanel.add(actualButton);
    buttonPanel.setBackground(COLOR_FONDO_MORADO); // <--- ¡Asegúrate de que esta línea esté presente!
    panelContenido.add(buttonPanel, BorderLayout.SOUTH);
    
deleteButton.addActionListener(e -> {
    int selectedRow = tblProductos.getSelectedRow(); 

    if (selectedRow != -1) {
        String codigoStr = (String) modeloTablaProductos.getValueAt(selectedRow, 0);
        
        Producto productoSeleccionado = null;
        for(Producto p : listaProductos) {
            if (p.getCodigo().equals(codigoStr)) {
                productoSeleccionado = p;
                break;
            }
        }

        if (productoSeleccionado != null) {
            // 1. Determinar el estado actual y el mensaje
            int estadoActual = productoSeleccionado.getactive();
            // Determina el nuevo estado (invierte 1 a 0, o 0 a 1)
            int nuevoEstado = (estadoActual == 1) ? 0 : 1; 
            
            // Determina las cadenas de acción y estado para el mensaje
            String accion = (nuevoEstado == 0) ? "DESACTIVAR" : "ACTIVAR";
            String estadoStr = (nuevoEstado == 0) ? "INACTIVO" : "ACTIVO";

            int confirm = JOptionPane.showConfirmDialog(
                this, 
                // Mensaje Dinámico:
                "¿Está seguro de **" + accion + "** el producto '" + productoSeleccionado.getNombre() + "' (Código: " + codigoStr + ")?\n" +
                "El producto pasará a estar: " + estadoStr, 
                "Confirmar Cambio de Estado", 
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    // *** Llamada al método del sistema para alternar el estado (0 o 1) ***
                    // (Asegúrate de que 'sistema.eliminarProducto' actualice la columna 'active' con el 'nuevoEstado')
                    boolean exito = sistema.eliminarProducto(productoSeleccionado.getCodigo(), nuevoEstado); 
                    
                    if(exito) {
                        
                        // 1. ELIMINAR LA FILA DE LA TABLA
                        // Si el estado cambia, el producto debe desaparecer de la vista actual
                        modeloTablaProductos.removeRow(selectedRow);
                        
                        // 2. ELIMINAR EL OBJETO DE LA LISTA LOCAL DE LA VISTA
                        listaProductos.remove(productoSeleccionado);
                        
                        // 3. Notificar al usuario
                        JOptionPane.showMessageDialog(this, "Producto " + estadoStr + " correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    } else {
                         JOptionPane.showMessageDialog(this, "Fallo en la base de datos al " + accion + ".", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) { 
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    } else {
        JOptionPane.showMessageDialog(this, "Seleccione una fila.", "Error", JOptionPane.WARNING_MESSAGE);
    }
});
    actualButton.addActionListener(e -> {
        int selectedRow = tblProductos.getSelectedRow(); 

        if (selectedRow != -1) {
            String codigoStr = (String) modeloTablaProductos.getValueAt(selectedRow, 0);
            
            Producto productoSeleccionado = null;
            for(Producto p : listaProductos) {
                if (p.getCodigo().equals(codigoStr)) {
                    productoSeleccionado = p;
                    break;
                }
            }

            if (productoSeleccionado != null) {
                // --- INTERFAZ DE EDICIÓN SIMPLE (Implementación dentro del Listener) ---
                
                // 1. Solicitar los nuevos valores (usando JOption, simple pero funcional)
                String nuevoNombre = JOptionPane.showInputDialog(this, 
                    "Nuevo Nombre para '" + productoSeleccionado.getNombre() + "':", 
                    productoSeleccionado.getNombre());
                
                String nuevoPrecioStr = JOptionPane.showInputDialog(this, 
                    "Nuevo Precio para '" + productoSeleccionado.getNombre() + "' (Actual: " + productoSeleccionado.getPrecio() + "):",
                    String.format("%.2f", productoSeleccionado.getPrecio()));

                String nuevoStockStr = JOptionPane.showInputDialog(this, 
                    "Nuevo Stock para '" + productoSeleccionado.getNombre() + "' (Actual: " + productoSeleccionado.getStock() + "):",
                    String.valueOf(productoSeleccionado.getStock()));
    // --- 1.5. AÑADIR SOLICITUD DE ESTADO ACTIVO ---
                int nuevoEstado = productoSeleccionado.getactive(); // Estado actual por defecto

                int confirmEstado = JOptionPane.showConfirmDialog(
                    this, 
                    "¿Desea que el producto esté ACTIVO?", 
                    "Modificar Estado Activo", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE
                );
                // 2. Validar y Procesar los datos
                if (confirmEstado == JOptionPane.YES_OPTION) {
                nuevoEstado = 1; // ACTIVO
            } else if (confirmEstado == JOptionPane.NO_OPTION) {
                nuevoEstado = 0; // INACTIVO
            }
            // --- FIN DE SOLICITUD DE ESTADO ACTIVO ---


            // 2. Validar y Procesar los datos
            if (nuevoNombre != null && nuevoPrecioStr != null && nuevoStockStr != null) {
                try {
                    double nuevoPrecio = Double.parseDouble(nuevoPrecioStr.replace(',', '.'));
                    int nuevoStock = Integer.parseInt(nuevoStockStr);

                    // Crear el objeto Producto Modificado (¡INCLUYENDO EL NUEVO ESTADO!)
                    Producto productoModificado = new Producto(
                        productoSeleccionado.getCodigo(),
                        nuevoNombre,
                        nuevoPrecio,
                        nuevoStock,
                        nuevoEstado // <-- ¡Nuevo estado activo/inactivo!
                    );

                    // 3. Llamar al Controlador para ejecutar el UPDATE en la BD
                    // ** Necesitas actualizar el método 'actualizarProducto' en MinimarketSistema **
                    if (sistema.actualizarProducto(productoModificado)) {
                        JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        String estadoActivoStr = (nuevoEstado == 1) ? "Sí" : "No";
                        // Actualizar la fila visible en la GUI (para reflejar los cambios)
                        modeloTablaProductos.setValueAt(productoModificado.getNombre(), selectedRow, 1);
                        modeloTablaProductos.setValueAt(String.format("%.2f", productoModificado.getPrecio()), selectedRow, 2);
                        modeloTablaProductos.setValueAt(productoModificado.getStock(), selectedRow, 3);
                        // Asumimos que la variable 'nuevoEstadoStr' se calculó antes
                        modeloTablaProductos.setValueAt(estadoActivoStr, selectedRow, 4); // Actualizar columna 'active'

                    } else {
                        JOptionPane.showMessageDialog(this, "Error al actualizar producto en la BD.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Precio o Stock inválido. Ingrese solo números.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    } else {
        JOptionPane.showMessageDialog(this, "Seleccione una fila para actualizar.", "Error", JOptionPane.WARNING_MESSAGE);
    }
    });

    JOptionPane.showMessageDialog(
        this,panelContenido,
        "Listado de " + titulo,
        JOptionPane.PLAIN_MESSAGE);
}
private JButton crearBotonEstiloTambo(String texto, Color bg, Color fg) {
    JButton boton = new JButton(texto);
    boton.setBackground(bg);
    boton.setForeground(fg);
    boton.setFont(new Font("Arial", Font.BOLD, 18));
    boton.setFocusPainted(false); // Quitar el borde de foco
    return boton;
}
private void registrarProducto() {
    JTextField codigoField = new JTextField();
    JTextField nombreField = new JTextField();
    JTextField precioField = new JTextField();
    JTextField stockField = new JTextField();

    // ➡️ APLICAR EL FILTRO AL CAMPO NOMBRE
    ((AbstractDocument) nombreField.getDocument()).setDocumentFilter(new LetterOnlyDocumentFilter());

    JPanel panel = new JPanel(new GridLayout(0, 2));
    panel.add(new JLabel("Código:"));
    panel.add(codigoField);
    panel.add(new JLabel("Nombre:"));
    panel.add(nombreField);
    panel.add(new JLabel("Precio:"));
    panel.add(precioField);
    panel.add(new JLabel("Stock:"));
    panel.add(stockField);

    int result = JOptionPane.showConfirmDialog(this, panel, "Registrar Producto",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
        try {
            String codigo = codigoField.getText();
            String nombre = nombreField.getText().trim(); // Limpiar espacios en blanco
            double precio = Double.parseDouble(precioField.getText());
            int stock = Integer.parseInt(stockField.getText());
            int active = 1;

            if (codigo.isEmpty() || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Código y Nombre no pueden estar vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Opcional: una validación final por si el filtro falla
            if (!nombre.matches("^[a-zA-Z\\s]+$")) {
                 JOptionPane.showMessageDialog(this, "El campo Nombre solo debe contener letras y espacios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                 return;
            }

            //Suponiendo la existencia de Producto y sistema
            Producto producto = new Producto(codigo, nombre, precio, stock, active);
            sistema.registrarProducto(producto);
            JOptionPane.showMessageDialog(this, "Producto registrado con éxito en MySQL.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese valores numéricos válidos para Precio y Stock.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (HeadlessException ex) {
            // Reemplaza 'MinimarketGUI.class' con la clase real donde se encuentra este método
            Logger.getLogger(MinimarketGUI.class.getName()).log(Level.SEVERE, "Error al registrar producto", ex);
            JOptionPane.showMessageDialog(this, "Error al registrar producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
private void Salir(){
        Login LoginFrame = new Login();
        LoginFrame.setVisible(true);
        LoginFrame.pack();
        LoginFrame.setLocationRelativeTo(null); 
        this.dispose();
}
private void registrarCliente() {
    final int LONGITUD_DNI_REQUERIDA = 8; // Define el límite

    JTextField dniField = new JTextField();

    // El filtro KeyListener para DNI (Solo números y límite de longitud)
    dniField.addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyTyped(java.awt.event.KeyEvent evt) {
            char c = evt.getKeyChar();

            if (!Character.isDigit(c)) {
                evt.consume();
            }

            if (dniField.getText().length() >= LONGITUD_DNI_REQUERIDA) {
                evt.consume();
            }
        }
    });

    JTextField nombreField = new JTextField();
    JTextField telefonoField = new JTextField();

    // ➡️ APLICAR EL FILTRO AL CAMPO NOMBRE (Solo permite letras y espacios)
    ((AbstractDocument) nombreField.getDocument()).setDocumentFilter(new LetterOnlyDocumentFilter());

    JPanel panel = new JPanel(new GridLayout(0, 2));
    panel.add(new JLabel("DNI (" + LONGITUD_DNI_REQUERIDA + " dígitos):"));
    panel.add(dniField);
    panel.add(new JLabel("Nombre Completo:"));
    panel.add(nombreField);
    panel.add(new JLabel("Teléfono:"));
    panel.add(telefonoField);

    int result = JOptionPane.showConfirmDialog(this, panel, "Registrar Cliente",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
        String dni = dniField.getText().trim();
        String nombre = nombreField.getText().trim();
        String telefono = telefonoField.getText().trim();

        // VALIDACIÓN DE CAMPOS LLENOS
        if (nombre.isEmpty() || dni.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y DNI no pueden estar vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // VALIDACIÓN ADICIONAL DEL NOMBRE (Para asegurarse por si el DocumentFilter falla o se pegó texto)
        if (!nombre.matches("^[a-zA-Z\\s]+$")) {
            JOptionPane.showMessageDialog(this, "El campo Nombre solo debe contener letras y espacios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // VALIDACIÓN FINAL (Asegura que la longitud requerida se cumpla)
        if (dni.length() != LONGITUD_DNI_REQUERIDA) {
            JOptionPane.showMessageDialog(this, "Debe ingresar los " + LONGITUD_DNI_REQUERIDA + " dígitos del DNI.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Cliente cliente = new Cliente(dni, nombre, telefono);
            sistema.registrarCliente(cliente);
            JOptionPane.showMessageDialog(this, "Cliente registrado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (HeadlessException ex) {
            // ... (manejo de errores) ...
        }
    }
}
private void registrarEmpleado() {
    JTextField idField = new JTextField();
    JTextField dniField = new JTextField();
    JTextField nombreField = new JTextField();
    JTextField cargoField = new JTextField();
    JPasswordField contraseñaField = new JPasswordField();

    // ➡️ APLICAR EL FILTRO AL CAMPO NOMBRE (Solo permite letras y espacios)
    ((AbstractDocument) nombreField.getDocument()).setDocumentFilter(new LetterOnlyDocumentFilter());
    
    // ➡️ APLICAR EL FILTRO AL CAMPO CARGO (Solo permite letras y espacios)
    ((AbstractDocument) cargoField.getDocument()).setDocumentFilter(new LetterOnlyDocumentFilter());


    JPanel panel = new JPanel(new GridLayout(0, 2));
    panel.add(new JLabel("ID Empleado:"));
    panel.add(idField);
    panel.add(new JLabel("DNI:"));
    panel.add(dniField);
    panel.add(new JLabel("Nombre:"));
    panel.add(nombreField);
    panel.add(new JLabel("Cargo:"));
    panel.add(cargoField);
    panel.add(new JLabel("Contraseña:"));
    panel.add(contraseñaField);

    int result = JOptionPane.showConfirmDialog(this, panel, "Registrar Empleado",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
        // Obtenemos los textos y eliminamos espacios al inicio/final
        String id = idField.getText().trim();
        String dni = dniField.getText().trim();
        String nombre = nombreField.getText().trim();
        String cargo = cargoField.getText().trim();
        String contraseña = new String(contraseñaField.getPassword());

        if (id.isEmpty() || nombre.isEmpty() || cargo.isEmpty() || contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // VALIDACIÓN DE LETRAS FINAL (por si se pega contenido inválido)
        if (!nombre.matches("^[a-zA-Z\\s]+$")) {
            JOptionPane.showMessageDialog(this, "El campo Nombre solo debe contener letras y espacios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!cargo.matches("^[a-zA-Z\\s]+$")) {
            JOptionPane.showMessageDialog(this, "El campo Cargo solo debe contener letras y espacios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Es recomendable añadir validación para que DNI e ID solo acepten números aquí también
        if (!id.matches("^\\d+$") || !dni.matches("^\\d+$")) {
             JOptionPane.showMessageDialog(this, "ID y DNI solo deben contener números.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
             return;
        }


        try {
            Empleado nuevoEmpleado = new Empleado(id,dni, nombre, contraseña, cargo);
            sistema.registrarEmpleado(nuevoEmpleado);
            JOptionPane.showMessageDialog(this, "Empleado registrado correctamente en MySQL.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (HeadlessException ex) {
            // ... (manejo de errores) ...
        }
    }
}
private void verListaClientes(List<Cliente> listaClientes, String titulo){
// 1. Verificar si hay datos
    if (listaClientes.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay " + titulo.toLowerCase() + " registrados en MySQL.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return;
    }

    // 2. Definir las columnas de la tabla de Clientes
    String[] nombresColumnas = {"DNI", "Nombre Completo", "Teléfono"};

    // 3. Crear el modelo de la tabla (usando 0 filas iniciales)
    DefaultTableModel modeloTablaClientes = new DefaultTableModel(nombresColumnas, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Evita la edición
        }
    };

    // 4. Llenar el modelo con los datos de la lista
    for (Cliente cliente : listaClientes) {
        Object[] fila = {
            cliente.getDni(),
            cliente.getNombreCompleto(), // Asumo que este método existe
            cliente.getTelefono(),       // Asumo que este método existe
        };
        modeloTablaClientes.addRow(fila);
    }

    // 5. Crear la JTable y aplicar estilos
    JTable tblClientes = new JTable(modeloTablaClientes);
    tblClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tblClientes.setRowHeight(25);
    tblClientes.setForeground(COLOR_BOTON_AMARILLO);
    tblClientes.setBackground(COLOR_FONDO_MORADO); 

    // Estilos del encabezado
    tblClientes.getTableHeader().setBackground(COLOR_BOTON_AMARILLO);
    tblClientes.getTableHeader().setForeground(COLOR_FONDO_MORADO);
    tblClientes.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

    // 6. Crear el JScrollPane
    JScrollPane scrollPane = new JScrollPane(tblClientes);
    scrollPane.setPreferredSize(new Dimension(650, 400)); // Dimensiones similares a la tabla de productos
    scrollPane.getViewport().setBackground(COLOR_FONDO_MORADO);
    
    // 7. Crear el panel de contenido (Principal)
    JPanel panelContenido = new JPanel(new BorderLayout(5, 5));
    panelContenido.add(scrollPane, BorderLayout.CENTER); 
    panelContenido.setBackground(COLOR_FONDO_MORADO);
    
    // 8. Crear el panel de botones
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(COLOR_FONDO_MORADO);

    // Botones con estilo Tambo (asumiendo que 'crearBotonEstiloTambo' existe)
    JButton deleteButton = crearBotonEstiloTambo("Eliminar Seleccionado", COLOR_BOTON_AMARILLO, COLOR_FONDO_MORADO);
    JButton btnCliente = crearBotonEstiloTambo("Registrar Cliente", COLOR_BOTON_AMARILLO, COLOR_FONDO_MORADO);
    
    buttonPanel.add(btnCliente);
    buttonPanel.add(deleteButton);
    panelContenido.add(buttonPanel, BorderLayout.SOUTH);

    // 9. Configurar Actions
    
    btnCliente.addActionListener(e -> registrarCliente());

    deleteButton.addActionListener(e -> {
        int selectedRow = tblClientes.getSelectedRow(); 

        if (selectedRow != -1) {
            // Obtenemos el DNI (clave principal) de la primera columna (columna 0)
            String dniStr = (String) modeloTablaClientes.getValueAt(selectedRow, 0);
            
            // Buscar el Cliente original en la lista para obtener el nombre completo
            Cliente clienteSeleccionado = null;
            for(Cliente c : listaClientes) {
                if (c.getDni().equals(dniStr)) {
                    clienteSeleccionado = c;
                    break;
                }
            }

            if (clienteSeleccionado != null) {
                int confirm = JOptionPane.showConfirmDialog(
                    this, 
                    "¿Está seguro de eliminar el cliente **" + clienteSeleccionado.getNombreCompleto() + "** (DNI: " + dniStr + ")?", 
                    "Confirmar Eliminación", 
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        // Eliminación en la BD
                        sistema.eliminarCliente(clienteSeleccionado.getDni()); 
                        
                        // Eliminación de la tabla y de la lista en memoria
                        modeloTablaClientes.removeRow(selectedRow);
                        listaClientes.remove(clienteSeleccionado);
                        
                        JOptionPane.showMessageDialog(this, "✅ Cliente eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) { 
                        Logger.getLogger(MinimarketGUI.class.getName()).log(Level.SEVERE, "Error al eliminar cliente", ex);
                        JOptionPane.showMessageDialog(this, "❌ Error al eliminar cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "⚠️ Seleccione una fila para eliminar.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    });

    // 10. Mostrar el panel contenedor en un cuadro de diálogo
    JOptionPane.showMessageDialog(
        this, 
        panelContenido,
        "Listado de " + titulo,
        JOptionPane.PLAIN_MESSAGE
    );
}
private void verListaEmpleados(List<Empleado> listaEmpleados, String titulo) {
 // 1. Verificar si hay datos
    if (listaEmpleados.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No hay " + titulo.toLowerCase() + " registrados en MySQL.", "Información", JOptionPane.INFORMATION_MESSAGE);
        return;
    }

    // 2. Definir las columnas de la tabla de Empleados
    // Asumo que Empleado tiene ID, Nombre, DNI/Cédula y Cargo
    String[] nombresColumnas = {"ID", "DNI/Cédula", "Nombre", "Cargo"};

    // 3. Crear el modelo de la tabla
    DefaultTableModel modeloTablaEmpleados = new DefaultTableModel(nombresColumnas, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Evita la edición
        }
    };

    // 4. Llenar el modelo con los datos de la lista
    for (Empleado empleado : listaEmpleados) {
        Object[] fila = {
            empleado.getId(),
            empleado.getDNI(), 
            empleado.getNombre(),// Asumo que este método existe
            empleado.getCargo()       // Asumo que este método existe
        };
        modeloTablaEmpleados.addRow(fila);
    }

    // 5. Crear la JTable y aplicar estilos
    JTable tblEmpleados = new JTable(modeloTablaEmpleados);
    tblEmpleados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tblEmpleados.setRowHeight(25);
    
    // Estilos de la tabla (Cuerpo)
    tblEmpleados.setBackground(COLOR_FONDO_MORADO); 
    tblEmpleados.setForeground(Color.WHITE); 

    // Estilos del encabezado
    tblEmpleados.getTableHeader().setBackground(COLOR_BOTON_AMARILLO);
    tblEmpleados.getTableHeader().setForeground(COLOR_FONDO_MORADO);
    tblEmpleados.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

    // 6. Crear el JScrollPane
    JScrollPane scrollPane = new JScrollPane(tblEmpleados);
    scrollPane.setPreferredSize(new Dimension(680, 400));
    scrollPane.getViewport().setBackground(COLOR_FONDO_MORADO);
    
    // 7. Crear el panel de contenido (Principal)
    JPanel panelContenido = new JPanel(new BorderLayout(5, 5));
    panelContenido.add(scrollPane, BorderLayout.CENTER); 
    panelContenido.setBackground(COLOR_FONDO_MORADO);
    
    // 8. Crear el panel de botones
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(COLOR_FONDO_MORADO);

    // Botones con estilo Tambo
    JButton deleteButton = crearBotonEstiloTambo("Eliminar Seleccionado", COLOR_BOTON_AMARILLO, COLOR_FONDO_MORADO);
    JButton btnEmpleado = crearBotonEstiloTambo("Registrar Empleado", COLOR_BOTON_AMARILLO, COLOR_FONDO_MORADO);
    
    buttonPanel.add(btnEmpleado);
    buttonPanel.add(deleteButton);
    panelContenido.add(buttonPanel, BorderLayout.SOUTH);

    // 9. Configurar Actions
    
    btnEmpleado.addActionListener(e -> registrarEmpleado());

    deleteButton.addActionListener(e -> {
        int selectedRow = tblEmpleados.getSelectedRow(); 

        if (selectedRow != -1) {
            // Obtenemos el ID (clave principal) de la primera columna (columna 0)
            String idStr = (String) modeloTablaEmpleados.getValueAt(selectedRow, 0);
            
            // Buscar el Empleado original en la lista para obtener el nombre
            Empleado empleadoSeleccionado = null;
            for(Empleado emp : listaEmpleados) {
                // Asumo que getId() devuelve un String que coincide con el valor de la tabla
                if (String.valueOf(emp.getId()).equals(idStr)) { 
                    empleadoSeleccionado = emp;
                    break;
                }
            }

            if (empleadoSeleccionado != null) {
                int confirm = JOptionPane.showConfirmDialog(
                    this, 
                    "¿Está seguro de eliminar el empleado **" + empleadoSeleccionado.getNombre() + "** (ID: " + idStr + ")?", 
                    "Confirmar Eliminación", 
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        // Usamos la clave primaria (ID) para la eliminación en la BD
                        sistema.eliminarEmpleado(empleadoSeleccionado.getId()); 
                        
                        // Eliminación de la tabla y de la lista en memoria
                        modeloTablaEmpleados.removeRow(selectedRow);
                        listaEmpleados.remove(empleadoSeleccionado);
                        
                        JOptionPane.showMessageDialog(this, "Empleado eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) { 
                        Logger.getLogger(MinimarketGUI.class.getName()).log(Level.SEVERE, "Error al eliminar empleado", ex);
                        JOptionPane.showMessageDialog(this, "Error al eliminar empleado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una fila para eliminar.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    });

    // 10. Mostrar el panel contenedor en un cuadro de diálogo
    JOptionPane.showMessageDialog(
        this, 
        panelContenido,
        "Listado de " + titulo,
        JOptionPane.PLAIN_MESSAGE
    );
}
private void generarComprobante() {
        comprobanteDialog = new JDialog(this, "Generar Comprobante", true);
        comprobanteDialog.setSize(500, 600);
        comprobanteDialog.setLayout(new BorderLayout());
        comprobanteDialog.setLocationRelativeTo(this);

        JPanel topPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        List<Cliente> clientes = sistema.getClientes();
        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay clientes registrados. Registre uno primero.", "Error", JOptionPane.ERROR_MESSAGE);
            comprobanteDialog.dispose();
            return;
        }
        cmbClientes = new JComboBox<>(clientes.toArray(Cliente[]::new));
        topPanel.add(new JLabel("Seleccione Cliente:"));
        topPanel.add(cmbClientes);

        Empleado empleadoLogueado = SesionEmpleado.getEmpleado();
        if (empleadoLogueado == null) {
            JOptionPane.showMessageDialog(this, "No hay empleado logueado. Inicie sesión primero.", "Error", JOptionPane.ERROR_MESSAGE);
            comprobanteDialog.dispose();
            return;
        }
        topPanel.add(new JLabel("Empleado:"));
        topPanel.add(new JLabel(empleadoLogueado.getNombre()));

        String[] tiposComprobante = {"Boleta", "Factura"};
        cmbTipoComprobante = new JComboBox<>(tiposComprobante);
        topPanel.add(new JLabel("Tipo de Comprobante:"));
        topPanel.add(cmbTipoComprobante);

        List<Producto> productosDisponibles = sistema.getProductos(); // Este método trae TODOS
        List<Producto> productosParaVenta = new ArrayList<>();
        for (Producto p : productosDisponibles) {
        // Filtra: 1. Debe tener Stock > 0, Y 2. Debe estar Activo (active == 1)
        if (p.getStock() > 0 && p.getactive() == 1) { // <--- ¡CAMBIO AQUÍ!
        productosParaVenta.add(p);
        }
        }

        if (productosParaVenta.isEmpty()) { // Usar la nueva lista
        JOptionPane.showMessageDialog(this, "No hay productos activos con stock disponible para la venta.", "Sin Stock", JOptionPane.INFORMATION_MESSAGE);
            comprobanteDialog.dispose();
            return;
        }
        cmbProductos = new JComboBox<>(productosParaVenta.toArray(Producto[]::new));
        topPanel.add(new JLabel("Seleccione Producto:"));
        topPanel.add(cmbProductos);

        txtCantidadProducto = new JTextField("1");
        topPanel.add(new JLabel("Cantidad:"));
        topPanel.add(txtCantidadProducto);

        btnAgregarProducto = new JButton("Agregar Producto a Venta");
        btnAgregarProducto.addActionListener((ActionEvent e) -> {
            agregarProductoAVenta();
        });
        topPanel.add(btnAgregarProducto);
        topPanel.add(new JLabel("")); 

        comprobanteDialog.add(topPanel, BorderLayout.NORTH);

        String[] columnas = {"Cant.", "Producto", "P. Unit.", "Subtotal"};
        modeloTablaDetalle = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblDetalleComprobante = new JTable(modeloTablaDetalle);
        JScrollPane scrollDetalle = new JScrollPane(tblDetalleComprobante);
        comprobanteDialog.add(scrollDetalle, BorderLayout.CENTER);

        lblTotalVenta = new JLabel("Total: S/ 0.00");
        lblTotalVenta.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotalVenta.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTotalVenta.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(lblTotalVenta, BorderLayout.NORTH);

        JPanel buttonSubPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnFinalizarVenta = new JButton("Finalizar Venta");
        btnCancelarVentaComprobante = new JButton("Cancelar Venta");

        btnFinalizarVenta.addActionListener((ActionEvent e) -> {
            finalizarVenta();
        });

        btnCancelarVentaComprobante.addActionListener((ActionEvent e) -> {
            int confirm = JOptionPane.showConfirmDialog(comprobanteDialog, "¿Está seguro de cancelar la venta en curso?", "Confirmar Cancelación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                ventaEnProgreso = null;
                comprobanteDialog.dispose();
            }
        });

        buttonSubPanel.add(btnFinalizarVenta);
        buttonSubPanel.add(btnCancelarVentaComprobante);
        bottomPanel.add(buttonSubPanel, BorderLayout.SOUTH);
        comprobanteDialog.add(bottomPanel, BorderLayout.SOUTH);

        ventaEnProgreso = new Venta(null, new Date(), null, null, "Boleta", 0.0); 
        ventaEnProgreso.setCliente((Cliente) cmbClientes.getSelectedItem());
        ventaEnProgreso.setEmpleado(empleadoLogueado);
        ventaEnProgreso.setTipoComprobante((String) cmbTipoComprobante.getSelectedItem());

        actualizarDetalleComprobante();

        comprobanteDialog.setVisible(true);
    }
private void agregarProductoAVenta() {
        Producto productoSeleccionado = (Producto) cmbProductos.getSelectedItem();
        if (productoSeleccionado == null) {
            JOptionPane.showMessageDialog(comprobanteDialog, "Seleccione un producto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidadProducto.getText());
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(comprobanteDialog, "La cantidad debe ser mayor a cero.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (cantidad > productoSeleccionado.getStock()) {
                JOptionPane.showMessageDialog(comprobanteDialog, "Cantidad excede el stock disponible (" + productoSeleccionado.getStock() + ").", "Stock Insuficiente", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(comprobanteDialog, "Ingrese una cantidad numérica válida.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean productoExistente = false;
        for (ItemVenta item : ventaEnProgreso.getItems()) {
            if (item.getProducto().getCodigo().equals(productoSeleccionado.getCodigo())) {
                int nuevaCantidad = item.getCantidad() + cantidad;
                if (nuevaCantidad > productoSeleccionado.getStock()) {
                    JOptionPane.showMessageDialog(comprobanteDialog, "La cantidad total para este producto excede el stock disponible.", "Stock Insuficiente", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                item.setCantidad(nuevaCantidad);
                item.setSubtotal(nuevaCantidad * item.getProducto().getPrecio());
                productoExistente = true;
                break;
            }
        }

        if (!productoExistente) {
            ventaEnProgreso.getItems().add(new ItemVenta(productoSeleccionado, cantidad));
        }

        ventaEnProgreso.calcularTotal();
        actualizarDetalleComprobante();
        txtCantidadProducto.setText("1");
    }
private void actualizarDetalleComprobante() {
        modeloTablaDetalle.setRowCount(0);

        if (ventaEnProgreso != null && ventaEnProgreso.getItems() != null) {
            for (ItemVenta item : ventaEnProgreso.getItems()) {
                Object[] fila = {
                    item.getCantidad(),
                    item.getProducto().getNombre(),
                    String.format("%.2f", item.getProducto().getPrecio()),
                    String.format("%.2f", item.getSubtotal())
                };
                modeloTablaDetalle.addRow(fila);
            }
        }
        lblTotalVenta.setText(String.format("Total: S/%.2f", ventaEnProgreso != null ? ventaEnProgreso.getTotal() : 0.00));
    }
private void finalizarVenta() {
        if (ventaEnProgreso == null || ventaEnProgreso.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(comprobanteDialog, "No hay productos añadidos a la venta.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            ventaEnProgreso.setCliente((Cliente) cmbClientes.getSelectedItem());
            ventaEnProgreso.setTipoComprobante((String) cmbTipoComprobante.getSelectedItem());
            ventaEnProgreso.calcularTotal();

            sistema.registrarVenta(ventaEnProgreso);

            JOptionPane.showMessageDialog(comprobanteDialog, "Venta finalizada y registrada con éxito.", "Venta Exitosa", JOptionPane.INFORMATION_MESSAGE);

            mostrarBoletaEnVentana(ventaEnProgreso);

            comprobanteDialog.dispose();
            ventaEnProgreso = null;
            actualizarCmbProductos();

        } catch (Exception ex) {
            Logger.getLogger(MinimarketGUI.class.getName()).log(Level.SEVERE, "Error al finalizar la venta", ex);
            JOptionPane.showMessageDialog(comprobanteDialog, "Error al finalizar la venta: " + ex.getMessage(), "Error de Venta", JOptionPane.ERROR_MESSAGE);

        }
    }
private void actualizarCmbProductos() {
        if (cmbProductos != null) {
            List<Producto> productosDisponibles = sistema.getProductos();
            List<Producto> productosConStock = new ArrayList<>();
            for (Producto p : productosDisponibles) {
                if (p.getStock() > 0) productosConStock.add(p);
            }
            cmbProductos.setModel(new DefaultComboBoxModel<>(productosConStock.toArray(Producto[]::new)));
        }
    }
private void mostrarBoletaEnVentana(Venta venta) {
        if (venta == null || venta.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay venta o ítems para mostrar la boleta.", "Error de Boleta", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        String clienteNombre = (venta.getCliente() != null) ? venta.getCliente().getNombreCompleto() : "Consumidor Final";
        String clienteDni = (venta.getCliente() != null) ? venta.getCliente().getDni() : "N/A";
        String empleadoNombre = (venta.getEmpleado() != null) ? venta.getEmpleado().getNombre() : "N/A";
        String tipoComprobante = venta.getTipoComprobante(); // Obtener el tipo de comprobante

        StringBuilder boletaContenido = new StringBuilder();

        boletaContenido.append("--------------------------------------------------\n");
        boletaContenido.append("           MINIMARKET J & J S.A.C.\n");
        boletaContenido.append("--------------------------------------------------\n");
        boletaContenido.append("RUC: 12345678901\n");
        boletaContenido.append("Dirección: Av. Principal 123, Ancón, Lima\n");
        boletaContenido.append("Teléfono: (01) 987654321\n");
        boletaContenido.append("--------------------------------------------------\n");
        boletaContenido.append(String.format("%s Nro: %s\n", tipoComprobante.toUpperCase(), venta.getId()));
        boletaContenido.append(String.format("Fecha y Hora: %s\n", sdf.format(venta.getFecha())));
        boletaContenido.append(String.format("Cliente: %s\n", clienteNombre));
        boletaContenido.append(String.format("DNI Cliente: %s\n", clienteDni));
        
        if ("Factura".equals(tipoComprobante)) {

            boletaContenido.append(String.format("RUC Cliente: %s\n", "10" + clienteDni)); 
            boletaContenido.append(String.format("Razón Social: %s\n", clienteNombre + " S.A.C.")); 
        }

        boletaContenido.append(String.format("Atendido por: %s\n", empleadoNombre));
        boletaContenido.append("--------------------------------------------------\n");
        boletaContenido.append(String.format("%-6s %-20s %-10s %-10s\n", "Cant.", "Producto", "P. Unit.", "Subtotal"));
        boletaContenido.append("--------------------------------------------------\n");

        for (ItemVenta item : venta.getItems()) {
            boletaContenido.append(String.format("%-6d %-20s %-10.2f %-10.2f\n",
                item.getCantidad(),
                item.getProducto().getNombre(),
                item.getProducto().getPrecio(),
                item.getSubtotal()));
        }

        boletaContenido.append("--------------------------------------------------\n");
        boletaContenido.append(String.format("TOTAL A PAGAR: S/ %.2f\n", venta.getTotal()));
        boletaContenido.append("--------------------------------------------------\n");
        boletaContenido.append("           ¡Gracias por su compra!\n");
        boletaContenido.append("           Vuelva Pronto\n");
        boletaContenido.append("--------------------------------------------------\n");

        JDialog boletaDialog = new JDialog(this, tipoComprobante + " de Venta", true); 
        boletaDialog.setSize(400, 550);
        boletaDialog.setLocationRelativeTo(this);
        boletaDialog.setLayout(new BorderLayout());

        JTextArea boletaTextArea = new JTextArea(boletaContenido.toString());
        boletaTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        boletaTextArea.setEditable(false);
        boletaTextArea.setLineWrap(true);
        boletaTextArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(boletaTextArea);
        boletaDialog.add(scrollPane, BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> boletaDialog.dispose());

        // AÑADIDO: Botón de Imprimir
        JButton btnImprimir = new JButton("Imprimir");
        btnImprimir.addActionListener((ActionEvent e) -> {
            try {
                // Configurar el encabezado y pie de página si es necesario
                MessageFormat header = new MessageFormat("Minimarket J & J - " + tipoComprobante);
                MessageFormat footer = new MessageFormat("Página {0}");
                boletaTextArea.print(header, footer);
            } catch (PrinterException ex) {
                Logger.getLogger(MinimarketGUI.class.getName()).log(Level.SEVERE, "Error al imprimir la boleta", ex);
                JOptionPane.showMessageDialog(boletaDialog, "Error al imprimir: " + ex.getMessage(), "Error de Impresión", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel buttonPanelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT)); 
        buttonPanelBottom.add(btnImprimir); 
        buttonPanelBottom.add(btnCerrar);
        boletaDialog.add(buttonPanelBottom, BorderLayout.SOUTH);

        boletaDialog.setVisible(true);
    }
public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
                Logger.getLogger(MinimarketGUI.class.getName()).log(Level.WARNING, "No se pudo establecer el LookAndFeel del sistema.", e);
            }
            new MinimarketGUI().setVisible(true);
        });
    }
}