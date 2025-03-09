package com.example.demo.vo.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Sort;

import java.util.LinkedHashMap;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class RestQuery {
    private int pageNumber;
    private int pageSize;
    private LinkedHashMap<String, Sort.Direction> sortBy;
    private String keySearch;
    private boolean isCount;
}
