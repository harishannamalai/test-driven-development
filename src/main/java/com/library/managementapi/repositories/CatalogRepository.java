package com.library.managementapi.repositories;

import com.library.managementapi.entities.CatalogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogRepository extends JpaRepository<CatalogEntry, Long> {
}
