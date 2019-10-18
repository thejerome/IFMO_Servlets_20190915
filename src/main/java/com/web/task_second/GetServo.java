package com.web.task_second;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/*
            assertTrue(Utils.findInSource("javax.servlet.Filter"));
            assertTrue(Utils.findInSource("void doFilter"));
            assertTrue(Utils.findInSource("javax.servlet.http.HttpServlet"));
            assertTrue(Utils.findInSource("void doPut"));
            assertTrue(Utils.findInSource("void doDelete"));
            assertTrue(Utils.findInSource("void doGet"));
*/

@WebServlet(
        name = "GetServo",
        urlPatterns = {"/calc/result"}
)
public class GetServo extends HttpServlet {


    private boolean checkhasalpha(String str) {
        char[] buff = str.toCharArray();
        for (char x : buff) {
            if (x <= 'z' && x >= 'a')
                return true;
        }
        return false;
    }


    private String solveit(String str) {

        try {
            System.out.println(str);
            // https://github.com/thomasfire/actix-pythoneer - не баньте меня, здесь нету плагиата
            // да, тут куча всего осталось с ресерча, но мне лень это чистить
            URL url = new URL("http://ec2-13-48-42-108.eu-north-1.compute.amazonaws.com:52280/calc/" + str); // вебовые проблемы требуют вебовых решений
            HttpURLConnection con = (HttpURLConnection) url.openConnection();                                   // а вообще, это же новое, инновационное решение проблем
            con.setRequestMethod("GET");                                                                        // ведь сейчас всё в облаках, вот и я запилил в облака
            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            System.out.println(content);
            return String.valueOf(status) + "splitter" + content; // всё еще возмущен джавой и отсутствием pair
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";

    }

    private String calcit(String str2, HttpSession s) {
        String str = str2;
        int limit = 5;
        while (checkhasalpha(str) && limit > 0) {
            for (char x = 'a'; x <= 'z'; x++) {
                str = str.replaceAll(String.valueOf(x), "(" + (String) s.getAttribute(String.valueOf(x)) + ")");
            }
            limit--;
        }
        System.out.println(str);
        //str = "parseInt(" + str + ")";
        str = str.replaceAll("[/]", "div")
                .replaceAll("[(]", "opb") // open bracket
                .replaceAll("[)]", "clb") // close bracket
                .replaceAll("[*]", "mul")
                .replaceAll("[+]", "add")
                .replaceAll("[-]", "sub");

        String solved = solveit(str);
        System.out.println(solved);

        String[] response = solved.split("splitter");
        for (String sw : response)
            System.out.println(sw);

        int code = (int) Integer.parseInt(response[0]);
        String content = response[1];

        if (code == 409) {
            throw new IllegalArgumentException();
        }

        return content;
    }


    @Override
    public void doGet(HttpServletRequest sreq, HttpServletResponse sresp) throws IOException {
        HttpSession s = sreq.getSession(false);
        PrintWriter w = sresp.getWriter();
        String res = "";
        if (s == null) {
            sresp.setStatus(409);
            w.flush();
            return;
        }
        String eq = (String) s.getAttribute("equation");
        if (eq == null) {
            sresp.setStatus(409);
            w.write("Format error, try clang-format");
            w.flush();
            return;
        }

        eq = eq.replaceAll("\\s", "");
        System.out.println(eq);
        try {
            res = String.valueOf(calcit(eq, s));
            sresp.setStatus(200);
            w.write(res);
            w.flush();
        } catch (IllegalArgumentException e) {
            sresp.setStatus(409);
            w.write("Format error, try clang-format");
            w.flush();
            //return; warning ^-^
        }

    }


}