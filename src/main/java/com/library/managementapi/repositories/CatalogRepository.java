package com.library.managementapi.repositories;

import com.library.managementapi.constants.CatalogStatus;
import com.library.managementapi.entities.CatalogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatalogRepository extends JpaRepository<CatalogEntry, Long> {

    List<CatalogEntry> findAllByStatusOrderByBookIdAsc(CatalogStatus status);

    List<CatalogEntry> findAllByBookIdAndStatusOrderByBookIdAsc(long bookId, CatalogStatus status);

    List<CatalogEntry> findAllByUserIdAndBookIdAndStatusOrderByBookIdAsc(long userId, long bookId, CatalogStatus status);

    List<CatalogEntry> findAllByUserId(long userId);

}
