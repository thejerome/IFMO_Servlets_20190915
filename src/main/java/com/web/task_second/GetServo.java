package com.web.task_second;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.el.ELProcessor;
import java.io.IOException;
import java.io.PrintWriter;


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

    public static int floored(double x) {
        return (int)Math.floor(Math.abs(x));
    }

    public int eval(String str) throws NoSuchMethodException, ClassNotFoundException {
        ELProcessor elp = new ELProcessor();
        elp.defineFunction("", "floored", "com.web.task_second.GetServo", "floored");
        Integer name = (Integer) elp.getValue(str, Integer.TYPE);
        return (int) name;
    }


    public int signof(int x) {
        if (x > 0) return 1;
        if (x < 0) return -1;
        return 0;
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

        String str_ = str.replaceAll("[(]", "floored(");
         //str = str.replaceAll("[(]", "((");
        System.out.println(str_);
        //str = "parseInt(" + str + ")";
        try {
            int evaled = eval(str);
            int evaled2 = eval(str_);
            System.out.println("Evaled:" + evaled);
            return String.valueOf(evaled2 * signof(evaled));
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
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