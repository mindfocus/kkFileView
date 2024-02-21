package cn.keking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO 后期可通过该方式重构
@Service
public class FilePreviewServiceFactory {
    private final Map<String, FilePreview> fileServiceMap;

    @Autowired
    public FilePreviewServiceFactory(List<FilePreview> services) {
        fileServiceMap = new HashMap<>();
        // 假设每个服务实现类都有一个方法来返回它们处理的文件类型
        services.forEach(service ->
                service.supportfileTypes().forEach(fileType -> fileServiceMap.put(fileType, service))
        );
    }

    public FilePreview getFilePreviewService(String fileType) {
        return fileServiceMap.get(fileType);
    }
}
