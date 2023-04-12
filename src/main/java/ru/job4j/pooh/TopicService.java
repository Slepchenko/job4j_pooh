package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    @Override
    public Resp process(Req req) {
        Map<String, ConcurrentLinkedQueue<String>> topic = new ConcurrentHashMap<>();
        Map<String, ConcurrentLinkedQueue<String>> ownTopic = new ConcurrentHashMap<>();
        ConcurrentLinkedQueue<String> clq = new ConcurrentLinkedQueue<>();
        if ("POST".equals(req.httpRequestType())) {
            topic.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
            topic.get(req.getSourceName()).add(
                    req.httpRequestType() + " " + req.getSourceName() + " " + req.getPoohMode() + " " + req.getParam());
            return new Resp(req.getParam(), "200");
        }
        if ("GET".equals(req.httpRequestType())) {

            ownTopic.putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
            topic.get(req.getParam()).add(
                    req.httpRequestType() + " " + req.getSourceName() + " " + req.getPoohMode() + " " + req.getParam());

        }
        return null;
    }
}
