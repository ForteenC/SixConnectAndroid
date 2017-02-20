package com.dbw.sixconnectandroid.utils;

import android.util.Log;
import com.dbw.sixconnectandroid.board.Board;

import java.util.*;

/**
 * Created by DBW on 2016/12/24.
 * 估值类，对棋盘上的特定棋子进行估值
 */
public class Evaluate {

    private static final String TAG = "Evaluate";

    private static final Map<String,Integer> black_map = new HashMap<>();             //基本棋型
    private static final Map<String,Integer> write_map = new HashMap<>();
    static {
        black_map.put("1111110",10000000);                                        //胜利
        black_map.put("1111111-",10000000);                                        //胜利
        black_map.put("1111111",10000000);                                        //胜利
        black_map.put("0111110",1000000);                                         //活五
        black_map.put("0111111-",500000);                                          //眠5
        black_map.put("1111100",500000);                                           //眠5
        black_map.put("0111100",100000);                                           //活4
        black_map.put("0011111-",80000);                                          //眠4
        black_map.put("0111101-",80000);                                          //眠4
        black_map.put("0111000",10000);                                           //活3
        black_map.put("0011100",10000);                                           //活3
        black_map.put("0001111-",5000);                                          //眠3
        black_map.put("0100111-",5000);                                          //眠3
        black_map.put("0010111-",5000);                                          //眠3
        black_map.put("0011000",1000);                                           //活2
        black_map.put("0110000",1000);                                           //活2
        black_map.put("0101000",1000);                                           //活2
        black_map.put("0010100",1000);                                           //活2
        black_map.put("0011001-",500);                                           //眠2
        black_map.put("0110001-",500);                                           //眠2
        black_map.put("0001101-",500);                                           //眠2
        black_map.put("0000111-",500);                                           //眠2
        black_map.put("0000001",200);
        black_map.put("0000010",200);
        black_map.put("0000100",200);
        black_map.put("0001000",200);
    }
    static {
        write_map.put("1-1-1-1-1-1-0",10000000);                                 //胜利
        write_map.put("1-1-1-1-1-1-1-",10000000);                                //胜利
        write_map.put("1-1-1-1-1-1-1",10000000);                                 //胜利
        write_map.put("01-1-1-1-1-0",1000000);                                   //活五
        write_map.put("01-1-1-1-1-1",500000);                                    //眠5
        write_map.put("1-1-1-1-1-00",500000);                                    //眠5
        write_map.put("01-1-1-1-00",100000);                                     //活4
        write_map.put("001-1-1-1-1",80000);                                      //眠4
        write_map.put("01-1-1-1-01",80000);                                      //眠4
        write_map.put("01-1-1-000",10000);                                       //活3
        write_map.put("001-1-1-00",10000);                                       //活3
        write_map.put("0001-1-1-1",5000);                                          //眠3
        write_map.put("01-001-1-1",5000);                                          //眠3
        write_map.put("001-01-1-1",5000);                                          //眠3
        write_map.put("001-1-000",1000);                                           //活2
        write_map.put("01-1-0000",1000);                                           //活2
        write_map.put("01-01-000",1000);                                           //活2
        write_map.put("001-01-00",1000);                                           //活2
        write_map.put("001-1-001",500);                                           //眠2
        write_map.put("01-1-0001",500);                                           //眠2
        write_map.put("0001-1-01",500);                                           //眠2
        write_map.put("00001-1-1",500);                                           //眠2
        write_map.put("0000001-",200);
        write_map.put("000001-0",200);
        write_map.put("00001-00",200);
        write_map.put("0001-000",200);
    }


    public  static  int evaluate(Board board,int piece){
        int[][] boardList = board.getBoard();
        int row = board.getRow();
        int column = board.getColumn();
        int space = 7;                                                  //只取7个格来缓存进行判断
        int totalValue = 0;                                             //棋盘的总估值
        StringBuilder sb1 = new StringBuilder();                         //获取行基本棋型
        StringBuilder sb2 = new StringBuilder();                         //获取列基本棋型
        StringBuilder sb3 = new StringBuilder();                         //左斜
        StringBuilder sb4 = new StringBuilder();                         //右斜

        for (int i = 0; i < row-space; i++) {
            for (int j = 0; j < column-space; j++) {


                //判断列
                for (int k = j; k < j+space; k++) {
                    sb1.append(boardList[i][k]);
                }
                totalValue += match(sb1.toString(),sb1.reverse().toString(),piece);
                sb1.delete(0,sb1.length());                        //清空缓存

                //判断行
                for (int m = i;m<i+space;m++){
                    sb2.append(boardList[m][j]);
                }
                totalValue += match(sb2.toString(),sb2.reverse().toString(),piece);
                sb2.delete(0,sb2.length());


                //判断左斜向
                for (int o = 0; o < space; o++) {
                    sb3.append(boardList[i+o][j+o]);
                }
                totalValue += match(sb3.toString(),sb3.reverse().toString(),piece);
                sb3.delete(0,sb3.length());


                //判断右斜向
                for (int p = space, u = 0; p > 0 && u< space; p--,u++) {
                    sb4.append(boardList[i+p][j+u]);
                }
                totalValue += match(sb4.toString(),sb4.reverse().toString(),piece);
                sb4.delete(0,sb4.length());

            }
        }



        return totalValue;
    }
    private static int match(String basic,String reBasic,int piece){
        int tempValue = 0;                                              //基本棋型的估值
        Set<String> list;
        Map<String,Integer> map;
        if (piece==1){
            map = black_map;
        }else {
            map = write_map;
        }

        list = map.keySet();
        Iterator<String> iterator = list.iterator();

        while (iterator.hasNext()){
            String next = iterator.next();
            if (basic.equals(next)){
                tempValue = map.get(basic);
            }
            if (reBasic.equals(next)){
                tempValue = map.get(reBasic);
            }
        }


        return tempValue;
    }

}
