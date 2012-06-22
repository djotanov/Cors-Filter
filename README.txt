Java Cross-Origin Resource Sharing (CORS) Filter
Copyright (c) Vladimir Dzhuvinov, 2010 - 2011

README

This package provides a Java servlet filter that implements the Cross-Origin
Resource Sharing (CORS) mechanism for allowing cross-domain HTTP requests from
web browsers. The CORS W3C working draft settled in 2009 and as of 2010 CORS is
supported by all major browsers such as Firefox, Safari, Chrome and IE.

To enable CORS for a particular HTTP resource, such as a servlet, a JSP or a
static file, attach a CORSFilter to it via a <filter-mapping> element in the
web.xml descriptor file. The default CORS filter policy is to allow any origin
(including credentials). To impose a stricter access policy configure the filter
using the supported <init-param> tags in the web.xml file. See the CORSFilter
online documentation for configuration details.

This CORS filter version implements the W3C working draft from 2010-07-27:

  http://www.w3.org/TR/2010/WD-cors-20100727/


For installation instructions, usage and other information visit the CORS Filter
website:

  http://software.dzhuvinov.com/cors-filter.html


Change log:

version 1.3.2 (2012-02-23)
  * Converted to a Maven based project (James Sumners)
  * Removed dependency on closed source library (James Sumners)
  * Published to bitbucket.org as a Mercurial repository (James Sumners)

version 1.3.1 (2011-12-02)
  * Removes improper filter chain for preflight HTTP OPTIONS CORS
    requests.

version 1.3 (2011-12-02)
  * Fixes improper detection of actual HTTP OPTIONS CORS requests.
  * Updates Property Util JAR to 1.5.

version 1.2.1 (2011-07-29)
  * Updates Property Util JAR to 1.4.
  * Updates documentation to reflect the latest W3C CORS terminology.

version 1.2 (2010-12-13)
  * Released under the Apache Open Source License 2.0.

version 1.1 (2010-10-10)
  * Tags CORS requests for downstream notification using
    HttpServletRequest.addAttribute().

version 1.0 (2010-09-29)
  * First official release.
