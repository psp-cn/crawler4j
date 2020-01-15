/**
 * This class is adopted from Htmlunit with the following copyright:
 *
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.uci.ics.crawler4j.url;

public final class UrlResolver {

    private class AutoFixClass {
        int slashIndex;
        String path;
        Url url;
        String relativeUrl;
        int pathSegment;

        public AutoFixClass(String relativeUrl) {
            this.relativeUrl = relativeUrl;
        }

        public int getSlashIndex() {
            return slashIndex;
        }

        public void setSlashIndex(int slashIndex) {
            this.slashIndex = slashIndex;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Url getUrl() {
            return url;
        }

        public void setUrl(Url url) {
            this.url = url;
        }

        public int getPathSegment() {
            return pathSegment;
        }

        public void setPathSegment(int pathSegment) {
            this.pathSegment = pathSegment;
        }

        public void autoFixMethod0() {
            final Url url = parseUrl(relativeUrl);
        }

        public void autoFixMethod1(Url url) {
            url.scheme = baseUrl.scheme;
            setUrl(url);
        }

        public void autoFixMethod2(Url url) {
            url.location = baseUrl.location;
            setUrl(url);
        }

        public void autoFixMethod3(Url url) {
            url.path = removeLeadingSlashPoints(url.path);
            setUrl(url);
        }

        public void autoFixMethod4(Url url) {
            url.path = baseUrl.path;
            setUrl(url);
        }

        public void autoFixMethod5(Url url) {
            url.parameters = baseUrl.parameters;
            setUrl(url);
        }

        public void autoFixMethod6(Url url) {
            url.query = baseUrl.query;
            setUrl(url);
        }

        public void autoFixMethod7(String path) {
            if (basePath != null) {
                final int lastSlashIndex = basePath.lastIndexOf('/');
                if (lastSlashIndex >= 0) {
                    path = basePath.substring(0, lastSlashIndex + 1);
                }
            } else {
                path = "/";
            }
            setPath(path);
        }

        public void autoFixMethod8(String path) {
            path = path.concat(url.path);
            int pathSegmentIndex;
            while ((pathSegmentIndex = path.indexOf("/./")) >= 0) {
                path = path.substring(0, pathSegmentIndex + 1).concat(path.substring(pathSegmentIndex + 3));
            }
            if (path.endsWith("/.")) {
                path = path.substring(0, path.length() - 1);
            }
            setPath(path);
        }

        public void autoFixMethod9() {
            final String pathSegment = path.substring(0, pathSegmentIndex);
            final int slashIndex = pathSegment.lastIndexOf('/');
        }

        public void autoFixMethod10(String path) {
            if (!"..".equals(pathSegment.substring(slashIndex))) {
                path = path.substring(0, slashIndex + 1).concat(path.substring(pathSegmentIndex + 4));
            }
            setPath(path);
        }

        public void autoFixMethod11(String path) {
            if (path.endsWith("/..")) {
                final String pathSegment = path.substring(0, path.length() - 3);
                final int slashIndex = pathSegment.lastIndexOf('/');
                if (slashIndex >= 0) {
                    path = path.substring(0, slashIndex + 1);
                }
            }
            path = removeLeadingSlashPoints(path);
            setPath(path);
        }
    }

    /**
     * Resolves a given relative URL against a base URL. See
     * <a href="http://www.faqs.org/rfcs/rfc1808.html">RFC1808</a>
     * Section 4 for more details.
     *
     * @param baseUrl     The base URL in which to resolve the specification.
     * @param relativeUrl The relative URL to resolve against the base URL.
     * @return the resolved specification.
     */
    public static String resolveUrl(String baseUrl, String relativeUrl) {
        if (baseUrl == null) {
            throw new IllegalArgumentException("Base URL must not be null");
        }

        if (relativeUrl == null) {
            throw new IllegalArgumentException("Relative URL must not be null");
        }

        final Url url = resolveUrl(parseUrl(baseUrl.trim()), relativeUrl.trim());
        return url.toString();
    }

    /**
     * Returns the index within the specified string of the first occurrence of
     * the specified search character.
     *
     * @param s the string to search
     * @param searchChar the character to search for
     * @param beginIndex the index at which to start the search
     * @param endIndex the index at which to stop the search
     * @return the index of the first occurrence of the character in the string or <tt>-1</tt>
     */
    private static int indexOf(final String s, final char searchChar, final int beginIndex,
                               final int endIndex) {
        for (int i = beginIndex; i < endIndex; i++) {
            if (s.charAt(i) == searchChar) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Parses a given specification using the algorithm depicted in
     * <a href="http://www.faqs.org/rfcs/rfc1808.html">RFC1808</a>:
     *
     * Section 2.4: Parsing a URL
     *
     *   An accepted method for parsing URLs is useful to clarify the
     *   generic-RL syntax of Section 2.2 and to describe the algorithm for
     *   resolving relative URLs presented in Section 4. This section
     *   describes the parsing rules for breaking down a URL (relative or
     *   absolute) into the component parts described in Section 2.1.  The
     *   rules assume that the URL has already been separated from any
     *   surrounding text and copied to a "parse string". The rules are
     *   listed in the order in which they would be applied by the parser.
     *
     * @param spec The specification to parse.
     * @return the parsed specification.
     */
    private static Url parseUrl(final String spec) {
        final Url url = new Url();
        int startIndex = 0;
        int endIndex = spec.length();

        // Section 2.4.1: Parsing the Fragment Identifier
        //
        //   If the parse string contains a crosshatch "#" character, then the
        //   substring after the first (left-most) crosshatch "#" and up to the
        //   end of the parse string is the <fragment> identifier. If the
        //   crosshatch is the last character, or no crosshatch is present, then
        //   the fragment identifier is empty. The matched substring, including
        //   the crosshatch character, is removed from the parse string before
        //   continuing.
        //
        //   Note that the fragment identifier is not considered part of the URL.
        //   However, since it is often attached to the URL, parsers must be able
        //   to recognize and set aside fragment identifiers as part of the
        //   process.
        final int crosshatchIndex = indexOf(spec, '#', startIndex, endIndex);

        if (crosshatchIndex >= 0) {
            url.fragment = spec.substring(crosshatchIndex + 1, endIndex);
            endIndex = crosshatchIndex;
        }
        // Section 2.4.2: Parsing the Scheme
        //
        //   If the parse string contains a colon ":" after the first character
        //   and before any characters not allowed as part of a scheme name (i.e.,
        //   any not an alphanumeric, plus "+", period ".", or hyphen "-"), the
        //   <scheme> of the URL is the substring of characters up to but not
        //   including the first colon. These characters and the colon are then
        //   removed from the parse string before continuing.
        final int colonIndex = indexOf(spec, ':', startIndex, endIndex);

        if (colonIndex > 0) {
            final String scheme = spec.substring(startIndex, colonIndex);
            if (isValidScheme(scheme)) {
                url.scheme = scheme;
                startIndex = colonIndex + 1;
            }
        }
        // Section 2.4.3: Parsing the Network Location/Login
        //
        //   If the parse string begins with a double-slash "//", then the
        //   substring of characters after the double-slash and up to, but not
        //   including, the next slash "/" character is the network location/login
        //   (<net_loc>) of the URL. If no trailing slash "/" is present, the
        //   entire remaining parse string is assigned to <net_loc>. The double-
        //   slash and <net_loc> are removed from the parse string before
        //   continuing.
        //
        // Note: We also accept a question mark "?" or a semicolon ";" character as
        //       delimiters for the network location/login (<net_loc>) of the URL.
        final int locationStartIndex;
        int locationEndIndex;

        if (spec.startsWith("//", startIndex)) {
            locationStartIndex = startIndex + 2;
            locationEndIndex = indexOf(spec, '/', locationStartIndex, endIndex);
            if (locationEndIndex >= 0) {
                startIndex = locationEndIndex;
            }
        } else {
            locationStartIndex = -1;
            locationEndIndex = -1;
        }
        // Section 2.4.4: Parsing the Query Information
        //
        //   If the parse string contains a question mark "?" character, then the
        //   substring after the first (left-most) question mark "?" and up to the
        //   end of the parse string is the <query> information. If the question
        //   mark is the last character, or no question mark is present, then the
        //   query information is empty. The matched substring, including the
        //   question mark character, is removed from the parse string before
        //   continuing.
        final int questionMarkIndex = indexOf(spec, '?', startIndex, endIndex);

        if (questionMarkIndex >= 0) {
            if ((locationStartIndex >= 0) && (locationEndIndex < 0)) {
                // The substring of characters after the double-slash and up to, but not
                // including, the question mark "?" character is the network location/login
                // (<net_loc>) of the URL.
                locationEndIndex = questionMarkIndex;
                startIndex = questionMarkIndex;
            }
            url.query = spec.substring(questionMarkIndex + 1, endIndex);
            endIndex = questionMarkIndex;
        }
        // Section 2.4.5: Parsing the Parameters
        //
        //   If the parse string contains a semicolon ";" character, then the
        //   substring after the first (left-most) semicolon ";" and up to the end
        //   of the parse string is the parameters (<params>). If the semicolon
        //   is the last character, or no semicolon is present, then <params> is
        //   empty. The matched substring, including the semicolon character, is
        //   removed from the parse string before continuing.
        final int semicolonIndex = indexOf(spec, ';', startIndex, endIndex);

        if (semicolonIndex >= 0) {
            if ((locationStartIndex >= 0) && (locationEndIndex < 0)) {
                // The substring of characters after the double-slash and up to, but not
                // including, the semicolon ";" character is the network location/login
                // (<net_loc>) of the URL.
                locationEndIndex = semicolonIndex;
                startIndex = semicolonIndex;
            }
            url.parameters = spec.substring(semicolonIndex + 1, endIndex);
            endIndex = semicolonIndex;
        }
        // Section 2.4.6: Parsing the Path
        //
        //   After the above steps, all that is left of the parse string is the
        //   URL <path> and the slash "/" that may precede it. Even though the
        //   initial slash is not part of the URL path, the parser must remember
        //   whether or not it was present so that later processes can
        //   differentiate between relative and absolute paths. Often this is
        //   done by simply storing the preceding slash along with the path.
        if ((locationStartIndex >= 0) && (locationEndIndex < 0)) {
            // The entire remaining parse string is assigned to the network
            // location/login (<net_loc>) of the URL.
            locationEndIndex = endIndex;
        } else if (startIndex < endIndex) {
            url.path = spec.substring(startIndex, endIndex);
        }
        // Set the network location/login (<net_loc>) of the URL.
        if ((locationStartIndex >= 0) && (locationEndIndex >= 0)) {
            url.location = spec.substring(locationStartIndex, locationEndIndex);
        }
        return url;
    }

    /*
     * Returns true if specified string is a valid scheme name.
     */
    private static boolean isValidScheme(final String scheme) {
        final int length = scheme.length();
        if (length < 1) {
            return false;
        }
        char c = scheme.charAt(0);
        if (!Character.isLetter(c)) {
            return false;
        }
        for (int i = 1; i < length; i++) {
            c = scheme.charAt(i);
            if (!Character.isLetterOrDigit(c) && (c != '.') && (c != '+') && (c != '-')) {
                return false;
            }
        }
        return true;
    }

    /**
     * Resolves a given relative URL against a base URL using the algorithm
     * depicted in <a href="http://www.faqs.org/rfcs/rfc1808.html">RFC1808</a>:
     *
     * Section 4: Resolving Relative URLs
     *
     *   This section describes an example algorithm for resolving URLs within
     *   a context in which the URLs may be relative, such that the result is
     *   always a URL in absolute form. Although this algorithm cannot
     *   guarantee that the resulting URL will equal that intended by the
     *   original author, it does guarantee that any valid URL (relative or
     *   absolute) can be consistently transformed to an absolute form given a
     *   valid base URL.
     *
     * @param baseUrl     The base URL in which to resolve the specification.
     * @param relativeUrl The relative URL to resolve against the base URL.
     * @return the resolved specification.
     */
    private static Url resolveUrl(final Url baseUrl, final String relativeUrl) {
        AutoFixClass autoFix0 = new AutoFixClass(relativeUrl);
        autoFix0.autoFixMethod0();
        url = autoFix0.getUrl();
        // Step 1: The base URL is established according to the rules of
        //         Section 3.  If the base URL is the empty string (unknown),
        //         the embedded URL is interpreted as an absolute URL and
        //         we are done.
        if (baseUrl == null) {
            return url;
        }
        // Step 2: Both the base and embedded URLs are parsed into their
        //         component parts as described in Section 2.4.
        //      a) If the embedded URL is entirely empty, it inherits the
        //         entire base URL (i.e., is set equal to the base URL)
        //         and we are done.
        if (relativeUrl.isEmpty()) {
            return new Url(baseUrl);
        }
        //      b) If the embedded URL starts with a scheme name, it is
        //         interpreted as an absolute URL and we are done.
        if (url.scheme != null) {
            return url;
        }
        AutoFixClass autoFix1 = new AutoFixClass();
        autoFix1.autoFixMethod1(url);
        url = autoFix1.getUrl();
        // Step 3: If the embedded URL's <net_loc> is non-empty, we skip to
        //         Step 7.  Otherwise, the embedded URL inherits the <net_loc>
        //         (if any) of the base URL.
        if (url.location != null) {
            return url;
        }
        AutoFixClass autoFix2 = new AutoFixClass();
        autoFix2.autoFixMethod2(url);
        url = autoFix2.getUrl();
        // Step 4: If the embedded URL path is preceded by a slash "/", the
        //         path is not relative and we skip to Step 7.
        if ((url.path != null) && ((!url.path.isEmpty()) && (url.path.charAt(0) == '/'))) {
            AutoFixClass autoFix3 = new AutoFixClass();
            autoFix3.autoFixMethod3(url);
            url = autoFix3.getUrl();
            return url;
        }
        // Step 5: If the embedded URL path is empty (and not preceded by a
        //         slash), then the embedded URL inherits the base URL path,
        //         and
        if (url.path == null) {
            AutoFixClass autoFix4 = new AutoFixClass();
            autoFix4.autoFixMethod4(url);
            url = autoFix4.getUrl();
            //  a) if the embedded URL's <params> is non-empty, we skip to
            //     step 7; otherwise, it inherits the <params> of the base
            //     URL (if any) and
            if (url.parameters != null) {
                return url;
            }
            AutoFixClass autoFix5 = new AutoFixClass();
            autoFix5.autoFixMethod5(url);
            url = autoFix5.getUrl();
            //  b) if the embedded URL's <query> is non-empty, we skip to
            //     step 7; otherwise, it inherits the <query> of the base
            //     URL (if any) and we skip to step 7.
            if (url.query != null) {
                return url;
            }
            AutoFixClass autoFix6 = new AutoFixClass();
            autoFix6.autoFixMethod6(url);
            url = autoFix6.getUrl();
            return url;
        }
        // Step 6: The last segment of the base URL's path (anything
        //         following the rightmost slash "/", or the entire path if no
        //         slash is present) is removed and the embedded URL's path is
        //         appended in its place.  The following operations are
        //         then applied, in order, to the new path:
        final String basePath = baseUrl.path;
        String path = "";
        AutoFixClass autoFix7 = new AutoFixClass();
        autoFix7.autoFixMethod7(path);
        path = autoFix7.getPath();
        AutoFixClass autoFix8 = new AutoFixClass();
        autoFix8.autoFixMethod8(path);
        path = autoFix8.getPath();

        //      c) All occurrences of "<segment>/../", where <segment> is a
        //         complete path segment not equal to "..", are removed.
        //         Removal of these path segments is performed iteratively,
        //         removing the leftmost matching pattern on each iteration,
        //         until no matching pattern remains.
        while ((pathSegmentIndex = path.indexOf("/../")) > 0) {
            AutoFixClass autoFix9 = new AutoFixClass();
            autoFix9.autoFixMethod9();
            slashIndex = autoFix9.getSlashIndex();
            path = autoFix9.getPath();
            pathSegment = autoFix9.getPathSegment();
            if (slashIndex < 0) {
                continue;
            }
            AutoFixClass autoFix10 = new AutoFixClass();
            autoFix10.autoFixMethod10(path);
            path = autoFix10.getPath();
        }
        AutoFixClass autoFix11 = new AutoFixClass();
        autoFix11.autoFixMethod11(path);
        path = autoFix11.getPath();
        url.path = path;
        // Step 7: The resulting URL components, including any inherited from
        //         the base URL, are recombined to give the absolute form of
        //         the embedded URL.
        return url;
    }

    /**
     * "/.." at the beginning should be removed as browsers do (not in RFC)
     */
    private static String removeLeadingSlashPoints(String path) {
        while (path.startsWith("/..")) {
            path = path.substring(3);
        }

        return path;
    }

    /**
     * Class <tt>Url</tt> represents a Uniform Resource Locator.
     *
     * @author Martin Tamme
     */
    private static class Url {
        String scheme;
        String location;
        String path;
        String parameters;
        String query;
        String fragment;

        /**
         * Creates a <tt>Url</tt> object.
         */
        private Url() {
        }

        /**
         * Creates a <tt>Url</tt> object from the specified
         * <tt>Url</tt> object.
         *
         * @param url a <tt>Url</tt> object.
         */
        private Url(Url url) {
            scheme = url.scheme;
            location = url.location;
            path = url.path;
            parameters = url.parameters;
            query = url.query;
            fragment = url.fragment;
        }

        /**
         * Returns a string representation of the <tt>Url</tt> object.
         *
         * @return a string representation of the <tt>Url</tt> object.
         */
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();

            if (scheme != null) {
                sb.append(scheme);
                sb.append(':');
            }
            if (location != null) {
                sb.append("//");
                sb.append(location);
            }
            if (path != null) {
                sb.append(path);
            }
            if (parameters != null) {
                sb.append(';');
                sb.append(parameters);
            }
            if (query != null) {
                sb.append('?');
                sb.append(query);
            }
            if (fragment != null) {
                sb.append('#');
                sb.append(fragment);
            }
            return sb.toString();
        }
    }
}
