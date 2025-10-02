package com.jobportal.jobmodule.data.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MyTriple<T, U, V> {
    T first;
    U second;
    V third;

    public MyTriple(T first, U second, V third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }
}
