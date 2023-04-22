package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {

    private Map<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        ConcurrentLinkedQueue<String> clq = new ConcurrentLinkedQueue<>();
        if ("POST".equals(req.httpRequestType())) {
            queue.putIfAbsent(req.getSourceName(), clq);
            queue.get(req.getSourceName()).add(
                    req.httpRequestType() + " " + req.getSourceName() + " " + req.getPoohMode() + " " + req.getParam());
            return new Resp(req.getParam(), "200");
        }
        if ("GET".equals(req.httpRequestType())) {
            if (!queue.isEmpty()) {
                String res = queue.getOrDefault(req.getSourceName(), clq).poll();
                res = Req.of(res).getParam();
                return new Resp(res, "200");
            }
        }
        return new Resp("", "204");
    }
}