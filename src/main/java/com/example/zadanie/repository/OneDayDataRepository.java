package com.example.zadanie.repository;

import com.example.zadanie.models.OneDayData;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class OneDayDataRepository {
    private final SessionFactory sessionFactory;

    public void save(OneDayData data){
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.persist(data);

            session.getTransaction().commit();
        }
    }

    public List<OneDayData> getCustomerData(String customerId, int rows){
        List<OneDayData> results;

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            String hql = "SELECT d " +
                    "FROM OneDayData d " +
                    "WHERE d.customerId like :customerId " +
                    "ORDER BY d.date desc";

            Query<OneDayData> query = session.createQuery(hql, OneDayData.class);
            query.setParameter("customerId", customerId);
            query.setMaxResults(rows);

            results = query.getResultList();

            session.getTransaction().commit();
        }

        return results;
    }
}
