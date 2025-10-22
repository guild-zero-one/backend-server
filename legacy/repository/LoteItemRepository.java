package com.zeroone.simlady.repository;

import com.zeroone.simlady.entity.LoteItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoteItemRepository extends JpaRepository<LoteItem, Integer> {
    List<LoteItem> findAllByLoteId(Integer loteId);
}
