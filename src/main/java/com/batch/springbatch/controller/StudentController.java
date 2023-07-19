package com.batch.springbatch.controller;

import com.batch.springbatch.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/api/students")
public class StudentController {

    private final StudentService studentService;

    @PostMapping(value = "/import", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<HttpStatus> importStudents(@RequestPart MultipartFile file) {
        studentService.importStudentsFromFile(file);
        return ResponseEntity.ok().build();
    }
}
