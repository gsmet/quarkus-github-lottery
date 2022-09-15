package io.quarkus.github.lottery.notification;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.github.lottery.draw.DrawRef;
import io.quarkus.github.lottery.draw.LotteryReport;
import io.quarkus.github.lottery.github.Issue;

@ApplicationScoped
public class NotificationFormatter {

    public String formatToTopicText(DrawRef drawRef, String username) {
        return username + "'s report for " + drawRef.repositoryName();
    }

    public MarkdownNotification formatToMarkdown(LotteryReport report) {
        // TODO produce better output, maybe with Qute templates?
        String repoName = report.drawRef().repositoryName();
        LocalDate date = report.drawRef().instant().atZone(report.timezone()).toLocalDate();
        return new MarkdownNotification(report.username(),
                "Hey @" + report.username() + ", here's your report for " + repoName + " on " + date + ".\n"
                        + renderCategory("Triage", report.issuesToTriage()));
    }

    private String renderCategory(String title, List<Issue> issues) {
        StringBuilder builder = new StringBuilder("# ").append(title).append('\n');
        if (issues.isEmpty()) {
            builder.append("No issues in this category this time.\n");
        } else {
            builder.append(issues.stream()
                    .map(issue -> issue.url().toString())
                    .collect(Collectors.joining("\n - ", " - ", "\n")));
        }
        return builder.toString();
    }

}
