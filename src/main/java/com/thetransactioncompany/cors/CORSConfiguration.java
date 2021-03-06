package com.thetransactioncompany.cors;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.lang.Exception;

/**
 * The CORS filter configuration (typically originating from the web application
 * descriptor file {@code web.xml}). The fields become immutable (final) after
 * the are initialised.
 *
 * @author <a href="http://dzhuvinov.com">Vladimir Dzhuvinov</a>
 * @version 1.3.1 (2011-07-29)
 */
public class CORSConfiguration {
  /**
   * If {@code true} generic HTTP requests must be allowed to pass
   * through the filter, else only valid and accepted CORS requests must
   * be allowed (strict CORS filtering).
   *
   * <p>Property key: cors.allowGenericHttpRequests
   */
  public final boolean allowGenericHttpRequests;

  /**
   * If {@code true} the CORS filter must allow requests from any origin,
   * else the origin whitelist {@link #allowedOrigins} must be consulted.
   *
   * <p>Property key: cors.allowOrigin (set to {@code *})
   */
  public final boolean allowAnyOrigin;

  /**
   * Whitelisted origins that the CORS filter must allow. Requests from
   * origins not included here must be refused with a HTTP 403 "Forbidden"
   * response. This property is overriden by {@link #allowAnyOrigin}.
   *
   * <p>Note: The set is of type String instead of Origin to bypass
   * parsing of the request origins before matching, see
   * http://lists.w3.org/Archives/Public/public-webapps/2010JulSep/1046.html
   *
   * <p>Property key: cors.allowOrigin
   */
  public final Set<String> allowedOrigins;

