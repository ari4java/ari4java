package ch.loway.oss.ari4java.sandbox.sample;

import ch.loway.oss.ari4java.ARI;
import ch.loway.oss.ari4java.AriVersion;
import ch.loway.oss.ari4java.generated.models.AsteriskInfo;
import ch.loway.oss.ari4java.generated.models.Message;
import ch.loway.oss.ari4java.generated.models.PlaybackFinished;
import ch.loway.oss.ari4java.generated.models.StasisStart;
import ch.loway.oss.ari4java.tools.AriConnectionEvent;
import ch.loway.oss.ari4java.tools.AriWSCallback;
import ch.loway.oss.ari4java.tools.RestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Weasels {

    private static final String ARI_APP = "weasels-app";

    private ARI ari;
    private Logger logger = LoggerFactory.getLogger(Weasels.class);

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Expecting 3 arguments: url user pass");
            System.exit(1);
        }
        new Weasels().start(args[0], args[1], args[2]);
    }

    private void start(String url, String user, String pass) {
        logger.info("THE START");
        boolean connected = connect(url, user, pass);
        if (connected) {
            try {
                weasels();
            } catch (Throwable t) {
                logger.error("Error: {}", t.getMessage(), t);
            } finally {
                logger.info("ARI cleanup");
                ari.cleanup();
            }
        }
        logger.info("THE END");
    }

    private boolean connect(String url, String user, String pass) {
        try {
            ari = ARI.build(url, ARI_APP, user, pass, AriVersion.IM_FEELING_LUCKY);
            logger.info("ARI Version: {}", ari.getVersion().version());
            AsteriskInfo info = ari.asterisk().getInfo().execute();
            logger.info("AsteriskInfo up since {}", info.getStatus().getStartup_time());
            return true;
        } catch (Throwable t) {
            logger.error("Error: {}", t.getMessage(), t);
        }
        return false;
    }

    private void weasels() throws InterruptedException, RestException {
        final ExecutorService threadPool = Executors.newFixedThreadPool(10);
        ari.events().eventWebsocket(ARI_APP).execute(new AriWSCallback<Message>() {
            @Override
            public void onSuccess(Message message) {
                threadPool.execute(() -> {
                    try {
                        if (message instanceof StasisStart) {
                            handleStart((StasisStart) message);
                        } else if (message instanceof PlaybackFinished) {
                            handlePlaybackFinished((PlaybackFinished) message);
                        } else {
                            logger.info("Unhandled Event - {}", message.getType());
                        }
                    } catch (Throwable e) {
                        logger.error("Error: {}", e.getMessage(), e);
                    }
                });
            }

            @Override
            public void onFailure(RestException e) {
                logger.error("Error: {}", e.getMessage(), e);
                threadPool.shutdown();
            }

            @Override
            public void onConnectionEvent(AriConnectionEvent event) {
                logger.debug(event.name());
            }
        });
        // usually we would not terminate and run indefinitely
        // waiting for 5 minutes before shutting down...
        threadPool.awaitTermination(5, TimeUnit.MINUTES);
    }

    private void handleStart(StasisStart start) throws RestException {
        logger.info("Stasis Start Channel: {}", start.getChannel().getId());
        ARI.sleep(300); // a slight pause before we start the playback ...
        ari.channels().play(start.getChannel().getId(), "sound:weasels-eaten-phonesys").execute();
    }

    private void handlePlaybackFinished(PlaybackFinished playback) throws RestException {
        logger.info("PlaybackFinished - {}", playback.getPlayback().getTarget_uri());
        if (playback.getPlayback().getTarget_uri().indexOf("channel:") == 0) {
            String chanId = playback.getPlayback().getTarget_uri().split(":")[1];
            logger.info("Hangup Channel: {}", chanId);
            ARI.sleep(300); // a slight pause before we hangup ...
            ari.channels().hangup(chanId).execute();
        } else {
            logger.error("Cannot handle URI - {}", playback.getPlayback().getTarget_uri());
        }
    }

}

