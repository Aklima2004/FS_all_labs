package com.example.fs_l3.repository;

import com.example.fs_l3.domain.Owner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "owners") // /api/owners
public interface OwnerRepository extends CrudRepository<Owner, Long> { }
