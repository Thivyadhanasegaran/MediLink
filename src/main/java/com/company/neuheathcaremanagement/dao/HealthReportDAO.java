package com.company.neuheathcaremanagement.dao;

import java.util.Collections;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import com.company.neuheathcaremanagement.pojo.HealthReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Path;

@Repository
@Transactional
public class HealthReportDAO {

    private static final Logger logger = LoggerFactory.getLogger(HealthReportDAO.class);

    @PersistenceContext
    private EntityManager entityManager;

    public void save(HealthReport healthReport) {
        try {
            entityManager.persist(healthReport);
            logger.info("Saving health report: {}", healthReport);
            logger.info("Health report saved successfully.");
        } catch (Exception e) {
            logger.error("Error saving health report: {}", healthReport, e);
        }
    }

    public HealthReport findById(int recordId) {
        try {
            HealthReport healthReport = entityManager.find(HealthReport.class, recordId);
            logger.info("Found health report with ID {}: {}", recordId, healthReport);
            return healthReport;
        } catch (Exception e) {
            logger.error("Error finding health report by ID: {}", recordId, e);
            return null;
        }
    }

    public List<HealthReport> findByUserId(int userId) {
        try {
            String queryResult = "SELECT h FROM HealthReport h WHERE h.appointment.student.neuid = :userId";
            TypedQuery<HealthReport> query = entityManager.createQuery(queryResult, HealthReport.class);
            query.setParameter("userId", userId);
            List<HealthReport> reports = query.getResultList();
            logger.info("Found {} health reports for userId: {}", reports.size(), userId);
            return reports;
        } catch (Exception e) {
            logger.error("Error finding health reports for userId: {}", userId, e);
            return null;
        }
    } 
    
    public Page<HealthReport> findByUserIdPage(int userId, Pageable pageable) {
        try {
            String jpql = "SELECT hr FROM HealthReport hr WHERE hr.appointment.student.neuid = :userId";
            TypedQuery<HealthReport> query = entityManager.createQuery(jpql, HealthReport.class);
            query.setParameter("userId", userId);
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());

            List<HealthReport> results = query.getResultList();
            long count = getCountByUserId(userId);

            return new PageImpl<>(results, pageable, count);
        } catch (Exception e) {
            logger.error("Error finding health reports by userId: {}", userId, e);
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
    }
 
    private long getCountByUserId(int userId) {
        try {
            String jpql = "SELECT COUNT(hr) FROM HealthReport hr WHERE hr.appointment.student.neuid = :userId";
            TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
            query.setParameter("userId", userId);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.error("Error counting health reports for userId: {}", userId, e);
            return 0L;
        }
    }
    
//  private long getCountByUserId(int userId) {
//  CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//  CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
//  Root<HealthReport> root = criteriaQuery.from(HealthReport.class);
//  criteriaQuery.select(criteriaBuilder.count(root));
//  
//  Predicate userIdPredicate = criteriaBuilder.equal(root.get("appointment").get("student").get("neuid"), userId);
//  criteriaQuery.where(userIdPredicate);
//  return entityManager.createQuery(criteriaQuery).getSingleResult();
//}
}



