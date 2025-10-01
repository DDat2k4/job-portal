package com.jobportal.jobmodule.data.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MyPair<T, V> {
    T key;
    V value;
    public MyPair(T key, V value) {
        this.key = key;
        this.value = value;
    }
}