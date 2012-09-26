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
package org.gatein.pc.portlet.impl.jsr168.api;

import org.gatein.pc.api.invocation.PortletInvocation;
import org.gatein.pc.api.invocation.response.PortletInvocationResponse;
import org.gatein.pc.api.invocation.response.RevalidateMarkupResponse;
import org.gatein.pc.api.invocation.response.ContentResponse;
import org.gatein.pc.api.invocation.response.ResponseProperties;
import org.gatein.pc.api.info.PortletInfo;
import org.gatein.pc.api.info.CacheInfo;
import org.gatein.pc.api.cache.CacheScope;
import org.gatein.pc.portlet.impl.jsr168.Buffer;
import org.gatein.pc.portlet.impl.jsr168.ByteBuffer;
import org.gatein.pc.portlet.impl.jsr168.CharBuffer;

import javax.portlet.MimeResponse;
import javax.portlet.PortletURL;
import javax.portlet.ResourceURL;
import javax.portlet.CacheControl;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Map;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public abstract class MimeResponseImpl extends PortletResponseImpl implements MimeResponse
{

   /** Not really used but we need it to memorize what the client set optionally. */
   protected int bufferSize;

   /** The cache control. */
   protected CacheControlImpl cacheControl;

   /** . */
   private Buffer buffer;

   /** . */
   private String contentType;

   public MimeResponseImpl(PortletInvocation invocation, PortletRequestImpl preq)
   {
      super(invocation, preq);

      // 0 means no buffering - we say no buffering
      this.bufferSize = 0;
      this.buffer = null;
   }

   protected abstract ContentResponse createResponse(
      ResponseProperties props,
      Map<String, Object> attrs,
      String contentType,
      byte[] bytes,
      org.gatein.pc.api.cache.CacheControl cacheControl
   );

   protected abstract ContentResponse createResponse(
      ResponseProperties props,
      Map<String, Object> attrs,
      String contentType,
      String chars,
      org.gatein.pc.api.cache.CacheControl cacheControl
   );

   protected abstract ContentResponse createResponse(
      ResponseProperties props,
      Map<String, Object> attrs,
      String contentType,
      org.gatein.pc.api.cache.CacheControl cacheControl
   );

   public PortletInvocationResponse getResponse()
   {
      org.gatein.pc.api.cache.CacheControl cc;
      if (cacheControl != null)
      {
         cc = new org.gatein.pc.api.cache.CacheControl(
            cacheControl.getExpirationTime(),
            cacheControl.isPublicScope() ? CacheScope.PUBLIC : CacheScope.PRIVATE,
            cacheControl.getETag());
      }
      else
      {
         PortletInfo info = preq.container.getInfo();
         CacheInfo cacheInfo = info.getCache();
         cc = new org.gatein.pc.api.cache.CacheControl(cacheInfo.getExpirationSecs(), CacheScope.PRIVATE, null);
      }

      //
      if (cacheControl != null && cacheControl.useCachedContent())
      {
         return new RevalidateMarkupResponse(cc);
      }
      else
      {
         ResponseProperties props = getProperties(false);
         Map<String, Object> attrs = preq.attributes.getAttributeMap();
         if (buffer instanceof CharBuffer)
         {
            return createResponse(props, attrs, contentType, ((CharBuffer)buffer).getChars(), cc);
         }
         else if (buffer instanceof ByteBuffer)
         {
            return createResponse(props, attrs, contentType, ((ByteBuffer)buffer).getBytes(), cc);
         }
         else
         {
            return createResponse(props, attrs, contentType, cc);
         }
      }
   }

   public String getContentType()
   {
      return contentType;
   }

   public void setContentType(String contentType)
   {
	  if (buffer == null)
	  {
		  this.contentType = contentType;
	  }
   }

   public PrintWriter getWriter() throws IOException
   {
      CharBuffer charBuffer;
      if (buffer != null)
      {
         if (buffer instanceof CharBuffer)
         {
            charBuffer = (CharBuffer)buffer;
         }
         else
         {
            throw new IllegalStateException("The method getPortletOutputStream() was already called");
         }
      }
      else
      {
         buffer = charBuffer = new CharBuffer();
      }
      return charBuffer.getWriter();
   }

   public OutputStream getPortletOutputStream() throws IOException
   {
      ByteBuffer byteBuffer;
      if (buffer != null)
      {
         if (buffer instanceof ByteBuffer)
         {
            byteBuffer = (ByteBuffer)buffer;
         }
         else
         {
            throw new IllegalStateException("The method getWriter() was already called");
         }
      }
      else
      {
         buffer = byteBuffer = new ByteBuffer();
      }
      return byteBuffer.getOutputStream();
   }

   public PortletURL createRenderURL()
   {
      return PortletURLImpl.createRenderURL(invocation, preq);
   }

   public PortletURL createActionURL()
   {
      return PortletURLImpl.createActionURL(invocation, preq);
   }

   public Locale getLocale()
   {
      return invocation.getUserContext().getLocale();
   }

   public void setBufferSize(int bufferSize)
   {
      if (bufferSize > -0)
      {
         this.bufferSize = bufferSize;
      }
   }

   public int getBufferSize()
   {
      return bufferSize;
   }

   public void flushBuffer() throws IOException
   {
      if (buffer != null)
      {
         buffer.commit();
      }
   }

   public void resetBuffer()
   {
      // Clear the buffer
      if (buffer != null)
      {
         buffer.reset();
      }
   }

   public void reset()
   {
      // Clear the buffer
      resetBuffer();

      // And properties
      getProperties().clear();
   }

   public boolean isCommitted()
   {
      return buffer != null && buffer.isCommited();
   }

   public ResourceURL createResourceURL()
   {
      return ResourceURLImpl.createResourceURL(invocation, preq);
   }

   private void setCache(String key, String value)
   {
      if (MimeResponse.EXPIRATION_CACHE.equals(key))
      {
         if (value != null)
         {
            try
            {
               int expirationSecs = Integer.parseInt(value);
               getCacheControl().setExpirationTime(expirationSecs);
            }
            catch (NumberFormatException e)
            {
               // todo: Make the portlet log instead
//            log.warn("Portlet " + invocation.getTarget() +
//                     " set a non integer cache value override during render " + value, e);
            }
         }
      }
      else if (MimeResponse.ETAG.equals(key))
      {
         if (value != null)
         {
            getCacheControl().setETag(value);
         }
      }
      else if (MimeResponse.USE_CACHED_CONTENT.equals(key))
      {
         if (value != null)
         {
            getCacheControl().setUseCachedContent(true);
         }
      }
   }

   public void addProperty(String key, String value) throws IllegalArgumentException
   {
      if (MimeResponse.EXPIRATION_CACHE.equals(key)
         || MimeResponse.ETAG.equals(key)
         || MimeResponse.USE_CACHED_CONTENT.equals(key))
      {
         setCache(key, value);
      }
      else
      {
         super.addProperty(key, value);
      }
   }


   public void setProperty(String key, String value) throws IllegalArgumentException
   {
      if (MimeResponse.EXPIRATION_CACHE.equals(key)
         || MimeResponse.ETAG.equals(key)
         || MimeResponse.USE_CACHED_CONTENT.equals(key))
      {
         setCache(key, value);
      }
      else
      {
         super.setProperty(key, value);
      }
   }

   public CacheControl getCacheControl()
   {
      if (cacheControl == null)
      {
         PortletInfo info = preq.container.getInfo();
         CacheInfo cacheInfo = info.getCache();
         cacheControl = new CacheControlImpl(cacheInfo.getExpirationSecs(), CacheScope.PRIVATE);
      }
      return cacheControl;
   }
}
