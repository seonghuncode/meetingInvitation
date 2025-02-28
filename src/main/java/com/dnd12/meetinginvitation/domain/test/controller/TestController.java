package com.dnd12.meetinginvitation.domain.test.controller;

<<<<<<< HEAD:src/main/java/com/dnd12/meetinginvitation/domain/test/controller/TestController.java
import com.dnd12.meetinginvitation.domain.test.entity.TestEntity;
import com.dnd12.meetinginvitation.domain.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
=======
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
>>>>>>> 27ba6721f7dcd753ca09bb209b1e3b56b5d97fda:src/main/java/com/dnd12/meetinginvitation/test/controller/TestController.java

@Controller
@RequestMapping("test")
public class TestController {

//    @Autowired
//    private TestService testService;
//
//    @PostMapping
//    public ResponseEntity<TestEntity> createTest(@RequestBody TestEntity testEntity){
//        TestEntity createTest = testService.saveTest(testEntity);
//        return ResponseEntity.ok(createTest);
//    }
//
//    @GetMapping("/showAll")
//    public ResponseEntity<List<TestEntity>> getAllTest(){
//        List<TestEntity> tests = testService.getAllTest();
//        return ResponseEntity.ok(tests);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<TestEntity> getTestById(@PathVariable Long id){
//        Optional<TestEntity> test = testService.getTestById(id);
//        return test.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteTest(@PathVariable Long id){
//        testService.deleteTest(id);
//        return ResponseEntity.noContent().build();
//    }


}
