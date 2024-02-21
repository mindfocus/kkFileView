package cn.keking.service;

import cn.keking.model.FileAttribute;
import cn.keking.model.ReturnResponse;
import org.springframework.ui.Model;

import java.util.List;

/**
 * @author fangtc
 */
public interface FileDownload {
    ReturnResponse<String> downLoad(FileAttribute fileAttribute, String fileName);

    List<String> supportDownloadType();
}
