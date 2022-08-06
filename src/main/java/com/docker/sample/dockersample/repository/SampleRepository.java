package com.docker.sample.dockersample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.docker.sample.dockersample.entity.Sample;

@Repository
public interface SampleRepository extends JpaRepository<Sample, Integer> {
    
}
