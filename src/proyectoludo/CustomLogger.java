/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoludo;

import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime; 

/**
 *
 * @author ricardo
 */
public class CustomLogger {
    private static File file  = new File("historial.log");
    private static CustomLogger single_instance = null;
    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");  
    
    CustomLogger(){
        System.out.println("Custom logger iniciado.");
    }
    
    public static CustomLogger getInstance(){
        if (single_instance == null){
            single_instance = new CustomLogger();
        }
        return single_instance;
    }
    
    public static void read(){
        try{
            if(!file.exists()){
                create();
            }
            FileReader reader = new FileReader(file);
            BufferedReader buffered = new BufferedReader(reader);
            String line = "";
            while((line=buffered.readLine()) != null){
                System.out.println(line);
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    public static String getText(){
        String allHistorial="";
        try{
            if(!file.exists()){
                create();
            }
            FileReader reader = new FileReader(file);
            
            BufferedReader buffered = new BufferedReader(reader);
            String line = "";
            while((line=buffered.readLine()) != null){
                allHistorial+=line+"\n";
            }
            reader.close();
            
        }catch(IOException e){
            e.printStackTrace();
        }
        return allHistorial;
    }
    public static void create(){
        try{
            if(file.createNewFile()){
                System.out.println("Archivo historal.log creado.");
            }else{
                System.out.println("Archivo historial ya existe.");
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public static void register(String info){
        FileWriter writer = null;
        BufferedWriter bw = null;
        PrintWriter pw = null;
        LocalDateTime now = LocalDateTime.now(); 
        String line = null;
      
        try{
            if(!file.exists()){
                create();
            }
            writer = new FileWriter(file,true);
            bw = new BufferedWriter(writer);
            pw = new PrintWriter(bw);
            line = ""+dtf.format(now)+" - "+info;
            pw.println(line);
            pw.flush();
            
        }
        catch(IOException e){
            e.printStackTrace();
        }finally{
            try{
                writer.close();
                bw.close();  
                pw.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    
}
