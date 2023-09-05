package com.example.transback;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@Component
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final static Logger LOG = Logger.getGlobal();
    private AtomicInteger uploadProgressPercentage = new AtomicInteger(10);

    private List<WebSocketSession> sessionList = new ArrayList<>(); // 세션 리스트 추가

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // 세션을 등록하고 최초 진행률을 전송합니다.
        sessionList.add(session);
        LOG.info("WebSocket session added: " + session.getId());
        sendUploadProgress(session);

    }



    @Scheduled(fixedRate = 1000) // 1초마다 실행
    private void sendUploadProgress(WebSocketSession session) {
        int progress = uploadProgressPercentage.get();
        String progressMessage = progress + "%";
        LOG.info("Send message: " + uploadProgressPercentage.get() + "%");
        TextMessage textMessage = new TextMessage(progressMessage);
        try {
            session.sendMessage(textMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(progress<100) {
            progress += 10;
            try {
                Thread.sleep(1000); // 10초 딜레이
            } catch (InterruptedException e) {
                // 예외 처리
            }

            try {
                String progressMessage0 = progress + "%";
                LOG.info("Send message: " + uploadProgressPercentage.get() + "%");

                TextMessage textMessage0 = new TextMessage(progressMessage0);
                session.sendMessage(textMessage0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    @Scheduled(fixedRate = 1000) // 1초마다 실행
//    public void updateUploadProgress() {
//        if (uploadProgressPercentage.get() < 100) {
//            LOG.info("Upload progress: " + uploadProgressPercentage.get() + "%");
//            uploadProgressPercentage.addAndGet(10); // 10씩 증가
//            // 등록된 모든 세션에 진행률을 전송합니다.
//            for (WebSocketSession session : sessionList) {
//                sendUploadProgress(session);
//            }
//        }
//    }
}