package notebook.repository;

import java.util.List;

/**
 * Data Access Object (DAO) слой с методом бд
 * @param <T> ��� ������� ��� ������/������.
 */
public interface Operation<T> {
    List<T> readAll();
    void saveAll(List<T> data);
}
