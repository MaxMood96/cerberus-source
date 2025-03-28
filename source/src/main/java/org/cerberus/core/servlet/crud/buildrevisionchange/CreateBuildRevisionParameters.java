/**
 * Cerberus Copyright (C) 2013 - 2025 cerberustesting
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This file is part of Cerberus.
 *
 * Cerberus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Cerberus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Cerberus.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.cerberus.core.servlet.crud.buildrevisionchange;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cerberus.core.crud.entity.BuildRevisionParameters;
import org.cerberus.core.crud.entity.LogEvent;
import org.cerberus.core.engine.entity.MessageEvent;
import org.cerberus.core.crud.factory.IFactoryBuildRevisionParameters;
import org.cerberus.core.enums.MessageEventEnum;
import org.cerberus.core.exception.CerberusException;
import org.cerberus.core.crud.service.IBuildRevisionParametersService;
import org.cerberus.core.crud.service.ILogEventService;
import org.cerberus.core.crud.service.impl.LogEventService;
import org.cerberus.core.util.ParameterParserUtil;
import org.cerberus.core.util.answer.Answer;
import org.cerberus.core.util.servlet.ServletUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

/**
 *
 * @author bcivel
 */
@WebServlet(name = "CreateBuildRevisionParameters", urlPatterns = {"/CreateBuildRevisionParameters"})
public class CreateBuildRevisionParameters extends HttpServlet {

    private static final Logger LOG = LogManager.getLogger(CreateBuildRevisionParameters.class);
    private final String OBJECT_NAME = "BuildRevisionParameters";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws org.cerberus.core.exception.CerberusException
     * @throws org.json.JSONException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, CerberusException, JSONException {
        JSONObject jsonResponse = new JSONObject();
        Answer ans = new Answer();
        MessageEvent msg = new MessageEvent(MessageEventEnum.DATA_OPERATION_ERROR_UNEXPECTED);
        msg.setDescription(msg.getDescription().replace("%DESCRIPTION%", ""));
        ans.setResultMessage(msg);
        PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
        String charset = request.getCharacterEncoding() == null ? "UTF-8" : request.getCharacterEncoding();

        response.setContentType("application/json");

        // Calling Servlet Transversal Util.
        ServletUtil.servletStart(request);
        
        /**
         * Parsing and securing all required parameters.
         */
        // Parameter that are already controled by GUI (no need to decode) --> We SECURE them
        // Parameter that needs to be secured --> We SECURE+DECODE them
        String build = ParameterParserUtil.parseStringParamAndDecodeAndSanitize(request.getParameter("build"), "", charset);
        String revision = ParameterParserUtil.parseStringParamAndDecodeAndSanitize(request.getParameter("revision"), "", charset);
        String release = ParameterParserUtil.parseStringParamAndDecodeAndSanitize(request.getParameter("release"), "", charset);
        String application = ParameterParserUtil.parseStringParamAndDecodeAndSanitize(request.getParameter("application"), "", charset);
        String project = ParameterParserUtil.parseStringParamAndDecodeAndSanitize(request.getParameter("project"), "", charset);
        String ticketidfixed = ParameterParserUtil.parseStringParamAndDecodeAndSanitize(request.getParameter("ticketidfixed"), "", charset);
        String bugidfixed = ParameterParserUtil.parseStringParamAndDecodeAndSanitize(request.getParameter("bugidfixed"), "", charset);
        String releaseowner = ParameterParserUtil.parseStringParamAndDecodeAndSanitize(request.getParameter("releaseowner"), "", charset);
        String subject = ParameterParserUtil.parseStringParamAndDecodeAndSanitize(request.getParameter("subject"), "", charset);
        String jenkinsbuildid = ParameterParserUtil.parseStringParamAndDecodeAndSanitize(request.getParameter("jenkinsbuildid"), "", charset);
        String mavenGroupID = ParameterParserUtil.parseStringParamAndDecodeAndSanitize(request.getParameter("mavengroupid"), "", charset);
        String mavenArtifactID = ParameterParserUtil.parseStringParamAndDecodeAndSanitize(request.getParameter("mavenartifactid"), "", charset);
        String mavenVersion = ParameterParserUtil.parseStringParamAndDecodeAndSanitize(request.getParameter("mavenversion"), "", charset);
        // Parameter that we cannot secure as we need the html --> We DECODE them
        String link = ParameterParserUtil.parseStringParamAndDecode(request.getParameter("link"), "", charset);
        String repositoryUrl = ParameterParserUtil.parseStringParamAndDecode(request.getParameter("repositoryurl"), "", charset);

        /**
         * Checking all constrains before calling the services.
         */
        if (false) {
            // No constrain on that Create operation.
        } else {
            /**
             * All data seems cleans so we can call the services.
             */
            ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
            IBuildRevisionParametersService buildRevisionParametersService = appContext.getBean(IBuildRevisionParametersService.class);
            IFactoryBuildRevisionParameters buildRevisionParametersFactory = appContext.getBean(IFactoryBuildRevisionParameters.class);

            BuildRevisionParameters brpData = buildRevisionParametersFactory.create(0, build, revision, release, application, project, ticketidfixed, bugidfixed, link, releaseowner, subject, null, jenkinsbuildid, mavenGroupID, mavenArtifactID, mavenVersion, repositoryUrl);
            ans = buildRevisionParametersService.create(brpData);

            if (ans.isCodeEquals(MessageEventEnum.DATA_OPERATION_OK.getCode())) {
                /**
                 * Object created. Adding Log entry.
                 */
                ILogEventService logEventService = appContext.getBean(LogEventService.class);
                logEventService.createForPrivateCalls("/CreateBuildRevisionParameters", "CREATE", LogEvent.STATUS_INFO, "Create BuildRevisionParameters : ['" + application + "'|'" + build + "'|'" + revision + "']", request);
            }
        }

        /**
         * Formating and returning the json result.
         */
        jsonResponse.put("messageType", ans.getResultMessage().getMessage().getCodeString());
        jsonResponse.put("message", ans.getResultMessage().getDescription());

        response.getWriter().print(jsonResponse);
        response.getWriter().flush();

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (CerberusException ex) {
            LOG.warn(ex);
        } catch (JSONException ex) {
            LOG.warn(ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (CerberusException ex) {
            LOG.warn(ex);
        } catch (JSONException ex) {
            LOG.warn(ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
