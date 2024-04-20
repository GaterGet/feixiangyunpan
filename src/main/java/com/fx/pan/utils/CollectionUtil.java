package com.fx.pan.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author leaving
 * @date 2024/2/17 10:04
 * @description:
 * @Version: 1.0
 */
public class CollectionUtil {



    /**
     * @author songzhenglin@sensedeal.ai
     * @date 2022/4/18
     * @Description [map收集器用于map值是list时list排序]
     *
     * @param c:Collectors 收集器
     * @return java.util.stream.Collector<T,?,java.util.List<T>>
     */
    public static <T> Collector<T, ?, List<T>> toSortedList(Comparator<? super T> c) {
        return Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(c)), ArrayList::new);
    }

}
