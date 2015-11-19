package com.biard.batch;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Main of the class : {@see App#main()}
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private App() {
        //do nothing
    }

    /**
     * To create database, execute src/test/resources/sripts/script-create-not-test.sql.<BR/>
     * If database spring-batch already install, execute TEST PART only.<BR/>
     *
     * @param args
     */
    public static void main(final String[] args) throws IOException {
        logger.info("Batch Execution");
        final File dirSrcTestResourcesInput = getDirectory(null, "src", "test", "resources", "input");
        logger.info("dirSrcTestResourcesInput : {}", dirSrcTestResourcesInput.getAbsolutePath());

        // Fisrt execution empty
        final File input1 = createTimeDir("input1");
        final JobExecution execution1 = executeJob("hellojob", input1, "batch.xml");
        logger.info("Exit Status : {}", execution1.getExitStatus().getExitCode());
        // Second execution with csv
        final File input2 = createTimeDir("input2");
        FileUtils.copyFileToDirectory(new File(dirSrcTestResourcesInput, "orders.csv"), input2);
        final JobExecution execution2 = executeJob("hellojob", input2, "batch.xml");
        logger.info("Exit Status : {}", execution2.getExitStatus().getExitCode());
        // Third execution with json
        final File input3 = createTimeDir("input3");
        FileUtils.copyFileToDirectory(new File(dirSrcTestResourcesInput, "sample-TEST01.json"), input3);
        final JobExecution execution3 = executeJob("hellojob", input3, "batch.xml");
        logger.info("Exit Status : {}", execution3.getExitStatus().getExitCode());
        // End
        logger.info("Finished Execution of Batch Job");

    }

    /**
     * Create the directory wanted in {@code target/test-classes/<prefix><yyyyMMddhhmmss>}
     *
     * @param prefix the prefix name.
     * @return the directory wanted.
     */
    private static File createTimeDir(final String prefix) {
        final Date date = Calendar.getInstance().getTime();
        final DateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
        final File input = getDirectory(null, "target", "test-classes", prefix + format.format(date));
        logger.info("Create dir : {}", input.getAbsolutePath());
        return input;
    }

    /**
     * Construct File with params and create directory if not exist.
     *
     * @param parent the parent directory.
     * @param sDirs  The name of directorys.
     * @return File : the directory wanted.
     */
    private static File getDirectory(final File parent, final String... sDirs) {
        final File fDir;
        if (sDirs == null || sDirs.length == 0) {
            fDir = parent;
        } else {
            final String sDir = sDirs[0];
            final String[] sDirSuite;
            if (sDirs.length == 1) {
                sDirSuite = null;
            } else {
                sDirSuite = Arrays.copyOfRange(sDirs, 1, sDirs.length);
            }
            final File temp;
            if (parent == null) {
                temp = new File(sDir);
            } else {
                temp = new File(parent, sDir);
            }
            if (!temp.exists()) {
                temp.setReadable(true, false);
                temp.mkdirs();
            }

            fDir = getDirectory(temp, sDirSuite);
        }

        return fDir;
    }

    /**
     * Execute the job with param jobName, with the parameter input in input, and files conf in param config.
     *
     * @param jobName the name of the job
     * @param input   the directory input.
     * @param config  the name of file configurations.
     * @return the result of JobExecution.
     */
    private static JobExecution executeJob(final String jobName, final File input, final String... config) {
        final ApplicationContext context = new ClassPathXmlApplicationContext(config);
        final JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
        final Job job = (Job) context.getBean(jobName);
        final Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
        parameters.put("input", new JobParameter(BatchUtils.getAbsoluthPath4SpringBatch(input)));
        try {
            return jobLauncher.run(job, new JobParameters(parameters));
        } catch (final Exception e) {
            logger.error("Can't execute job with params {}, {}, {}", jobName, input.getAbsolutePath(), config);
            logger.error("Exception was thrown", e);
        }
        return null;
    }

}
