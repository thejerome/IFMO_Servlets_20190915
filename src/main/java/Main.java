import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {

        int port = 8080;

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);

        String webappDirLocation = "src/main/webapp/";
        StandardContext ctx = (StandardContext) tomcat.addWebapp(
                "", new File(webappDirLocation).getAbsolutePath()
        );
        System.out.println(
                "configuring app with basedir: "
                        + new File("./" + webappDirLocation).getAbsolutePath()
        );

        File additionWebInfClasses = new File("build/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(
                new DirResourceSet(
                        resources,
                        "/WEB-INF/classes",
                        additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);

        tomcat.start();
        tomcat.getServer().await();
    }
}

