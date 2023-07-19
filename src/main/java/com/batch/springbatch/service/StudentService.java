package com.batch.springbatch.service;

import org.springframework.web.multipart.MultipartFile;

public interface StudentService {
    void importStudentsFromFile(MultipartFile file);
}
