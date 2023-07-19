package com.batch.springbatch.batch;

import com.batch.springbatch.entity.Student;
import com.batch.springbatch.model.StudentDTO;
import org.springframework.batch.item.ItemProcessor;

import java.util.Date;

public class StudentItemProcessor implements ItemProcessor<StudentDTO, Student> {
    @Override
    public Student process(StudentDTO item) throws Exception {
        return Student.builder()
                .email(item.getEmail())
                .lastName(item.getLastName())
                .firstName(item.getFirstName())
                .phoneNumber(item.getPhoneNumber())
                .createDate(new Date())
                .build();
    }
}