  /**
   * Helper method to check whether requests from the specified origin
   * must be allowed. This is done by looking up {@link #allowAnyOrigin}
   * and {@link #allowedOrigins} (in that order).
   *
   * @param origin The origin as reported by the web client (browser),
   *               {@code null} if unknown.
   *
   * @return {@code true} if the origin is allowed, else {@code false}.
   */
  public final boolean isAllowedOrigin(final String origin) {
    if (this.allowAnyOrigin) {
      return true;
    }

    if (origin == null) {
      return false;
    }

    if (this.allowedOrigins.contains(origin)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * The supported HTTP methods. Requests for methods not included here
   * must be refused by the CORS filter with a HTTP 405 "Method not
   * allowed" response.
   *
   * <p>Property key: cors.supportedMethods
   */
  public final Set<HTTPMethod> supportedMethods;

  /**
   * Helper method to check whether the specified HTTP method is
   * supported. This is done by looking up {@link #supportedMethods}.
   *
   * @param method The HTTP method.
   *
   * @return {@code true} if the method is supported, else {@code false}.
   */
  public final boolean isSupportedMethod(final HTTPMethod method) {
    if (this.supportedMethods.contains(method)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * The names of the supported author request headers.
   *
   * <p>Property key: cors.supportedHeaders
   */
  public final Set<HeaderFieldName> supportedHeaders;

  /**
   * Helper method to check whether the specified (non-simple) author
   * request header is supported. This is done by looking up
   * {@link #supportedHeaders}.
   *
   * @param header The header field name.
   *
   * @return {@code true} if the header is supported, else {@code false}.
   */
  public final boolean isSupportedHeader(final HeaderFieldName header) {
    if (this.supportedHeaders.contains(header)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * The non-simple response headers that the web browser should expose to
   * the author of the CORS request.
   *
   * <p>Property key: cors.exposedHeaders
   */
  public final Set<HeaderFieldName> exposedHeaders;

  /**
   * Indicates whether user credentials, such as cookies, HTTP
   * authentication or client-side certificates, are supported.
   *
   * <p>Property key: cors.supportsCredentials
   */
  public final boolean supportsCredentials;

  /**
   * Indicates how long the results of a preflight request can be cached
   * by the web client, in seconds. If {@code -1} unspecified.
   *
   * <p>Property key: cors.maxAge
   */
  public final int maxAge;

  /**
   * Parses a string containing words separated by space and/or comma.
   *
   * @param s The string to parse.
   *
   * @return An array of the parsed words, empty if none were found.
   */
  protected static String[] parseWords(final String s) {
    String s1 = s.trim();

    if (s1.isEmpty()) {
      return new String[]{};
    } else {
      return s1.split("\\s*,\\s*|\\s+");
    }
  }

  /**
   * Creates a new CORS configuration from the specified properties.
   *
   * <p>The following properties are recognised (if missing they default
   * to the specified values):
   *
   * <ul>
   *     <li>cors.allowGenericHttpRequests {true|false} defaults to {@code true}.
   *     <li>cors.allowOrigin {"*"|origin-list} defaults to {@code *}.
   *     <li>cors.supportedMethods {method-list} defaults to {@code "GET, POST, HEAD, OPTIONS"}.
   *     <li>cors.supportedHeaders {header-list} defaults to empty list.
   *     <li>cors.exposedHeaders {header-list} defaults to empty list.
   *     <li>cors.supportsCredentials {true|false} defaults to {@code true}.
   *     <li>cors.maxAge {int} defaults to {@code -1} (unspecified).
   * </ul>
   *
   * @param props The properties.
   *
   * @throws CORSConfigurationException On a invalid property.
   */
  public CORSConfiguration(final Properties props)
  throws CORSConfigurationException
  {
    try {
      // Parse the allow generic HTTP requests option
      String allowGenericProperty =
        props.getProperty("cors.allowGenericHttpRequests", "true");
      this.allowGenericHttpRequests =
        Boolean.parseBoolean(allowGenericProperty.trim());

      // Parse the allowed origins list
      String originSpec = props.getProperty("cors.allowOrigin", "*").trim();
      this.allowedOrigins = new HashSet<String>();

      if (originSpec.equals("*")) {
        this.allowAnyOrigin = true;
      } else {
        allowAnyOrigin = false;
        String[] urls = this.parseWords(originSpec);

        for (String url: urls) {
          try {
            this.allowedOrigins.add(new Origin(url).toString());
          } catch (OriginException e) {
            throw new Exception("Bad origin URL in property cors.allowOrigin: " + url);
          }
        }
      }

      // Parse the supported methods list
      String methodSpec = props.getProperty("cors.supportedMethods", "GET, POST, HEAD, OPTIONS").trim().toUpperCase();
      String[] methodNames = this.parseWords(methodSpec);
      this.supportedMethods = new HashSet<HTTPMethod>();

      for (String methodName: methodNames) {
        try {
          this.supportedMethods.add(HTTPMethod.valueOf(methodName));
        } catch (IllegalArgumentException e) {
          throw new Exception("Bad HTTP method name in property cors.allowMethods: " + methodName);
        }
      }

      // Parse the supported headers list
      String[] headers = this.parseWords(props.getProperty("cors.supportedHeaders", ""));
      this.supportedHeaders = new HashSet<HeaderFieldName>();

      for (String header: headers) {
        try {
          this.supportedHeaders.add(new HeaderFieldName(header));

        } catch (IllegalArgumentException e) {
          throw new Exception("Bad header field name in property cors.supportedHeaders: " + header);
        }
      }

      // Parse the exposed headers list
      headers = this.parseWords(props.getProperty("cors.exposedHeaders", ""));
      this.exposedHeaders = new HashSet<HeaderFieldName>();

      for (String header: headers) {
        try {
          this.exposedHeaders.add(new HeaderFieldName(header));
        } catch (IllegalArgumentException e) {
          throw new Exception("Bad header field name in property cors.exposedHeaders: " + header);
        }
      }

      // Parse the allow credentials option
      String supportsCredentialsProperty =
        props.getProperty("cors.supportsCredentials", "true");
      this.supportsCredentials =
        Boolean.parseBoolean(supportsCredentialsProperty.trim());

      // Parse the max cache age of preflight requests
      String maxAgeProperty = props.getProperty("cors.maxAge", "-1");
      this.maxAge = Integer.parseInt(maxAgeProperty);
    } catch (Exception e) {
      // Simply reformat as a more specific exception class
      // to improve stack trace clarity (config exceptions
      // are often dumped to the web client screen)
      throw new CORSConfigurationException(e.getMessage());
    }
  }
}
