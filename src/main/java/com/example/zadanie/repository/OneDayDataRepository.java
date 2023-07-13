package com.example.zadanie.repository;

import com.example.zadanie.dto.ResponseData;
import com.example.zadanie.dto.ResponseNoteDTO;
import com.example.zadanie.models.Customer;
import com.example.zadanie.models.Note;
import com.example.zadanie.models.OneDayData;
import jakarta.persistence.NoResultException;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class OneDayDataRepository {
    private final SessionFactory sessionFactory;

    public void saveOneDayData(OneDayData data){
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.persist(data);

            session.getTransaction().commit();
        }
    }

    public void saveCustomer(Customer customer){
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.persist(customer);

            session.getTransaction().commit();
        }
    }

    public void saveNote(Note note) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.persist(note);

            session.getTransaction().commit();
        }
    }

    public Optional<Customer> getCustomer(String customerId){
        try (Session session = sessionFactory.openSession()) {


            session.beginTransaction();

            String hql = "SELECT d " +
                    "FROM Customer d " +
                    "WHERE d.id like :customerId ";

            Query<Customer> query = session.createQuery(hql, Customer.class);
            query.setParameter("customerId", customerId);

            Customer customer = query.uniqueResult();
            session.getTransaction().commit();

            return Optional.ofNullable(customer);
        } catch (NoResultException e){
            return Optional.empty();
        }
    }

    public List<OneDayData> getCustomerData(String customerId, int rows){
        List<OneDayData> results;

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            String hql = "SELECT d " +
                    "FROM OneDayData d " +
                    "WHERE d.customer.id like :customerId " +
                    "ORDER BY d.date desc";

            Query<OneDayData> query = session.createQuery(hql, OneDayData.class);
            query.setParameter("customerId", customerId);
            query.setMaxResults(rows);

            results = query.getResultList();

            session.getTransaction().commit();
        }

        return results;
    }

    public Optional<ResponseData> getNewestDataByCustomer(String customerId, Optional<LocalDate> date){

        String hql;
        Object[] result;

        if(date.isPresent()){
            hql = "SELECT d, c " +
                    "FROM OneDayData d " +
                    "JOIN d.customer c " +
                    "WHERE c.id = :customerId " +
                    "and d.date = :date " +
                    "ORDER BY d.date DESC";
        }
        else {
            hql = "SELECT d, c " +
                    "FROM OneDayData d " +
                    "JOIN d.customer c " +
                    "WHERE c.id = :customerId " +
                    "ORDER BY d.date DESC";
        }

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Query<Object[]> query = session.createQuery(hql);
            query.setParameter("customerId", customerId);
            date.ifPresent(localDate -> query.setParameter("date", localDate));
            query.setMaxResults(1);

            result = query.getSingleResult();

            session.getTransaction().commit();

            return Optional.of(new ResponseData((Customer) result[1], (OneDayData) result[0]));
        } catch (NoResultException e){
            return Optional.empty();
        }
    }

    public List<ResponseNoteDTO> getNotes(String customerId, LocalDate since, LocalDate until) {
        List<ResponseNoteDTO> results;

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            String hql = "SELECT n " +
                    "FROM Note n " +
                    "WHERE n.customer.id like :customerId " +
                    "and (n.date between :since and :until) " +
                    "ORDER BY n.date desc";

            Query<Note> query = session.createQuery(hql, Note.class);
            query.setParameter("customerId", customerId);
            query.setParameter("since", since);
            query.setParameter("until", until);

            results = query.getResultList().stream()
                    .map(ResponseNoteDTO::new)
                    .collect(Collectors.toList());

            session.getTransaction().commit();
        }

        return results;
    }
}
