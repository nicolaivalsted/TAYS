/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.smp.order.model;

/**
 *
 * @author jablo
 */
public class Priority {
	private final int p;
	
	public final static Priority SYNCER_PRIORITY = new Priority(3);
	public final static Priority ASU_PRIORITY = new Priority(4);	

	private Priority(int p) {
		if (p < 1 || p > 5) {
			throw new RuntimeException("Priority must be between 1 and 5 (OSS/J)");
		}
		this.p = p;
	}

	public int asInt() {
		return p;
	}
}
