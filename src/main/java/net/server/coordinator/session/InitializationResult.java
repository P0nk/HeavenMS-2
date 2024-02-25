package net.server.coordinator.session;

import net.server.coordinator.session.SessionCoordinator.AntiMulticlientResult;

enum InitializationResult {
    SUCCESS(AntiMulticlientResult.SUCCESS),
    ALREADY_INITIALIZED(AntiMulticlientResult.REMOTE_PROCESSING),
    TIMED_OUT(AntiMulticlientResult.COORDINATOR_ERROR),
    ERROR(AntiMulticlientResult.COORDINATOR_ERROR);

    private final AntiMulticlientResult antiMulticlientResult;

    InitializationResult(AntiMulticlientResult antiMulticlientResult) {
        this.antiMulticlientResult = antiMulticlientResult;
    }

    public AntiMulticlientResult getAntiMulticlientResult() {
        return antiMulticlientResult;
    }
}
