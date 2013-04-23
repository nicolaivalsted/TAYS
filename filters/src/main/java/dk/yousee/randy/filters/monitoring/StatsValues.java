/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.filters.monitoring;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author jablo
 */
public class StatsValues {
    AtomicInteger executionTime = new AtomicInteger();
    AtomicInteger calls = new AtomicInteger();
    AtomicInteger retTotal = new AtomicInteger();
    AtomicInteger ret200 = new AtomicInteger();
    AtomicInteger ret300 = new AtomicInteger();
    AtomicInteger ret400 = new AtomicInteger();
    AtomicInteger ret500 = new AtomicInteger();
}

