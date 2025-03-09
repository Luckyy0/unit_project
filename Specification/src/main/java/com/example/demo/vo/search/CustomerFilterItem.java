package com.example.demo.vo.search;

import lombok.*;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CustomerFilterItem extends RestQuery {
    // search field
    private String country;
    private String customerName;
}
