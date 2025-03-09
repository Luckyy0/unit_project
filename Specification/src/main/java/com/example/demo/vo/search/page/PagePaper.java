package com.example.demo.vo.search.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagePaper<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private int totalPage;
    private long totalElements;
    private Map<String, Map<String, Long>> countInformation;

    public static PagePaper from(Page page) {
        return PagePaper
                .builder()
                .content(page.getContent())
                .pageNumber(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalPage(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .build();
    }

    public static PagePaper from(Page page,  Map<String, Map<String, Long>> countInformation) {
        return PagePaper
                .builder()
                .content(page.getContent())
                .pageNumber(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalPage(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .countInformation(countInformation)
                .build();
    }
}
