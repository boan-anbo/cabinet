package com.boan.apps.cabinet.repositories;

import com.boan.apps.cabinet.entities.Card;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;


//@CrossOrigin
//@RepositoryRestResource(path = "cards")
//@RepositoryRestResource(exported = false)
@Repository
@RepositoryRestResource(exported = false)
public interface CardRepository extends PagingAndSortingRepository<Card, String>, CardRepostioryCustom
{
}

