package com.dbw.sixconnectandroid.utils;

import com.dbw.sixconnectandroid.activitys.BoardCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DBW on 2016/12/22.
 * 创建19x19的棋盘格式的工具类
 */
public class CellListCreater {

    private static List<BoardCell.Cell> cellTypes;

    public static List<BoardCell.Cell> getCellTypes(int row,int column){

        cellTypes = new ArrayList<>();

        //添加第一行
        cellTypes.add(BoardCell.Cell.LEFT_TOP_1);
        for (int i = 0; i < column-2; i++) {
            cellTypes.add(BoardCell.Cell.BOTTOM_2);
        }
        cellTypes.add(BoardCell.Cell.RIGHT_TOP_1);

        //添加第2行致倒数第2行
        for (int j = 0; j < row-2; j++) {
            cellTypes.add(BoardCell.Cell.RIGHT_2);
            for (int m = 0; m < column-2; m++) {
                cellTypes.add(BoardCell.Cell.CENTER_4);
            }
            cellTypes.add(BoardCell.Cell.LEFT_2);
        }

        //添加最后一行
        cellTypes.add(BoardCell.Cell.LEFT_BOTTOM_1);
        for (int k = 0; k < column-2; k++) {
            cellTypes.add(BoardCell.Cell.TOP_2);
        }
        cellTypes.add(BoardCell.Cell.RIGHT_BOTTOM_1);

        return cellTypes;

    }

}
