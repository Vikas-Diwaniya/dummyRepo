package com.flowdemo.core.servlets;

import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkflowData;
import com.day.cq.workflow.model.WorkflowModel;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service = Servlet.class)
@SlingServletPaths(
        value = {"/bin/executeworkflow" , "/geeks/executeworkflow" }
)
public class ExecuteWorkflow extends SlingSafeMethodsServlet {
    private static final Logger LOG = LoggerFactory.getLogger(ExecuteWorkflow.class);

    @Override
    protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws ServletException, IOException {

        String status = "Workflow Executing from the Variable we set earlier!";
        final ResourceResolver resourceResolver = req.getResourceResolver();
        String payload  = req.getRequestParameter("page").toString();
        try {
            if(StringUtils.isNotBlank(payload)) {
                WorkflowSession workflowSession = resourceResolver.adaptTo(WorkflowSession.class);

                WorkflowModel workflowModel = workflowSession.getModel("/var/workflow/models/geeks-page-version");
                WorkflowData workflowData = workflowSession.newWorkflowData("JCR_PATH", payload);
                workflowSession.startWorkflow (workflowModel, workflowData);

            }
        } catch   (Exception e) {
            LOG.info("\n ERROR IN WORKFLOW {}", e.getMessage());
        }

        resp.setContentType("application/json");
        resp.getWriter().write(status);
    }

}


