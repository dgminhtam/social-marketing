package com.social.marketing.rest.factory;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.lang.NonNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class ResponseTypeFactory {

    /**
     * Create a ParameterizedTypeReference for a single (non-collection) type.
     *
     * @param clazz Class of the data type.
     * @param <T>   The return type.
     * @return ParameterizedTypeReference<T>
     */
    public static <T> ParameterizedTypeReference<T> createFor(Class<T> clazz) {
        return new ParameterizedTypeReference<>() {
            @Override
            @NonNull
            public Type getType() {
                return clazz;
            }
        };
    }

    /**
     * Create a ParameterizedTypeReference for a List<T> type.
     *
     * @param elementType Class of the elements in the list.
     * @param <T>         The return type.
     * @return ParameterizedTypeReference<List < T>>
     */
    public static <T> ParameterizedTypeReference<List<T>> createForListOf(Class<T> elementType) {
        return new ParameterizedTypeReference<>() {
            @Override
            @NonNull
            public Type getType() {
                return new ParameterizedType() {
                    @Override
                    public Type[] getActualTypeArguments() {
                        return new Type[]{elementType};
                    }

                    @Override
                    public Type getRawType() {
                        return List.class;
                    }

                    @Override
                    public Type getOwnerType() {
                        return null;
                    }
                };
            }
        };
    }
}
