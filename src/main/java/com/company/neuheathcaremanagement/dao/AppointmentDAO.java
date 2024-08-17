package com.company.neuheathcaremanagement.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.company.neuheathcaremanagement.pojo.Appointment;
import com.company.neuheathcaremanagement.pojo.User;

@Repository
@Transactional
public class AppointmentDAO {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentDAO.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void save(Appointment appointment) {
        try {
            entityManager.persist(appointment);
            logger.info("Appointment saved successfully.");
        } catch (Exception e) {
            logger.error("Error saving appointment: {}", appointment, e);
        }
    }
    
    public Appointment findAppointmentByDoctorAndDateTime(int doctorId, LocalDate appointmentDate, LocalTime appointmentTime) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Appointment> criteriaQuery = criteriaBuilder.createQuery(Appointment.class);
            Root<Appointment> appointmentRoot = criteriaQuery.from(Appointment.class);

            Predicate doctorIdPredicate = criteriaBuilder.equal(appointmentRoot.get("doctor").get("id"), doctorId);
            Predicate appointmentDatePredicate = criteriaBuilder.equal(appointmentRoot.get("appointmentDate"), appointmentDate);
            Predicate appointmentTimePredicate = criteriaBuilder.equal(appointmentRoot.get("appointmentTime"), appointmentTime);

            criteriaQuery.where(doctorIdPredicate, appointmentDatePredicate, appointmentTimePredicate);

            TypedQuery<Appointment> query = entityManager.createQuery(criteriaQuery);
            Appointment appointment = query.getSingleResult();

            logger.info("Found appointment for doctorId: {}, appointmentDate: {}, appointmentTime: {}", doctorId, appointmentDate, appointmentTime);
            return appointment;
        } catch (NoResultException e) {
            logger.info("No appointment found for doctorId: {}, appointmentDate: {}, appointmentTime: {}", doctorId, appointmentDate, appointmentTime);
            return null;
        } catch (Exception e) {
            logger.error("Error finding appointment for doctorId: {}, appointmentDate: {}, appointmentTime: {}", doctorId, appointmentDate, appointmentTime, e);
            return null;
        }
    }


    public List<Appointment> findAppointmentsByDoctorId(int doctorId) {
        try {
            String jpql = "SELECT a FROM Appointment a WHERE a.doctor.neuid = :doctorId";
            TypedQuery<Appointment> query = entityManager.createQuery(jpql, Appointment.class);
            query.setParameter("doctorId", doctorId);     
            List<Appointment> appointments = query.getResultList();
            logger.info("Found {} appointments for doctorId: {}", appointments.size(), doctorId);
            return appointments;
        } catch (Exception e) {
            logger.error("Error finding appointments for doctorId: {}", doctorId, e);
            return Collections.emptyList();
        }
    }
    

    public Appointment findById(int appointmentId) {
        try {
            String jpql = "SELECT a FROM Appointment a WHERE a.appointmentId = :appointmentId";
            TypedQuery<Appointment> query = entityManager.createQuery(jpql, Appointment.class);
            query.setParameter("appointmentId", appointmentId);         
            Appointment appointment = query.getSingleResult();
            logger.info("Found appointment: {}", appointment);
            return appointment;
        } catch (Exception e) {
            logger.error("Error finding appointment by ID: {}", appointmentId, e);
            return null;
        }
    }

    public void update(Appointment appointment) {
        try {
            entityManager.merge(appointment);
            logger.info("Updating appointment: {}", appointment);
            logger.info("Appointment updated successfully.");
        } catch (Exception e) {
            logger.error("Error updating appointment: {}", appointment, e);
        }
    }

    public List<Appointment> findByStudent(User student) {
        try {
            String jpql = "SELECT a FROM Appointment a WHERE a.student = :student";
            TypedQuery<Appointment> query = entityManager.createQuery(jpql, Appointment.class);
            query.setParameter("student", student);         
            List<Appointment> appointments = query.getResultList();
            logger.info("Found {} appointments for student: {}", appointments.size(), student);
            return appointments;
        } catch (Exception e) {
            logger.error("Error finding appointments for student: {}", student, e);
            return Collections.emptyList();
        }
    }
 
}
