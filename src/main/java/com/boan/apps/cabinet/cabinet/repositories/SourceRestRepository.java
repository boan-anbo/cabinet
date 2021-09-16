package com.boan.apps.cabinet.cabinet.repositories;

import com.boan.apps.cabinet.cabinet.entities.Card;
import com.boan.apps.cabinet.cabinet.entities.Source;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

//@RepositoryRestResource(path = "cards")
//@RepositoryRestResource(path="card")

@RepositoryRestResource(exported = false)
public interface SourceRestRepository extends PagingAndSortingRepository<Source, String> {
    //    List<Tag> findByKey(@Param("key") String key);
    Page<Source> findByTitle(String title, Pageable pageable);
}
