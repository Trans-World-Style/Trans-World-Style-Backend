package com.example.transback.dto;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueDTO {
    private Queue<String> jwtQueue = new LinkedList<>(); // JWT를 저장하는 큐

    public void addJWT(String jwt) {
        jwtQueue.add(jwt);
    }

    public String pollJWT() {
        return jwtQueue.poll();
    }

    public boolean isEmpty() {
        return jwtQueue.isEmpty();
    }

    public int size() {
        return jwtQueue.size();
    }
}
