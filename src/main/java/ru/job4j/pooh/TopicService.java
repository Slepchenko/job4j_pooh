package ru.job4j.pooh;

import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    private Map<String, Map<String, ConcurrentLinkedQueue<String>>> topics = new ConcurrentHashMap<>();

    private Queue<String> topicQueue = new ConcurrentLinkedQueue<>();

    @Override
    public Resp process(Req req) {
        Map<String, ConcurrentLinkedQueue<String>> recipients = new ConcurrentHashMap<>();
        StringBuilder topicStr = new StringBuilder();
        topicStr.append(req.httpRequestType()).append(" ")
                .append(req.getPoohMode()).append(" ")
                .append(req.getSourceName()).append(" ")
                .append(req.getParam());
        if ("POST".equals(req.httpRequestType())) {
            if (topics.containsKey(req.getSourceName())) {
                for (String name : topics.get(req.getSourceName()).keySet()) {
                    topics.get(req.getSourceName()).get(name).add(req.httpRequestType() + " "
                            + req.getPoohMode() + " "
                            + req.getSourceName() + " "
                            + req.getParam());
                    System.out.println(topics.get(req.getSourceName()).get(name));
                    return new Resp(topicStr.toString(), "200");
                }
            }
            return new Resp("", "204");
        }

        if ("GET".equals(req.httpRequestType())) {
            if (topics.putIfAbsent(req.getSourceName(), recipients) == null) {
                topics.get(req.getSourceName()).put(req.getParam(), new ConcurrentLinkedQueue<>());

//                return new Resp(Req.of(topicStr.toString()).getParam(), "200");
            }
            topics.get(req.getSourceName()).putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
//                return new Resp(topics.get(req.getSourceName()).get(req.getParam()).poll(), "200");

            return new Resp(, "200");
        }
        return new Resp("", "204");
    }
}
