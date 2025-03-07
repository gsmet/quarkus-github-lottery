package io.quarkus.github.lottery.config;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(ignoreNested = false) // For deserialization from the GitHub repository
public record LotteryConfig(
        @JsonProperty(required = true) NotificationsConfig notifications,
        @JsonProperty(required = true) BucketsConfig buckets,
        List<ParticipantConfig> participants) {

    public static final String FILE_NAME = "quarkus-github-lottery.yml";

    public record BucketsConfig(
            @JsonProperty(required = true) TriageBucketConfig triage) {

        public record TriageBucketConfig(
                @JsonProperty(required = true) String needsTriageLabel,
                @JsonProperty(required = true) Duration notificationExpiration) {
        }
    }

    public record NotificationsConfig(
            @JsonProperty(required = true) CreateIssuesConfig createIssues) {
        public record CreateIssuesConfig(
                @JsonProperty(required = true) String repository) {
        }
    }

    public record ParticipantConfig(
            @JsonProperty(required = true) String username,
            @JsonProperty(required = true) @JsonDeserialize(as = TreeSet.class) Set<DayOfWeek> days,
            Optional<ZoneId> timezone,
            ParticipationConfig triage) {

    }

    public record ParticipationConfig(
            @JsonProperty(required = true) int maxIssues) {
    }

}
