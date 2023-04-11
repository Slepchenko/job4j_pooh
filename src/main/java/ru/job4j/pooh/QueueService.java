package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    @Override
    public Resp process(Req req) {
        Map<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();
        ConcurrentLinkedQueue<String> clq = new ConcurrentLinkedQueue<>();
        if ("POST".equals(req.httpRequestType())) {
            queue.putIfAbsent(req.getSourceName(), clq);
            queue.get(req.getSourceName()).add(
                    req.httpRequestType() + " " + req.getSourceName() + " " + req.getPoohMode() + " " + req.getParam());
            return new Resp(req.getParam(), "200");
        }
        if ("GET".equals(req.httpRequestType())) {
            return new Resp(queue.getOrDefault(req.getSourceName(), clq).poll(), "200");
        }
        return new Resp("", "204");
    }
}
