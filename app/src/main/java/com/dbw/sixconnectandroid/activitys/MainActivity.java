package com.dbw.sixconnectandroid.activitys;

import android.content.DialogInterface;
import android.os.*;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.dbw.sixconnectandroid.R;
import com.dbw.sixconnectandroid.game.Referee;
import com.dbw.sixconnectandroid.utils.CellListCreater;
import com.dbw.sixconnectandroid.utils.Data;
import com.dbw.sixconnectandroid.utils.SearchBest;


import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private List<BoardCell.Cell> cellTypes;
    private BoardCell cell;                                     //棋盘的最小格
    private Button btn_start;                                   //开始按钮
    private Button btn_regret;                                  //悔棋按钮
    private GridLayout gl;                                      //装载棋盘的布局
    private int ROW,COLUMN;
    private TextView info;
    private Data data;
    private Referee referee ;                                   //裁判类
    private SearchBest search;                                  //后台算法类
    private boolean isGameOver = false;                                 //标志游戏结束


    private Handler mHandler = new Handler(new Handler.Callback() {   //用来动态更新提示信息
        @Override
        public boolean handleMessage(Message message) {
            if (message.what == 123) {
                info.setText((String) message.obj);
            }
            return true;
        }
    });

    /**
     * 当单个board cell被点击后出发的回调函数
     */
    private View.OnClickListener cellClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BoardCell cell = (BoardCell) view;
            data = (Data) getApplication();
            int who = data.getWho();
            int id = cell.getId();

            if (!data.isStarted || data.isPaused) {
                return;
            }

            if (data.addPiece(id, who, cell.cellType)) {
                ((BoardCell) view).originCellType = cell.cellType;
                refreshCell(who,cell);
            }
        }
    };

    /**
     * 更新BoardCell的状态，修改为黑子或者白子
     * @param who
     * @param cell
     */
    private void refreshCell(int who,BoardCell cell){

        //设置悔棋按钮为可点击状态
        if (!btn_regret.isEnabled()){
            btn_regret.setEnabled(true);
        }

        if (who == 1) {
            cell.cellType = BoardCell.Cell.BLACK;
        } else if (who == -1) {
            cell.cellType = BoardCell.Cell.WRITE;
        }

        cell.invalidate();

        if (referee.judge(data.getBoard(),data.getWho())){
            //如果形成了六连，游戏结束
            data.pause();
            isGameOver = true;
            gameOver(data.getWho());
        }

        data.next();

        //裁判判断是否形成了六连

        takeTurn();
    }


    /**
     * 玩家和AI轮流下棋，当AI下棋时，玩家无法点击棋盘
     * 当玩家下棋时，AI停止计算
     */
    private void takeTurn() {
        int who = data.getWho();
         Message message = new Message();
         message.what = 123;
         message.obj = "游戏结束";

        //如果接下来AI下棋
        if (who==data.getAi()&&!isGameOver){
            message.obj = "计算中。。。。。";
            data.pause();                           //暂停棋盘的点击事件
            AI();
        }else if (who==data.getPerson()&&!isGameOver){
            message.obj = "请下子";
            data.unPause();                         //恢复棋盘的点击事件
        }
        mHandler.sendMessage(message);
    }

    /**
     * 在这个方法中执行各种算法，寻找最佳下棋的方法
     *
     */
    private void AI() {

        search = new SearchBest(data.getAi(),data.isFirst());

        search.setOnFindBestListener(new SearchBest.OnFindBestListener() {
            @Override
            public void complete(int id) {
                BoardCell cell  = (BoardCell)findViewById(id);
                BoardCell.Cell cellType = cell.getCellType();
                if (data.addPiece(id,data.getAi(),cellType)){
                    refreshCell(data.getAi(),cell);
                }else {
                    takeTurn();
                }
            }
        });

        search.execute(data.getBoard());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = (Data) getApplication();
        this.ROW = data.ROW;
        this.COLUMN = data.COLUMN;

        cellTypes = CellListCreater.getCellTypes(ROW,COLUMN);

        RelativeLayout viewGroup = (RelativeLayout) findViewById(R.id.RL_board);
        btn_start = (Button) findViewById(R.id.btn_start);
        info = (TextView) findViewById(R.id.TV_info);
        btn_regret = (Button) findViewById(R.id.btn_regret);

        referee = new Referee();


        //获取手机屏幕的宽度和高度
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int sWidth = dm.widthPixels;
        int sHeight = dm.heightPixels;

        //创建表格
        gl = new GridLayout(this);
        gl.setRowCount(ROW);
        gl.setColumnCount(COLUMN);

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.setMargins(0,0,0,0);
        params.setGravity(GridLayout.VERTICAL);
        gl.setLayoutParams(params);

        for (int i = 0; i<ROW*COLUMN;i++){
            BoardCell cell = new BoardCell(this);
            cell.setId(i);
            cell.setOriginCellType(cellTypes.get(i),sWidth/COLUMN, (int) (sHeight/(ROW*1.5)));
            //为每个cell添加监听事件
            cell.setOnClickListener(cellClickListener);
            gl.addView(cell);
        }

        viewGroup.addView(gl);

        //开始游戏按钮
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameStart();
            }
        });

        //悔棋按钮
        btn_regret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取栈顶的Map对象
                while (true){
                    Map<String, Object> regretMap = data.getRegretId();
                    if (regretMap != null){
                        int id = (int) regretMap.get("id");
                        int piece = (int) regretMap.get("piece");

                        BoardCell tmp = (BoardCell) findViewById(id);
                        tmp.reset((BoardCell.Cell) regretMap.get("cellType"));
                        //如果是玩家下的棋子，悔棋操作会去除玩家下的一个棋子
                        if (piece == data.getPerson()){

                            break;
                        }
                        //如果是AI下的棋子，悔棋操作会先去除所有AI下的棋子，再去除玩家下的一个棋子


                    }else {
                        //若已全部出栈，就无法再悔棋了
                        btn_regret.setEnabled(false);
                        break;
                    }
                }

            }
        });

    }



    /**
     * 开始游戏
     */
    private void gameStart() {
        //提示选择谁先手
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("请选择谁先手")
                .setItems(new String[]{"玩家", "AI"},  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                data.setPerson(data.BLACK);
                                data.setAi(data.WRITE);
                                break;
                            case 1:
                                data.setPerson(data.WRITE);
                                data.setAi(data.BLACK);
                                break;
                        }
                        btn_start.setVisibility(View.INVISIBLE);
                        data.start();
                        if (data.getAi()==data.BLACK){
                           takeTurn();
                        }
                    }
                })
                .show();
    }

    /**
     * 游戏结束
     */
    private void gameOver(int who){
        if (search!=null){
            search.cancel(true);
            search = null;
        }
        String winner = who==1?"黑子":"白子";
        new AlertDialog.Builder(this)
                .setTitle("胜利！！！")
                .setMessage(winner+"取得了胜利！")
                .setNegativeButton("退出游戏", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        exit();
                    }
                })
                .setPositiveButton("再来一局", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cleanBoard();
                    }
                })
                .show();
    }

    /**
     * 重置棋盘的状态
     */
    public void cleanBoard(){
         data.cleanBoard();
         isGameOver = false;
         btn_start.setVisibility(View.VISIBLE);
         btn_regret.setEnabled(false);
         Message message = new Message();
         message.what = 123;
         message.obj = "六子棋博弈游戏";
         mHandler.sendMessage(message);
        BoardCell cell;
         for (int i=0;i<ROW*COLUMN;i++){
              cell = (BoardCell) findViewById(i);
              if (cell.cellType == BoardCell.Cell.BLACK||cell.cellType == BoardCell.Cell.WRITE){
                  cell.setCellType(cell.originCellType);
                  cell.invalidate();
              }
         }
    }

    /**
     * 退出
     */
    private void exit(){
        MainActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        data.cleanBoard();
    }
}
