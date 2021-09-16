package com.boan.apps.cabinet.cabinet.repositories;

import com.boan.apps.cabinet.cabinet.entities.Tag;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource(path = "tags")
public interface TagRestRepository extends PagingAndSortingRepository<Tag, UUID> {
}
