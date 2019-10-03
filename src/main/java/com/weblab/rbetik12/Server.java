package com.weblab.rbetik12;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

public class Server {
    public static void main(String[] args) throws Exception{
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8000);

        String webappDir = "src/main/webapp";
        StandardContext ctx = (StandardContext) tomcat.addWebapp("", new File(webappDir).getAbsolutePath());

        File additionalClasses = new File("build/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", additionalClasses.getAbsolutePath(), "/"));

        ctx.setResources(resources);

        tomcat.start();
        tomcat.getServer().await();
    }
}
