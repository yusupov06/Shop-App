package uz.md.shopapp.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.md.shopapp.dtos.request.*;

import java.util.ArrayList;
import java.util.List;

@Service(value = "queryService")
@RequiredArgsConstructor
public class QueryService {

    private final EntityManager entityManager;


    public <T> TypedQuery<T> generateSimpleSearchQuery(Class<T> clazz, SimpleSearchRequest searchRequest) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);

        Root<T> root = criteriaQuery.from(clazz);
        List<Predicate> predicates = new ArrayList<>();
        for (String field : searchRequest.getFields()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get(field)),
                    "%" + searchRequest.getKey().toUpperCase() + "%"));
        }

        Predicate or = criteriaBuilder.or(predicates.toArray(Predicate[]::new));

        Order order = searchRequest.getSortDirection() == Sort.Direction.ASC
                ? criteriaBuilder.asc(root.get(searchRequest.getSortBy()))
                : criteriaBuilder.desc(root.get(searchRequest.getSortBy()));

        criteriaQuery.where(or).orderBy(order);

        return entityManager
                .createQuery(criteriaQuery)
                .setMaxResults(searchRequest.getPageCount())
                .setFirstResult(searchRequest.getPage() * searchRequest.getPageCount());
    }

    public <T> TypedQuery<T> generateSimpleSortQuery(Class<T> clazz, SimpleSortRequest request) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);

        Root<T> root = criteriaQuery.from(clazz);

        Order order = request.getDirection() == Sort.Direction.ASC
                ? criteriaBuilder.asc(root.get(request.getSortBy()))
                : criteriaBuilder.desc(root.get(request.getSortBy()));

        criteriaQuery.orderBy(order);
        return entityManager
                .createQuery(criteriaQuery)
                .setMaxResults(request.getPageCount())
                .setFirstResult(request.getPage() * request.getPageCount());
    }

    public <T> TypedQuery<T> generateFilterQuery(Class<T> clazz, FilterRequest request) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);

        Root<T> root = criteriaQuery.from(clazz);

        List<Predicate> predicates = new ArrayList<>();
        for (FilterCriteria filter : request.getFilterCriteria()) {
            predicates.add(
                    switch (FilterOperation.valueOf(filter.getOperation())) {
                        case GREATER_THAN -> criteriaBuilder
                                .greaterThan(root.get(filter.getFilterKey()),
                                        filter.getValue());
                        case LESS_THAN -> criteriaBuilder
                                .lessThan(root.get(filter.getFilterKey()),
                                        filter.getValue());
                        case EQUALS -> criteriaBuilder
                                .equal(root.get(filter.getFilterKey()),
                                        filter.getValue());
                    }
            );
        }

        Predicate and = criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        criteriaQuery.where(and);

        return entityManager
                .createQuery(criteriaQuery)
                .setMaxResults(request.getPageCount())
                .setFirstResult(request.getPage() * request.getPageCount());
    }
}
