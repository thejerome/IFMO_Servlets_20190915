package com.dorofeeva;
public class CalcP {

    public static int cal(String st) {
        int ans;
        int i1;
        int i2;
        int Tans;
        char k;
        String sk;
        String sl;
        StringBuilder STB = new StringBuilder(st);
        StringBuilder TempSB = new StringBuilder();
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
            STB = change(STB, k);
        }
        while (STB.toString().contains("+")||STB.toString().contains("-")){
            if (STB.charAt(0)=='-'){
                break;
            }
            k = pm(STB.toString());
            STB = change(STB, k);
        }
        ans = Integer.valueOf(STB.toString());
        return(ans);
    }
    private static char pm(String s){
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
    private  static StringBuilder change (StringBuilder ST, char k){
        int i0;
        int i1;
        int i2;
        String sk;
        int pt1;
        int pt2;
        int Tans;
        StringBuilder T1 = new StringBuilder();
        StringBuilder T2 = new StringBuilder();
        i0 = ST.indexOf(String.valueOf(k));
        i1 = i0-1;
        while ((!op(ST.charAt(i1)))&&(i1>-1)){
            T1.append(ST.charAt(i1));
            --i1;
            if(i1<0){
                break;
            }
        }
        T1.reverse();
        if (i1>-1){
            if((i1==0)&&(ST.charAt(i1)=='-')){
                T1.insert(0,'-');
                i1--;
            }
            else if (i1!=0){
                sk = ST.substring(i1-1,i1+1);
                if (negative(sk))
                {
                    T1.insert(0,'-');
                    i1--;
                }
            }
        }
        pt1 = Integer.valueOf(T1.toString());
        T1.setLength(0);
        i2 = i0+1;
        if (ST.charAt(i2)=='-')
        {
            T2.append('-');
            i2++;
        }
        while ((!op(ST.charAt(i2)))&&(i2!=ST.length())){
            T2.append(ST.charAt(i2));
            i2++;
            if(i2==ST.length()){
                break;
            }
        }
        pt2 = Integer.valueOf(T2.toString());
        T2.setLength(0);
        Tans = count(pt1,pt2,k);
        ST.delete(i1+1,i2);
        ST.insert(i1+1,Tans);
        return ST;
    }
    private static int count(int p1, int p2, char k){
        int a=0;
        switch(k){
            case '*':
                a = p1*p2;
                break;
            case '/':
                a = p1/p2;
                break;
            case '+':
                a=p1+p2;
                break;
            case '-':
                a=p1-p2;
                break;
            default:
                break;
        }
        return(a);
    }
    private static char ud(String s){
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
    private static boolean negative ( String s){
        if ((s.charAt(1)=='-')&&(op(s.charAt(0)))){
            return true;
        }
        else
            return false;
    }
    private static boolean op(char c){
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

