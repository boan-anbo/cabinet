package com.boan.apps.cabinet.repositories;

import com.boan.apps.cabinet.entities.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

// Disable Tags Repository for now because otherwise Spring Rest defaults to use hyperlinks to the domain of tags rather than inline data

@RepositoryRestResource(exported = false)
// @RepositoryRestResource(path = "tags")
public interface TagRestRepository extends PagingAndSortingRepository<Tag, String> {
}
