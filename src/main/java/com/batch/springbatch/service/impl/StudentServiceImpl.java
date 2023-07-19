package com.batch.springbatch.service.impl;

import com.batch.springbatch.service.FileService;
import com.batch.springbatch.service.StudentService;
import lombok.SneakyThrows;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class StudentServiceImpl implements StudentService {

    private final JobLauncher jobLauncher;
    private final FileService fileService;
    private final Job studentCsvImportJob;

    public StudentServiceImpl(JobLauncher jobLauncher, FileService fileService, @Qualifier(value = "importStudentsJob") Job studentCsvImportJob) {
        this.jobLauncher = jobLauncher;
        this.fileService = fileService;
        this.studentCsvImportJob = studentCsvImportJob;
    }


    @Override
    @SneakyThrows
    public void importStudentsFromFile(MultipartFile file) {
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        fileService.saveFile(file.getBytes(), fileName);
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("filename", fileName)
                .toJobParameters();

        jobLauncher.run(studentCsvImportJob, jobParameters);
    }
}
