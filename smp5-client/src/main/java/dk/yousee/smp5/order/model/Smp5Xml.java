package dk.yousee.smp5.order.model;

public final class Smp5Xml {
	private String request;
	private String response;

	/**
	 * @param request
	 *            the string made by convertRequest
	 * @param response
	 *            XML string with what came back from SMP
	 */
	public Smp5Xml(String request, String response) {
		this.request = request;
		this.response = response;
	}

	/**
	 * @return Request send to SMP. It is XML as a string
	 */
	public String getRequest() {
		return request;
	}

	/**
	 * @return response that came back from SMP. It is XML as a string
	 */
	public String getResponse() {
		return response;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("{\"request\":\"").append(getRequest()).append('"');
		if (getResponse() != null)
			sb.append(", \"response\":\"").append(response).append('"');
		sb.append('}');
		return sb.toString();
	}

}
