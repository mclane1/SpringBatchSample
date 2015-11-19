package com.biard.batch.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * Listener for a Step to change exitStatus of a Step.
 * Can change the exitStatus of the step. In the Tasklet put String value in the ExecutionContext with the ChangeExitStatusExecutionListener.EXIT_STATUS key.
 */
public class ChangeExitStatusExecutionListener implements StepExecutionListener {
    public static final String EXIT_STATUS = "EXIT_STATUS";

    /**
     * Give a listener a chance to modify the exit status from a step. The value
     * returned will be combined with the normal exit status using
     * {@link ExitStatus#and(ExitStatus)}.
     * <p/>
     * Called after execution of step's processing logic (both successful or
     * failed). Throwing exception in this method has no effect, it will only be
     * logged.
     *
     * @param stepExecution
     * @return an {@link ExitStatus} to combine with the normal value. Return
     * null to leave the old value unchanged.
     */
    @Override
    public ExitStatus afterStep(final StepExecution stepExecution) {
        final ExitStatus endValue;
        final String exitStatus = (String) stepExecution.getExecutionContext().get(EXIT_STATUS);
        if (exitStatus == null) {
            endValue = ExitStatus.COMPLETED;
        } else {
            endValue = new ExitStatus(exitStatus);
            // change status of job
            stepExecution.getJobExecution().getExecutionContext().put(JobListener.EXIT_STATUS, exitStatus);
        }

        return endValue;
    }


    /**
     * Initialize the state of the listener with the {@link StepExecution} from
     * the current scope.
     *
     * @param stepExecution
     */
    @Override
    public void beforeStep(final StepExecution stepExecution) {
        // do nothing
    }

}
