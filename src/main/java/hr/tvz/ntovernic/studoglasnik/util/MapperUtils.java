package hr.tvz.ntovernic.studoglasnik.util;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class MapperUtils {

    private static final ModelMapper mapper = new ModelMapper();

    public static <D, T> D map(final T object, Class<D> outClass) {
        return mapper.map(object, outClass);
    }

    public static <D, T> List<D> mapAll(final List<T> objectList, final Class<D> outClass) {
        return objectList.stream()
                .map(object -> mapper.map(object, outClass))
                .collect(Collectors.toList());
    }

    public static <D, T> Page<D> mapAll(final Page<T> objectPage, final Class<D> outClass) {
        return objectPage.map(object -> map(object, outClass));
    }
}
