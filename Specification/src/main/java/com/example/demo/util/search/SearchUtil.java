package com.example.demo.util.search;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.stream.Collectors;

public class SearchUtil {
    public static <T> boolean isExistField(Class<T> clazz, String field) {
        if (clazz == null) {
            return false;
        }
        try {
            clazz.getDeclaredField(field);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    @SuppressWarnings(value = {"unchecked"})
    public static <T extends Selection<?>> List<T> buildSelections(
            Root<?> root, CriteriaBuilder cb, List<String> fields, boolean isCount) {
        List<T> results = new ArrayList<>();
        fields.forEach(field -> results.add((T) root.get(field)));
        if (isCount) results.add((T) cb.count(root));
        return results;
    }

    public static void buildCountResult(Map<String, Map<String, Long>> res, List<Object[]> countInfo, String keyMap) {
        res.putIfAbsent(keyMap,
                countInfo.stream().collect(Collectors.toMap(
                        item -> {
                            StringBuilder key = new StringBuilder(item[0] == null ? "" : (String) item[0]);
                            for (int i = 1; i < item.length - 1; i++) {
                                key.append(item[i] != null ? "." + item[i] : "");
                            }
                            return key.toString().isEmpty() ? "Other" : key.toString();
                        },
                        item -> (Long) item[item.length - 1],
                        (existing, replace) -> existing
                )));
    }

    public static <T> Sort initSortBy(LinkedHashMap<String, Sort.Direction> sortBy, Class<T> clazz) {
        List<Sort.Order> orders = new ArrayList<>();
        sortBy.forEach((field, direction) -> {
            if (isExistField(clazz, field)) {
                orders.add(new Sort.Order(direction, field));
            }
        });
        return orders.isEmpty() ? Sort.unsorted() : Sort.by(orders);
    }

    public static LinkedHashMap<String, Sort.Direction> convertSort(List<String> sorts) {
        try {
            return sorts.stream().collect(Collectors.toMap(
                    sortItem -> sortItem.split("\\.")[0],
                    sortItem -> Sort.Direction.valueOf(sortItem.split("\\.")[1]),
                    (existing, repacement) -> existing,
                    LinkedHashMap::new
            ));
        } catch (Exception e) {
            throw new RuntimeException("Sort data is not valid");
        }

    }
}
