package mainpkg;

import java.io.File;
import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

public class Main {

    public static void main(String[] args) throws Exception {
        int port = 8080;

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);

        String webLocation = "src/main/webapp";
        Context context = tomcat.addWebapp("", new File(webLocation).getAbsolutePath());

        File additionalClasses = new File("build/classes");

        StandardRoot resources = new StandardRoot(context);

        resources.addPreResources(new DirResourceSet(
                resources,
                "/WEB-INF/classes",
                additionalClasses.getAbsolutePath(), "/"
        ));

        context.setResources(resources);

        tomcat.start();
        tomcat.getServer().await();
    }

}
