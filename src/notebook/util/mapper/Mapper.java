package notebook.util.mapper;

/**
 * Конвертер моделей из одного типа в другой.
 * @param <E> тип, хранимый в БД
 * @param <T> тип, подходящий для конвертации E
 */
public interface Mapper<E, T> {
    T toInput(E e);
    E toOutput(T t);
}
