package com.xwtxumm.xummnftwalletcreator.controller;

import com.xwtxumm.xummnftwalletcreator.impl.Xumm;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;

import java.io.*;
import java.util.Objects;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class AuthenticationController extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession httpSession = request.getSession(false);
        if (httpSession == null || Objects.requireNonNull(httpSession).getAttribute("xumm") == null) {
            httpSession = request.getSession();
            httpSession.setMaxInactiveInterval(180);
            Xumm x = new Xumm();
            httpSession.setAttribute("xumm", x);

            //Detect If Smartphone / Personal computer
            UserAgentStringParser parser = UADetectorServiceFactory.getOnlineUpdatingParser();
            ReadableUserAgent agent = parser.parse(request.getHeader("User-Agent"));
            x.setDeviceType(agent.getDeviceCategory().getCategory().getName());

            x.processAuthorization();
            response.sendRedirect(x.getLoginURL_Redirect());
        }else{
            Xumm x = (Xumm)httpSession.getAttribute("xumm");
            x.checkAuthorization();
            RequestDispatcher dispatcher = request.getRequestDispatcher("dashboard.jsp");
            dispatcher.forward(request, response);
        }
    }
}
