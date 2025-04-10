package de.stynxyxy.stynxyxyAPI.database.sql;

import de.stynxyxy.stynxyxyAPI.database.BaseRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SQLRepository <ENTITY,ID> extends BaseRepository<ENTITY,ID> {
    private SessionFactory sessionFactory;

    public SQLRepository(Class<ENTITY> entityClass, Class<ID> idClass, SessionFactory sessionFactory) {
        super(entityClass, idClass);
        this.sessionFactory = sessionFactory;
    }

    @Override
    public CompletableFuture<Void> save(Object o) {
        return CompletableFuture.supplyAsync(() -> {
            try(Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                session.persist(o);
                transaction.commit();

                return null;
            }

        });
    }

    @Override
    public CompletableFuture<Void> removeBy(Object key) {
        return null;
    }

    @Override
    public CompletableFuture<ENTITY> findBy(Object key) {
        return CompletableFuture.supplyAsync(() -> {
            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                ENTITY entity = session.find(entityClass, key);
                transaction.commit();
                return entity;
            }
        });
    }


    @Override
    public CompletableFuture<ENTITY> findById(ID o) {
        return findBy(o);
    }

    @Override
    public CompletableFuture<Void> removeById(ID o) {
        return CompletableFuture.supplyAsync(() -> {
            try (Session session = sessionFactory.openSession()) {
              Transaction transaction = session.beginTransaction();
              ENTITY entity = findById(o).get();
              if (entity != null) {
                  session.remove(entity);
              }
              transaction.commit();
              return null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public CompletableFuture<List<ENTITY>> findAll() {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> existsById(ID id) {
        return CompletableFuture.supplyAsync(() -> {
            try (Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                ENTITY result = session.get(entityClass,id);
                transaction.commit();
                return result != null;
            }
        });

    }
}
