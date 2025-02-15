package com.dnd12.meetinginvitation.domain.test.repository;

import com.dnd12.meetinginvitation.domain.test.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<TestEntity, Long> {

}
