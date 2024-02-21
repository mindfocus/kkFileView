package cn.keking.service.impl;

import cn.keking.model.FileAttribute;
import cn.keking.model.ReturnResponse;
import cn.keking.service.FileDownload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VFileDownloader implements FileDownload {
    @Override
    public ReturnResponse<String> downLoad(FileAttribute fileAttribute, String fileName) {
        return null;
    }

    @Override
    public List<String> supportDownloadType() {
        return List.of("vfile");
    }
}
