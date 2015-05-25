/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.enfasis3.entity;

import edu.enfasis3.entity.exceptions.IllegalOrphanException;
import edu.enfasis3.entity.exceptions.NonexistentEntityException;
import edu.enfasis3.entity.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Frank
 */
public class UserJpaController implements Serializable {

    public UserJpaController(EntityManagerFactory emf) {
        this.utx = null;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(User user) throws RollbackFailureException, Exception {
        if (user.getProyectoList() == null) {
            user.setProyectoList(new ArrayList<Proyecto>());
        }
        if (user.getParticipanteList() == null) {
            user.setParticipanteList(new ArrayList<Participante>());
        }
        EntityManager em = null;
        try {
          //  utx.begin();
            em = getEntityManager();
            em.getTransaction().begin();
            List<Proyecto> attachedProyectoList = new ArrayList<Proyecto>();
            for (Proyecto proyectoListProyectoToAttach : user.getProyectoList()) {
                proyectoListProyectoToAttach = em.getReference(proyectoListProyectoToAttach.getClass(), proyectoListProyectoToAttach.getIdproyecto());
                attachedProyectoList.add(proyectoListProyectoToAttach);
            }
            user.setProyectoList(attachedProyectoList);
            List<Participante> attachedParticipanteList = new ArrayList<Participante>();
            for (Participante participanteListParticipanteToAttach : user.getParticipanteList()) {
                participanteListParticipanteToAttach = em.getReference(participanteListParticipanteToAttach.getClass(), participanteListParticipanteToAttach.getIdparticipante());
                attachedParticipanteList.add(participanteListParticipanteToAttach);
            }
            user.setParticipanteList(attachedParticipanteList);
            em.persist(user);
            for (Proyecto proyectoListProyecto : user.getProyectoList()) {
                User oldManagerOfProyectoListProyecto = proyectoListProyecto.getManager();
                proyectoListProyecto.setManager(user);
                proyectoListProyecto = em.merge(proyectoListProyecto);
                if (oldManagerOfProyectoListProyecto != null) {
                    oldManagerOfProyectoListProyecto.getProyectoList().remove(proyectoListProyecto);
                    oldManagerOfProyectoListProyecto = em.merge(oldManagerOfProyectoListProyecto);
                }
            }
            for (Participante participanteListParticipante : user.getParticipanteList()) {
                User oldIduserOfParticipanteListParticipante = participanteListParticipante.getIduser();
                participanteListParticipante.setIduser(user);
                participanteListParticipante = em.merge(participanteListParticipante);
                if (oldIduserOfParticipanteListParticipante != null) {
                    oldIduserOfParticipanteListParticipante.getParticipanteList().remove(participanteListParticipante);
                    oldIduserOfParticipanteListParticipante = em.merge(oldIduserOfParticipanteListParticipante);
                }
            }
           // utx.commit();
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
             //   utx.rollback();
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(User user) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
           // utx.begin();
            em = getEntityManager();
            em.getTransaction().begin();
            User persistentUser = em.find(User.class, user.getIduser());
            List<Proyecto> proyectoListOld = persistentUser.getProyectoList();
            List<Proyecto> proyectoListNew = user.getProyectoList();
            List<Participante> participanteListOld = persistentUser.getParticipanteList();
            List<Participante> participanteListNew = user.getParticipanteList();
            List<String> illegalOrphanMessages = null;
            for (Proyecto proyectoListOldProyecto : proyectoListOld) {
                if (!proyectoListNew.contains(proyectoListOldProyecto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Proyecto " + proyectoListOldProyecto + " since its manager field is not nullable.");
                }
            }
            for (Participante participanteListOldParticipante : participanteListOld) {
                if (!participanteListNew.contains(participanteListOldParticipante)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Participante " + participanteListOldParticipante + " since its iduser field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Proyecto> attachedProyectoListNew = new ArrayList<Proyecto>();
            for (Proyecto proyectoListNewProyectoToAttach : proyectoListNew) {
                proyectoListNewProyectoToAttach = em.getReference(proyectoListNewProyectoToAttach.getClass(), proyectoListNewProyectoToAttach.getIdproyecto());
                attachedProyectoListNew.add(proyectoListNewProyectoToAttach);
            }
            proyectoListNew = attachedProyectoListNew;
            user.setProyectoList(proyectoListNew);
            List<Participante> attachedParticipanteListNew = new ArrayList<Participante>();
            for (Participante participanteListNewParticipanteToAttach : participanteListNew) {
                participanteListNewParticipanteToAttach = em.getReference(participanteListNewParticipanteToAttach.getClass(), participanteListNewParticipanteToAttach.getIdparticipante());
                attachedParticipanteListNew.add(participanteListNewParticipanteToAttach);
            }
            participanteListNew = attachedParticipanteListNew;
            user.setParticipanteList(participanteListNew);
            user = em.merge(user);
            for (Proyecto proyectoListNewProyecto : proyectoListNew) {
                if (!proyectoListOld.contains(proyectoListNewProyecto)) {
                    User oldManagerOfProyectoListNewProyecto = proyectoListNewProyecto.getManager();
                    proyectoListNewProyecto.setManager(user);
                    proyectoListNewProyecto = em.merge(proyectoListNewProyecto);
                    if (oldManagerOfProyectoListNewProyecto != null && !oldManagerOfProyectoListNewProyecto.equals(user)) {
                        oldManagerOfProyectoListNewProyecto.getProyectoList().remove(proyectoListNewProyecto);
                        oldManagerOfProyectoListNewProyecto = em.merge(oldManagerOfProyectoListNewProyecto);
                    }
                }
            }
            for (Participante participanteListNewParticipante : participanteListNew) {
                if (!participanteListOld.contains(participanteListNewParticipante)) {
                    User oldIduserOfParticipanteListNewParticipante = participanteListNewParticipante.getIduser();
                    participanteListNewParticipante.setIduser(user);
                    participanteListNewParticipante = em.merge(participanteListNewParticipante);
                    if (oldIduserOfParticipanteListNewParticipante != null && !oldIduserOfParticipanteListNewParticipante.equals(user)) {
                        oldIduserOfParticipanteListNewParticipante.getParticipanteList().remove(participanteListNewParticipante);
                        oldIduserOfParticipanteListNewParticipante = em.merge(oldIduserOfParticipanteListNewParticipante);
                    }
                }
            }
           // utx.commit();
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
              //  utx.rollback();
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = user.getIduser();
                if (findUser(id) == null) {
                    throw new NonexistentEntityException("The user with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            //utx.begin();
            em = getEntityManager();
           em.getTransaction().begin();
            User user;
            try {
                user = em.getReference(User.class, id);
                user.getIduser();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The user with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Proyecto> proyectoListOrphanCheck = user.getProyectoList();
            for (Proyecto proyectoListOrphanCheckProyecto : proyectoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Proyecto " + proyectoListOrphanCheckProyecto + " in its proyectoList field has a non-nullable manager field.");
            }
            List<Participante> participanteListOrphanCheck = user.getParticipanteList();
            for (Participante participanteListOrphanCheckParticipante : participanteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This User (" + user + ") cannot be destroyed since the Participante " + participanteListOrphanCheckParticipante + " in its participanteList field has a non-nullable iduser field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(user);
         //   utx.commit();
            em.getTransaction().commit();
        } catch (Exception ex) {
            try {
              //  utx.rollback();
                em.getTransaction().rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<User> findUserEntities() {
        return findUserEntities(true, -1, -1);
    }

    public List<User> findUserEntities(int maxResults, int firstResult) {
        return findUserEntities(false, maxResults, firstResult);
    }

    private List<User> findUserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(User.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public User findUser(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<User> rt = cq.from(User.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
