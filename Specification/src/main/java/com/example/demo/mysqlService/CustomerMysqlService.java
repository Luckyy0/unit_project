package com.example.demo.mysqlService;

import com.example.demo.domain.Customer;
import com.example.demo.entity.CustomerEntity;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.specification.CustomerSpecification;
import com.example.demo.util.search.SearchUtil;
import com.example.demo.vo.constant.CustomerConstant;
import com.example.demo.vo.search.page.PagePaper;
import com.example.demo.vo.search.CustomerFilterItem;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerMysqlService {
    private final CustomerRepository customerRepository;
    private final CustomerSpecification customerSpecification;

    @SuppressWarnings(value = {"unchecked", "rawtypes"})
    public PagePaper<Customer> getListCustomer(CustomerFilterItem customerFilterItem) {
        PageRequest pageRequest =
                PageRequest.of(customerFilterItem.getPageNumber() - 1, customerFilterItem.getPageSize());
        Specification<CustomerEntity> spec;
        Page<CustomerEntity> results;
        Map<String, Map<String, Long>> countInformation;
        if (StringUtils.isBlank(customerFilterItem.getKeySearch())) {
            pageRequest = pageRequest.withSort(SearchUtil.initSortBy(customerFilterItem.getSortBy(), CustomerEntity.class));
            spec = CustomerSpecification.customerFilterNotKeyword(customerFilterItem);
            results = customerRepository.findAll(spec, pageRequest);
            countInformation = customerSpecification.countInfomation(spec);
        } else {
            Map<String, Float> keySearchField = new HashMap<>();
            CustomerConstant.CustomerSearch.searchKeyWord
                    .forEach((field, weight) -> {
                        if (SearchUtil.isExistField(CustomerEntity.class, field)) {
                            keySearchField.putIfAbsent(field, weight);
                        }
                    });
            spec = CustomerSpecification.customerFilterWithKeyword(customerFilterItem, keySearchField);
            results = customerRepository.findAll(spec, pageRequest);
            countInformation = customerSpecification.countInfomation(CustomerSpecification.customerFilterWithKeywordBase(customerFilterItem, keySearchField));
        }
        PagePaper pagePaper = PagePaper.from(results, countInformation);
        pagePaper.setContent(results.stream().map(CustomerEntity::toDomain).toList());
        return pagePaper;
    }
}
