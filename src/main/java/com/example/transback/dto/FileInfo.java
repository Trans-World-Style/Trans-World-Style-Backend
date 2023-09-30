package com.example.transback.dto;

public class FileInfo {
    private String filename;
    private long fileSize;

    public FileInfo(String filename, long fileSize) {
        this.filename = filename;
        this.fileSize = fileSize;
    }

    public String getFilename() {
        return filename;
    }

    public long getFileSize() {
        return fileSize;
    }
}
