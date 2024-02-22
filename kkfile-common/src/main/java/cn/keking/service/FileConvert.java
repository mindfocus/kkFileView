package cn.keking.service;

import cn.keking.model.FileAttribute;
import cn.keking.model.ReturnResponse;
import org.springframework.ui.Model;

import java.util.List;

/**
 * Created by kl on 2018/1/17.
 * Content :
 */
public interface FileConvert {

    ReturnResponse<String> convert(FileAttribute fileAttribute, String fileName);

    List<String> supportConvertType();
    List<String> supportConvertedType();
}
