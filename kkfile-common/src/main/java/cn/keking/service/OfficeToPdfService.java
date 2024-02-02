package cn.keking.service;

import cn.keking.model.FileAttribute;
import com.sun.star.document.UpdateDocMode;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.local.LocalConverter;

public interface OfficeToPdfService {

    void openOfficeToPDF(String inputFilePath, String outputFilePath, FileAttribute fileAttribute) throws OfficeException;

    void office2pdf(String inputFilePath, String outputFilePath, FileAttribute fileAttribute) throws OfficeException;
}
