package com.dorofeeva;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

@WebServlet(
        name = "Calc",
        urlPatterns = {"/calc"}
)
public class Calc extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        PrintWriter out = resp.getWriter();
        final Map<String, String[]> parameterMap= req.getParameterMap();
        //честно подсмотрела вариант для замены всех переменных и пробелов
        String equation = req.getParameter("equation").replace(" ", "");

        while (!Pattern.matches("^[0-9*+-/()]+$", equation)) {
            for (Map.Entry<String, String[]> parameterEntry : parameterMap.entrySet()) {
                equation = equation.replace(parameterEntry.getKey(), parameterEntry.getValue()[0]);
            }
        }
        out.println(cal(equation));
        out.flush();
        out.close();
    }
    private int cal(String st) {
        int ans;
        int i1;
        int i0;
        int i2;
        int Tans=0;
        int pt1;
        int pt2;
        char k;
        String sk;
        String sl;
        StringBuilder STB = new StringBuilder(st);
        StringBuilder TempSB = new StringBuilder();
        StringBuilder Temp1 = new StringBuilder();
        StringBuilder Temp2 = new StringBuilder();
        while (STB.toString().contains("(")){
            i1 = STB.indexOf("(");
            i2 = STB.indexOf(")");
            TempSB.append(STB.substring(i1+1,i2));
            if (TempSB.toString().contains("(")){
                sl = TempSB.substring(TempSB.indexOf("(")+1);
                TempSB.setLength(0);
                TempSB.append(sl);
                i1 = i2 - TempSB.length() - 1;
            }
            sk = TempSB.toString();
            Tans = cal(sk);
            STB.delete(i1,i2+1);
            STB.insert(i1,Tans);
            TempSB.setLength(0);
        }
        while (STB.toString().contains("*")||STB.toString().contains("/")){
            k = ud(STB.toString());
            i0 = STB.indexOf(String.valueOf(k));
            i1 = i0-1;
            while ((!op(STB.charAt(i1)))&&(i1>-1)){
                Temp1.append(STB.charAt(i1));
                --i1;
                if(i1<0){
                    break;
                }
            }
            Temp1.reverse();
            if (i1>-1){
                if((i1==0)&&(STB.charAt(i1)=='-')){
                    Temp1.insert(0,'-');
                }
                else if (i1!=0){
                    sk = STB.substring(i1-1,i1+1);
                    if (negative(sk))
                        Temp1.insert(0,'-');
                }
            }
            pt1 = Integer.valueOf(Temp1.toString());

            Temp1.setLength(0);
            i2 = i0+1;
            if (STB.charAt(i2)=='-')
            {
                Temp2.append('-');
                i2++;
            }
            while ((!op(STB.charAt(i2)))&&(i2!=STB.length())){
                Temp2.append(STB.charAt(i2));
                i2++;
                if(i2==STB.length()){
                    break;
                }
            }
            pt2 = Integer.valueOf(Temp2.toString());
            Temp2.setLength(0);
            switch(k){
                case'*':
                    Tans = pt1*pt2;
                    break;
                case '/':
                    Tans = pt1/pt2;
                    break;
            }
            STB.delete(i1+1,i2);
            STB.insert(i1+1,Integer.toString(Tans));
//            STB.replace(i1,i2,Integer.toString(Tans));
        }
        while (STB.toString().contains("+")||STB.toString().contains("-")){
            if (STB.charAt(0)=='-'){
                TempSB.setLength(0);
                TempSB.append(STB.substring(1));
                if (Pattern.matches("^[0-9]+$", TempSB)){
                    break;
                }
            }
            Tans = 0;
            k = pm(STB.toString());
            i0 = STB.indexOf(String.valueOf(k));
            i1 = i0-1;
            while ((i1!=-1)&&(!op(STB.charAt(i1)))){
                Temp1.append(STB.charAt(i1));
                --i1;
            }
            Temp1.reverse();
            if (i1>-1){
                if((i1==0)&&(STB.charAt(i1)=='-')){
                    Temp1.insert(0,'-');
                }
                else if (i1!=0){
                    sk = STB.substring(i1-1,i1);
                    if (negative(sk))
                        Temp1.insert(0,'-');
                }
            }
            pt1 = Integer.valueOf(Temp1.toString());
            Temp1.setLength(0);
            i2 = i0+1;
            if (STB.charAt(i2)=='-')
            {
                Temp2.append('-');
                i2++;
            }
            while ((i2!=STB.length())&&(!op(STB.charAt(i2)))){
                Temp2.append(STB.charAt(i2));
                ++i2;
                if(i2==STB.length()){
                    break;
                }
            }
            pt2 = Integer.valueOf(Temp2.toString());
            Temp2.setLength(0);
            switch(k){
                case'+':
                    Tans = pt1 + pt2;
                    break;
                case '-':
                    Tans = pt1 - pt2;
                    break;
            }
            STB.delete(i1+1,i2);
            STB.insert(i1+1,Integer.toString(Tans));
            //STB.replace(i1,i2+1,Integer.toString(Tans));
        }
        ans = Integer.valueOf(STB.toString());
        return(ans);
    }
    private char pm(String s){
        int a;
        int b;

        if ((s.contains("+"))&&(s.contains("-"))){
            a = s.indexOf("+");
            b = s.indexOf("-");
            if (a<b)
                return('+');
            if (b<a)
                return ('-');
        }
        else if (s.contains("+"))
            return ('+');
        return('-');
    }
    private char ud(String s){
        int a;
        int b;

        if ((s.contains("*"))&&(s.contains("/"))){
            a = s.indexOf("*");
            b = s.indexOf("/");
            if (a<b)
                return('*');
            if (b<a)
                return ('/');
        }
        else if (s.contains("*"))
            return ('*');
        return('/');
    }
    private boolean negative ( String s){
        if ((s.charAt(1)=='-')&&(op(s.charAt(0)))){
            return true;
        }
        else
            return false;
    }
    private boolean op(char c){
        switch (c){
            case '+':
            case '-':
            case '*':
            case '/':
                return true;
            default:
                return false;
        }
    }
}