package com.mingjiang.android.app.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.mingjiang.android.app.R;
import com.mingjiang.android.app.adapter.FillerAdapter;
import com.mingjiang.android.app.bean.PerfusionMachine;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 灌注机数据库采集
 * Created by wangdongjia on 2016/7/11.
 */
public class PerfusionMachineActivity extends Activity {

    protected final static String TAG = PerfusionMachineActivity.class.getSimpleName();
    protected boolean status;
    protected final static String IPADDRESS = "10.18.113.237:1433";
    PerfusionMachine perfusionMachine = null;
    Timer timer = null;
    @InjectView(R.id.weld_imageveiw)
    ImageView weldImageveiw;
    @InjectView(R.id.weldClock)
    TextClock weldClock;
    @InjectView(R.id.device_name)
    TextView deviceName;
    @InjectView(R.id.post)
    TextView post;
    @InjectView(R.id.pre_step)
    TextView preStep;
    @InjectView(R.id.af_step)
    TextView afStep;
    @InjectView(R.id.equip_status)
    TextView equipStatus;
    @InjectView(R.id.lv_filler)
    ListView lvFiller;

    private List<PerfusionMachine> perfusionMachines;
    private FillerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfusion_machine);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        perfusionMachines=new ArrayList<>();
        adapter=new FillerAdapter(this,perfusionMachines);
        lvFiller.setAdapter(adapter);
    }

    //数据处理、UI更新
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    showMessage(R.string.update);
                    perfusionMachines.add(perfusionMachine);
                    List<PerfusionMachine> list=new ArrayList<>();
                    for (PerfusionMachine per : perfusionMachines) {
                        list.add(per);
                    }
                    Collections.reverse(list);
                    if (list.size()>20){
                        for (int i=21;i<list.size();i++){
                            list.remove(i);
                        }
                    }
                    adapter.setPerfusionMachines(list);
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 判断服务器连接状态
     */
    private boolean pingIp() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    status = InetAddress.getByName(IPADDRESS).isReachable(1000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000);//1000ms 1s
        return status;
    }

    @Override
    public void onResume() {
        super.onResume();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                initSQL();
            }
        }, 0, 20000);
    }

    private void initSQL(){
        try {
            db = "perfusion";
            username = "sa";
            password = "123";
            connect = ConnectionHelper(username, password, db, IPADDRESS);
            if (connect.isClosed()==false)
            {
                _isOpened=true;
                System.out.println("connect ok");
                testConnection(connect);
            }
            else
            {
                _isOpened=false;
                System.out.println("connect fail");
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean _isOpened=false;
    String ipaddress, db, username, password;
    public static Connection connect;
    Statement st;
    public boolean isOpened()
    {
        return _isOpened;
    }

    @SuppressLint("NewApi")
    private Connection ConnectionHelper(String user, String password,
                                        String database, String server) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + server + ";"
                    + "databaseName=" + database + ";user=" + user
                    + ";password=" + password + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (SQLException se) {
            Log.e("ERROR", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERROR", e.getMessage());
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
        return connection;
    }

    /**
     * 查找数据库中最后一条记录
     * @param con
     * @throws SQLException
     */
    public void testConnection(Connection con) throws SQLException {
        try {
//            String sql = "select top 1 * from dbo.GZ where id='1' order by id desc";
            String sql = "select top 1 * from dbo.GZ order by id desc";//根据Id查询最新一条记录
//            String sql = "SELECT * FROM dbo.GZ";//查询表名为“table_test”的所有内容
            Statement stmt = con.createStatement();//创建Statement
            ResultSet rs = stmt.executeQuery(sql);//ResultSet类似Cursor
            while (rs.next()) {//<code>ResultSet</code>最初指向第一行
                System.out.println(rs.getString("id"));//输出第n行，列名为“test_id”的值
                System.out.println(rs.getString("BarCode"));
                System.out.println(rs.getString("ReportDateTime"));
                System.out.println(rs.getString("SISTEMA"));
                System.out.println(rs.getString("CICLO"));
                System.out.println(rs.getString("VUOTO"));
                System.out.println(rs.getString("E_VUOTO"));
                System.out.println(rs.getString("DOSE_IMP"));
                System.out.println(rs.getString("DOSE_CAR"));
                System.out.println(rs.getString("E_CARICA"));
                System.out.println(rs.getString("REFRIG"));
                System.out.println(rs.getString("REFRIG_ID"));
                System.out.println(rs.getString("Temperature"));
                System.out.println(rs.getString("PRESS"));
                perfusionMachine = new PerfusionMachine(rs.getString("id"),rs.getString("BarCode"),
                        rs.getString("ReportDateTime"),rs.getString("SISTEMA"),rs.getString("CICLO"),
                        rs.getString("VUOTO"),rs.getString("E_VUOTO"),rs.getString("DOSE_IMP"),
                        rs.getString("DOSE_CAR"),rs.getString("E_CARICA"),rs.getString("REFRIG"),
                        rs.getString("REFRIG_ID"),rs.getString("Temperature"),rs.getString("PRESS"));
                handler.sendEmptyMessage(0);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage().toString());
        } finally {
            if (con != null)
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }

    private void showMessage(int sMsg) {
        Toast.makeText(this.getApplicationContext(),
                getString(sMsg), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStop() {
        Log.i(TAG,"onStop");
        super.onStop();
        timer.cancel();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"onDestroy");
        super.onDestroy();
    }
}

