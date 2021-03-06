package uk.gov.justice.hmpps.datacompliance.events.listeners.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OffenderPendingDeletionReferralComplete {

    @JsonProperty("batchId")
    private Long batchId;

    @JsonProperty("numberReferred")
    private Long numberReferred;

    @JsonProperty("totalInWindow")
    private Long totalInWindow;
}
