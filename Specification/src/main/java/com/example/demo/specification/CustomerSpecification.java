package com.example.demo.specification;

import com.example.demo.entity.CustomerEntity;
import com.example.demo.util.search.SearchUtil;
import com.example.demo.vo.search.CustomerFilterItem;
import com.example.demo.vo.constant.CustomerConstant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class CustomerSpecification extends BaseSpecification<CustomerEntity> {
    public static EntityManager entityManager;

    public CustomerSpecification(@Autowired EntityManager _entityManager) {
        super(CustomerEntity.class);
        entityManager = _entityManager;
    }

    public static Specification<CustomerEntity> customerFilterNotKeyword(CustomerFilterItem customerFilterItem) {
        Specification<CustomerEntity> spec = Specification.where(filterInitiation());
        return spec
                .and(filterStingEqual("country", customerFilterItem.getCountry()))
                .and(filterStringLike("customerName", customerFilterItem.getCustomerName()));
    }

    public static Specification<CustomerEntity> customerFilterWithKeywordBase(
            CustomerFilterItem customerFilterItem, Map<String, Float> keySearchField) {
        return (root, cq, cb) -> {
            final Predicate[] preAgg = {cb.disjunction()};

            keySearchField.forEach((field, weight) -> {
                Predicate pre = cb.like(cb.lower(root.get(field)), "%" + customerFilterItem.getKeySearch() + "%");
                preAgg[0] = cb.or(preAgg[0], pre);
            });
            return cb.or(preAgg[0]);
        };
    }

    public static Specification<CustomerEntity> customerFilterWithKeyword(
            CustomerFilterItem customerFilterItem, Map<String, Float> keySearchField) {
        return (root, cq, cb) -> {
            final Expression<Float>[] expAgg = new Expression[]{cb.literal(0f)};
            final Predicate[] preAgg = {cb.disjunction()};

            keySearchField.forEach((field, weight) -> {
                Predicate pre = cb.like(cb.lower(root.get(field)), "%" + customerFilterItem.getKeySearch() + "%");
                Expression<Float> exp = cb.<Float>selectCase().when(pre, cb.literal(weight)).otherwise(cb.literal(0f));
                expAgg[0] = cb.sum(expAgg[0], exp);
                preAgg[0] = cb.or(preAgg[0], pre);
            });
            cq.orderBy(cb.desc(expAgg[0]));
            return cb.or(preAgg[0]);
        };
    }

    public Map<String, Map<String, Long>> countInfomation(Specification<CustomerEntity> spec) {
        Map<String, Map<String, Long>> res = new HashMap<>();
        // init base
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);
        Root<CustomerEntity> root = cq.from(CustomerEntity.class);
        Predicate predicate = spec.toPredicate(root, cq, cb);

        // create query with counter and sort
        for (String countBy : CustomerConstant.CustomerSearch.countBy) {

            List<String> countList = new ArrayList<>(Stream.of(countBy.split("\\."))
                    .filter(field -> SearchUtil.isExistField(CustomerEntity.class, field)).toList());
            if (countList.isEmpty()) continue;
            List<Selection<?>> selections = SearchUtil.buildSelections(root, cb, countList, true);
            List<Expression<?>> groupBy = SearchUtil.buildSelections(root, cb, countList, false);

            cq.multiselect(selections).where(predicate).groupBy(groupBy);

            List<Object[]> countInfo = entityManager.createQuery(cq).getResultList();
            SearchUtil.buildCountResult(res, countInfo, countList.getFirst());

        }
        return res;
    }
}
