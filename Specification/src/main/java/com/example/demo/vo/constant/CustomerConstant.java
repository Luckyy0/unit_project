package com.example.demo.vo.constant;

import java.util.List;
import java.util.Map;

public class CustomerConstant {

    public static class CustomerSearch {
        public static final List<String> countBy = List.of("country.city.state", "postalCode");
        public static final Map<String, Float> searchKeyWord =
                Map.of("customerName", 0.7f, "country", 0.3f);
    }
}
