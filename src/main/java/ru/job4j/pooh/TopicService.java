package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    private Map<String, Map<String, ConcurrentLinkedQueue<String>>> topics = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Map<String, ConcurrentLinkedQueue<String>> subscribers = new ConcurrentHashMap<>();
        StringBuilder reqStr = new StringBuilder();
        reqStr.append(req.httpRequestType()).append(" ")
                .append(req.getPoohMode()).append(" ")
                .append(req.getSourceName()).append(" ")
                .append(req.getParam());
        if ("POST".equals(req.httpRequestType())) {
            if (topics.containsKey(req.getSourceName())) {
                for (String name : topics.get(req.getSourceName()).keySet()) {
                    topics.get(req.getSourceName()).get(name).add(reqStr.toString());
                }
                return new Resp(reqStr.toString(), "200");
            }
            return new Resp("", "204");
        }
        if ("GET".equals(req.httpRequestType())) {
            if (topics.putIfAbsent(req.getSourceName(), subscribers) == null) {
                topics.get(req.getSourceName()).put(req.getParam(), new ConcurrentLinkedQueue<>());
                return new Resp("", "200");
            }
            if (topics.get(req.getSourceName()).putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>()) == null) {
                return new Resp("", "200");
            }
            if (topics.get(req.getSourceName()).get(req.getParam()).isEmpty()) {
                return new Resp("", "204");
            }
            return new Resp(Req.of(topics.get(req.getSourceName()).get(req.getParam()).poll()).getParam(), "200");
        }
        return new Resp("", "204");
    }
}
