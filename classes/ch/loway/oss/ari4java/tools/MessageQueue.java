
package ch.loway.oss.ari4java.tools;

import ch.loway.oss.ari4java.ARI;
import ch.loway.oss.ari4java.generated.Message;
import java.util.LinkedList;
import java.util.List;

/**
 * This is a thread-safe data structure that acts as a communication channel
 * for events.
 *
 * @author lenz
 */
public class MessageQueue {

    public List<Message> lEvents = new LinkedList<Message>();

    /**
     * Adds a message to the queue.
     * 
     * @param msg
     */

    public synchronized void queue( Message msg ) {
        lEvents.add(msg);
    }

    /**
     * Adds an error to the queue, using a specilized message pleceholder.
     * 
     * @param error
     */

    public void queueError( String error ) {
        ErrorMessage err = new ErrorMessage();
        err.setType(error);
        queue(err);
    }

    /**
     * Remove a message from the queue. This return immediately -
     * if no message found, return null.
     * 
     * @return the message just removed.
     */

    public synchronized Message dequeue() {
        if ( lEvents.isEmpty() ) {
            return null;
        } else {
            Message e = lEvents.get(0);
            lEvents.remove(0);
            return e;
        }
    }

    /**
     * Gets a message from the queue. If no message found, wait up to
     * a 'max' of ms polling every 'interval' ms.
     * This method in NOT synchronized - it only locks when calling dequeue().
     *
     * @param max
     * @param interval
     * @return the message.
     */

    public Message dequeueMax( int max, int interval ) {
        long endAfter = System.currentTimeMillis() + max;
        while ( System.currentTimeMillis() < endAfter ) {
            Message m = dequeue();
            if ( m != null ) {
                return m;
            } else {
                ARI.sleep(interval);
            }
        }
        return null;
    }


    /*
     * How many messages are queued?
     */

    public synchronized int size() {
        return lEvents.size();
    }

    /**
     * A placeholder for error messages.
     * 
     */

    public static class ErrorMessage implements Message {

        String type = "";
        String asterisk_id = "";

        @Override
        public void setType(String val) {
            this.type=val;
        }

        @Override
        public String getType() {
            return this.type;
        }

        @Override
        public void setAsterisk_id(String val) {
            this.asterisk_id = val;
        }

        @Override
        public String getAsterisk_id() {
            return this.asterisk_id;
        }
        
    }
}

