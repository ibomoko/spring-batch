package com.batch.springbatch.service.impl;

import com.batch.springbatch.service.FileService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;

@Service
public class FileServiceImpl implements FileService {
    public static final String RESOURCE_PATH = "D:/spring-batch/";

    @Override
    @SneakyThrows
    public void saveFile(byte[] bytes, String fileName) {
        File path = new File(RESOURCE_PATH + fileName);
        if(!path.createNewFile()) {
            return;
        }
        FileOutputStream output = new FileOutputStream(path);
        output.write(bytes);
        output.close();
    }

    @Override
    public File getFile(String fileName) {
        return new File(RESOURCE_PATH, fileName);
    }
}
