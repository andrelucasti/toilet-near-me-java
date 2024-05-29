package io.andrelucas.toiletnearme.common;

import java.util.List;
import java.util.Optional;

public interface Repository<T extends AggregateRoot, ID> {
    void save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void delete(T entity);
    void update(T entity);
}
