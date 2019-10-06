package com.web.lab;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;

@WebServlet(
        name = "JavaSucks",
        urlPatterns = {"/calc"}
)
public class JavaSucks extends HttpServlet {

    private int calc_it(String m_str) {
        Deque<Integer> deque_ = new LinkedList<>();
        String equation = to_reversed_polska_notazia(m_str);
        int left_part;
        int right_part;
        StringTokenizer str_parter = new StringTokenizer(equation);
        String temp_str;
        /**/
        while (str_parter.hasMoreTokens()) {

            temp_str = str_parter.nextToken().trim();

            if (1 == temp_str.length() && is_action(temp_str.charAt(0))) {
                right_part = deque_.pop();
                left_part = deque_.pop();

                /**/
                switch (temp_str.charAt(0)) {
                    case '+':
                        left_part += right_part;
                        break;
                    case '-':
                        left_part -= right_part;
                        break;
                    case '/':
                        left_part /= right_part;
                        break;
                    case '*':
                        left_part *= right_part;
                        break;
                    default:
                        break;
                }
                /**/
                deque_.push(left_part);

            } else {
                left_part = Integer.parseInt(temp_str);
                deque_.push(left_part);
            }


            /*end while*/
        }
        return deque_.pop();
    }

    private int get_action_weight(char x) {
        switch (x) {
            case '*':
            case '/':
                return 2;
            default:
                return 1;
        }
    }


    private boolean ist_digitable(String mstr) {
        for (int i = 0; i < mstr.length(); ++i) {
            if (mstr.charAt(i) > '9' || mstr.charAt(i) < '0') {
                return false;
            }
        }
        return true;
    }


    private String to_reversed_polska_notazia(String ne_polska) {
        char buff;
        StringBuilder result = new StringBuilder();
        char temp;
        StringBuilder str_b_st = new StringBuilder();
        /*
         *
         *
         * */
        for (int i = 0; i < ne_polska.length(); i++) {
            /**/
            buff = ne_polska.charAt(i);

            if (is_action(buff)) {

                while (str_b_st.length() > 0) {
                    temp = str_b_st.substring(str_b_st.length() - 1).charAt(0);
                    /*
                     *
                     * */
                    if (is_action(temp) && (get_action_weight(buff) <= get_action_weight(temp))) {
                        result.append(" ").append(temp).append(" ");
                        /*-^-^-*/
                        str_b_st.setLength(str_b_st.length() - 1);
                    } else {
                        result.append(" ");
                        break;
                    }
                }

                result.append(" ");
                str_b_st.append(buff);

            } else if ('(' == buff)
                str_b_st.append(buff);

            else if (')' == buff) {

                temp = str_b_st.substring(str_b_st.length() - 1).charAt(0);

                while ('(' != temp) {
                    result.append(" ").append(temp);
                    str_b_st.setLength(str_b_st.length() - 1);
                    /*berem substring*/
                    temp = str_b_st.substring(str_b_st.length() - 1).charAt(0);
                }

                str_b_st.setLength(str_b_st.length() - 1);

            } else
                result.append(buff);

            /*end for*/
        }

        while (str_b_st.length() > 0) {
            result.append(" ").append(str_b_st.substring(str_b_st.length() - 1));
            str_b_st.setLength(str_b_st.length() - 1);
        }

        return result.toString();
    }

    private boolean is_action(char x) {
        switch (x) {
            case '+':
            case '*':
            case '/':
            case '-':
                return true;
            default:
                return false;
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        PrintWriter out_writer = resp.getWriter();
        String eq_param = req.getParameter("equation");
        Map<String, String[]> parameters_dict = req.getParameterMap();

        StringBuilder str_b = new StringBuilder();
        /*
         * */
        for (int i = 0; i < eq_param.length(); ++i) {
            char current = eq_param.charAt(i);
            if (current >= 'a' && current <= 'z') {
                String buff = parameters_dict.get(String.valueOf(/**/current))[0];
                while (/*-----*/!ist_digitable(buff)) {
                    /*---*/
                    buff = parameters_dict.get(/**/buff)[/**/0];
                }
                str_b.append(buff);
            } else {
                str_b.append(eq_param.charAt(i));
            }/*

             */
        }
        eq_param = str_b.toString();
        out_writer.print(calc_it(eq_param));
        out_writer.flush(/**/);
        out_writer.close(/**/);
    }


}