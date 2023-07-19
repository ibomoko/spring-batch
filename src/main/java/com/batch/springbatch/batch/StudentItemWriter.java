package com.batch.springbatch.batch;

import com.batch.springbatch.entity.Student;
import com.batch.springbatch.repository.StudentRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

@RequiredArgsConstructor
public class StudentItemWriter implements ItemWriter<Student> {

    private final StudentRepository studentRepository;

    @Override
    public void write(List<? extends Student> items) throws Exception {
        if (items.isEmpty())
            return;

        studentRepository.saveAll(items);
    }
}
