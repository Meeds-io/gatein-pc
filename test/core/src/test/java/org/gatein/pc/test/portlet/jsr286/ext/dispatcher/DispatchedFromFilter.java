package org.gatein.pc.test.portlet.jsr286.ext.dispatcher;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/** @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a> */
public class DispatchedFromFilter extends HttpServlet
{
   @Override
   protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
   {
      // Perhaps we should check it is called
   }
}
