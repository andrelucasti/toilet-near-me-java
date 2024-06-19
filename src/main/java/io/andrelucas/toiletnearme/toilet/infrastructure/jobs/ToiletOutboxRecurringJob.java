package io.andrelucas.toiletnearme.toilet.infrastructure.jobs;

import io.andrelucas.toiletnearme.toilet.business.events.ToiletEventFactory;
import io.andrelucas.toiletnearme.toilet.business.events.ToiletEventPublisher;
import io.andrelucas.toiletnearme.toilet.infrastructure.db.jpa.ToiletOutboxSpringRepository;
import io.opentelemetry.api.OpenTelemetry;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.annotations.Recurring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ToiletOutboxRecurringJob {
    private static final Logger logger = LoggerFactory.getLogger(ToiletOutboxRecurringJob.class);
    private final ToiletOutboxSpringRepository toiletOutboxSpringRepository;
    private final ToiletEventPublisher toiletEventPublisher;
    private final ToiletEventFactory toiletEventFactory;

    public ToiletOutboxRecurringJob(final ToiletOutboxSpringRepository toiletOutboxSpringRepository,
                                    final ToiletEventPublisher toiletEventPublisher,
                                    final ToiletEventFactory toiletEventFactory,
                                    final OpenTelemetry openTelemetry) {

        this.toiletOutboxSpringRepository = toiletOutboxSpringRepository;
        this.toiletEventPublisher = toiletEventPublisher;
        this.toiletEventFactory = toiletEventFactory;
    }

    @Recurring(id = "toilet-outbox-recurring-job", cron = "* * * * *")
    @Job(name = "Toilet Outbox Job")
    public void execute() {
        toiletOutboxSpringRepository.findAllByPublishedFalse()
                .forEach(toiletOutboxEntity -> {
                    final var toiletEvent = toiletEventFactory.createBy(toiletOutboxEntity.getType(), toiletOutboxEntity.getContent());
                    toiletEventPublisher.publish(toiletEvent);
                });

        logger.info("Finished Toilet Outbox Job");
    }
}
