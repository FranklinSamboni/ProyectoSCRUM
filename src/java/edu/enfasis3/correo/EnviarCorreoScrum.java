/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.enfasis3.correo;

/**
 *
 * @author Frank
 */

public class EnviarCorreoScrum {
    String destino;
    String contraseña;
    Correo_scrum c;
    
    public EnviarCorreoScrum(String destino, String contraseña) {
    
    this.destino=destino;
    this.contraseña=contraseña;
    }
    
    public void enviarCorreo(){//hey
        c=new Correo_scrum();//hey variable global
        c.setContrasenia("szhqbkzhrlzveeyo");
        c.setUsuarioCorreo("elric941@gmail.com");
        c.setAsunto("Correo de verificacion");
        c.setMensaje("Su contraseña para el ingreso es:"+contraseña); 
        c.setDestino(destino);
        
        c.setNombreArchivo("Verificacion.txt");
        c.setRutaArchivo("D:\\Universidad\\Enfasis 3\\ProyectoEnfasis\\SCRUMproyecto\\Verificacion.txt");
        
        ControladorScrum co=new ControladorScrum();
        if(co.enviarCorreo(c)){
        System.out.println("Envio correo");
        }else{ System.out.println("Error no lo envio");}
        
    }
    
     public void enviarCorreoRegistrado(){//hey
        c=new Correo_scrum();//hey variable global
        c.setContrasenia("szhqbkzhrlzveeyo");
        c.setUsuarioCorreo("elric941@gmail.com");
        c.setAsunto("Invitacion a proyecto SCRUM");
        c.setMensaje("Ha sido invitado a ser parte de un proyecto SCRUM, Ingrese a el siguiente link"
                + " para aceptar la invitación http://localhost:11318/SCRUMproyecto/faces/index.xhtml"); 
        c.setDestino(destino);
        
        c.setNombreArchivo("Verificacion.txt");
        c.setRutaArchivo("D:\\Universidad\\Enfasis 3\\ProyectoEnfasis\\SCRUMproyecto\\Verificacion.txt");
        
        ControladorScrum co=new ControladorScrum();
        if(co.enviarCorreo(c)){
        System.out.println("Envio correo");
        }else{ System.out.println("Error no lo envio");}
        
    }
      public void enviarCorreoNoRegistrado(){//hey
        c=new Correo_scrum();//hey variable global
        c.setContrasenia("szhqbkzhrlzveeyo");
        c.setUsuarioCorreo("elric941@gmail.com");
        c.setAsunto("Correo de verificacion");
        c.setMensaje("Ha sido invitado a ser parte de un proyecto SCRUM, Ingrese a el siguiente link"
                   + "para registrarse y aceptar la "
                   + "invitacion http://localhost:11318/SCRUMproyecto/faces/RegistrarUsuarios.xhtml");
        c.setDestino(destino);
        
        c.setNombreArchivo("Verificacion.txt");
        c.setRutaArchivo("D:\\Universidad\\Enfasis 3\\ProyectoEnfasis\\SCRUMproyecto\\Verificacion.txt");
        
        ControladorScrum co=new ControladorScrum();
        if(co.enviarCorreo(c)){
        System.out.println("Envio correo");
        }else{ System.out.println("Error no lo envio");}
        
    }

    public Correo_scrum getC() {
        return c;
    }

    public void setC(Correo_scrum c) {
        this.c = c;
    }
}