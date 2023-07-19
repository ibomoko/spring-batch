package com.batch.springbatch.service;

import java.io.File;

public interface FileService {
    void saveFile(byte[] bytes, String fileName);
    File getFile(String fileName);

}
