package com.example.calculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class CalculateHelper {
    public static double num1;
    public static double num2;
    public static double resultNumber;

    private ArrayList splitTokens(String equation){ //연산식을 arraylist에 담아 반환
        String[] constant = equation.split(" ");

        ArrayList constantList = new ArrayList();
        double number=0;

        boolean flag= false;
        for(String data:constant){
            if(data.equals(" ")){
                continue;
            }
            if(checkNumber(data)){
                number = number*10+Double.parseDouble(data);
                flag=true;
            }
            else{
                if(flag){
                    constantList.add(number);
                    number=0;
                }
                flag=false;
                constantList.add(data);
            }
        }

        if(flag){
            constantList.add(number);
        }

        return constantList;
    }

    private ArrayList infixToPostfix(ArrayList constant){//중위 표기법을 후위 표기법을 바꿈
        ArrayList result= new ArrayList();
        HashMap level = new HashMap();
        Stack stack = new Stack();

        level.put("*",3);
        level.put("/",3);
        level.put("+",2);
        level.put("-",2);
        level.put("(",1); //해쉬맵으로 우선순위를 3,2,1로 나눔

        for(Object object : constant){
            if(object.equals(")")){
                stack.push(object);
            }else if(object.equals(")")){
                while(!stack.peek().equals("(")){
                    Object val = stack.pop();
                    if(!val.equals("(")){
                        result.add(val);
                    }
                }
                stack.pop(); // (가 나오면 스택에 넣고 )가 나오면 모든내용을 pop한다.
            }else if(level.containsKey(object)){
                if(stack.isEmpty()){
                    stack.push(object);
                }else{
                    if(Double.parseDouble(level.get(stack.peek()).toString()) >= Double.parseDouble(level.get(object).toString())){
                        result.add(stack.pop());
                        stack.push(object);
                    }else{
                        stack.push(object);
                    }
                }
            }else{
                result.add(object);
            }
        }
        while(!stack.isEmpty()){
            result.add(stack.pop());
        }
        return result; //우선순위가 높은게 나오면 pop해서 result에 추가하고 낮은 것을 쌓음
    }

    private Double postFixEval(ArrayList expr){ //후위 표기법의 식을 계산
        Stack numberStack = new Stack();
        for(Object o : expr){
            if(o instanceof Double){
                numberStack.push(o); //숫자가 나올 경우 스택에 push!!!
            }else if(o.equals("+")){
                num1=(Double)numberStack.pop();
                num2=(Double)numberStack.pop();
                numberStack.push(num2+num1);
            }else if(o.equals("-")){
                num1=(Double)numberStack.pop();
                num2=(Double)numberStack.pop();
                numberStack.push(num2-num1);
            }else if(o.equals("*")){
                num1=(Double)numberStack.pop();
                num2=(Double)numberStack.pop();
                numberStack.push(num2*num1);
            }else if(o.equals("/")){
                num1=(Double)numberStack.pop();
                num2=(Double)numberStack.pop();
                numberStack.push(num2/num1);
            }
        }
        resultNumber = (Double)numberStack.pop(); //연산자가 나올경우 첫번째 pop한 수와 두번째 pop한 숫자 연산

        return resultNumber;
    }

    public Double process(String equation){//최종 값을 리턴하는 함수수
        ArrayList postfix = infixToPostfix(splitTokens(equation));
        Double result = postFixEval(postfix);
        return result;
    }

    public boolean checkNumber(String str){//아스키코드로 숫자인지 판별해서 boolean값 반환
        char check;

        if(str.equals("")){
            return false;
        }

        for(int i=0;i<str.length();i++){
            check=str.charAt(i);
            if(check<48||check>58){
                if(check!='.')
                    return false;
            }
        }

        return true;
    }

}