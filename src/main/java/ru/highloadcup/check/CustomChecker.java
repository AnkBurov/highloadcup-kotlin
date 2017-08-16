package ru.highloadcup.check;

import org.springframework.stereotype.Component;

public interface CustomChecker<T> {

    void checkFullyNull(T object);
}
