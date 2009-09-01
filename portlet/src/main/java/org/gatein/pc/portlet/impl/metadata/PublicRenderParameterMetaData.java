/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2006, Red Hat Middleware, LLC, and individual                    *
 * contributors as indicated by the @authors tag. See the                     *
 * copyright.txt in the distribution for a full listing of                    *
 * individual contributors.                                                   *
 *                                                                            *
 * This is free software; you can redistribute it and/or modify it            *
 * under the terms of the GNU Lesser General Public License as                *
 * published by the Free Software Foundation; either version 2.1 of           *
 * the License, or (at your option) any later version.                        *
 *                                                                            *
 * This software is distributed in the hope that it will be useful,           *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU           *
 * Lesser General Public License for more details.                            *
 *                                                                            *
 * You should have received a copy of the GNU Lesser General Public           *
 * License along with this software; if not, write to the Free                *
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA         *
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.                   *
 ******************************************************************************/
package org.gatein.pc.portlet.impl.metadata;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import org.gatein.pc.portlet.impl.metadata.common.DescribableMetaData;

/**
 * @author <a href="mailto:emuckenh@redhat.com">Emanuel Muckenhuber</a>
 * @version $Revision$
 */
@XmlType(name = "public-render-parameterType", propOrder={"description", "identifier", "name", "qname", "alias"})
public class PublicRenderParameterMetaData extends DescribableMetaData
{

   /** The public render parameter id */
   private String id;

   /** The public render parameter identifier */
   private String identifier;

   /** The QName */
   private QName qname;
   
   /** The name */
   private String name;

   /** The public render parameter alias */
   private List<QName> alias;

   public PublicRenderParameterMetaData() {}
   
   public PublicRenderParameterMetaData(String id)
   {
      this.id = id;
   }
   
   @XmlAttribute(name = "id")
   public String getId()
   {
      return id;
   }

   public void setId(String id)
   {
      this.id = id;
   }

   @XmlElement(name = "identifier")
   public String getIdentifier()
   {
      return identifier;
   }

   public void setIdentifier(String identifier)
   {
      this.identifier = identifier;
   }

   @XmlElement(name = "qname")
   public QName getQname()
   {
      return qname;
   }

   public void setQname(QName qname)
   {
      this.qname = qname;
   }
   
   @XmlElement(name = "name")
   public String getName()
   {
      return name;
   }
   
   public void setName(String name)
   {
      this.name = name;
   }

   @XmlElement(name = "alias")
   public List<QName> getAlias()
   {
      return alias;
   }

   public void setAlias(List<QName> alias)
   {
      this.alias = alias;
   }
   
   public void addAlias(QName alias)
   {
      if(this.alias == null)
      {
         this.alias = new ArrayList<QName>();
      }
      this.alias.add(alias);
   }
   
}
