package vista;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.time.LocalTime;
import controlador.MinimarketSistema; // Asegúrate de importar tu controlador

public class Asistencia extends javax.swing.JFrame {
private static final LocalTime HORA_LIMITE_ENTRADA = LocalTime.of(8, 30);
private final MinimarketSistema sistema;
    public Asistencia() {
        initComponents();
        sistema = new MinimarketSistema();
        iniciarRelojCompacto();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Right = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        Left = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCodigoUnico = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        lblReloj = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("LOGIN");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 500));
        jPanel1.setLayout(null);

        Right.setBackground(new java.awt.Color(125, 50, 146));
        Right.setPreferredSize(new java.awt.Dimension(400, 500));

        jLabel6.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/tambin.png"))); // NOI18N

        javax.swing.GroupLayout RightLayout = new javax.swing.GroupLayout(Right);
        Right.setLayout(RightLayout);
        RightLayout.setHorizontalGroup(
            RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RightLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(142, 142, 142))
        );
        RightLayout.setVerticalGroup(
            RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RightLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel1.add(Right);
        Right.setBounds(0, 0, 390, 320);

        Left.setBackground(new java.awt.Color(125, 50, 146));
        Left.setMinimumSize(new java.awt.Dimension(400, 500));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 210, 0));
        jLabel1.setText("ASISTENCIA");

        jLabel2.setBackground(new java.awt.Color(102, 102, 102));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 210, 0));
        jLabel2.setText("ID:");

        txtCodigoUnico.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtCodigoUnico.setForeground(new java.awt.Color(102, 102, 102));

        jButton1.setBackground(new java.awt.Color(255, 210, 0));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(125, 50, 146));
        jButton1.setText("Registrar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(255, 210, 0));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(125, 50, 146));
        jButton3.setText("Login");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        lblReloj.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblReloj.setForeground(new java.awt.Color(255, 210, 0));
        lblReloj.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel4.setForeground(new java.awt.Color(255, 210, 0));
        jLabel4.setText("I don't have an account");

        javax.swing.GroupLayout LeftLayout = new javax.swing.GroupLayout(Left);
        Left.setLayout(LeftLayout);
        LeftLayout.setHorizontalGroup(
            LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftLayout.createSequentialGroup()
                .addGroup(LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LeftLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2))
                    .addGroup(LeftLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1))
                    .addGroup(LeftLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblReloj, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(LeftLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(txtCodigoUnico, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(LeftLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel4))
                    .addGroup(LeftLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(160, Short.MAX_VALUE))
        );
        LeftLayout.setVerticalGroup(
            LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblReloj, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCodigoUnico, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap(208, Short.MAX_VALUE))
        );

        jPanel1.add(Left);
        Left.setBounds(390, 0, 250, 320);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 638, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    Login LoginFrame = new Login();
        LoginFrame.setVisible(true);
        LoginFrame.pack();
        LoginFrame.setLocationRelativeTo(null); 
        this.dispose();    // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    registrarAsistencia();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed
private void iniciarRelojCompacto() {
    final SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss    dd/MM/yyyy");
    new Timer(1000, (e) -> {
        lblReloj.setText(formatoHora.format(new Date()));
    }).start();
}
private void mostrarMensaje(String mensaje, String infoExtra, int tipoMensaje) {
        // Usamos JOptionPane que es el método estándar de Swing sin estilo
        JOptionPane.showMessageDialog(this, 
                                      mensaje + "\n" + infoExtra, 
                                      "Notificación", 
                                      tipoMensaje);}
private void registrarAsistencia() {
    String codigo = txtCodigoUnico.getText().trim();
    String info;
    
    // Validación de campo vacío (Código anterior)
    if (codigo.isEmpty()) {
        mostrarMensaje("¡ERROR!", "El código no puede estar vacío.", JOptionPane.ERROR_MESSAGE); 
        SwingUtilities.invokeLater(() -> txtCodigoUnico.requestFocusInWindow());
        return;
    }
    
    txtCodigoUnico.setText(""); // Limpiar campo
    
    // El sistema ahora devuelve: 0 (Salida), 2 (Asistió), 3 (Tardanza), 4 (Jornada Completa), o -1 (Inválido)
    int estadoRegistro = sistema.registrarAsistencia(codigo); 
    
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss a");
    String horaActualDisplay = sdf.format(new Date());
    String mensaje;
    int tipoMensaje;

    // --- Lógica de Manejo de Estados Finales ---
    
    if (estadoRegistro == 0) {
        // Estado 0: SALIDA
        mensaje = "SALIDA REGISTRADA";
        info = "Hora de salida: " + horaActualDisplay;
        tipoMensaje = JOptionPane.INFORMATION_MESSAGE;
        
    } else if (estadoRegistro == 2) {
        // Estado 2: ENTRADA ASISTIÓ (a tiempo)
        mensaje = "ENTRADA REGISTRADA (ASISTIÓ)";
        info = "Llegó a tiempo. Hora: " + horaActualDisplay;
        tipoMensaje = JOptionPane.INFORMATION_MESSAGE;
        
    } else if (estadoRegistro == 3) {
        // Estado 3: ENTRADA TARDANZA
        mensaje = "ENTRADA REGISTRADA (TARDANZA)";
        info = "Llegó tarde. Hora: " + horaActualDisplay;
        tipoMensaje = JOptionPane.WARNING_MESSAGE;
        
    } else if (estadoRegistro == 4) { // <-- NUEVA LÓGICA AGREGADA
        // Estado 4: Jornada Completa (ya marcó Salida)
        mensaje = "JORNADA COMPLETA";
        info = "Ya registró su salida hoy. No es necesario marcar de nuevo.";
        tipoMensaje = JOptionPane.INFORMATION_MESSAGE;
        
    } else { // estadoRegistro == -1 o cualquier otro error
        mensaje = "CÓDIGO INVÁLIDO";
        info = "Por favor, verifique el código.";
        tipoMensaje = JOptionPane.ERROR_MESSAGE;
    }
    
    mostrarMensaje(mensaje, info, tipoMensaje);
    SwingUtilities.invokeLater(() -> txtCodigoUnico.requestFocusInWindow());
}
    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Left;
    private javax.swing.JPanel Right;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblReloj;
    private javax.swing.JTextField txtCodigoUnico;
    // End of variables declaration//GEN-END:variables
}