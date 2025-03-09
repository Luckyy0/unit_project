package com.example.demo.specification;

import com.example.demo.util.search.SearchUtil;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class BaseSpecification<V> implements SpecificationOutPutPort<V> {
    public static Class<?> clazz;

    public BaseSpecification(Class<V> _clazz) {
        clazz = _clazz;
    }

    // Search all
    public static <T> Specification<T> filterInitiation() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    public static <T> Specification<T> filterStingEqual(String field, String searchData) {
        return StringUtils.isNotBlank(searchData) && SearchUtil.isExistField(clazz, field)
                ? (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get(field), searchData) : null;
    }

    public static <T> Specification<T> filterStringLike(String field, String searchData) {
        return StringUtils.isNotBlank(searchData) && SearchUtil.isExistField(clazz, field)
                ? (root, query, criteriaBuilder)
                -> criteriaBuilder.like(root.get(field), "%" + searchData + "%") : null;
    }
}
