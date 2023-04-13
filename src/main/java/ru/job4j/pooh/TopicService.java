package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    private Map<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();
    private Map<String, ConcurrentLinkedQueue<String>> recipients = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {


        if ("POST".equals(req.httpRequestType())) {
            queue.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
            queue.get(req.getSourceName()).add(
                    req.httpRequestType() + " " + req.getSourceName() + " " + req.getPoohMode() + " " + req.getParam());
            if (queue.get(req.getSourceName()) == null) {
                return new Resp("", "204");
            }
            return new Resp(req.getParam(), "200");
        }
        if ("GET".equals(req.httpRequestType())) {
            String topic = queue.get(req.getSourceName()).poll();
            ConcurrentLinkedQueue<String> clq = new ConcurrentLinkedQueue<>();
            if ("".equals(topic)) {
                clq.add(topic);
                queue.put(req.getSourceName(), clq);
                recipients.putIfAbsent(req.getParam(), clq);
                return new Resp("", "204");
            }
            return new Resp(req.getSourceName(), "200");
        }
        return new Resp("", "204");
    }
}
