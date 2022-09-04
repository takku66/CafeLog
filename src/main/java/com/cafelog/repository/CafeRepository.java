package com.cafelog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cafelog.entity.Cafe;

@Repository
public interface CafeRepository extends JpaRepository<Cafe, Integer> {
    
}
