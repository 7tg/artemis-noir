package com.limelight.binding.input;

/**
 * Rotates relative mouse/touchpad deltas by multiples of 90 degrees.
 *
 * Some touchpads (notably Samsung Book Cover Keyboards on Tab S tablets) report
 * relative motion in the device's natural orientation frame instead of the
 * current display frame, which makes the cursor move at a right angle to the
 * finger. Applying a quarter-turn correction restores the expected mapping.
 */
public final class MouseDeltaRotator {
    private MouseDeltaRotator() {}

    public static int rotatedX(int quarters, int dx, int dy) {
        switch (Math.floorMod(quarters, 4)) {
            case 1: return -dy;
            case 2: return -dx;
            case 3: return dy;
            default: return dx;
        }
    }

    public static int rotatedY(int quarters, int dx, int dy) {
        switch (Math.floorMod(quarters, 4)) {
            case 1: return dx;
            case 2: return -dy;
            case 3: return -dx;
            default: return dy;
        }
    }

    public static short rotatedX(int quarters, short dx, short dy) {
        return (short) rotatedX(quarters, (int) dx, (int) dy);
    }

    public static short rotatedY(int quarters, short dx, short dy) {
        return (short) rotatedY(quarters, (int) dx, (int) dy);
    }
}
