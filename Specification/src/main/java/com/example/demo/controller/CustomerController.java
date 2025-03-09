package com.example.demo.controller;

import com.example.demo.domain.Customer;
import com.example.demo.mysqlService.CustomerMysqlService;
import com.example.demo.util.search.SearchUtil;
import com.example.demo.vo.search.page.PagePaper;
import com.example.demo.vo.search.CustomerFilterItem;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static com.example.demo.vo.constant.search.SearchConstant.RestQuery;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerMysqlService customerMysqlService;

    @GetMapping("/list")
    public PagePaper<Customer> getListCustomer(
            @RequestParam(value = RestQuery.PAGE_NUMBER, defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = RestQuery.PAGE_SIZE, defaultValue = "20", required = false) int pageSize,
            @RequestParam(value = RestQuery.IS_COUNT, defaultValue = "false", required = false) boolean isCount,
            @RequestParam(value = RestQuery.SORT_BY, required = false) List<String> sorts,
            @RequestParam(value = RestQuery.KEY_SEARCH, required = false) String keySearch,
            @RequestParam(value = RestQuery.COUNTRY, required = false) String country,
            @RequestParam(value = RestQuery.CUSTOMER_NAME, required = false) String customerName
    ) {
        return customerMysqlService.getListCustomer(
                CustomerFilterItem.builder()
                        .pageNumber(pageNumber)
                        .pageSize(pageSize)
                        .isCount(isCount)
                        .sortBy(CollectionUtils.isEmpty(sorts) ? new LinkedHashMap<>() : SearchUtil.convertSort(sorts))
                        .keySearch(keySearch)
                        .country(country)
                        .customerName(customerName)
                        .build());
    }
}
