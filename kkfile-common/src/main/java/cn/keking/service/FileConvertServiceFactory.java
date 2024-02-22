package cn.keking.service;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO 后期可通过该方式重构
@Service
public class FileConvertServiceFactory {
    private final Table<String, String, FileConvert> fileConvertServiceTable;

    @Autowired
    public FileConvertServiceFactory(List<FileConvert> services) {
        fileConvertServiceTable = HashBasedTable.create();
        services.forEach(service ->
                service.supportConvertType().forEach(convertType ->
                service.supportConvertedType().forEach( convertedType -> {
                    fileConvertServiceTable.put(convertType, convertedType, service);
                })
                        )
        );
    }

    public FileConvert getFileConvertService(String sourceType, String targetType) {
        return fileConvertServiceTable.get(sourceType, targetType);
    }
}
