/*
 * Copyright (C) 2013 Klaus Reimer <k@ailis.de>
 * See LICENSE.md for licensing information.
 */

package org.usb4java.javax.test;

import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;

/**
 * Runner for TCK tests. TCK tests are only run when the property TCK_TESTS
 * is set.
 * 
 * @author Klaus Reimer (k@ailis.de)
 */
public class TCKRunner extends JUnit38ClassRunner
{
    /**
     * Constructor.
     * 
     * @param testClass
     *            The test class.
     * @throws NoTestsRemainException
     *             When no tests are to be run in this test class.
     */
    public TCKRunner(final Class<?> testClass) throws NoTestsRemainException
    {
        super(testClass);
        try 
        {
	        filter(new Filter()
	        {
	            @Override
	            public boolean shouldRun(Description description)
	            {
	                try
	                {
	                    UsbAssume.assumeTckTestsEnabled();
	                    return true;
	                }
	                catch (AssumptionViolatedException e)
	                {
	                    return false;
	                }
	            }

	            @Override
	            public String describe()
	            {
	                return "TCK tests only when enabled";
	            }
	        });
        }
        catch (NoTestsRemainException e)
        {
            // This is expected when we filter out the whole test class
        }
    }
}
