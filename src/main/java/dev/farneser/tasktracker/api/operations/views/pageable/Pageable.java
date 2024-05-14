package dev.farneser.tasktracker.api.operations.views.pageable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pageable<T> {
    private List<T> content;
    private Integer pagesCount;
    private Long itemsCount;
}
