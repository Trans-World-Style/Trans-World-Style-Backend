package com.example.transback.dto;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueDTO {
    private Queue<FileInfo> fileInfoQueue = new LinkedList<>();

    public void addFile(String filename, long fileSize) {
        FileInfo fileInfo = new FileInfo(filename, fileSize);
        fileInfoQueue.add(fileInfo);
    }

    public FileInfo pollFile() {
        return fileInfoQueue.poll();
    }

    public boolean isEmpty() {
        return fileInfoQueue.isEmpty();
    }

    public int size() {
        return fileInfoQueue.size();
    }
    public Queue<FileInfo> getFileInfoQueue() {
        return fileInfoQueue;
    }
}


