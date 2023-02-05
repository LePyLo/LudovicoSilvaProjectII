package proyectoludo;
import java.sql.*;
import javax.swing.JOptionPane;
/**
 *
 * @author ricardo
 */
public class SQLConn {
    static String db_Name ="suministrosdb";
    static String db_Username = "ricfranren"; //Tanto el nombre como contraseña no deberian estar en el codigo pero por facilidad se quedan
    static String db_Password = "ludovico2022";
    static String db_Url = "jdbc:mysql://localhost/"+db_Name; //Cambiar en caso de querer usar un servidor en la nube
    
    Connection conn = null;
    
    public SQLConn(){
        try{
           Class.forName("com.mysql.jdbc.Driver");
           conn = DriverManager.getConnection(db_Url,db_Username,db_Password);
           System.out.println("Exito al conectarse a la base de datos: "+db_Name);
        }catch(SQLException | ClassNotFoundException e){
            JOptionPane.showMessageDialog(null, "ERROR","ERROR AL TRATAR DE CONECTAR A LA BASE DE DATOS",JOptionPane.ERROR);
            System.out.println(e);
        }
    }
    
    public Connection getConnection(){
        return conn;
    }
    public void desconectar(String origin){
        conn = null;
        System.out.println("Saliendo de la base de datos: "+db_Name+" desde "+origin);
    }
}
