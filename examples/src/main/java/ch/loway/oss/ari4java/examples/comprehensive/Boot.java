package ch.loway.oss.ari4java.examples.comprehensive;

import ch.loway.oss.ari4java.AriVersion;

public class Boot {

    public static void main(String[] args) throws Exception {
        if (args.length < 4) {
            System.err.println("** Expecting at least 4 arguments:\n  port url user pass [ariversion]");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        AriVersion ver = AriVersion.IM_FEELING_LUCKY;
        if (args.length == 5) {
            ver = AriVersion.fromVersionString(args[4]);
        }
        Asterisk asterisk = new Asterisk(args[1], args[2], args[3], ver);
        WebServer webserver = new WebServer(port, asterisk);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            asterisk.stop();
            webserver.stop();
        }));
        if (!asterisk.start()) {
            System.exit(1);
        }
        webserver.start();
    }

}
