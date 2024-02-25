package scripting;

import net.jcip.annotations.ThreadSafe;

import javax.script.Invocable;
import javax.script.ScriptException;

/**
 * Thread safe wrapper around Invocable.
 * Thread safety is achieved by synchronizing all methods.
 * Needed to get around the restriction that GraalVM imposes on evaluated scripts: no concurrent access allowed.
 */
@ThreadSafe
public class SynchronizedInvocable implements Invocable {
    private final Invocable invocable;

    private SynchronizedInvocable(Invocable invocable) {
        this.invocable = invocable;
    }

    public static Invocable of(Invocable invocable) {
        return new SynchronizedInvocable(invocable);
    }

    @Override
    public synchronized Object invokeMethod(Object thiz, String name, Object... args) throws ScriptException, NoSuchMethodException {
        return invocable.invokeMethod(thiz, name, args);
    }

    @Override
    public synchronized Object invokeFunction(String name, Object... args) throws ScriptException, NoSuchMethodException {
        return invocable.invokeFunction(name, args);
    }

    @Override
    public synchronized <T> T getInterface(Class<T> clasz) {
        return invocable.getInterface(clasz);
    }

    @Override
    public synchronized <T> T getInterface(Object thiz, Class<T> clasz) {
        return invocable.getInterface(thiz, clasz);
    }
}
