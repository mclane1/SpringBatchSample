package com.biard.batch.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * Listener for the jab to change the exitStatus.
 */
public class JobListener implements JobExecutionListener {
    public static final String EXIT_STATUS = "JOB_EXIT_STATUS";

    /**
     * Callback after completion of a job. Called after both both successful and
     * failed executions. To perform logic on a particular status, use
     * "if (jobExecution.getStatus() == BatchStatus.X)".
     *
     * @param jobExecution the current {@link JobExecution}
     */
    @Override
    public void afterJob(final JobExecution jobExecution) {
        final Object exitStatus = jobExecution.getExecutionContext().get(EXIT_STATUS);
        if (exitStatus != null) {
            jobExecution.setExitStatus(new ExitStatus(exitStatus.toString(), "Value of the file name JSon in parameter between \"-\" and \"extension\""));
        }
    }

    /**
     * Callback before a job executes.
     *
     * @param jobExecution the current {@link JobExecution}
     */
    @Override
    public void beforeJob(final JobExecution jobExecution) {
        // do nothing
    }
}
