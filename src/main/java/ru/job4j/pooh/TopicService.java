package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    private Map<String, Map<String, ConcurrentLinkedQueue<String>>> recipients = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        if ("POST".equals(req.httpRequestType())) {
            if (recipients.containsKey(req.getSourceName())) {
                for (Map.Entry<String, Map<String, ConcurrentLinkedQueue<String>>> r : recipients.entrySet()) {
                    for (String t : r.getValue().get(req.getSourceName())) {
                    }
                }
            }
        }
        if ("GET".equals(req.httpRequestType())) {

        }
        return new Resp("", "204");
    }
}
