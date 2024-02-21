package cn.keking.service.impl;

import cn.keking.model.FileAttribute;
import cn.keking.service.FilePreview;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

/**
 * EML 文件处理
 */
@Service
public class EmlFilePreviewImpl implements FilePreview {

    private final CommonPreviewImpl commonPreview;

    public EmlFilePreviewImpl(CommonPreviewImpl commonPreview) {
        this.commonPreview = commonPreview;
    }

    @Override
    public String filePreviewHandle(String url, Model model, FileAttribute fileAttribute) {
        commonPreview.filePreviewHandle(url,model,fileAttribute);
        return EML_FILE_PREVIEW_PAGE;
    }

    @Override
    public List<String> supportfileTypes() {
        return List.of("eml");
    }
}
