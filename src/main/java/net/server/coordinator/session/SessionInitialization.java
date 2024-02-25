package net.server.coordinator.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Manages session initialization using remote host (ip address).
 */
public class SessionInitialization {
    private final static Logger log = LoggerFactory.getLogger(SessionInitialization.class);
    private static final int MAX_INIT_TRIES = 2;
    private static final long RETRY_DELAY_MILLIS = 1777;

    private final Set<String> remoteHostsInInitState = new HashSet<>();
    private final List<Lock> locks = new ArrayList<>(100);

    SessionInitialization() {
        for (int i = 0; i < 100; i++) {
            locks.add(new ReentrantLock());
        }
    }

    private Lock getLock(String remoteHost) {
        return locks.get(Math.abs(remoteHost.hashCode()) % 100);
    }

    /**
     * Try to initialize a session. Should be called <em>before</em> any session initialization procedure.
     *
     * @return InitializationResult.SUCCESS if initialization was successful.
     * If it was successful, finalize() needs to be called shortly after,
     * or else the initialization will be left hanging in a bad state,
     * which means any subsequent initialization from the same remote host will fail.
     */
    public InitializationResult initialize(String remoteHost) {
        final Lock lock = getLock(remoteHost);
        try {
            int tries = 0;
            while (true) {
                if (lock.tryLock()) {
                    try {
                        if (remoteHostsInInitState.contains(remoteHost)) {
                            return InitializationResult.ALREADY_INITIALIZED;
                        }

                        remoteHostsInInitState.add(remoteHost);
                    } finally {
                        lock.unlock();
                    }

                    break;
                } else {
                    if (tries++ == MAX_INIT_TRIES) {
                        return InitializationResult.TIMED_OUT;
                    }

                    Thread.sleep(RETRY_DELAY_MILLIS);
                }
            }
        } catch (Exception e) {
            log.error("Failed to initialize session.", e);
            return InitializationResult.ERROR;
        }

        return InitializationResult.SUCCESS;
    }

    /**
     * Finalize an initialization. Should be called <em>after</em> any session initialization procedure.
     */
    public void finalize(String remoteHost) {
        final Lock lock = getLock(remoteHost);
        lock.lock();
        try {
            remoteHostsInInitState.remove(remoteHost);
        } finally {
            lock.unlock();
        }
    }
}
