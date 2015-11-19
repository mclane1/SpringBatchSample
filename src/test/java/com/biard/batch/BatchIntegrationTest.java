package com.biard.batch;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Test Classe to test all batch.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:testConfiguration.xml"})
public class BatchIntegrationTest {
    private final Logger logger = LoggerFactory.getLogger(BatchIntegrationTest.class);
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;


//    @Autowired
//    public void setDataSource(DataSource dataSource) {
//
//    }

    @Autowired
    private DataSource dataSource;


    @Test
    public void testJob() throws Exception {
        final String stepJSon = "simpleChunkJSonSample";
        final String lastStringAfterLess = "TEST01";
        final Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
        final File input = new File("src/test/resources/input");
        Assert.assertTrue("This file must exist", input.exists());
        logger.info("Input : {}", input.getAbsolutePath());

        parameters.put("input", new JobParameter(BatchUtils.getAbsoluthPath4SpringBatch(input)));
        final JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParameters(parameters));


        Assert.assertEquals(lastStringAfterLess, jobExecution.getExitStatus().getExitCode());
        boolean find = false;
        for (final StepExecution step : jobExecution.getStepExecutions()) {
            if (stepJSon.equals(step.getStepName())) {
                find = true;
                Assert.assertEquals("We waiting another out for this step.", lastStringAfterLess, step.getExitStatus().getExitCode());
            }
        }
        Assert.assertTrue("We waiting a step with name " + stepJSon, find);
//        JdbcTemplate jdbcTemplate=new JdbcTemplate(dataSource);
//        final List<Order> results = jdbcTemplate.queryForList("SELECT * FROM `order`", Order.class);
//        Assert.assertEquals("Not insert", 1, results.size());
    }
}
