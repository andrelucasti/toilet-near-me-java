package io.andrelucas.toiletnearme.toilet.infrastructure.jobs;

import io.andrelucas.toiletnearme.toilet.business.events.*;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletOutboxEntity;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletOutboxSpringRepository;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.annotations.Recurring;
import org.springframework.stereotype.Component;

@Component
public class ToiletOutboxRecurringJob {
    private final ToiletOutboxSpringRepository toiletOutboxSpringRepository;
    private final ToiletEventPublisher toiletEventPublisher;
    private final ToiletEventFactory toiletEventFactory;

    public ToiletOutboxRecurringJob(final ToiletOutboxSpringRepository toiletOutboxSpringRepository,
                                    final ToiletEventPublisher toiletEventPublisher,
                                    final ToiletEventFactory toiletEventFactory) {

        this.toiletOutboxSpringRepository = toiletOutboxSpringRepository;
        this.toiletEventPublisher = toiletEventPublisher;
        this.toiletEventFactory = toiletEventFactory;
    }

    @Recurring(id = "toilet-outbox-recurring-job", cron = "* * * * *")
    @Job(name = "Toilet Outbox Job")
    public void execute() {
        toiletOutboxSpringRepository.findAllByPublishedFalse()
                .forEach(toiletOutboxEntity -> toiletEventPublisher.publish(this.toEvent(toiletOutboxEntity)));
    }

    private ToiletEvent toEvent(final ToiletOutboxEntity toiletOutboxEntity) {
        return toiletEventFactory.createBy(toiletOutboxEntity.getType(), toiletOutboxEntity.getContent());
    }
}
