package budhioct.dev.common;

import budhioct.dev.entity.PostEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public class Models<T> {

    public Specification<T> where(Map<String, Object> request) {
        Map<String, Object> filter = new HashMap<>(request);
        filter.remove("page");
        filter.remove("size");
        filter.remove("sort");
        return new Specification<>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                for (var data : filter.entrySet()) {
                    if (Objects.nonNull(data)) {
//                        log.info("data type: {}", data.getValue());
                        predicates.add(criteriaBuilder.or(
                                criteriaBuilder.like(root.get(data.getKey()).as(String.class), "%" + data.getValue() + "%")
                        ));
                    }
                }
                return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
            }
        };
    }

    public PageRequest pageable(Map<String, Object> page) {
        int pageNumber = 0;
        int pageSize = 10;

        if (page.containsKey("page") && page.get("page") != "") {

            pageNumber = Integer.parseInt(page.get("page").toString());
        }

        if (page.containsKey("size") && page.get("size") != "") {

            pageSize = Integer.parseInt(page.get("size").toString());
        }

        return PageRequest.of(pageNumber, pageSize);

    }

    public PageRequest pageableSort(Map<String, Object> page, Class<?> entityClass) {
        int pageNumber = 0;
        int pageSize = 10;
        String sortPage = "createdAt";

        try {
            if (page.containsKey("page") && page.get("page") != "") {
                pageNumber = Integer.parseInt(page.get("page").toString());
            }

            if (page.containsKey("size") && page.get("size") != "") {
                pageSize = Integer.parseInt(page.get("size").toString());
            }

            if (page.containsKey("sort") && page.get("sort") != "") {
                sortPage = String.valueOf(page.get("sort"));
                if (!isValidSortField(entityClass, sortPage)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input, please enter a key from list of field");
                }
                return PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.asc(sortPage)));
            }

            return PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc(sortPage)));

        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input, please enter a number");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

    private boolean isValidSortField(Class<?> entityClass, String fieldName) {
        try {
            // check fieldName exist on class entity
            Field field = entityClass.getDeclaredField(fieldName);
            return field != null;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

}
