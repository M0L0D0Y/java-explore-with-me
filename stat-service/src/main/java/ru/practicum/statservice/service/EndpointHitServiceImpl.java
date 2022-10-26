package ru.practicum.statservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.statservice.model.EndpointHit;
import ru.practicum.statservice.model.ViewStats;
import ru.practicum.statservice.storage.EndpointHitStorage;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class EndpointHitServiceImpl implements EndpointHitService {
    private final EndpointHitStorage storage;
    private final EntityManager entityManager;

    @Autowired
    public EndpointHitServiceImpl(EndpointHitStorage storage, EntityManager entityManager) {
        this.storage = storage;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void addEndpoint(EndpointHit endpointHit) {
        storage.save(endpointHit);
        log.info("Эндпоинт сохранен");
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EndpointHit> cq = cb.createQuery(EndpointHit.class);
        Root<EndpointHit> root = cq.from(EndpointHit.class);
        Predicate[] param = createPredicates(cb, root, start, end, uris);
        List<EndpointHit> results = search(cq, root, param, unique);
        List<ViewStats> viewStatsList = createListViewStats(results);
        log.info("Найдены вся статистика соответствующая заданным фильтрам");
        return viewStatsList;
    }

    private List<ViewStats> createListViewStats(List<EndpointHit> results) {
        List<ViewStats> resultsSearch = new LinkedList<>();
        List<String> listUris = new LinkedList<>();
        for (EndpointHit endpointHit : results) {
            listUris.add(endpointHit.getUri());
        }
        Map<String, Long> map = listUris.stream()
                .collect(Collectors.toMap(
                        e -> e,
                        e -> 1L,
                        Long::sum));
        Set<String> uniqueUri = new HashSet<>(listUris);
        for (String s : uniqueUri) {
            ViewStats viewStats = new ViewStats();
            viewStats.setApp("ewm-main-service");
            viewStats.setUri(s);
            viewStats.setHits(map.get(s));
            resultsSearch.add(viewStats);
        }
        return resultsSearch;
    }

    private Predicate[] createPredicates(CriteriaBuilder cb, Root<EndpointHit> root,
                                         LocalDateTime start, LocalDateTime end, List<String> uris) {
        List<Predicate> predicates = new LinkedList<>();
        if (uris != null) {
            predicates.add(root.get("uri").in(uris));
        }
        predicates.add(cb.between(root.get("timestamp"), start, end));
        Predicate[] param = new Predicate[predicates.size()];
        for (int j = 0; j < predicates.size(); j++) {
            param[j] = predicates.get(j);
        }
        return param;
    }

    private List<EndpointHit> search(CriteriaQuery<EndpointHit> cq, Root<EndpointHit> root,
                                     Predicate[] param, Boolean unique) {
        cq.select(root).where(param);
        if (unique.equals(true)) {
            //cq.select(root).where(param).distinct(true);
            cq.select(root).where(param).select(root.get("ip")).distinct(true);
        }
        TypedQuery<EndpointHit> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}
