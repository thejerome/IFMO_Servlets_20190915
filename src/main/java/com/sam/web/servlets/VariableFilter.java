//package com.sam.web.servlets;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@WebFilter(filterName = "ServletFilterVariable",
//        urlPatterns = "/calc/*")
//public class VariableFilter implements javax.servlet.Filter {
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {}
//
//    @Override
//    public void doFilter(ServletRequest reqSer, ServletResponse respSer, FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpServletRequest req = (HttpServletRequest) reqSer;
//        HttpServletResponse resp = (HttpServletResponse) respSer;
//
//        String url = req.getRequestURI().substring(6);
//        System.out.println(url);
//        if (url.length() == 1)
//            System.out.println(url);
//        boolean notError = true;
//
//    }
//
//    private boolean goodFormatVar(String name, String value){
//        if (name.charAt(0) < 'a' || name.charAt(0) >'z' || name.length() != 1)
//            return false;
//        try{
//            int a = Integer.parseInt(value);
//            if (a < -10000 && a > 10000)
//                return false;
//        } catch (Exception e) {
//            return false;
//        }
//        return true;
//    }
//
//
//    private boolean goodFormatEquation(String equation){
//        for (int i = 0; i < equation.length() - 1; i++) {
//            if (Character.isLetter(equation.charAt(i)) && Character.isLetter(equation.charAt(i + 1)))
//                return false;
//        }
//        return true;
//    }
//
//    @Override
//    public void destroy() {}
//}
//
