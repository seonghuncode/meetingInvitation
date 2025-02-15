package com.dnd12.meetinginvitation.test.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
