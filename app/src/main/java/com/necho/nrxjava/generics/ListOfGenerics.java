package com.necho.nrxjava.generics;

import java.util.ArrayList;
import java.util.List;

/**
 * 泛型数组
 */
public class ListOfGenerics<T> {

    private List<T> array = new ArrayList<T>();

    public int size() {
        return array.size();
    }

    /**
     * 添加元素
     *
     * @param item
     */
    public void add(T item) {
        array.add(item);
    }

    /**
     * 获取元素
     *
     * @param index
     * @return
     */
    public T get(int index) {
        return array.get(index);
    }
}
