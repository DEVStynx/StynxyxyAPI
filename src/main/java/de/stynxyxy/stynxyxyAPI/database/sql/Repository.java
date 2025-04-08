package de.stynxyxy.stynxyxyAPI.database.sql;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class Repository<ENTITY,ID> {
    public SessionFactory sessionfactory;
    public Class<ENTITY> entity;
    public Class<ID> id;

    public Repository(SessionFactory sessionfactory, Class<ENTITY> entity, Class<ID> id) {
        this.sessionfactory = sessionfactory;
        this.entity = entity;
        this.id = id;
    }

    public void save(ENTITY entity) {
        CompletableFuture.runAsync(() -> {
            try (Session sqlsession = sessionfactory.openSession()) {
                Transaction transaction = sqlsession.beginTransaction();
                sqlsession.merge(entity); // Speichert oder updated
                transaction.commit();
            } catch (Exception e) {
                e.printStackTrace(); // oder logge es mit BaseAPI.APIlogger
            }
        });
    }
    public CompletableFuture<Optional<ENTITY>> findBy(String fieldName, Object value) {
        return CompletableFuture.supplyAsync(() -> {
            try (Session session = sessionfactory.openSession()) {
                Transaction tx = session.beginTransaction();
                String queryString = "FROM " + entity.getSimpleName() + " WHERE " + fieldName + " = :value";
                ENTITY result = session.createQuery(queryString, entity)
                        .setParameter("value", value)
                        .setMaxResults(1)
                        .uniqueResult();
                tx.commit();
                return Optional.ofNullable(result);
            } catch (Exception e) {
                e.printStackTrace();
                return Optional.empty();
            }
        });
    }

    public void saveSmart(ENTITY entityObj, ID idValue) {
        CompletableFuture.runAsync(() -> {
            try (Session session = sessionfactory.openSession()) {
                Transaction tx = session.beginTransaction();

                ENTITY existing = session.find(this.entity, idValue);
                if (existing == null) {
                    session.persist(entityObj);
                } else {
                    session.merge(entityObj);
                }

                tx.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void removeByID(ID id) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try (Session sqlsession = sessionfactory.openSession()) {
                    Transaction transaction = sqlsession.beginTransaction();
                    ENTITY result = sqlsession.find(entity,id);
                    sqlsession.remove(result);
                    transaction.commit();

                }
            }
        });
        thread.run();
    }

    public CompletableFuture<ENTITY> findByID(ID id) {
        return CompletableFuture.supplyAsync(() -> {
            ENTITY result;
            try (Session sqlsession = sessionfactory.openSession()) {
                Transaction transaction = sqlsession.beginTransaction();
                result = sqlsession.find(entity,id);
                transaction.commit();
                return result;
            }}
        );

    }

}
