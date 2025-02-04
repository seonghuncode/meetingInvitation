package com.dnd12.meetinginvitation.test.service;

import com.dnd12.meetinginvitation.test.entity.TestEntity;
import com.dnd12.meetinginvitation.test.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    public TestEntity saveTest(TestEntity testEntity){
        return testRepository.save(testEntity);
    }

    public List<TestEntity> getAllTest(){
        return testRepository.findAll();
    }

    public Optional<TestEntity> getTestById(Long id){
        return testRepository.findById(id);
    }

    public void deleteTest(Long id){
        testRepository.deleteById(id);
    }

}
