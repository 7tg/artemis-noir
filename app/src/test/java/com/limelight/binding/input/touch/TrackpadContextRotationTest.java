package com.limelight.binding.input.touch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyShort;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.limelight.nvstream.NvConnection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class TrackpadContextRotationTest {

    private static short totalX(ArgumentCaptor<Short> captor) {
        short sum = 0;
        for (short v : captor.getAllValues()) sum += v;
        return sum;
    }

    @Test
    public void threeQuarters_appliesSwapAxesAndInvertY() {
        // The Tab S9 Book Cover Keyboard transform (host-side equivalent: swap,iy):
        // a rightward finger swipe must become upward motion after the 270° fix.
        NvConnection conn = mock(NvConnection.class);
        TrackpadContext ctx = new TrackpadContext(conn, 0, false, 100, 100, () -> 3);

        ctx.setPointerCount(1);
        ctx.touchDownEvent(500, 500, 0, true);
        ctx.touchMoveEvent(580, 500, 50);

        ArgumentCaptor<Short> x = ArgumentCaptor.forClass(Short.class);
        ArgumentCaptor<Short> y = ArgumentCaptor.forClass(Short.class);
        verify(conn, atLeastOnce()).sendMouseMove(x.capture(), y.capture());
        assertEquals(0, totalX(x));
        assertTrue("rightward swipe must map to upward motion", totalY(y) < 0);
    }

    @Test
    public void noProvider_keepsOriginalDirection() {
        NvConnection conn = mock(NvConnection.class);
        TrackpadContext ctx = new TrackpadContext(conn, 0, false, 100, 100);

        ctx.setPointerCount(1);
        ctx.touchDownEvent(500, 500, 0, true);
        ctx.touchMoveEvent(580, 500, 50);

        ArgumentCaptor<Short> x = ArgumentCaptor.forClass(Short.class);
        ArgumentCaptor<Short> y = ArgumentCaptor.forClass(Short.class);
        verify(conn, atLeastOnce()).sendMouseMove(x.capture(), y.capture());
        assertTrue("rightward swipe must stay rightward", totalX(x) > 0);
        assertEquals(0, totalY(y));
    }

    private static short totalY(ArgumentCaptor<Short> captor) {
        short sum = 0;
        for (short v : captor.getAllValues()) sum += v;
        return sum;
    }
}
