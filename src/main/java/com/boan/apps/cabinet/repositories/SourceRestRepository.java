package com.boan.apps.cabinet.repositories;

import com.boan.apps.cabinet.entities.Source;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

//@RepositoryRestResource(path = "cards")
//@RepositoryRestResource(path="card")

@Repository

@RepositoryRestResource(exported = false)
public interface SourceRestRepository extends PagingAndSortingRepository<Source, String> {
}
