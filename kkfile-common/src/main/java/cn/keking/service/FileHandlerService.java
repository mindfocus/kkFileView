package cn.keking.service;

import cn.keking.model.FileAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface FileHandlerService {

    /**
     * @return 已转换过的文件集合(缓存)
     */
    Map<String, String> listConvertedFiles();

    /**
     * @return 已转换过的文件，根据文件名获取
     */
    String getConvertedFile(String key);

    /**
     * @param key pdf本地路径
     * @return 已将pdf转换成图片的图片本地相对路径
     */
    Integer getPdf2jpgCache(String key);

    /**
     * 从路径中获取文件负
     *
     * @param path 类似这种：C:\Users\yudian-it\Downloads
     * @return 文件名
     */
    String getFileNameFromPath(String path);

    /**
     * 获取相对路径
     *
     * @param absolutePath 绝对路径
     * @return 相对路径
     */
    String getRelativePath(String absolutePath);

    /**
     * 添加转换后PDF缓存
     *
     * @param fileName pdf文件名
     * @param value    缓存相对路径
     */
    void addConvertedFile(String fileName, String value);

    /**
     * 添加转换后图片组缓存
     *
     * @param pdfFilePath pdf文件绝对路径
     * @param num         图片张数
     */
    void addPdf2jpgCache(String pdfFilePath, int num);

    /**
     * 获取redis中压缩包内图片文件
     *
     * @param compressFileKey compressFileKey
     * @return 图片文件访问url列表
     */
    List<String> getImgCache(String compressFileKey);

    /**
     * 设置redis中压缩包内图片文件
     *
     * @param fileKey fileKey
     * @param imgs    图片文件访问url列表
     */
    void putImgCache(String fileKey, List<String> imgs);

    /**
     * 对转换后的文件进行操作(改变编码方式)
     *
     * @param outFilePath 文件绝对路径
     */
    void doActionConvertedFile(String outFilePath);

    /**
     * pdf文件转换成jpg图片集
     * fileNameFilePath pdf文件路径
     * pdfFilePath pdf输出文件路径
     * pdfName     pdf文件名称
     * loadPdf2jpgCache 图片访问集合
     */
    List<String> pdf2jpg(String fileNameFilePath, String pdfFilePath, String pdfName, FileAttribute fileAttribute) throws Exception;

    /**
     * cad文件转pdf
     *
     * @param inputFilePath  cad文件路径
     * @param outputFilePath pdf输出文件路径
     * @return 转换是否成功
     */
    String cadToPdf(String inputFilePath, String outputFilePath, String cadPreviewType, FileAttribute fileAttribute) throws Exception;

    /**
     * 获取文件属性
     *
     * @param url url
     * @return 文件属性
     */
    FileAttribute getFileAttribute(String url, HttpServletRequest req);

    /**
     * @return 已转换过的视频文件集合(缓存)
     */
    Map<String, String> listConvertedMedias();

    /**
     * 添加转换后的视频文件缓存
     *
     * @param fileName
     * @param value
     */
    void addConvertedMedias(String fileName, String value);

    /**
     * @return 已转换视频文件缓存，根据文件名获取
     */
    String getConvertedMedias(String key);
}
