package cn.keking.service;

import cn.keking.model.FileAttribute;

public interface CompressFileReader {
    String unRar(String filePath, String filePassword, String fileName, FileAttribute fileAttribute) throws Exception;
}
