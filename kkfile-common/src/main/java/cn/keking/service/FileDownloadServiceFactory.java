package cn.keking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO 后期可通过该方式重构
@Service
public class FileDownloadServiceFactory {
    private final Map<String, FileDownload> fileServiceMap;

    @Autowired
    public FileDownloadServiceFactory(List<FileDownload> services) {
        fileServiceMap = new HashMap<>();
        services.forEach(service ->
                service.supportDownloadType().forEach(downloadType -> fileServiceMap.put(downloadType, service))
        );
    }

    public FileDownload getFileDownloadService(String downloadType) {
        return fileServiceMap.get(downloadType);
    }
}
