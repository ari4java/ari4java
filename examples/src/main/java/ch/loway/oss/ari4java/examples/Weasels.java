package ch.loway.oss.ari4java.examples;

import ch.loway.oss.ari4java.ARI;
import ch.loway.oss.ari4java.AriVersion;
import ch.loway.oss.ari4java.generated.AriWSHelper;
import ch.loway.oss.ari4java.generated.models.*;
import ch.loway.oss.ari4java.tools.ARIException;
import ch.loway.oss.ari4java.tools.AriCallback;
import ch.loway.oss.ari4java.tools.RestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Weasels {

    private static final String ARI_APP = "weasels-app";

    private ARI ari;
    private final Logger logger = LoggerFactory.getLogger(Weasels.class);

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.err.println("** Expecting at least 3 arguments:\n  url user pass [ariversion]");
            System.exit(1);
        }
        AriVersion ver = AriVersion.IM_FEELING_LUCKY;
        if (args.length == 4) {
            ver = AriVersion.fromVersionString(args[3]);
        }
        new Weasels().start(args[0], args[1], args[2], ver);
    }

    private void start(String url, String user, String pass, AriVersion ver) {
        logger.info("THE START");
        boolean connected = connect(url, user, pass, ver);
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

    private boolean connect(String url, String user, String pass, AriVersion ver) {
        try {
            ari = ARI.build(url, ARI_APP, user, pass, ver);
            logger.info("ARI Version: {}", ari.getVersion().version());
            AsteriskInfo info = ari.asterisk().getInfo().execute();
            logger.info("AsteriskInfo up since {}", info.getStatus().getStartup_time());
            return true;
        } catch (Throwable t) {
            logger.error("Error: {}", t.getMessage(), t);
        }
        return false;
    }

    private void weasels() throws InterruptedException, ARIException {
        final ExecutorService threadPool = Executors.newFixedThreadPool(10);
        ari.eventsCallback(new AriWSHelper() {
            @Override
            public void onSuccess(Message message) {
                threadPool.execute(() -> super.onSuccess(message));
            }

            @Override
            public void onFailure(RestException e) {
                logger.error("Error: {}", e.getMessage(), e);
                threadPool.shutdown();
            }

            @Override
            protected void onStasisStart(StasisStart message) {
                handleStart(message, threadPool);
            }

            @Override
            protected void onPlaybackFinished(PlaybackFinished message) {
                handlePlaybackFinished(message, threadPool);
            }

        });
        // usually we would not terminate and run indefinitely
        // waiting for 5 minutes before shutting down...
        threadPool.awaitTermination(5, TimeUnit.MINUTES);
        ari.cleanup();
        System.exit(0);
    }

    private void handleStart(StasisStart start, final ExecutorService threadPool) {
        logger.info("Stasis Start Channel: {}", start.getChannel().getId());
        ARI.sleep(300); // a slight pause before we start the playback ...
        try {
            ari.channels().play(start.getChannel().getId(), "sound:weasels-eaten-phonesys")
                    .execute(new AriCallback<Playback>() {
                        @Override
                        public void onSuccess(Playback playback) {
                            logger.debug("Playback success: {}", playback.getId());
                        }

                        @Override
                        public void onFailure(RestException e) {
                            logger.error("Playback Error: {}", e.getMessage(), e);
                            threadPool.shutdown();
                        }
                    });
        } catch (Throwable e) {
            logger.error("Error: {}", e.getMessage(), e);
            threadPool.shutdown();
        }
    }

    private void handlePlaybackFinished(PlaybackFinished playback, final ExecutorService threadPool) {
        logger.info("PlaybackFinished - {} {}", playback.getPlayback().getId(), playback.getPlayback().getTarget_uri());
        if (playback.getPlayback().getTarget_uri().indexOf("channel:") == 0) {
            try {
                String chanId = playback.getPlayback().getTarget_uri().split(":")[1];
                logger.info("Hangup Channel: {}", chanId);
                ARI.sleep(300); // a slight pause before we hangup ...
                ari.channels().hangup(chanId).execute(new AriCallback<Void>() {
                    @Override
                    public void onSuccess(Void a) {
                        logger.debug("Hangup success");
                        threadPool.shutdown();
                    }

                    @Override
                    public void onFailure(RestException e) {
                        logger.error("Hangup Error: {}", e.getMessage(), e);
                        threadPool.shutdown();
                    }
                });
            } catch (Throwable e) {
                logger.error("Error: {}", e.getMessage(), e);
            }
        } else {
            logger.error("Cannot handle URI - {}", playback.getPlayback().getTarget_uri());
        }
    }

}

