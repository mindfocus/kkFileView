package cn.keking.web.controller;

import cn.keking.model.FileAttribute;
import cn.keking.model.ReturnResponse;
import cn.keking.service.FileConvertServiceFactory;
import cn.keking.service.FilePreview;
import cn.keking.service.FilePreviewFactory;
import cn.keking.service.cache.CacheService;
import cn.keking.service.impl.FileHandlerServiceImpl;
import cn.keking.service.OtherFilePreviewImpl;
import cn.keking.utils.DownloadUtils;
import cn.keking.utils.KkFileUtils;
import cn.keking.utils.WebUtils;
import io.mola.galimatias.GalimatiasParseException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static cn.keking.service.FilePreview.PICTURE_FILE_PREVIEW_PAGE;

/**
 * @author fangtc
 */
@RestController
@Slf4j
public class PreviewController {

    public static final String BASE64_DECODE_ERROR_MSG = "Base64解码失败，请检查你的 %s 是否采用 Base64 + urlEncode 双重编码了！";
    private final FilePreviewFactory previewFactory;
    private final CacheService cacheService;
    private final FileHandlerServiceImpl fileHandlerService;
    private final OtherFilePreviewImpl otherFilePreview;

    private final FileConvertServiceFactory convertFactory;

    public PreviewController(FilePreviewFactory filePreviewFactory, FileHandlerServiceImpl fileHandlerService, CacheService cacheService, OtherFilePreviewImpl otherFilePreview, FileConvertServiceFactory convertFactory) {
        this.previewFactory = filePreviewFactory;
        this.fileHandlerService = fileHandlerService;
        this.cacheService = cacheService;
        this.otherFilePreview = otherFilePreview;
        this.convertFactory = convertFactory;
    }

    @GetMapping("/preview")
    public void preview(String url, HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String fileUrl = WebUtils.decodeUrl(url);
        FileAttribute fileAttribute = fileHandlerService.getFileAttribute(fileUrl, req);
        val fileConverter = convertFactory.getFileConvertService(fileAttribute.getSuffix(), "pdf");
        val result = fileConverter.convert(fileAttribute, fileAttribute.getName());
        File r = new File(result.getContent());
        if (!r.exists()) {
            throw new IOException("文件未找到");
        }
        try (val tempStream = Files.newInputStream(r.toPath())) {
            FileNameMap fileNameMap = URLConnection.getFileNameMap();
            resp.setContentType(fileNameMap.getContentTypeFor(result.getContent()));
            IOUtils.copy(tempStream, resp.getOutputStream());
        }
    }
}
