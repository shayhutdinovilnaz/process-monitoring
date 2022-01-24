package com.core.process.monitoring.service.converter;

public interface Converter<S, T> {
    /**
     * Convert the instance of one class to another one.
     *
     * @param source - source class
     *
     * @return instance of class after convert.
     */
    T convert(S source);

    /**
     * Reverse convert the instance of one class to another one.
     *
     * @param source - source class
     *
     * @return instance of class after reverse convert.
     */
    S reverseConvert(T source);
}
