package net.server.task;

import net.server.world.World;

/**
 * @author Shavit
 */
public class TimeoutTask extends BaseTask implements Runnable {

    @Override
    public void run() {
        
    }

    public TimeoutTask(World world) {
        super(world);
    }
}
