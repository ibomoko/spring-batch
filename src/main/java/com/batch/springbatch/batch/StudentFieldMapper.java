package com.batch.springbatch.batch;

import com.batch.springbatch.model.StudentDTO;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class StudentFieldMapper implements FieldSetMapper<StudentDTO> {
    @Override
    public StudentDTO mapFieldSet(FieldSet fieldSet) throws BindException {
        return StudentDTO.builder()
                .firstName(fieldSet.readString("first_name"))
                .lastName(fieldSet.readString("last_name"))
                .email(fieldSet.readString("email"))
                .phoneNumber(fieldSet.readString("phone_number"))
                .build();
    }
}
