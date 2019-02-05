package com.kenvix.utils;

import java.io.Serializable;

public class Holder<T> implements Cloneable, Serializable {
    private T t;

    public Holder() {
    }

    public Holder(T t) {
        this.t = t;
    }

    public T get(){
        return t ;
    }

    public void set(T t){
        this.t = t;
    }
}