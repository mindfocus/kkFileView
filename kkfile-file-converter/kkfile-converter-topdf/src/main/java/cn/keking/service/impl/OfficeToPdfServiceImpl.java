package cn.keking.service.impl;

import cn.keking.config.ConfigConstants;
import cn.keking.model.FileAttribute;
import cn.keking.model.FileType;
import cn.keking.model.ReturnResponse;
import cn.keking.service.FileConvert;
import cn.keking.service.FileHandlerService;
import cn.keking.service.OfficeToPdfService;
import cn.keking.utils.*;
import com.sun.star.document.UpdateDocMode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.local.LocalConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yudian-it
 */
@Component
@Slf4j
public class OfficeToPdfServiceImpl implements OfficeToPdfService, FileConvert {

    private final String fileDir = ConfigConstants.getFileDir();
    private final FileHandlerService fileHandlerService;

    public OfficeToPdfServiceImpl(FileHandlerService fileHandlerService) {
        this.fileHandlerService = fileHandlerService;
    }

    public static void converterFile(File inputFile, String outputFilePath_end, FileAttribute fileAttribute) throws OfficeException {
        File outputFile = new File(outputFilePath_end);
        // 假如目标路径不存在,则新建该路径
        if (!outputFile.getParentFile().exists() && !outputFile.getParentFile().mkdirs()) {
            log.error("创建目录【{}】失败，请检查目录权限！", outputFilePath_end);
        }
        LocalConverter.Builder builder;
        Map<String, Object> filterData = new HashMap<>();
        filterData.put("EncryptFile", true);
        if (!ConfigConstants.getOfficePageRange().equals("false")) {
            filterData.put("PageRange", ConfigConstants.getOfficePageRange()); //限制页面
        }
        if (!ConfigConstants.getOfficeWatermark().equals("false")) {
            filterData.put("Watermark", ConfigConstants.getOfficeWatermark());  //水印
        }
        filterData.put("Quality", ConfigConstants.getOfficeQuality()); //图片压缩
        filterData.put("MaxImageResolution", ConfigConstants.getOfficeMaxImageResolution()); //DPI
        if (ConfigConstants.getOfficeExportBookmarks()) {
            filterData.put("ExportBookmarks", true); //导出书签
        }
        if (ConfigConstants.getOfficeExportNotes()) {
            filterData.put("ExportNotes", true); //批注作为PDF的注释
        }
        if (ConfigConstants.getOfficeDocumentOpenPasswords()) {
            filterData.put("DocumentOpenPassword", fileAttribute.getFilePassword()); //给PDF添加密码
        }
        Map<String, Object> customProperties = new HashMap<>();
        customProperties.put("FilterData", filterData);
        if (StringUtils.isNotBlank(fileAttribute.getFilePassword())) {
            Map<String, Object> loadProperties = new HashMap<>();
            loadProperties.put("Hidden", true);
            loadProperties.put("ReadOnly", true);
            loadProperties.put("UpdateDocMode", UpdateDocMode.NO_UPDATE);
            loadProperties.put("Password", fileAttribute.getFilePassword());
            builder = LocalConverter.builder().loadProperties(loadProperties).storeProperties(customProperties);
        } else {
            builder = LocalConverter.builder().storeProperties(customProperties);
        }
        builder.build().convert(inputFile).to(outputFile).execute();
    }

    public static String getPostfix(String inputFilePath) {
        return inputFilePath.substring(inputFilePath.lastIndexOf(".") + 1);
    }

    public static String getOutputFilePath(String inputFilePath) {
        return inputFilePath.replaceAll("." + getPostfix(inputFilePath), ".pdf");
    }

    @Override
    public void openOfficeToPDF(String inputFilePath, String outputFilePath, FileAttribute fileAttribute) throws OfficeException {
        office2pdf(inputFilePath, outputFilePath, fileAttribute);
    }


    @Override
    public void office2pdf(String inputFilePath, String outputFilePath, FileAttribute fileAttribute) throws OfficeException {
        if (null != inputFilePath) {
            File inputFile = new File(inputFilePath);
            // 判断目标文件路径是否为空
            if (null == outputFilePath) {
                // 转换后的文件路径
                String outputFilePath_end = getOutputFilePath(inputFilePath);
                if (inputFile.exists()) {
                    // 找不到源文件, 则返回
                    converterFile(inputFile, outputFilePath_end, fileAttribute);
                }
            } else {
                if (inputFile.exists()) {
                    // 找不到源文件, 则返回
                    converterFile(inputFile, outputFilePath, fileAttribute);
                }
            }
        }
    }

    @Override
    public ReturnResponse<String> convert(FileAttribute fileAttribute, String fileName) {
        ReturnResponse<String> response = DownloadUtils.downLoad(fileAttribute, fileAttribute.getName());
        String filePath = response.getContent();
        boolean isPwdProtectedOffice =  OfficeUtils.isPwdProtected(filePath);    // 判断是否加密文件
        String suffix = fileAttribute.getSuffix();
        String originFileName = fileAttribute.getOriginFilePath(); //原始文件名

        originFileName = KkFileUtils.htmlEscape(originFileName);  //文件名处理
        String cacheFilePrefixName = originFileName.substring(0, originFileName.lastIndexOf(".")) + suffix + "."; //这里统一文件名处理 下面更具类型 各自添加后缀
        String outFilePath = cacheFilePrefixName + "pdf"; //生成文件的路径
        if (isPwdProtectedOffice && !org.springframework.util.StringUtils.hasLength(fileAttribute.getFilePassword())) {
            throw new UnsupportedOperationException("缺少密码");
        } else {
            if (org.springframework.util.StringUtils.hasText(outFilePath)) {
                try {
                    openOfficeToPDF(filePath, outFilePath, fileAttribute);
                } catch (OfficeException e) {
                    if (isPwdProtectedOffice && !OfficeUtils.isCompatible(filePath, fileAttribute.getFilePassword())) {
                        throw new UnsupportedOperationException("密码错误");
                    }
                }
                //是否保留OFFICE源文件
                if (!fileAttribute.isCompressFile() && ConfigConstants.getDeleteSourceFile()) {
                    KkFileUtils.deleteFileByPath(filePath);
                }
                if (fileAttribute.isUsePasswordCache() || !isPwdProtectedOffice) {
                    // 加入缓存
                    fileHandlerService.addConvertedFile(fileAttribute.getCacheName(), fileHandlerService.getRelativePath(outFilePath));
                }
            }
        }
        return new ReturnResponse<>(0, "转换成功", outFilePath);
    }

    @Override
    public List<String> supportConvertType() {
        return List.of("xls", "xlsx", "docx", "doc", "ppt", "pptx");
    }

    @Override
    public List<String> supportConvertedType() {
        return List.of("pdf");
    }
}
