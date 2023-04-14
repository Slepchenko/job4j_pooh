package ru.job4j.pooh;

public class Req {

    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;
    private final static String POST = "POST";
    private final static String GET = "GET";

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        String[] lines = content.split(System.lineSeparator());
        if (lines.length == 1) {
            String[] parameters = lines[0].split(" ");
            return new Req(parameters[0], parameters[1], parameters[2], parameters[3]);
        }
        String type = lines[0].split(" ")[0];
        String[] modes = lines[0].split(" ")[1].split("/");
        Req req = null;
        if (POST.equals(type)) {
            req = new Req(type, modes[1], modes[2], lines[lines.length - 1]);
        }
        if (GET.equals(type)) {
            String param = "";
            if (modes.length > 3) {
                param = modes[3];
            }
            req = new Req(type, modes[1], modes[2], param);
        }
        return req;
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
