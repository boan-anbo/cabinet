package com.boan.apps.cabinet.cabinet.repositories;

import com.boan.apps.cabinet.cabinet.entities.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.transaction.Transactional;
import java.util.UUID;

//@RepositoryRestResource(path = "cards")
//@RepositoryRestResource(path="card")
public interface CardRestRepository extends PagingAndSortingRepository<Card, UUID> {
    //    List<Tag> findByKey(@Param("key") String key);
    Page<Card> findByText(String name, Pageable pageable);
}
