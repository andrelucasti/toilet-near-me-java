package io.andrelucas.toiletnearme.toilet.business.events;

public interface ToiletEventPublisher {
    void publish(ToiletEvent event);
}
