package com.changhong.sei.core.log.extend;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/6/21 10:38
 */
public class LogMarkerFilter extends AbstractMatcherFilter<ILoggingEvent> {

    private Marker markerToMatch;

    @Override
    public void start() {
        if (markerToMatch != null) {
            super.start();
        } else {
            addError("The marker property must be set for [" + getName() + "]");
        }
    }

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (!isStarted()) {
            return FilterReply.NEUTRAL;
        }
        Marker marker = event.getMarker();
        if (marker == null) {
            return onMismatch;
        }

        if (marker.contains(markerToMatch)) {
            return onMatch;
        } else {
            return onMismatch;
        }
    }

    /**
     * The marker to match in the event.
     *
     * @param markerStr
     */
    public void setMarker(String markerStr) {
        if (markerStr != null) {
            this.markerToMatch = MarkerFactory.getMarker(markerStr);
        }
    }
}
