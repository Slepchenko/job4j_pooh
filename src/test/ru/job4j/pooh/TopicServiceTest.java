package ru.job4j.pooh;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TopicServiceTest {

    @Test
    public void whenTopic() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        /* Режим topic. Подписываемся на топик weather. client407. */
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        /* Режим topic. Добавляем данные в топик weather. */
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client407. */
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        /* Режим topic. Пытаемся забрать данные из индивидуальной очереди в топике weather. Очередь client6565.
        Эта очередь отсутствует, т.к. client6565 еще не был подписан, поэтому он получит пустую строку.
        Будет создана индивидуальная очередь для client6565 */
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );

        assertThat(result1.text(), is("temperature=18"));
        assertThat(result2.text(), is(""));
    }

    @Test
    public void whenTwoTopics() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForPublisher1 = "event=Sheveluch_eruption";
        String paramForPublisher2 = "temperature=5";
        String paramForPublisher3 = "temperature=30";
        String paramForSubscriber = "client407";
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber)
        );
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        topicService.process(
                new Req("GET", "topic", "news", paramForSubscriber)
        );
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher2)
        );
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher3)
        );
        topicService.process(
                new Req("POST", "topic", "news", paramForPublisher1)
        );
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber)
        );
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber)
        );
        Resp result3 = topicService.process(
                new Req("GET", "topic", "news", paramForSubscriber)
        );

        assertThat(result1.text(), is("temperature=18"));
        assertThat(result2.text(), is("temperature=5"));
        assertThat(result3.text(), is("event=Sheveluch_eruption"));
    }

    @Test
    public void whenThreeSubscribersInOneTopics() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        String paramForSubscriber3 = "client999";
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber3)
        );
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        Resp result = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        assertThat(result.text(), is("temperature=18"));
    }

    @Test
    public void whenNoDataTopics() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";

        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );

        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );

        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );

        Resp result2 = topicService.process(
                new Req("GET", "topic", "news", paramForSubscriber1)
        );
        assertThat(result1.text(), is(""));
        assertThat(result2.text(), is(""));
    }
}