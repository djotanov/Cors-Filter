package com.thetransactioncompany.cors;


/**
 * Unsupported HTTP method exception.
 *
 * @author <a href="http://dzhuvinov.com">Vladimir Dzhuvinov</a>
 * @version 1.3.1 (2010-09-27)
 */
public class UnsupportedHTTPMethodException extends CORSException {

	
	/**
	 * The requested HTTP method.
	 */
	private HTTPMethod method = null;
	
	
	/**
	 * Creates a new unsupported HTTP method exception with the specified 
	 * message.
	 *
	 * @param message The message.
	 */
	public UnsupportedHTTPMethodException(final String message) {
	
		this(message, null);
	}
	
	
	/**
	 * Creates a new unsupported HTTP method exception with the specified 
	 * message and requested method.
	 *
	 * @param message         The message.
	 * @param requestedMethod The requested HTTP method, {@code null} if 
	 *                        unknown.
	 */
	public UnsupportedHTTPMethodException(final String message, final HTTPMethod requestedMethod) {
	
		super(message);
		
		method = requestedMethod;
	}
	
	
	/**
	 * Gets the requested method.
	 *
	 * @return The requested method, {@code null} if unknown or not set.
	 */
	public HTTPMethod getRequestedMethod() {
	
		return method;
	}
}
