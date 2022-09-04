package com.cafelog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cafelog.entity.Sample;

@Repository
public interface SampleRepository extends JpaRepository<Sample, Integer> {
    
}
