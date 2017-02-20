package com.dbw.sixconnectandroid.board;


/**
 * Created by DBW on 2016/12/8.
 * 棋盘类
 */
public class Board {

    private   int[][] board ;
    private   int ROW ;
    private   int COLUMN ;


    public Board(int row,int column){
        ROW = row;
        COLUMN = column;
        board = new int[ROW][COLUMN];
        for (int i=0;i<ROW;i++){
            for (int j=0;j<COLUMN;j++){
                board[i][j]=0;
            }
        }
    }

    public void setBoard(int[][] board){
        for (int i=0;i<ROW;i++){
            for (int j=0;j<COLUMN;j++){
                this.board[i][j]=board[i][j];
            }
        }

    }

    /**
     * 重置棋盘的状态
     */
    public void cleanBoard(){
        for (int i=0;i<ROW;i++){
            for (int j=0;j<COLUMN;j++){
                board[i][j]=0;
            }
        }
    }



    /**
     * 获取当前棋局
     * @return      返回当前的棋局
     */
    public int[][] getBoard(){
        return board;
    }

    public int getRow(){
        return ROW;
    }
    public int getColumn(){
        return COLUMN;
    }

}
