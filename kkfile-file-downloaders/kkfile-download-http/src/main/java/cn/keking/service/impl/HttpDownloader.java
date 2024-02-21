package cn.keking.service.impl;

import cn.keking.model.FileAttribute;
import cn.keking.model.ReturnResponse;
import cn.keking.service.FileDownload;
import cn.keking.utils.DownloadUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HttpDownloader implements FileDownload {
    @Override
    public ReturnResponse<String> downLoad(FileAttribute fileAttribute, String fileName) {
        return DownloadUtils.downLoad(fileAttribute, fileName);
    }

    @Override
    public List<String> supportDownloadType() {
        return List.of("http", "https", "ftp", "ftps");
    }
}
