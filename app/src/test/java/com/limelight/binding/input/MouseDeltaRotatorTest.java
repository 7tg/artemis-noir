package com.limelight.binding.input;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MouseDeltaRotatorTest {

    private void assertRotated(int quarters, int dx, int dy, int expectedX, int expectedY) {
        assertEquals((short) expectedX, MouseDeltaRotator.rotatedX(quarters, (short) dx, (short) dy));
        assertEquals((short) expectedY, MouseDeltaRotator.rotatedY(quarters, (short) dx, (short) dy));
    }

    @Test
    public void zeroQuarters_isIdentity() {
        assertRotated(0, 5, -3, 5, -3);
    }

    @Test
    public void oneQuarter_mapsLeftwardDeltaToUpward() {
        // Samsung Book Cover Keyboard bug: physically swiping up arrives as a
        // leftward delta (-1, 0). One quarter turn must restore it to up (0, -1).
        assertRotated(1, -1, 0, 0, -1);
        assertRotated(1, 7, 2, -2, 7);
    }

    @Test
    public void twoQuarters_invertsBothAxes() {
        assertRotated(2, 4, -9, -4, 9);
    }

    @Test
    public void threeQuarters_isInverseOfOneQuarter() {
        short dx = 13, dy = -6;
        short rx = MouseDeltaRotator.rotatedX(1, dx, dy);
        short ry = MouseDeltaRotator.rotatedY(1, dx, dy);
        assertEquals(dx, MouseDeltaRotator.rotatedX(3, rx, ry));
        assertEquals(dy, MouseDeltaRotator.rotatedY(3, rx, ry));
    }

    @Test
    public void quartersWrapAroundModuloFour() {
        assertRotated(4, 5, -3, 5, -3);
        assertRotated(5, -1, 0, 0, -1);
    }
}
