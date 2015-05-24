/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.enfasis3.beans;

import edu.enfasis3.entity.Actividad;
import edu.enfasis3.entity.ActividadJpaController;
import edu.enfasis3.entity.Comentario;
import edu.enfasis3.entity.ComentarioJpaController;
import edu.enfasis3.entity.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Frank
 */
@ManagedBean
@ViewScoped
public class ComentarioBean {

    private Comentario comentario;
    private List<Comentario> listaComentario;
    private Actividad actividad;
    private Integer seleccionActividad;
    
    
    public ComentarioBean() {
    }

    public Integer getSeleccionActividad() {
        return seleccionActividad;
    }

    public void setSeleccionActividad(Integer seleccionActividad) {
        this.seleccionActividad = seleccionActividad;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }
    
    public Comentario getComentario() {
        return comentario;
    }

    public void setComentario(Comentario comentario) {
        this.comentario = comentario;
    }

    public List<Comentario> getListaComentario() {
        
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        EntityManager em = emf.createEntityManager();
        
        Query q = em.createNamedQuery("Comentario.findByComentarioListaAc");
        q.setParameter("idactividad", seleccionActividad);
        
        listaComentario = q.getResultList();
        
        return listaComentario;
    }

    public void setListaComentario(List<Comentario> listaComentario) {
        this.listaComentario = listaComentario;
    }
    public void nuevoComentario(){
        comentario = new Comentario();
    }
    
    public void crearComentario () {
        
        GregorianCalendar fecha = new GregorianCalendar();
        Date d = fecha.getTime();
        
        comentario.setRegistrationdateComentario(d);
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        ComentarioJpaController cjc = new ComentarioJpaController(emf);
        
        ActividadJpaController ajc = new ActividadJpaController(emf);
        actividad = ajc.findActividad(seleccionActividad);
        
        try{
            comentario.setIdactividad(actividad);
            cjc.create(comentario);
        }catch(Exception e) {
            System.out.println(e);
        }
    }
    public void guardarComentario(){
        
        GregorianCalendar fecha = new GregorianCalendar();
        Date d = fecha.getTime();
        
        comentario.setRegistrationdateComentario(d);
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        ComentarioJpaController cjc = new ComentarioJpaController(emf);
        
        try{
            cjc.edit(comentario);
        }catch(Exception e) {
            System.out.println(e);
        }
    }
    public void eliminarComentario(){
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCRUMproyectoPU");
        ComentarioJpaController cjc = new ComentarioJpaController(emf);
        
        try{
            cjc.destroy(comentario.getIdcomentario());
        }catch(Exception e) {
            System.out.println(e);
        }
    }
   
}
