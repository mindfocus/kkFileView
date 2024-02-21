package cn.keking.model;

import lombok.Data;

@Data
public class ProcessResult {
    private String pdfUrl;
    private String currentUrl;
    private String csvUrl;
    private String viewName;
}
