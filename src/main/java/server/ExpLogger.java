package server;

import config.YamlConfig;
import tools.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class ExpLogger {
    private static final LinkedBlockingQueue<ExpLogRecord> expLoggerQueue = new LinkedBlockingQueue<>();
    private static final short EXP_LOGGER_THREAD_SLEEP_DURATION_SECONDS = 60;
    private static final short EXP_LOGGER_THREAD_SHUTDOWN_WAIT_DURATION_MINUTES = 5;

    public record ExpLogRecord(int worldExpRate, int expCoupon, long gainedExp, int currentExp,Timestamp expGainTime, int charid) {}

    public static void putExpLogRecord(ExpLogRecord expLogRecord) {
        try {
            expLoggerQueue.put(expLogRecord);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static private ScheduledExecutorService schdExctr = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setPriority(Thread.MIN_PRIORITY);
            return t;
        }
    });

    private static Runnable saveExpLoggerToDBRunnable = new Runnable() {
        @Override
        public void run() {
            try (Connection con = DatabaseConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement("INSERT INTO characterexplogs (world_exp_rate, exp_coupon, gained_exp, current_exp, exp_gain_time, charid) VALUES (?, ?, ?, ?, ?, ?)")) {

                List<ExpLogRecord> drainedExpLogs = new ArrayList<>();
                expLoggerQueue.drainTo(drainedExpLogs);
                for (ExpLogRecord expLogRecord : drainedExpLogs) {
                    ps.setInt(1, expLogRecord.worldExpRate);
                    ps.setInt(2, expLogRecord.expCoupon);
                    ps.setLong(3, expLogRecord.gainedExp);
                    ps.setInt(4, expLogRecord.currentExp);
                    ps.setTimestamp(5, expLogRecord.expGainTime);
                    ps.setInt(6, expLogRecord.charid);
                    ps.addBatch();
                }
                ps.executeBatch();
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
        }
    };


    private static void startExpLogger() {
        schdExctr.scheduleWithFixedDelay(saveExpLoggerToDBRunnable, EXP_LOGGER_THREAD_SLEEP_DURATION_SECONDS, EXP_LOGGER_THREAD_SLEEP_DURATION_SECONDS, SECONDS);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
           stopExpLogger();
        }));
    }

    private static boolean stopExpLogger() {
        schdExctr.shutdown();
        try {
            schdExctr.awaitTermination(EXP_LOGGER_THREAD_SHUTDOWN_WAIT_DURATION_MINUTES, MINUTES);
            Thread runThreadBeforeShutdown = new Thread(saveExpLoggerToDBRunnable);
            runThreadBeforeShutdown.setPriority(Thread.MIN_PRIORITY);
            runThreadBeforeShutdown.start();
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    static {
        if (YamlConfig.config.server.USE_EXP_GAIN_LOG) {
            startExpLogger();
        }
    }
}
