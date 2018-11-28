package com.edd.app.service;

import com.edd.app.service.MyAppImpl;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/context-app.xml" })
@TransactionConfiguration
public class MyAppImplTest {

    @Autowired
    private MyAppImpl underTest;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetSpringProperty() throws Exception {
        // given

        // when
        underTest.testMethod();

        // then
        Assert.assertNotNull(underTest);
    }


}
