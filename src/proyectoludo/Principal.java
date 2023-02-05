/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoludo;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import javax.swing.JOptionPane;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashSet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.text.AbstractDocument;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author ricardo
 */
public class Principal extends javax.swing.JFrame {
    //int[] adminRange = {2,3,10};
    ArrayList<Integer> adminRange = new ArrayList<Integer>();
    
    String totalSumDes;
    String sumTambores;
    String totalSumi;
    String totalsumTambor;
    String maxOrden;
    
    Object[][] dtPer;
    String[] columnNames ={"ID","Fecha","Num. orden","Producto","Destinos","Tambores"};
    SQLConn con;
    PreparedStatement pst;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");  
    LocalDateTime now = LocalDateTime.now();  
    Map<Integer,String> empresaList=new HashMap<Integer,String>();
    Map<String,Integer> productoList=new HashMap<String,Integer>();
    Map<String,Integer> destinoList=new HashMap<String,Integer>();
    String destinos[] = new String[5];
    int destinosCount = -1;
    int maxOrdenValue = 0;
    String keySearch = null;

    public Principal() {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("sum4.png")));
        adminRange.add(2);
        adminRange.add(3);
        adminRange.add(10);
        try{
            con = new SQLConn();
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, "ERROR FATAL - ERROR AL TRATAR DE CREAR CONECCION A LA BASE DE DATOS","ERROR Principal-SQLCONN",JOptionPane.ERROR_MESSAGE);
        }
        initComponents();
        initConfig();

    }

    public void initConfig(){
        keySearch = "ID_suministro";
        updateEmpresaList();
        updateProductList();
        updateEmpresaName();
        updateDestinoList();
        updateKeyList();
        checkearPrivilegios();
        principal_btn_buscar.setVisible(false); //OCULTA EL BOTON DE BUSQUEDA AUXILIAR
       
        
    }
    public void checkearPrivilegios(){
        System.out.println("User rank: "+User.Rank);
        System.out.println("es admin?: "+Arrays.asList(adminRange).contains(User.Rank));
        if (!adminRange.contains(User.Rank)){
            principal_menu_procesos.hide();
            principal_btn_add.hide();
            principal_btn_edit.hide();
            principal_btn_del.hide();
        }
    }
    
    public void updateKeyList(){
        principal_cb_buscar.removeAllItems();
        principal_cb_buscar.addItem("ID_suministro");
        principal_cb_buscar.addItem("numOrden");
        principal_cb_buscar.addItem("fecha");
        principal_cb_buscar.addItem("producto");
        //principal_cb_buscar.addItem("ID_suministro");
        
    }

    public void updateEmpresaList(){
        System.out.println("Dentro de updateEmpresaList");
        try{
            principal_cb_ListaEmpresas.removeAllItems();
            pst = con.conn.prepareStatement("SELECT"+"`ID_empresa`,`nombreEmpresa`"+" FROM empresas"+" ORDER BY ID_empresa");
            ResultSet res = pst.executeQuery();
            while(res.next()){
                empresaList.put(res.getInt("ID_empresa"),res.getString("nombreEmpresa"));
            }
            for(Map.Entry m:empresaList.entrySet()){  
                principal_cb_ListaEmpresas.addItem(m.getValue().toString());
            }  
        }catch(SQLException ex){
           System.out.println("ERROR EN Principal/updateEmpresaList: "+ex.getMessage());
        }
        
    }
    public void updateProductList(){
        
        try{
            principal_cb_producto.removeAllItems();
            pst = con.conn.prepareStatement("SELECT"+" `ID_producto`,`nombreProd`"+" FROM productos"+" ORDER BY nombreProd");
            ResultSet res = pst.executeQuery();
            while(res.next()){
                productoList.put(res.getString("nombreProd"),res.getInt("ID_producto"));
            }
            for(Map.Entry m:productoList.entrySet()){  
                principal_cb_producto.addItem(m.getKey().toString());
            }  
        }catch(SQLException ex){
           System.out.println("ERROR EN Principal/updateProductList: "+ex.getMessage());
        }
    }
    public void updateDestinoList(){ 
        try{
            principal_cb_destino.removeAllItems();
            pst = con.conn.prepareStatement("SELECT"+" `ID_destino`,`nombreDestino`"+" FROM destinos"+" ORDER BY nombreDestino");
            ResultSet res = pst.executeQuery();
            while(res.next()){
                destinoList.put(res.getString("nombreDestino"),res.getInt("ID_destino"));
            }
            for(Map.Entry m:destinoList.entrySet()){  
                principal_cb_destino.addItem(m.getKey().toString());
            }  
        }catch(SQLException ex){
           System.out.println("ERROR EN Principal/updateDestinoList: "+ex.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        testDayPicker1 = new com.qt.datapicker.TestDayPicker();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        principal_jl_id = new javax.swing.JLabel();
        principal_jl_user = new javax.swing.JLabel();
        principal_jl_rank = new javax.swing.JLabel();
        principal_jl_time = new javax.swing.JLabel();
        principal_jl_totalUsers = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        principal_tbl_suministros = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        principal_cb_ListaEmpresas = new javax.swing.JComboBox<>();
        principal_jl_warninEmpresa = new javax.swing.JLabel();
        principal_btn_showSuminInfo = new javax.swing.JButton();
        principal_jl_titulosumin = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        principal_cb_buscar = new javax.swing.JComboBox<>();
        principal_jtf_search = new javax.swing.JTextField();
        principal_btn_buscar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        principal_jtf_id = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        principal_jtf_numOrden = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        principal_cb_producto = new javax.swing.JComboBox<>();
        principal_cb_destino = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        principal_jtf_destino = new javax.swing.JTextField();
        jCheckBox2 = new javax.swing.JCheckBox();
        principal_jtf_tambores = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        principal_dateCho_fecha = new com.toedter.calendar.JDateChooser();
        jPanel6 = new javax.swing.JPanel();
        principal_btn_limpiar = new javax.swing.JButton();
        principal_btn_add = new javax.swing.JButton();
        principal_btn_edit = new javax.swing.JButton();
        principal_btn_del = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        principal_menu_procesos = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SISTEMA DE GESTION DE ORDENES DE SUMINISTROS MIRANDA 200 C.A.");
        setBackground(new java.awt.Color(255, 255, 255));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jTabbedPane2.setBackground(new java.awt.Color(204, 255, 255));

        principal_jl_id.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        principal_jl_id.setText("id");

        principal_jl_user.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        principal_jl_user.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoludo/user3.png"))); // NOI18N
        principal_jl_user.setText("Nombre");

        principal_jl_rank.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        principal_jl_rank.setText("Rango");

        principal_jl_time.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        principal_jl_time.setText("tiempo");

        principal_jl_totalUsers.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        principal_jl_totalUsers.setText("jLabel4");

        jSeparator1.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator3.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator2.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(principal_jl_id)
                .addGap(56, 56, 56)
                .addComponent(principal_jl_user)
                .addGap(94, 94, 94)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69)
                .addComponent(principal_jl_rank)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 382, Short.MAX_VALUE)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(107, 107, 107)
                .addComponent(principal_jl_totalUsers)
                .addGap(102, 102, 102)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(principal_jl_time)
                .addGap(53, 53, 53))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(principal_jl_id)
                        .addComponent(principal_jl_rank)
                        .addComponent(principal_jl_user))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(principal_jl_totalUsers)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(principal_jl_time)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Sistema", jPanel3);

        jPanel2.setBackground(new java.awt.Color(255, 255, 204));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel2.setLayout(new java.awt.BorderLayout());

        principal_tbl_suministros.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        principal_tbl_suministros.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        principal_tbl_suministros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        principal_tbl_suministros.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        principal_tbl_suministros.setSelectionBackground(new java.awt.Color(204, 102, 0));
        principal_tbl_suministros.getTableHeader().setReorderingAllowed(false);
        principal_tbl_suministros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                principal_tbl_suministrosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(principal_tbl_suministros);

        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel5.setBackground(new java.awt.Color(204, 255, 255));
        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Empresa destino");

        jButton1.setBackground(new java.awt.Color(255, 153, 0));
        jButton1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoludo/select1.png"))); // NOI18N
        jButton1.setText("Seleccionar");
        jButton1.setToolTipText("Seleccionar la empresa destino");
        jButton1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        principal_cb_ListaEmpresas.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        principal_cb_ListaEmpresas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        principal_cb_ListaEmpresas.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                principal_cb_ListaEmpresasItemStateChanged(evt);
            }
        });
        principal_cb_ListaEmpresas.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                principal_cb_ListaEmpresasFocusLost(evt);
            }
        });
        principal_cb_ListaEmpresas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                principal_cb_ListaEmpresasMouseExited(evt);
            }
        });
        principal_cb_ListaEmpresas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                principal_cb_ListaEmpresasActionPerformed(evt);
            }
        });

        principal_jl_warninEmpresa.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        principal_jl_warninEmpresa.setForeground(new java.awt.Color(153, 102, 0));
        principal_jl_warninEmpresa.setText("EMPRESA MOSTRADA: %");

        principal_btn_showSuminInfo.setBackground(new java.awt.Color(255, 153, 0));
        principal_btn_showSuminInfo.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        principal_btn_showSuminInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoludo/report1.png"))); // NOI18N
        principal_btn_showSuminInfo.setText("Mostrar info");
        principal_btn_showSuminInfo.setToolTipText("Mostrar Información del conteo de Ordenes y Tambores");
        principal_btn_showSuminInfo.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        principal_btn_showSuminInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                principal_btn_showSuminInfoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(33, 33, 33)
                        .addComponent(principal_cb_ListaEmpresas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(principal_jl_warninEmpresa)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(principal_btn_showSuminInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(principal_cb_ListaEmpresas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(principal_btn_showSuminInfo)
                    .addComponent(principal_jl_warninEmpresa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        principal_jl_titulosumin.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        principal_jl_titulosumin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoludo/build1.png"))); // NOI18N
        principal_jl_titulosumin.setText("Suministros para \"\"");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Buscar");

        principal_cb_buscar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        principal_cb_buscar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        principal_cb_buscar.setToolTipText("en base a que se buscara");
        principal_cb_buscar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        principal_cb_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                principal_cb_buscarActionPerformed(evt);
            }
        });

        principal_jtf_search.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        principal_jtf_search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                principal_jtf_searchKeyReleased(evt);
            }
        });

        principal_btn_buscar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        principal_btn_buscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoludo/search1.png"))); // NOI18N
        principal_btn_buscar.setText("Buscar");
        principal_btn_buscar.setEnabled(false);
        principal_btn_buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                principal_btn_buscarActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("igual a");

        jPanel7.setBackground(new java.awt.Color(204, 255, 255));
        jPanel7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 255), 2, true));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("ID suministro");
        jPanel7.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        ((AbstractDocument) principal_jtf_numOrden.getDocument()).setDocumentFilter(new CustomDocumentFilter());
        principal_jtf_id.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        principal_jtf_id.setText("0");
        principal_jtf_id.setToolTipText("Numero de orden");
        principal_jtf_id.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        principal_jtf_id.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        principal_jtf_id.setEnabled(false);
        jPanel7.add(principal_jtf_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 11, 290, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("Numero de orden");
        jPanel7.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 41, -1, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(204, 102, 0));
        jLabel7.setText("?");
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });
        jPanel7.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(113, 41, 20, -1));

        ((AbstractDocument) principal_jtf_numOrden.getDocument()).setDocumentFilter(new CustomDocumentFilter());
        principal_jtf_numOrden.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        principal_jtf_numOrden.setText("0");
        principal_jtf_numOrden.setToolTipText("Numero de orden");
        principal_jtf_numOrden.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        principal_jtf_numOrden.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        principal_jtf_numOrden.setEnabled(false);
        jPanel7.add(principal_jtf_numOrden, new org.netbeans.lib.awtextra.AbsoluteConstraints(137, 38, 100, -1));

        jCheckBox1.setForeground(new java.awt.Color(204, 102, 0));
        jCheckBox1.setText("Editar");
        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jPanel7.add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(254, 38, 130, -1));

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setText("Producto");
        jPanel7.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 65, -1));

        principal_cb_producto.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        principal_cb_producto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        principal_cb_producto.setToolTipText("Nombre del producto");
        principal_cb_producto.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jPanel7.add(principal_cb_producto, new org.netbeans.lib.awtextra.AbsoluteConstraints(74, 65, 310, -1));

        principal_cb_destino.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        principal_cb_destino.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        principal_cb_destino.setToolTipText("Nombre de la empresa destino");
        principal_cb_destino.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        principal_cb_destino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                principal_cb_destinoActionPerformed(evt);
            }
        });
        jPanel7.add(principal_cb_destino, new org.netbeans.lib.awtextra.AbsoluteConstraints(58, 95, 240, -1));

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText("Destino");
        jPanel7.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        jButton5.setBackground(new java.awt.Color(255, 153, 0));
        jButton5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton5.setText("Añadir");
        jButton5.setToolTipText("Añadir destino");
        jButton5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton5MouseClicked(evt);
            }
        });
        jPanel7.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 100, 80, -1));

        principal_jtf_destino.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        principal_jtf_destino.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        principal_jtf_destino.setEnabled(false);
        jPanel7.add(principal_jtf_destino, new org.netbeans.lib.awtextra.AbsoluteConstraints(7, 125, 250, -1));

        jCheckBox2.setForeground(new java.awt.Color(204, 102, 0));
        jCheckBox2.setText("Editar");
        jCheckBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox2ItemStateChanged(evt);
            }
        });
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });
        jPanel7.add(jCheckBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(263, 124, 121, -1));

        ((AbstractDocument) principal_jtf_tambores.getDocument()).setDocumentFilter(new CustomDocumentFilter());
        principal_jtf_tambores.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        principal_jtf_tambores.setText("0");
        principal_jtf_tambores.setToolTipText("Numero de tambores");
        principal_jtf_tambores.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        principal_jtf_tambores.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                principal_jtf_tamboresCaretUpdate(evt);
            }
        });
        jPanel7.add(principal_jtf_tambores, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 156, 240, -1));

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setText("Numero de tambores");
        jPanel7.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel21.setText("Fecha");
        jPanel7.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, -1, -1));

        principal_dateCho_fecha.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        principal_dateCho_fecha.setDateFormatString("yyyy/MM/dd");
        principal_dateCho_fecha.setMaxSelectableDate(new java.util.Date(253370782870000L));
        principal_dateCho_fecha.setMinSelectableDate(new java.util.Date(-62135751530000L));
        jPanel7.add(principal_dateCho_fecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(144, 183, 240, -1));

        jPanel6.setBackground(new java.awt.Color(255, 153, 0));
        jPanel6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        principal_btn_limpiar.setBackground(new java.awt.Color(255, 255, 255));
        principal_btn_limpiar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        principal_btn_limpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoludo/clear1.png"))); // NOI18N
        principal_btn_limpiar.setText("Limpiar");
        principal_btn_limpiar.setToolTipText("Limpiar los campos");
        principal_btn_limpiar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        principal_btn_limpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                principal_btn_limpiarActionPerformed(evt);
            }
        });

        principal_btn_add.setBackground(new java.awt.Color(153, 255, 153));
        principal_btn_add.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        principal_btn_add.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoludo/add1.png"))); // NOI18N
        principal_btn_add.setText("Añadir");
        principal_btn_add.setToolTipText("Añadir a la tabla de suministros");
        principal_btn_add.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        principal_btn_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                principal_btn_addActionPerformed(evt);
            }
        });

        principal_btn_edit.setBackground(new java.awt.Color(255, 204, 204));
        principal_btn_edit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        principal_btn_edit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoludo/edit1.png"))); // NOI18N
        principal_btn_edit.setText("Editar");
        principal_btn_edit.setToolTipText("Editar elemento de la tabla de suministros");
        principal_btn_edit.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        principal_btn_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                principal_btn_editActionPerformed(evt);
            }
        });

        principal_btn_del.setBackground(new java.awt.Color(255, 102, 102));
        principal_btn_del.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        principal_btn_del.setForeground(new java.awt.Color(51, 51, 51));
        principal_btn_del.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoludo/delete1.png"))); // NOI18N
        principal_btn_del.setText("Eliminar");
        principal_btn_del.setToolTipText("Eliminar elemento seleccionado");
        principal_btn_del.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        principal_btn_del.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                principal_btn_delMouseClicked(evt);
            }
        });
        principal_btn_del.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                principal_btn_delActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(principal_btn_del, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                    .addComponent(principal_btn_edit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(principal_btn_add, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(principal_btn_limpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(principal_btn_add)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(principal_btn_edit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(principal_btn_del)
                    .addComponent(principal_btn_limpiar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 220, 300, 140));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoludo/sum3.png"))); // NOI18N

        jMenuBar1.setBackground(new java.awt.Color(255, 255, 255));

        jMenu1.setBackground(new java.awt.Color(255, 255, 255));
        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoludo/home1.png"))); // NOI18N
        jMenu1.setText("Inicio");

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoludo/salir1.png"))); // NOI18N
        jMenuItem1.setText("Salir");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        principal_menu_procesos.setBackground(new java.awt.Color(255, 255, 255));
        principal_menu_procesos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoludo/setting1.png"))); // NOI18N
        principal_menu_procesos.setText("Procesos");

        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoludo/user2.png"))); // NOI18N
        jMenuItem3.setText("Usuarios");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        principal_menu_procesos.add(jMenuItem3);

        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoludo/build1.png"))); // NOI18N
        jMenuItem9.setText("Empresas");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        principal_menu_procesos.add(jMenuItem9);

        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoludo/place1.png"))); // NOI18N
        jMenuItem6.setText("Destinos");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        principal_menu_procesos.add(jMenuItem6);

        jMenuItem10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoludo/prod1.png"))); // NOI18N
        jMenuItem10.setText("Productos");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        principal_menu_procesos.add(jMenuItem10);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/proyectoludo/report1.png"))); // NOI18N
        jMenu2.setText("Generar Reporte");

        jMenuItem4.setText("De la empresa actual");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuItem5.setText("De todas las empresas");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        principal_menu_procesos.add(jMenu2);

        jMenuBar1.add(principal_menu_procesos);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(principal_jl_titulosumin))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(92, 92, 92)
                                        .addComponent(jLabel6)))
                                .addGap(101, 101, 101))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(principal_cb_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(principal_jtf_search, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(principal_btn_buscar))
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(principal_cb_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(principal_jtf_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(principal_btn_buscar)
                            .addComponent(jLabel4))
                        .addGap(10, 10, 10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(principal_jl_titulosumin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        this.dispose();
        System.exit(0);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        principal_jl_user.setText("Usuario: "+User.Username);
        principal_jl_id.setText("ID: "+User.ID);
        principal_jl_rank.setText("Rank: "+User.Rank+" | "+User.rankName);
        principal_jl_time.setText(dtf.format(now));
        principal_jl_totalUsers.setText("Total de usuarios: "+User.totalUsers);
        update_suministrosTable("","");
        updateEmpresaName();
        principal_jtf_numOrden.setText(""+(maxOrdenValue+1));
        Instant instant = now.toInstant(ZoneOffset.UTC);
        principal_dateCho_fecha.setDate(Date.from(instant));
        principal_dateCho_fecha.setDateFormatString("yyy-MM-dd");
    }//GEN-LAST:event_formWindowOpened

    private void principal_jtf_tamboresCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_principal_jtf_tamboresCaretUpdate
        
    }//GEN-LAST:event_principal_jtf_tamboresCaretUpdate

    private void principal_btn_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_principal_btn_addActionPerformed
        if(!adminRange.contains(User.Rank)){
            JOptionPane.showMessageDialog(null, "No tienes permiso para realizar esta accion","Informacion",JOptionPane.OK_OPTION);
        }
        else{
            Map<String,Integer> empresaResultPos = empresaList.entrySet().stream()
                .filter(map->principal_cb_ListaEmpresas.getSelectedItem().equals(map.getValue()))
                .collect(Collectors.toMap(map->map.getValue(),map->map.getKey()));
            int empresaPos = empresaResultPos.get(principal_cb_ListaEmpresas.getSelectedItem());
            try{
                pst = con.conn.prepareStatement("INSERT INTO `suministros`(`ID_empre`, `fecha_suministro`, `numOrden`, `ID_producto`, `destinoList`, `numTambores`) VALUES (?,?,?,?,?,?)");
                Date date = principal_dateCho_fecha.getDate();
                System.out.println("time: "+date.getTime());
                //java.sql.Date dateSql = new java.sql.Date(date.getDay(),date.getMonth(),date.getYear());
                pst.setInt(1, empresaPos );
                //System.out.println("FECHA: "+dateSql.toString());
                pst.setDate(2, new java.sql.Date(date.getTime()));
                pst.setInt(3, Integer.valueOf(principal_jtf_numOrden.getText()) );
                pst.setInt(4, productoList.get(principal_cb_producto.getSelectedItem().toString()));
                pst.setString(5, principal_jtf_destino.getText() );
                pst.setInt(6, Integer.valueOf(principal_jtf_tambores.getText()) );
                
                int row = pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Suministro añadido correctamente","Agregar suministro",JOptionPane.OK_OPTION);
            }catch(Exception ex){
                JOptionPane.showMessageDialog(null, "Ocurrio un problema al tratar de agregar un suministro","Error - agregar suministro",JOptionPane.ERROR_MESSAGE);
                System.out.println(ex.getMessage());
            }
            finally{
                update_suministrosTable("","");
                limpiarCampos();
            }
        }
    }//GEN-LAST:event_principal_btn_addActionPerformed

    private void principal_btn_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_principal_btn_editActionPerformed
        if(!adminRange.contains(User.Rank)){
            JOptionPane.showMessageDialog(null, "No tienes permiso para realizar esta accion","Informacion",JOptionPane.OK_OPTION);
        }
        else{
            try{
                pst = con.conn.prepareStatement("UPDATE `suministros` SET `ID_empre`=?,`fecha_suministro`=?,`numOrden`=?,`ID_producto`=?,`destinoList`=?,`numTambores`=? WHERE `ID_suministro`=?");
                Map<String,Integer> empresaResultPos = empresaList.entrySet().stream()
                .filter(map->principal_cb_ListaEmpresas.getSelectedItem().equals(map.getValue()))
                .collect(Collectors.toMap(map->map.getValue(),map->map.getKey()));
                
                int empresaId = empresaResultPos.get(principal_cb_ListaEmpresas.getSelectedItem());
                
                System.out.println("empresaId: "+empresaId);
                pst.setInt(1, empresaId);
                
                Date date = principal_dateCho_fecha.getDate();
                System.out.println("fechaaaaaa: "+principal_dateCho_fecha.getDate().toString());
                
                java.sql.Date dateSql = new java.sql.Date(date.getYear(),date.getMonth(),date.getDay());
                System.out.println("fechaaaaaa 2: "+dateSql.toString());
                pst.setDate(2, dateSql);
                System.out.println("Fecha: "+dateSql);
                
                pst.setInt(3, Integer.valueOf(principal_jtf_numOrden.getText()) );
                System.out.println("num orden: "+principal_jtf_numOrden.getText());
                
                pst.setInt(4, productoList.get(principal_cb_producto.getSelectedItem().toString()));
                System.out.println("Producto: "+principal_cb_producto.getSelectedItem().toString());
                
                pst.setString(5, principal_jtf_destino.getText() );
                System.out.println("Destino: "+principal_jtf_destino.getText());
                
                pst.setInt(6,Integer.valueOf(principal_jtf_tambores.getText()));
                System.out.println("Tambores: "+Integer.valueOf(principal_jtf_tambores.getText()));
                
                pst.setInt(7,Integer.valueOf(principal_jtf_id.getText()));
                System.out.println("id: "+Integer.valueOf(principal_jtf_id.getText()));
                
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Suministro editado correctamente","Editar suministro",JOptionPane.OK_OPTION);
            }catch(Exception ex){
                System.out.println("Error jButton3ActionPerformed: "+ex.getMessage());
                JOptionPane.showMessageDialog(this, "Ocurrio un error al tratar de editar este suministro","Editar suministro",JOptionPane.ERROR_MESSAGE);
            }finally{
                update_suministrosTable("","");
                limpiarCampos();
            }
        }
    }//GEN-LAST:event_principal_btn_editActionPerformed
    public void limpiarCampos(){
        principal_jtf_numOrden.setText(""+(maxOrdenValue+1));
        principal_cb_producto.setSelectedIndex(0);
        principal_cb_destino.setSelectedIndex(0);
        principal_jtf_tambores.setText("0");
        principal_dateCho_fecha.setDate(java.util.Date.from(now.atZone(ZoneId.systemDefault()).toInstant()));
        principal_dateCho_fecha.cleanup();
        principal_jtf_destino.setText("");
        principal_cb_destino.setSelectedIndex(0);
        principal_jtf_id.setText("0");
        principal_cb_buscar.setSelectedIndex(0);
        principal_jtf_search.setText("");
    }
    private void principal_btn_limpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_principal_btn_limpiarActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_principal_btn_limpiarActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        if(!adminRange.contains(User.Rank)){
            JOptionPane.showMessageDialog(null, "No tienes permiso para realizar esta accion","Informacion",JOptionPane.OK_OPTION);
        }
        else{
            Usuarios usuarios = new Usuarios();
            usuarios.start();
        }
        
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
        principal_jtf_numOrden.setEnabled(evt.getStateChange()==1 ? true:false);
    }//GEN-LAST:event_jCheckBox1ItemStateChanged

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        con.desconectar("Principal");
    }//GEN-LAST:event_formWindowClosed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        update_suministrosTable("","");
        updateEmpresaName();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void principal_cb_ListaEmpresasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_principal_cb_ListaEmpresasItemStateChanged

    }//GEN-LAST:event_principal_cb_ListaEmpresasItemStateChanged

    private void principal_cb_ListaEmpresasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_principal_cb_ListaEmpresasFocusLost

    }//GEN-LAST:event_principal_cb_ListaEmpresasFocusLost

    private void principal_cb_ListaEmpresasMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_principal_cb_ListaEmpresasMouseExited

             
    }//GEN-LAST:event_principal_cb_ListaEmpresasMouseExited

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        Empresas empresas = new Empresas(this);
        empresas.start();
        this.dispose();
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jCheckBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox2ItemStateChanged
        principal_jtf_destino.setEnabled(evt.getStateChange()==1 ? true:false);
    }//GEN-LAST:event_jCheckBox2ItemStateChanged

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jButton5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseClicked
        String line = principal_jtf_destino.getText();
        String selectedI = principal_cb_destino.getSelectedItem().toString();
        if(principal_jtf_destino.getText().isEmpty()){
            principal_jtf_destino.setText(line+selectedI);
        }
        else{
            principal_jtf_destino.setText(line+" / "+selectedI); 
        }
    }//GEN-LAST:event_jButton5MouseClicked

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        Destinos destinos = new Destinos(this);
        destinos.start();
        this.dispose();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void principal_btn_delMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_principal_btn_delMouseClicked
        if(!adminRange.contains(User.Rank)){
            JOptionPane.showMessageDialog(null, "No tienes permiso para realizar esta accion","Informacion",JOptionPane.OK_OPTION);
        }
        else{
            if(principal_jtf_id.getText().isEmpty() || principal_jtf_id.getText()=="0"){
                JOptionPane.showMessageDialog(this, "Porfavor escriba o seleccione la id del suministro a eliminar","Eliminar suministro",JOptionPane.OK_OPTION);
            }else{
                try{
                    pst = con.conn.prepareStatement("SELECT * FROM `suministros` WHERE `ID_suministro`=?");
                    pst.setString(1, principal_jtf_id.getText());
                    ResultSet rs = pst.executeQuery();
                    rs.next();
                    int pos = rs.getInt("ID_suministro");
                    rs.close();
                    if(pos <=0){
                        JOptionPane.showMessageDialog(this, "Este suministro no existe","Eliminar suministro",JOptionPane.OK_OPTION);
                    }else{
                        pst = con.conn.prepareStatement("DELETE FROM `suministros` WHERE `ID_suministro`=?");
                        pst.setString(1, principal_jtf_id.getText());
                        pst.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Suministro eliminado correctamente","Eliminar suministro",JOptionPane.OK_OPTION);
                    }

                }catch(SQLException ex){
                    System.out.println("ERROR ELIMINAR SUMINISTRO: "+ex.getMessage());
                    JOptionPane.showMessageDialog(this, "Ocurrio un error al tratar de eliminar el suministro","Eliminar suministro",JOptionPane.ERROR_MESSAGE);
                }finally{
                    update_suministrosTable("","");
                    limpiarCampos();
                }

            }

        }

    }//GEN-LAST:event_principal_btn_delMouseClicked

    private void principal_btn_delActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_principal_btn_delActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_principal_btn_delActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        Productos productos = new Productos();
        productos.start();
        this.dispose();
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void principal_tbl_suministrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_principal_tbl_suministrosMouseClicked
        //={"ID","Fecha","Num. orden","Producto","Destinos","Tambores"};
        DefaultTableModel model = (DefaultTableModel)principal_tbl_suministros.getModel();
        int myIndex = principal_tbl_suministros.getSelectedRow();
        
        System.out.println("ID: "+model.getValueAt(myIndex, 0).toString());
        System.out.println("FECHA: "+model.getValueAt(myIndex, 1).toString());
        
        try{
            principal_jtf_id.setText(model.getValueAt(myIndex, 0).toString());
        }catch(Exception ex){
            System.out.println("error al dar click en registro de tabla");
        }
        
        String dateString = model.getValueAt(myIndex, 1).toString();
        SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = f1.parse(dateString);
        } catch (ParseException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        principal_dateCho_fecha.setDate(date);
        principal_jtf_numOrden.setText(model.getValueAt(myIndex, 2).toString());
        principal_cb_producto.setSelectedItem(model.getValueAt(myIndex, 3).toString());
        principal_jtf_destino.setText(model.getValueAt(myIndex, 4).toString());
        principal_jtf_tambores.setText(model.getValueAt(myIndex, 5).toString());
    }//GEN-LAST:event_principal_tbl_suministrosMouseClicked

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        JOptionPane.showMessageDialog(this, "El numero de orden estara por defecto en el valor maximo de orden que ya exista en los suministros + 1, con el motivo de mantener el conteo correcto del incremento de ordenes","Numero de orden",JOptionPane.INFORMATION_MESSAGE); 
    }//GEN-LAST:event_jLabel7MouseClicked

    private void principal_btn_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_principal_btn_buscarActionPerformed
        String searchKey = principal_cb_buscar.getSelectedItem().toString();
        String searchValue = "";
        if(searchKey=="producto"){
            //productoList.get(principal_cb_producto.getSelectedItem().toString()
            searchKey = "nombreProd";
            searchValue = principal_jtf_search.getText();
        }else{
            searchValue = principal_jtf_search.getText();
        }
        System.out.println("search: "+searchKey+" : "+searchValue);
        update_suministrosTable(searchKey,searchValue);
    }//GEN-LAST:event_principal_btn_buscarActionPerformed

    private void principal_cb_buscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_principal_cb_buscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_principal_cb_buscarActionPerformed

    private void principal_jtf_searchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_principal_jtf_searchKeyReleased
        String searchKey = principal_cb_buscar.getSelectedItem().toString();
        Statement st_cont;
        String join1 = " JOIN empresas as e ON s.ID_empre=e.ID_empresa";
        String join2 =" JOIN productos as p ON s.ID_producto=p.ID_producto";
        String searchValue = principal_jtf_search.getText();
        if(searchKey=="producto"){
            //productoList.get(principal_cb_producto.getSelectedItem().toString()
            searchKey = "nombreProd";
        }
        if(searchKey=="fecha"){
            searchKey = "fecha_suministro";
        }
        
        int valor = 0;
        int cont = 0;
        String aux = "" + principal_jtf_search.getText();//aqui obtenemos cada letra que ingresemos en el textfield en tiempo real

        
        Map<String,Integer> empresaResultPos = empresaList.entrySet().stream()
                    .filter(map->principal_cb_ListaEmpresas.getSelectedItem().equals(map.getValue()))
                    .collect(Collectors.toMap(map->map.getValue(),map->map.getKey()));
        int empresaPos = empresaResultPos.get(principal_cb_ListaEmpresas.getSelectedItem());
        
        
        try {
            
            System.out.println("search key keyreleased: "+searchKey);
            pst = con.getConnection().prepareStatement("SELECT COUNT(1) as total FROM suministros as s "+ join2+" WHERE ID_empre="+empresaPos+" AND "+searchKey+" LIKE ?"); //hacemos lo mismo que con el metodo mostrar, buscamos el numero de filas dela tabla
            if(searchKey == "nombreProd" || searchKey == "fecha_suministro"){
                pst.setString(1, searchValue+"%");
            }
            else if(searchKey == "ID_suministro" || searchKey == "numOrden"){
                pst.setInt(1, Integer.valueOf(searchValue));
            }

            
            ResultSet res = pst.executeQuery();//solo que esta ves usamos like
            if (res.next()) {// like nos ayudara a buscar nombres que tengan similitudes con lo que estamos escribiendo en el texfield
                valor = res.getInt(1); //una vez que obtenimos el numero de filas continuamos a sacar  el valor que buscamos
            }
            System.out.println("valor: "+valor);
        }catch(Exception ex){
            System.out.println("ERROR AAAA 1:"+ex);
        }
        try{
            
            dtPer = new String[valor][6];
            st_cont = con.getConnection().createStatement();
            ResultSet res = st_cont.executeQuery("SELECT ID_suministro, fecha_suministro, numOrden, nombreEmpresa, ID_empresa, nombreProd, destinoList, numTambores FROM suministros as s "+join1+join2+" WHERE ID_empresa="+empresaPos+" AND "+ searchKey+" LIKE '" + principal_jtf_search.getText() + "%' ORDER BY ID_suministro ASC"); //aqui es donde buscaremos a a la persona en especifico o las personas
            while (res.next()) {
                String idSumin = res.getString("ID_suministro");
                String fecha = res.getString("fecha_suministro");
                String numOrden = res.getString("numOrden");
                String nombreProducto = res.getString("nombreProd");
                String nombreDestino = res.getString("destinoList");
                //String nombreEmpresa = res.getString("nombreEmpresa");
                String numTambores = res.getString("numTambores");

                dtPer[cont][0] = idSumin;
                dtPer[cont][1] = fecha;
                dtPer[cont][2] = numOrden;
                dtPer[cont][3] = nombreProducto;
                dtPer[cont][4] = nombreDestino;
                dtPer[cont][5] = numTambores;
                cont++;
            }
            DefaultTableModel dtm_datos = new DefaultTableModel(dtPer, columnNames) {
                public boolean isCellEditable(int row, int column) {//este metodo es muy util si no quieren que editen su tabla, 
                return false;  //si quieren modificar los campos al dar clic entonces borren este metodo
                }
            };
            principal_tbl_suministros.setModel(dtm_datos);
            //trs = new TableRowSorter<>(dtm_datos);
            //jtable_datos.setRowSorter(trs);

        } catch (Exception ex) {
            System.out.println("ERROR AAAA 2:"+ex);
        }
    }//GEN-LAST:event_principal_jtf_searchKeyReleased

    private void principal_cb_ListaEmpresasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_principal_cb_ListaEmpresasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_principal_cb_ListaEmpresasActionPerformed

    private void principal_cb_destinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_principal_cb_destinoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_principal_cb_destinoActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed

        String reportDir;
        JasperDesign jdesign;
        try {
            
            try{
                //DIRECTORIO PARA COMPILAR
                reportDir = ".\\recursos\\reports\\reporteSuminEmpresas.jrxml";
                jdesign = JRXmlLoader.load(reportDir);
            }catch(Exception ex){
                //DIRECTORIO PARA EJECUTAR EN NETBEANS
                reportDir = ".\\src\\recursos\\reports\\reporteSuminEmpresas.jrxml";
                jdesign = JRXmlLoader.load(reportDir);
            }

            String query = "SELECT fecha_suministro, numOrden, nombreEmpresa, nombreProd, destinoList, numTambores FROM suministros as s JOIN empresas as e ON s.ID_empre=e.ID_empresa JOIN productos as p ON s.ID_producto=p.ID_producto ORDER BY s.ID_empre ASC";
            
            JRDesignQuery updateQuery = new JRDesignQuery();
            updateQuery.setText(query);
            jdesign.setQuery(updateQuery);
            
            JasperReport jreport = JasperCompileManager.compileReport(jdesign);
            
 
            JasperPrint jprint = JasperFillManager.fillReport(jreport,null,con.getConnection());
            
            JasperViewer.viewReport(jprint,false);
        
        } catch (JRException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        Map<String,Integer> empresaResultPos = empresaList.entrySet().stream()
                    .filter(map->principal_cb_ListaEmpresas.getSelectedItem().equals(map.getValue()))
                    .collect(Collectors.toMap(map->map.getValue(),map->map.getKey()));
        int empresaPos = empresaResultPos.get(principal_cb_ListaEmpresas.getSelectedItem());
        String reportDir;
        JasperDesign jdesign;
        try {
            
            try{
                //DIRECTORIO PARA COMPILAR
                reportDir = ".\\recursos\\reports\\reporteSuminPerEmpre.jrxml";
                jdesign = JRXmlLoader.load(reportDir);
            }catch(Exception ex){
                //DIRECTORIO PARA EJECUTAR EN NETBEANS
                reportDir = ".\\src\\recursos\\reports\\reporteSuminPerEmpre.jrxml";
                jdesign = JRXmlLoader.load(reportDir);
            }

            String query = "SELECT fecha_suministro, numOrden, nombreProd, destinoList, numTambores FROM suministros as s JOIN empresas as e ON s.ID_empre=e.ID_empresa JOIN productos as p ON s.ID_producto=p.ID_producto WHERE s.ID_empre="+empresaPos+" ORDER BY s.fecha_suministro ASC";
            
            JRDesignQuery updateQuery = new JRDesignQuery();
            updateQuery.setText(query);
            jdesign.setQuery(updateQuery);
            
            JasperReport jreport = JasperCompileManager.compileReport(jdesign);
            
            Map<String,Object> para = new HashMap<String,Object>();
            para.put("NOMBRE_EMPRESA","REPORTE DE "+principal_cb_ListaEmpresas.getSelectedItem().toString());
            
            JasperPrint jprint = JasperFillManager.fillReport(jreport,para,con.getConnection());
            
            JasperViewer.viewReport(jprint,false);
        
        } catch (JRException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void principal_btn_showSuminInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_principal_btn_showSuminInfoActionPerformed
        showSuminData();
    }//GEN-LAST:event_principal_btn_showSuminInfoActionPerformed

    /**
     * @param args the command line arguments
     */
    public void start(){
         /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
            }
        });
    }
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
            }
        });
    }
    public void clearTable(){
        ((DefaultTableModel)principal_tbl_suministros.getModel()).setRowCount(0);
    }
    public void update_suministrosTable(String key,String value){
        clearTable();
        
        if (key=="ID_key" || key==""){
            dtPer = getSuministrosDatos(true,"","");
        }else{
            dtPer = getSuministrosDatos(false,key,value);
        }
        
        DefaultTableModel datos = new DefaultTableModel(dtPer,columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
            return false;
        }
        };
        try{
            principal_tbl_suministros.setModel(datos);
            final TableColumnModel columnModel = principal_tbl_suministros.getColumnModel();
            for (int column = 0; column < principal_tbl_suministros.getColumnCount(); column++) {
                int width = 15; // Min width
                for (int row = 0; row < principal_tbl_suministros.getRowCount(); row++) {
                    TableCellRenderer renderer = principal_tbl_suministros.getCellRenderer(row, column);
                    Component comp = principal_tbl_suministros.prepareRenderer(renderer, row, column);
                    width = Math.max(comp.getPreferredSize().width +1 , width);
                }
                if(width > 300)
                    width=300;
                columnModel.getColumn(column).setPreferredWidth(width);
            }
        }catch(Exception ex){
            System.out.println("Error update_suministrosTable: "+ex.getMessage());
        }
        
    }
    
    public void showSuminData(){
        JFrame frameInfo = new JFrame();
        JPanel pane = new JPanel();
        
        JLabel label1 = new JLabel("** INFORMACIÓN DE ORDENES Y TAMBORES **");
        label1.setForeground(Color.yellow);
        JLabel label12 = new JLabel("d");
        JLabel label13 = new JLabel("d");
        label12.setForeground(Color.black);
        label13.setForeground(Color.black);
        JLabel label2 = new JLabel("----------Información de empresa seleccionada----------");
        label2.setForeground(Color.white);
        JLabel label3 = new JLabel("-> Total Ordenes: " + totalSumDes);
        label3.setBackground(Color.white);
        label3.setForeground(Color.red);
        
        JLabel label4 = new JLabel("-> Total Tambores: " + sumTambores);
        label4.setBackground(Color.white);
        label4.setForeground(Color.red);
        JLabel label5 = new JLabel("");
        JLabel label6 = new JLabel("------------------------Información General---------------------");
        label6.setForeground(Color.white);
        JLabel label7 = new JLabel("-> Total Ordenes: " + totalSumi);
        label7.setBackground(Color.white);
        label7.setForeground(Color.red);
        JLabel label8 = new JLabel("-> Total Tambores: " + totalsumTambor);
        label8.setBackground(Color.white);
        label8.setForeground(Color.red);
        JLabel label9 = new JLabel("-------------------------------------------------------------------------");
        label9.setForeground(Color.yellow);
        JLabel label10 = new JLabel("");
        JLabel label11 = new JLabel("Número de Orden más alto: " + maxOrden);
        label11.setForeground(Color.ORANGE);
        frameInfo.add(pane);
        pane.add(label1);
        pane.add(label12);
        pane.add(label13);
        pane.add(label2);
        pane.add(label3);
        pane.add(label4);
        pane.add(label5);
        pane.add(label6);
        pane.add(label7);
        pane.add(label8);
        pane.add(label9);
        pane.add(label10);
        pane.add(label11);

        pane.setBackground(Color.black);
        frameInfo.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pane.setLayout(new GridLayout(0,1));
        frameInfo.setSize(500,500);
        frameInfo.setTitle("ORDENES Y TAMBORES");
        frameInfo.pack();
        frameInfo.setResizable(false);
        frameInfo.setLocationRelativeTo(null);
        frameInfo.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("sum4.png")));
        frameInfo.setVisible(true);
    }
    
    public void updateSuminData(int _registros, int _sumTambores, int _totalRegistros, int _totalTambores, int _maxOrden){
        
        
        totalSumDes = ""+_registros;
        sumTambores = ""+_sumTambores;
        totalSumi = ""+_totalRegistros;
        totalsumTambor = ""+_totalTambores;
        maxOrden = ""+_maxOrden;   
        maxOrdenValue = _maxOrden;
    }
    public void updateEmpresaName(){
        principal_jl_titulosumin.setText("Suministros para \""+principal_cb_ListaEmpresas.getSelectedItem()+"\"");
        principal_jl_warninEmpresa.setText("EMPRESA MOSTRADA: "+principal_cb_ListaEmpresas.getSelectedItem());
     
    }
    public Object [][] getSuministrosDatos(boolean defaultOp, String a_keySearch, String a_keyValue){
        Map<String,Integer> empresaResultPos = empresaList.entrySet().stream()
                    .filter(map->principal_cb_ListaEmpresas.getSelectedItem().equals(map.getValue()))
                    .collect(Collectors.toMap(map->map.getValue(),map->map.getKey()));
        int empresaPos = empresaResultPos.get(principal_cb_ListaEmpresas.getSelectedItem());
        String searchAdd = "";
        String keyValue = "";
        String keySearch ="";
        int registros = 0;
        int sumTambores = 0;
        int totalRegistros = 0;
        int totalSumTambores = 0;
        int maxOrden = 0;
        System.out.println("Empresa n: "+principal_cb_ListaEmpresas.getSelectedItem().toString());
        System.out.println("Empresa id: "+empresaPos);
        if(defaultOp == true){
            keyValue = ""+empresaPos;
            
        }else{
            
            keyValue = a_keyValue;
            keySearch = a_keySearch;
            searchAdd+=" AND ";
            searchAdd+=keySearch; 
        }
        
        try{
            
            pst = con.conn.prepareStatement("SELECT count(1) as total, SUM(numTambores) as sumTambores FROM suministros WHERE ID_empre="+empresaPos);
            ResultSet res = pst.executeQuery();
            res.next();
            registros = res.getInt("total");
            sumTambores = res.getInt("sumTambores");
            res.close();
            pst = con.conn.prepareStatement("SELECT count(1) as total, SUM(numTambores) as sumTambores FROM suministros");
            res = pst.executeQuery();
            res.next();
            totalRegistros = res.getInt("total");
            totalSumTambores = res.getInt("sumTambores");
            res.close();
            pst = con.conn.prepareStatement("SELECT MAX(numOrden) as maxOrden FROM suministros");
            res = pst.executeQuery();
            res.next();
            maxOrden = res.getInt("maxOrden");
            res.close();
        }catch(SQLException ex){
            
            System.out.println("ERROR getSuministrosDatos 1(): "+ex);
        }
        updateSuminData(registros,sumTambores,totalRegistros,totalSumTambores, maxOrden);
        Object[][] data = new String[registros][6];
        
        try{
            String join1 = " JOIN empresas as e ON s.ID_empre=e.ID_empresa";
            String join2 =" JOIN productos as p ON s.ID_producto=p.ID_producto";
            System.out.println("add search: "+searchAdd);
            
            if(defaultOp==true){
                pst = con.conn.prepareStatement("SELECT"+" ID_suministro, fecha_suministro, numOrden, nombreEmpresa, ID_empresa, nombreProd, destinoList, numTambores"+" FROM suministros as s "+join1+join2+" WHERE ID_empresa="+empresaPos+" ORDER BY numOrden");
            }
            else{
                pst = con.conn.prepareStatement("SELECT"+" ID_suministro, fecha_suministro, numOrden, nombreEmpresa, ID_empresa, nombreProd, destinoList, numTambores"+" FROM suministros as s "+join1+join2+" WHERE ID_empresa="+empresaPos+searchAdd+"=? ORDER BY numOrden");
                if(keySearch=="nombreProd"){
                    pst.setString(1, keyValue);
                }else{
                    try{
                        pst.setInt(1,Integer.valueOf(keyValue));
                    }
                    catch(Exception ex){
                        System.out.println("Error num: "+ex.getMessage());
                    }
                    
                }
            }
            
            
            
            ResultSet res = pst.executeQuery();
            int i = 0;
            while(res.next()){
                String idSumin = res.getString("ID_suministro");
                String fecha = res.getString("fecha_suministro");
                String numOrden = res.getString("numOrden");
                String nombreProducto = res.getString("nombreProd");
                String nombreDestino = res.getString("destinoList");
                String numTambores = res.getString("numTambores");
                
                data[i][0] = idSumin;
                data[i][1] = fecha;
                data[i][2] = numOrden;
                data[i][3] = nombreProducto;
                data[i][4] = nombreDestino;
                data[i][5] = numTambores;
                i++;
            }
            res.close();
        }catch(SQLException ex){
            System.out.println("ERROR getSuministrosDatos 2(): "+ex);
        }
        return data;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton5;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JButton principal_btn_add;
    private javax.swing.JButton principal_btn_buscar;
    private javax.swing.JButton principal_btn_del;
    private javax.swing.JButton principal_btn_edit;
    private javax.swing.JButton principal_btn_limpiar;
    private javax.swing.JButton principal_btn_showSuminInfo;
    private javax.swing.JComboBox<String> principal_cb_ListaEmpresas;
    private javax.swing.JComboBox<String> principal_cb_buscar;
    private javax.swing.JComboBox<String> principal_cb_destino;
    private javax.swing.JComboBox<String> principal_cb_producto;
    private com.toedter.calendar.JDateChooser principal_dateCho_fecha;
    private javax.swing.JLabel principal_jl_id;
    private javax.swing.JLabel principal_jl_rank;
    private javax.swing.JLabel principal_jl_time;
    private javax.swing.JLabel principal_jl_titulosumin;
    private javax.swing.JLabel principal_jl_totalUsers;
    private javax.swing.JLabel principal_jl_user;
    private javax.swing.JLabel principal_jl_warninEmpresa;
    private javax.swing.JTextField principal_jtf_destino;
    private javax.swing.JTextField principal_jtf_id;
    private javax.swing.JTextField principal_jtf_numOrden;
    private javax.swing.JTextField principal_jtf_search;
    private javax.swing.JTextField principal_jtf_tambores;
    private javax.swing.JMenu principal_menu_procesos;
    private javax.swing.JTable principal_tbl_suministros;
    private com.qt.datapicker.TestDayPicker testDayPicker1;
    // End of variables declaration//GEN-END:variables
}
