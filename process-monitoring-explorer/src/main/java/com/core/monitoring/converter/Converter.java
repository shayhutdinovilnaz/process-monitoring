package com.core.monitoring.converter;

public interface Converter<S, T> {

    /**
     * Convert the instance of one class to another one.
     *
     * @param source - source class
     *
     * @return instance of class after convert.
     */
    T convert(S source);
}
