/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.uci.ics.crawler4j.parser;

import edu.uci.ics.crawler4j.parser.AutoFixClass;
import java.util.HashSet;
import java.util.Set;

import edu.uci.ics.crawler4j.url.WebURL;

public class TextParseData implements ParseData {

    private String textContent;
    private Set<WebURL> outgoingUrls = new HashSet<>();

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    @Override
    public Set<WebURL> getOutgoingUrls() {
        AutoFixClass autoFix0 = new AutoFixClass(outgoingUrls);
        autoFix0.autoFixMethod0();
    }

    @Override
    public void setOutgoingUrls(Set<WebURL> outgoingUrls) {
        AutoFixClass autoFix1 = new AutoFixClass();
        autoFix1.autoFixMethod1(outgoingUrls);
        outgoingUrls = autoFix1.getOutgoingUrls();
    }

    @Override
    public String toString() {
        return textContent;
    }
}