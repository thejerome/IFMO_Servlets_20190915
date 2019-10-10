package com.efimchick.ifmo.web.servlets;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Random;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class CalcServletTest {

    private static Tomcat tomcat;

    @BeforeClass
    public static void startServer() throws Exception {

        int port = 8080;

        tomcat = new Tomcat();
        tomcat.setPort(port);

        String webappDirLocation = "src/main/webapp/";
        StandardContext ctx = (StandardContext) tomcat.addWebapp(
                "", new File(webappDirLocation).getAbsolutePath()
        );
        System.out.println(
                "configuring app with basedir: "
                        + new File("./" + webappDirLocation).getAbsolutePath()
        );

        // Declare an alternative location for your "WEB-INF/classes" dir
        // Servlet 3.0 annotation will work

        File additionWebInfClasses = new File("build/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(
                new DirResourceSet(
                        resources,
                        "/WEB-INF/classes",
                        additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);

        tomcat.start();

    }

    @AfterClass
    public static void stopServer() throws Exception {
        tomcat.stop();
    }

    private static void testEquation(final String equation, final ImmutableMap<String, Object> params, final int result) throws URISyntaxException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        final URIBuilder uriBuilder = new URIBuilder()
                .setScheme("http")
                .setHost("localhost:8080")
                .setPath("/calc");
        uriBuilder.setParameter("equation", equation);
        for (Map.Entry<String, Object> param : params.entrySet()) {
            uriBuilder.setParameter(param.getKey(), String.valueOf(param.getValue()));
        }


        final URI uri = uriBuilder.build();
        System.out.println(uri);
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = httpClient.execute(httpGet);

        final String responseText = EntityUtils.toString(response.getEntity()).trim();

        assertEquals(String.valueOf(result), responseText);

    }

    @Test
    public void test001() throws Exception {
        testEquation("a+b/c",
                ImmutableMap.of("a", 1, "b", 2, "c", 3),
                1);
    }

    @Test
    public void test002() throws Exception {
        testEquation("x+y/z",
                ImmutableMap.of("x", 1, "y", 2, "z", 3),
                1);
    }

    @Test
    public void test003() throws Exception {
        testEquation("x+y/z",
                ImmutableMap.of("z", 1, "y", 2, "x", 3),
                5);
    }

    @Test
    public void test004() throws Exception {
        testEquation("x+y/z",
                ImmutableMap.of("x", 1, "y", 2, "z", "x"),
                3);
    }

    @Test
    public void test005() throws Exception {
        testEquation("(f + k)*(h - g)/f",
                ImmutableMap.of("f", 61, "k", 32, "h", 354, "g", 19),
                510);
    }

    @Test
    public void test006() throws Exception {
        testEquation("a/b/c/d",
                ImmutableMap.of("a", 89411, "b", 32, "c", 7, "d", 5),
                79);
    }

    @Test
    public void test007() throws Exception {
        final int a = randomInt();
        final int b = randomInt();
        final int c = randomInt();
        testEquation("a+b/c",
                ImmutableMap.of("a", a, "b", b, "c", c),
                a + b / c);
    }

    @Test
    public void test008() throws Exception {
        final int a = randomInt();
        final int b = randomInt();
        final int c = randomInt();
        testEquation("(c*(a-b)/b)*a",
                ImmutableMap.of("a", a, "b", b, "c", c),
                (c * (a - b) / b) * a);
    }

    @Test
    public void test009() throws Exception {
        final int a = randomInt();
        final int b = randomInt();
        final Arg<Integer> c = randomChoose(arg("a", a), arg("b", b));
        testEquation("(c*(a-b)/b)*a",
                ImmutableMap.of("a", a, "b", b, "c", c.name),
                (c.val * (a - b) / b) * a);
    }

    private Random random = new Random();

    private int randomInt() {
        return random.nextInt(100) + 1;
    }

    @SafeVarargs
    private final <T> T randomChoose(T... values) {
        return values[random.nextInt(values.length)];
    }

    private static Arg<Integer> arg(final String name, final Integer val){
        return new Arg<Integer>(name, val);
    }

    private static class Arg<T> {
        private final String name;
        private final T val;

        private Arg(final String name, final T val) {
            this.name = name;
            this.val = val;
        }
    }

}


/*
a + a*(b-c) + d/a

(f + k)*(h + g)/f

a/b/c/d






 */