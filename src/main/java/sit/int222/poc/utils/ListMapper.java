package sit.int222.poc.utils;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.function.Function;

public class ListMapper {

    private final ModelMapper modelMapper;

    private ListMapper() {
        modelMapper = new ModelMapper();
    }

    private static class SingletonHelper {
        // The inner static helper class ensures that the instance is created in a thread-safe manner without requiring synchronized.
        private static final ListMapper INSTANCE = new ListMapper();
    }

    public static ListMapper getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public <S, T> List<T> map(List<S> source, Class<T> targetClass) {
        return source.stream()
                .map(entity ->
                        modelMapper.map(
                                entity, targetClass)
                ).toList();
    }

    public <S, T> List<T> map(List<S> source, Function<S, T> customMapper) {
        return source.stream()
                .map(customMapper)
                .toList();
    }
}
