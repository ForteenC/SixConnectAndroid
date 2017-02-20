package com.dbw.sixconnectandroid.game;

import com.dbw.sixconnectandroid.board.Board;


/**
 * Created by DBW on 2016/12/8.
 * 裁判类
 */
public class Referee {

    /**
     * 判断当前棋局是否有赢家
     * @param currentBoard 当前的棋局
     * @param piece 当前棋子类型
     * @return 返回值为true时当前棋子类型胜利，返回false未出现赢局
     */
    public boolean judge(Board currentBoard, int piece) {


        int[][] board = currentBoard.getBoard();
        int rowNum = currentBoard.getRow();
        int columnNum = currentBoard.getColumn();


        int indexRow = 0;                           //代表行相同棋子的数量，如果为6，则代表胜利
        int indexCol = 0;                           //代表列相同棋子的数量，如果为6，则代表胜利
        int indexLOb = 0;                           //代表左斜相同棋子的数量，如果为6，则代表胜利
        int indexROb = 0;                           //代表右斜相同棋子的数量，如果为6，则代表胜利
        int currentRowPiece;                        //保存行当前的棋子
        int currentColPiece;                        //保存列当前的棋子
        int currentLObPiece;                        //保存左斜当前的棋子
        int currentRObPiece;                        //保存右斜当前的棋子

        int Loffset = 0;                             //斜向的偏移量
        int Roffset = 5;

        for (int i = 0; i < rowNum; i++) {

            indexCol = 0;                               //每一列的计数不能代入下一行
            indexRow = 0;                               //每一行的计数不能代入下一列

            for (int j = 0; j < columnNum; j++) {

                //判断一行中是否形成了六子
                currentRowPiece = board[i][j];
                if (currentRowPiece == piece) {
                    indexRow++;
                } else {
                    indexRow = 0;
                }

                //判断一列中是否形成了六子
                currentColPiece = board[j][i];
                if (currentColPiece == piece) {
                    indexCol++;
                } else {
                    indexCol = 0;
                }



                //判断左斜向是否形成了六子
                if (i+Loffset>= rowNum){//限制越界条件
                    Loffset--;
                }else {
                    currentLObPiece = board[i+Loffset][j];
                    if (currentLObPiece == piece){
                        Loffset++;
                        indexLOb++ ;
                    }else {
                        Loffset = 0;
                        indexLOb = 0;
                    }
                }


                //判断右斜方向是否形成了六子
                if (i+Roffset>= columnNum){//限制越界条件
                    Roffset--;
                }else {
                    currentRObPiece = board[i+Roffset][j];
                    if (currentRObPiece == piece){
                        Roffset--;
                        indexROb++;
                    }else {
                        Roffset = 5;
                        indexROb = 0;
                    }

                }

                if (indexCol == 6 || indexRow == 6 || indexLOb == 6 || indexROb == 6) {
                    return true;
                }


            }

        }
        return false;
    }
}
