package uk.gov.justice.hmpps.datacompliance.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(value = "sns.provider", matchIfMissing = true, havingValue = "no value set")
public class OffenderDeletionNoOpEventPusher implements OffenderDeletionEventPusher {

    @Override
    public void sendEvent(final String offenderIdDisplay) {
        log.warn("Pretending to push offender deletion for {} to event topic", offenderIdDisplay);
    }
}