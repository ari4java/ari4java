package ch.loway.oss.ari4java.sandbox.sample;

import ch.loway.oss.ari4java.ARI;
import ch.loway.oss.ari4java.AriVersion;
import ch.loway.oss.ari4java.generated.models.*;
import ch.loway.oss.ari4java.tools.AriCallback;
import ch.loway.oss.ari4java.tools.RestException;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Weasels {

    private static final String ARI_URL  = "http://192.168.99.100:18088/";
    private static final String ARI_USER = "ari4java";
    private static final String ARI_PASS = "yothere";
    private static final String ARI_APP = "weasels-app";

    private ARI ari;

    public static void main(String[] args) {
        new Weasels().start();
    }

    private void start() {
        log("THE START");
        boolean connected = connect();
        if (connected) {
            try {
                weasels();
            } catch (Throwable t) {
                log("Error: " + t.getMessage());
                t.printStackTrace();
            } finally {
                log("ARI cleanup");
                ari.cleanup();
            }
        }
        log("THE END");
    }

    private void log(String message) {
        String time = LocalDateTime.now().toString();
        String thread = Thread.currentThread().getName();
        System.out.println(time + " - [" + thread + "] - " + message);
    }

    private boolean connect() {
        try {
            ari = ARI.build(ARI_URL, ARI_APP, ARI_USER, ARI_PASS, AriVersion.IM_FEELING_LUCKY);
            log("Detect Server ARI Version: " + ari.getVersion().version());
            AsteriskInfo info = ari.asterisk().getInfo().execute();
            log("System up since " + info.getStatus().getStartup_time());
            return true;
        } catch (Throwable t) {
            log("Error: " + t.getMessage());
            t.printStackTrace();
        }
        return false;
    }

    private void weasels() throws InterruptedException, RestException {
        final ExecutorService threadPool = Executors.newFixedThreadPool(10);
        ari.events().eventWebsocket(ARI_APP).execute(new AriCallback<Message>() {
            @Override
            public void onSuccess(Message message) {
                threadPool.execute(() -> {
                    try {
                        if (message instanceof StasisStart) {
                            handleStart((StasisStart) message);
                        } else if (message instanceof PlaybackFinished) {
                            handlePlaybackFinished((PlaybackFinished) message);
                        } else {
                            log("Unhandled Event - " + message.getType());
                        }
                    } catch (Throwable e) {
                        log("Error: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            }
            @Override
            public void onFailure(RestException e) {
                log("Error: " + e.getMessage());
                e.printStackTrace();
            }
        });
        // usually we would not terminate and run indefinitely
        // waiting for 5 minutes before shutting down...
        threadPool.awaitTermination(5, TimeUnit.MINUTES);
    }

    private void handleStart(StasisStart start) throws RestException {
        log("Stasis Start Channel: " + start.getChannel().getId());
        ARI.sleep(300); // a slight pause before we start the playback ...
        ari.channels().play(start.getChannel().getId(), "sound:weasels-eaten-phonesys").execute();
    }

    private void handlePlaybackFinished(PlaybackFinished playback) throws RestException {
        log("PlaybackFinished - " + playback.getPlayback().getTarget_uri());
        if (playback.getPlayback().getTarget_uri().indexOf("channel:") == 0) {
            String chanId = playback.getPlayback().getTarget_uri().split(":")[1];
            log("Hangup Channel: " + chanId);
            ARI.sleep(300); // a slight pause before we hangup ...
            ari.channels().hangup(chanId).execute();
        } else {
            log("Error: Cannot handle URI - " + playback.getPlayback().getTarget_uri());
        }
    }

}

