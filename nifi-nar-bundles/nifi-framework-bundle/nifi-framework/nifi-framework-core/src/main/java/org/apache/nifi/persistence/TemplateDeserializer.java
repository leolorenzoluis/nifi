/*
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
package org.apache.nifi.persistence;

import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import org.apache.nifi.controller.serialization.FlowSerializationException;
import org.apache.nifi.web.api.dto.TemplateDTO;

public class TemplateDeserializer {

    public static TemplateDTO deserialize(final InputStream inStream) {
        try {
            JAXBContext context = JAXBContext.newInstance(TemplateDTO.class);

            // Manually constructing the XIF is necessary to prevent XXE attacks
            XMLInputFactory xif = XMLInputFactory.newFactory();
            xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
            xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            XMLStreamReader xsr = xif.createXMLStreamReader(new StreamSource(inStream));

            Unmarshaller unmarshaller = context.createUnmarshaller();
            JAXBElement<TemplateDTO> templateElement = unmarshaller.unmarshal(xsr, TemplateDTO.class);
            return templateElement.getValue();
        } catch (final JAXBException | XMLStreamException e) {
            throw new FlowSerializationException(e);
        }
    }
}
