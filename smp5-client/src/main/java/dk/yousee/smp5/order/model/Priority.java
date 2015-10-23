/**
 * 
 */
package dk.yousee.smp5.order.model;


/**
 * @author m64746
 *
 *         Date: 14/10/2015 Time: 13:20:41
 */
public class Priority {
	private final int p;

	public final static Priority SYNCER_PRIORITY = new Priority(3);
	public final static Priority ASU_PRIORITY = new Priority(4);

	private Priority(int p) {
		if (p < 1 || p > 5) {
			throw new RuntimeException(
					"Priority must be between 1 and 5 (OSS/J)");
		}
		this.p = p;
	}

	public int asInt() {
		return p;
	}

}
