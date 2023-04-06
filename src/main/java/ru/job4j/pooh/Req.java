package ru.job4j.pooh;

import java.util.Arrays;

public class Req {

    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        String[] lines = content.split(System.lineSeparator());
        String[] types = lines[0].split(" ");
        String[] modes = types[1].split("/");
        String param = null;
        if (lines.length > 4 && lines[lines.length - 2].equals("")) {
            param = lines[lines.length -1];
        }

        return new Req(types[0], modes[1], modes[2], param);
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}
