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
        String spec;
        int endIndex;
        int colonIndex;
        int locationEndIndex;
        int locationStartIndex;
        Url url;
        int startIndex;

        public AutoFixClass(String spec) {
            this.spec = spec;
        }

        public int getEndIndex() {
            return endIndex;
        }

        public void setEndIndex(int endIndex) {
            this.endIndex = endIndex;
        }

        public int getColonIndex() {
            return colonIndex;
        }

        public void setColonIndex(int colonIndex) {
            this.colonIndex = colonIndex;
        }

        public int getLocationEndIndex() {
            return locationEndIndex;
        }

        public void setLocationEndIndex(int locationEndIndex) {
            this.locationEndIndex = locationEndIndex;
        }

        public int getLocationStartIndex() {
            return locationStartIndex;
        }

        public void setLocationStartIndex(int locationStartIndex) {
            this.locationStartIndex = locationStartIndex;
        }

        public Url getUrl() {
            return url;
        }

        public void setUrl(Url url) {
            this.url = url;
        }

        public int getStartIndex() {
            return startIndex;
        }

        public void setStartIndex(int startIndex) {
            this.startIndex = startIndex;
        }

        public void autoFixMethod0() {
            final Url url = new Url();
            int startIndex = 0;
            int endIndex = spec.length();
            final int crosshatchIndex = indexOf(spec, '#', startIndex, endIndex);
            if (crosshatchIndex >= 0) {
                url.fragment = spec.substring(crosshatchIndex + 1, endIndex);
                endIndex = crosshatchIndex;
            }
            final int colonIndex = indexOf(spec, ':', startIndex, endIndex);
        }

        public void autoFixMethod1(Url url, int startIndex) {
            if (colonIndex > 0) {
                final String scheme = spec.substring(startIndex, colonIndex);
                if (isValidScheme(scheme)) {
                    url.scheme = scheme;
                    startIndex = colonIndex + 1;
                }
            }
            final int locationStartIndex;
            int locationEndIndex;
            setStartIndex(startIndex);
            setUrl(url);
        }

        public void autoFixMethod2(int startIndex) {
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
            setStartIndex(startIndex);
        }

        public void autoFixMethod3(int endIndex, Url url, int locationEndIndex, int startIndex) {
            if ((locationStartIndex >= 0) && (locationEndIndex < 0)) {
                locationEndIndex = questionMarkIndex;
                startIndex = questionMarkIndex;
            }
            url.query = spec.substring(questionMarkIndex + 1, endIndex);
            endIndex = questionMarkIndex;
            setLocationEndIndex(locationEndIndex);
            setStartIndex(startIndex);
            setEndIndex(endIndex);
            setUrl(url);
        }

        public void autoFixMethod4(int endIndex, Url url, int locationEndIndex, int startIndex) {
            if ((locationStartIndex >= 0) && (locationEndIndex < 0)) {
                locationEndIndex = semicolonIndex;
                startIndex = semicolonIndex;
            }
            url.parameters = spec.substring(semicolonIndex + 1, endIndex);
            endIndex = semicolonIndex;
            setLocationEndIndex(locationEndIndex);
            setStartIndex(startIndex);
            setEndIndex(endIndex);
            setUrl(url);
        }

        public void autoFixMethod5(Url url, int locationEndIndex) {
            if ((locationStartIndex >= 0) && (locationEndIndex < 0)) {
                locationEndIndex = endIndex;
            } else if (startIndex < endIndex) {
                url.path = spec.substring(startIndex, endIndex);
            }
            setLocationEndIndex(locationEndIndex);
            setUrl(url);
        }

        public void autoFixMethod6(Url url) {
            if ((locationStartIndex >= 0) && (locationEndIndex >= 0)) {
                url.location = spec.substring(locationStartIndex, locationEndIndex);
            }
            setUrl(url);
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
        AutoFixClass autoFix0 = new AutoFixClass(spec);
        autoFix0.autoFixMethod0();
        endIndex = autoFix0.getEndIndex();
        colonIndex = autoFix0.getColonIndex();
        url = autoFix0.getUrl();
        startIndex = autoFix0.getStartIndex();
        AutoFixClass autoFix1 = new AutoFixClass();
        autoFix1.autoFixMethod1(url, startIndex);
        url = autoFix1.getUrl();
        startIndex = autoFix1.getStartIndex();
        AutoFixClass autoFix2 = new AutoFixClass();
        autoFix2.autoFixMethod2(startIndex);
        startIndex = autoFix2.getStartIndex();
        locationEndIndex = autoFix2.getLocationEndIndex();
        locationStartIndex = autoFix2.getLocationStartIndex();
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
            AutoFixClass autoFix3 = new AutoFixClass();
            autoFix3.autoFixMethod3(endIndex, url, locationEndIndex, startIndex);
            startIndex = autoFix3.getStartIndex();
            locationEndIndex = autoFix3.getLocationEndIndex();
            endIndex = autoFix3.getEndIndex();
            url = autoFix3.getUrl();
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
            AutoFixClass autoFix4 = new AutoFixClass();
            autoFix4.autoFixMethod4(endIndex, url, locationEndIndex, startIndex);
            endIndex = autoFix4.getEndIndex();
            url = autoFix4.getUrl();
            locationEndIndex = autoFix4.getLocationEndIndex();
            startIndex = autoFix4.getStartIndex();
        }
        AutoFixClass autoFix5 = new AutoFixClass();
        autoFix5.autoFixMethod5(url, locationEndIndex);
        locationEndIndex = autoFix5.getLocationEndIndex();
        url = autoFix5.getUrl();
        AutoFixClass autoFix6 = new AutoFixClass();
        autoFix6.autoFixMethod6(url);
        url = autoFix6.getUrl();
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
        final Url url = parseUrl(relativeUrl);
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
        //      c) Otherwise, the embedded URL inherits the scheme of
        //         the base URL.
        url.scheme = baseUrl.scheme;
        // Step 3: If the embedded URL's <net_loc> is non-empty, we skip to
        //         Step 7.  Otherwise, the embedded URL inherits the <net_loc>
        //         (if any) of the base URL.
        if (url.location != null) {
            return url;
        }
        url.location = baseUrl.location;
        // Step 4: If the embedded URL path is preceded by a slash "/", the
        //         path is not relative and we skip to Step 7.
        if ((url.path != null) && ((!url.path.isEmpty()) && (url.path.charAt(0) == '/'))) {
            url.path = removeLeadingSlashPoints(url.path);
            return url;
        }
        // Step 5: If the embedded URL path is empty (and not preceded by a
        //         slash), then the embedded URL inherits the base URL path,
        //         and
        if (url.path == null) {
            url.path = baseUrl.path;
            //  a) if the embedded URL's <params> is non-empty, we skip to
            //     step 7; otherwise, it inherits the <params> of the base
            //     URL (if any) and
            if (url.parameters != null) {
                return url;
            }
            url.parameters = baseUrl.parameters;
            //  b) if the embedded URL's <query> is non-empty, we skip to
            //     step 7; otherwise, it inherits the <query> of the base
            //     URL (if any) and we skip to step 7.
            if (url.query != null) {
                return url;
            }
            url.query = baseUrl.query;
            return url;
        }
        // Step 6: The last segment of the base URL's path (anything
        //         following the rightmost slash "/", or the entire path if no
        //         slash is present) is removed and the embedded URL's path is
        //         appended in its place.  The following operations are
        //         then applied, in order, to the new path:
        final String basePath = baseUrl.path;
        String path = "";

        if (basePath != null) {
            final int lastSlashIndex = basePath.lastIndexOf('/');

            if (lastSlashIndex >= 0) {
                path = basePath.substring(0, lastSlashIndex + 1);
            }
        } else {
            path = "/";
        }
        path = path.concat(url.path);
        //      a) All occurrences of "./", where "." is a complete path
        //         segment, are removed.
        int pathSegmentIndex;

        while ((pathSegmentIndex = path.indexOf("/./")) >= 0) {
            path = path.substring(0, pathSegmentIndex + 1)
                       .concat(path.substring(pathSegmentIndex + 3));
        }
        //      b) If the path ends with "." as a complete path segment,
        //         that "." is removed.
        if (path.endsWith("/.")) {
            path = path.substring(0, path.length() - 1);
        }
        //      c) All occurrences of "<segment>/../", where <segment> is a
        //         complete path segment not equal to "..", are removed.
        //         Removal of these path segments is performed iteratively,
        //         removing the leftmost matching pattern on each iteration,
        //         until no matching pattern remains.
        while ((pathSegmentIndex = path.indexOf("/../")) > 0) {
            final String pathSegment = path.substring(0, pathSegmentIndex);
            final int slashIndex = pathSegment.lastIndexOf('/');

            if (slashIndex < 0) {
                continue;
            }
            if (!"..".equals(pathSegment.substring(slashIndex))) {
                path =
                    path.substring(0, slashIndex + 1).concat(path.substring(pathSegmentIndex + 4));
            }
        }
        //      d) If the path ends with "<segment>/..", where <segment> is a
        //         complete path segment not equal to "..", that
        //         "<segment>/.." is removed.
        if (path.endsWith("/..")) {
            final String pathSegment = path.substring(0, path.length() - 3);
            final int slashIndex = pathSegment.lastIndexOf('/');

            if (slashIndex >= 0) {
                path = path.substring(0, slashIndex + 1);
            }
        }

        path = removeLeadingSlashPoints(path);

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
