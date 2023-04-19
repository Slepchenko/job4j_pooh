package ru.job4j.pooh;

import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    private Map<String, Map<String, ConcurrentLinkedQueue<String>>> topics = new ConcurrentHashMap<>();
    private Map<String, ConcurrentLinkedQueue<String>> recipients = new ConcurrentHashMap<>();
    private Queue<String> topicQueue = new ConcurrentLinkedQueue<>();

    @Override
    public Resp process(Req req) {

        StringBuilder topicStr = new StringBuilder();
        topicStr.append(req.httpRequestType()).append(" ")
                .append(req.getPoohMode()).append(" ")
                .append(req.getSourceName()).append(" ")
                .append(req.getParam());
        if ("POST".equals(req.httpRequestType())) {
            if (topics.containsKey(req.getSourceName())) {
                for (Map.Entry<String, Map<String, ConcurrentLinkedQueue<String>>> r : topics.entrySet()) {
                    for (String t : r.getValue().get(req.getSourceName())) {
                    }
                }
            }
        }
        if ("GET".equals(req.httpRequestType())) {

            if (topics.putIfAbsent(req.getSourceName(), recipients) != null) {

            }
        }
        return new Resp("", "204");
    }
}
