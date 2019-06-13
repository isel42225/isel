import java.util.concurrent.atomic.AtomicInteger;

public class UnsafeMessageBox<T> {

    private class MsgHolder {
        final T msg;
        final AtomicInteger lives;

        public MsgHolder(T msg, int lives) {
            this.msg = msg;
            this.lives = new AtomicInteger(lives);
        }
    }

    /**
     * volatile is needed because visibility must be ensured to not "lose" publishes
     */
    private volatile MsgHolder msgHolder = null;

    public void publish(T m, int lvs) {
        msgHolder = new MsgHolder(m, lvs);
    }

    public T tryConsume() {
        MsgHolder observedMsgHolder = this.msgHolder;
        if(observedMsgHolder == null) return null;
        int observedLives ;
        do{
            observedLives = observedMsgHolder.lives.get();
            if(observedLives <= 0) return null;
        }while(!observedMsgHolder.lives.compareAndSet(observedLives, observedLives - 1));
        return observedMsgHolder.msg;
    }
}
