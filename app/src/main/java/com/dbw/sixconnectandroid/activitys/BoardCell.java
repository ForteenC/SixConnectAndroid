package com.dbw.sixconnectandroid.activitys;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.Toast;
import com.dbw.sixconnectandroid.utils.Data;

/**
 * Created by DBW on 2016/12/22.
 * 自定义view，每一个view代表棋盘的一个可点击点，以此实现棋盘的准确下子
 */
public class BoardCell extends View  {

    private static final String TAG = "BoardCell";

    private Paint mPaint;                      //画笔，用来绘制view
    private int width = 0;


    private int height = 0;
    public Cell cellType;                    //当前view所代表的cell类型
    public Cell originCellType;              //当前view所代表的原始cell类型
    private Context context;

    public enum Cell {
        LEFT_TOP_1, LEFT_BOTTOM_1, RIGHT_TOP_1, RIGHT_BOTTOM_1,
        LEFT_2, TOP_2, RIGHT_2, BOTTOM_2,
        CENTER_4,
        BLACK, WRITE
    }

    /**
     * 设置当前cell的类型
     *
     * @param cellType
     */
    public void setOriginCellType(Cell cellType, int width, int height) {
        this.originCellType = cellType;
        this.cellType = cellType;
        this.width = width;
        this.height = height;
    }
    /**
     * 设置当前cell的类型
     *
     * @param cellType
     */
    public void setCellType(Cell cellType) {
        this.cellType = cellType;
    }

    /**
     * 清除view上的棋子
     *
     * @param cell
     */
    public void reset(Cell cell) {
        cellType = cell;
        mPaint.setColor(Color.BLACK);
        invalidate();
    }

    public BoardCell(Context context) {
        super(context);
        this.context = context;
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth((float) 3.0);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (width != 0 && height != 0) {
            setMeasuredDimension(width, height);
        }
    }

    /**
     * 在onDraw方法中绘制自己的view
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();

        float centerWidth = width / 2;
        float centerHeight = height / 2;
        float r = Math.min(centerWidth, centerHeight);
        float[] pts;
        mPaint.setColor(Color.BLACK);
        switch (cellType) {
            case LEFT_TOP_1:
                pts = new float[]{
                        centerWidth, centerHeight, centerWidth, height,
                        centerWidth, centerHeight, width, centerHeight
                };
                canvas.drawLines(pts, mPaint);
                break;
            case LEFT_BOTTOM_1:
                pts = new float[]{
                        centerWidth, centerHeight, centerWidth, 0,
                        centerWidth, centerHeight, width, centerHeight
                };
                canvas.drawLines(pts, mPaint);
                break;
            case RIGHT_TOP_1:
                pts = new float[]{
                        centerWidth, centerHeight, centerWidth, height,
                        centerWidth, centerHeight, 0, centerHeight
                };
                canvas.drawLines(pts, mPaint);
                break;
            case RIGHT_BOTTOM_1:
                pts = new float[]{
                        centerWidth, centerHeight, centerWidth, 0,
                        centerWidth, centerHeight, 0, centerHeight
                };
                canvas.drawLines(pts, mPaint);
                break;
            case LEFT_2:
                pts = new float[]{
                        centerWidth, centerHeight, centerWidth, height,
                        centerWidth, centerHeight, 0, centerHeight,
                        centerWidth, centerHeight, centerWidth, 0
                };
                canvas.drawLines(pts, mPaint);
                break;
            case TOP_2:
                pts = new float[]{
                        centerWidth, centerHeight, width, centerHeight,
                        centerWidth, centerHeight, 0, centerHeight,
                        centerWidth, centerHeight, centerWidth, 0
                };
                canvas.drawLines(pts, mPaint);
                break;
            case RIGHT_2:
                pts = new float[]{
                        centerWidth, centerHeight, centerWidth, height,
                        centerWidth, centerHeight, centerWidth, 0,
                        centerWidth, centerHeight, width, centerHeight
                };
                canvas.drawLines(pts, mPaint);
                break;
            case BOTTOM_2:
                pts = new float[]{
                        centerWidth, centerHeight, centerWidth, height,
                        centerWidth, centerHeight, 0, centerHeight,
                        centerWidth, centerHeight, width, centerHeight
                };
                canvas.drawLines(pts, mPaint);
                break;
            case CENTER_4:
                pts = new float[]{
                        centerWidth, centerHeight, centerWidth, height,
                        centerWidth, centerHeight, centerWidth, 0,
                        centerWidth, centerHeight, 0, centerHeight,
                        centerWidth, centerHeight, width, centerHeight
                };
                canvas.drawLines(pts, mPaint);
                break;
            case BLACK:
                mPaint.setColor(Color.BLACK);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setAntiAlias(true);
                canvas.drawCircle(centerWidth, centerHeight, r, mPaint);
                break;
            case WRITE:
                mPaint.setColor(Color.WHITE);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setAntiAlias(true);
                canvas.drawCircle(centerWidth, centerHeight, r, mPaint);
                break;
            default:
        }

    }

    public Cell getCellType() {
        return cellType;
    }

}
