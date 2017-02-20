package com.dbw.sixconnectandroid.utils;

import android.app.Application;
import android.util.Log;
import com.dbw.sixconnectandroid.activitys.BoardCell;
import com.dbw.sixconnectandroid.board.Board;
import com.dbw.sixconnectandroid.game.Referee;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by DBW on 2016/12/22.
 * 保存全局变量
 */
public class Data extends Application {

    private static final String TAG = "Data";
    public final int BLACK = 1;
    public final int WRITE = -1;
    public final int ROW = 19;
    public final int COLUMN = 19;

    public boolean isStarted = false;
    public boolean isPaused = false;

    private Board board;                                                //棋盘
    private int[][] currentBoard;

    private Stack<Map<String, Object>> stack;                           //保存棋盘的栈，用于实现悔棋的功能

    private int person, ai;

    private int who = BLACK;                                            //当前正在操作的棋子

    private boolean isFirst = true;                                     //是否下的第一个棋子
    private int index = 1;                                              //表示当前已经落下的棋子

    private Referee referee;                                            //裁判类

    public Data() {

        //初始化棋盘
        board = new Board(ROW, COLUMN);
        currentBoard = board.getBoard();

        //初始化裁判类
        referee = new Referee();

    }

    public int getWho() {                            //获取当前是黑子还是白子
        return who;
    }

    public void pause() {                            //暂停棋盘的点击事件
        isPaused = true;
    }

    public void unPause() {                          //从暂停中恢复
        isPaused = false;
    }

    public void start() {                            //开始游戏
        isStarted = true;
        stack = new Stack<>();                      //新建一个棋盘状态栈
    }

    public boolean addPiece(int id, int piece, BoardCell.Cell cellType) {                   //下子
        int x = id / COLUMN;
        int y = id % COLUMN;

        if (currentBoard[x][y] != 0) {

            return false;

        } else {

            currentBoard[x][y] = piece;
            board.setBoard(currentBoard);
            Log.d(TAG, "addPiece: set board");

            Map<String,Object> map = new HashMap<>();
            map.put("id",id);
            map.put("piece",piece);
            map.put("cellType",cellType);
            stack.push(map);                                      //将改变后的棋盘入栈


            return true;
        }
    }

    /**
     * 获取需要悔棋的棋子id
     *
     * @return 如果栈为空，就返回-1，如果栈非空，就返回栈顶
     */
    public Map<String, Object> getRegretId() {
        if (stack.empty()) {
            return null;
        } else {
            Map<String, Object> map = stack.peek();
            int id = (int) map.get("id");
            int x = id / COLUMN;
            int y = id % COLUMN;
            currentBoard[x][y] = 0;
            if (index == 1) {
                index = 2;
            }else if (index==2){
                index = 1;
            }

            return stack.pop();
        }
    }


    public void next() {                             //下一步，更新计算的指数
        if (isFirst) {
            index = 2;
            isFirst = false;
        }
        if (index == 2) {

            turn();
        } else {
            index++;
        }
    }


    private void turn() {                             //轮换棋子颜色
        index = 1;
        who = -who;
    }

    /**
     * 判断当前下的是第一手还是第二手棋子
     * @return true代表下的是第一手，false代表下的第二手
     */
    public boolean isFirst(){

        return index == 1;
    }
    /**
     * 重置棋盘的状态
     */
    public void cleanBoard(){
        board.cleanBoard();
        currentBoard = board.getBoard();
        isPaused = false;
        isFirst = true;
        isStarted = false;
        stack.clear();
        stack = null;
        who = BLACK;

    }



    public Board getBoard() {
        return board;
    }

    public int getPerson() {
        return person;
    }

    public void setPerson(int piece) {
        this.person = piece;
    }

    public int getAi() {
        return ai;
    }

    public void setAi(int piece) {
        this.ai = piece;
    }
}
