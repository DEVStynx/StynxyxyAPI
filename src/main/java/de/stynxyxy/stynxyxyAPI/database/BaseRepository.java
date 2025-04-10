package de.stynxyxy.stynxyxyAPI.database;

import de.stynxyxy.stynxyxyAPI.BaseAPI;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract  class BaseRepository <ENTITY,ID>{
    protected Class<ENTITY> entityClass;
    protected Class<ID> idClass;


    public BaseRepository(Class<ENTITY> entityClass, Class<ID> idClass) {
        this.entityClass = entityClass;
        this.idClass = idClass;
    }

    public abstract CompletableFuture<Void> save(Object entity);

    public abstract CompletableFuture<Void> removeBy(Object key);

    public abstract CompletableFuture<ENTITY> findBy(Object key);

    public abstract CompletableFuture<ENTITY> findById(ID id);
    public abstract CompletableFuture<Void> removeById(ID id);

    public abstract CompletableFuture<List<ENTITY>> findAll();
    public abstract CompletableFuture<Boolean> existsById(ID id);


}
