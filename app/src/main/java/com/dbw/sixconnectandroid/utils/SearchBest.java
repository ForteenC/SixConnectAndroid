package com.dbw.sixconnectandroid.utils;

import android.os.AsyncTask;
import android.util.Log;
import com.dbw.sixconnectandroid.board.Board;

import java.util.*;


/**
 * Created by DBW on 2016/12/23.
 * 运用算法寻找最佳的下棋点
 */
public  class SearchBest  extends AsyncTask<Board,Integer,Integer>{

    private static final String TAG = "SearchBest";

    private int[][] board;
    private int piece;
    private OnFindBestListener listener ;
    private final  int wid = 1;
    private boolean isFirst;                                    //是否是下的第一个子

    private int minRow,maxRow,minColumn,maxColumn;              //代表可选下子位置的边界

    /**
     * 找到最佳下棋位置后的回调函数
     */
    public interface OnFindBestListener{
        void complete(int id);
    }

    public void setOnFindBestListener(OnFindBestListener listener){
        this.listener = listener;
    }

    public SearchBest(int piece,boolean isFirst){
        this.piece = piece;
        this.isFirst = isFirst;
    }

    @Override
    protected  Integer doInBackground(Board... boards) {
        this.board = boards[0].getBoard();
        int row = boards[0].getRow();
        int column = boards[0].getColumn();


        //判断是否有可以赢的棋型

        setBorder(boards[0]);


        int id = -1;
        int value;

        Board tempBoard = new Board(row,column);
        tempBoard.setBoard(board);
        int[][] tempBoardArray = tempBoard.getBoard();

        List<int[]> list = new ArrayList<>();

        //设置寻找的边界，避免计算量太大
        setBorder(boards[0]);

        for (int i = minRow; i <= maxRow; i++) {
            for (int j = minColumn; j <= maxColumn; j++) {

                if (board[i][j]==0){
                    id = i*column+j;
                    tempBoardArray[i][j] = piece;
                    if(!isFirst){
                        if (Evaluate.evaluate(tempBoard,-piece)>500000){
                            return id;
                        }
                    }

                    value = Evaluate.evaluate(tempBoard,piece) - Evaluate.evaluate(tempBoard,-piece);
                    int[] map = new int[2];
                    map[0] = value;
                    map[1] = id;
                    list.add(map);
                    tempBoardArray[i][j] = 0;
                }

            }
        }

        int v = Integer.MIN_VALUE;


        for (int[] aList : list) {
            if (aList[0] > v) {
                v = aList[0];
                id = aList[1];
            }
        }


//        Log.d(TAG, "doInBackground: id = "+id+",x = "+id%row+",y = "+id/column);
        if (id == -1){
            id = (int) (Math.random()*19*19+1);
        }



        return id;



    }


    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        listener.complete(integer);
    }



    /**
     * 设置可选的下棋的边界，避免因为可选的位置过多而使得计算太复杂
     * @param board1
     */
    private void setBorder(Board board1){
//        Log.d(TAG, "setBorder: minRow = "+minRow+",maxRow = "+maxRow+",minColumn = "+minColumn+",maxColumn = "+maxColumn);
        minRow = minColumn = maxRow = maxColumn = 0;
        int row = board1.getRow();
        int column = board1.getColumn();

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (board[i][j]!=0){
                    if (minRow>=i||minRow==0){
                        minRow = i;
                    }
                    if (maxRow<=i){
                        maxRow = i;
                    }
                    if (minColumn>=j||minColumn == 0){
                        minColumn = j;
                    }
                    if (maxColumn <=j){
                        maxColumn = j;
                    }
                }

            }
        }

        if (minRow-wid>=0){
            minRow -= wid;
        }
        if (minColumn-wid>=0){
            minColumn -=wid;
        }
        if (maxRow+wid<row){
            maxRow +=wid;
        }
        if (maxColumn+wid<column){
            maxColumn +=wid;
        }



    }

}
