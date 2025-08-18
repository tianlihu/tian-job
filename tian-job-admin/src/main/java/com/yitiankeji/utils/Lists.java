package com.yitiankeji.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Lists {

    @SafeVarargs
    public static <E> List<E> of(E... elements) {
        return new ArrayList<>(Arrays.asList(elements));
    }

    @SafeVarargs
    public static <E> List<E> join(Collection<E>... lists) {
        if (lists == null) {
            return new ArrayList<>();
        }
        int length = 0;
        for (Collection<E> list : lists) {
            if (list != null) {
                length += list.size();
            }
        }
        List<E> result = new ArrayList<>(length);
        for (Collection<E> list : lists) {
            if (list != null) {
                result.addAll(list);
            }
        }
        return result;
    }
}
