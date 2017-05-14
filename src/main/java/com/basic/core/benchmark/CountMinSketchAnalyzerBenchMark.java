package com.basic.core.benchmark;

import com.basic.core.benchmarkmodel.HotKeyMapSize;
import com.basic.core.main.CountMinSketchAnalyzer;
import com.basic.core.model.DataStructure;
import com.basic.core.util.DataBaseUtil;
import com.basic.core.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * locate com.basic.core.main
 * Created by 79875 on 2017/5/14.
 */
public class CountMinSketchAnalyzerBenchMark {
    private static final Logger logger = LoggerFactory.getLogger(CountMinSketchAnalyzer.class);
    private static double epsilon = 0.0001;
    private static double delta = 0.001;
    private static double support = 0.001;
    private static Queue<HotKeyMapSize> hotKeyMapSizes=new ArrayDeque<>();
    private static DataBaseUtil dataBaseUtil=DataBaseUtil.getDataBaseUtilInstance();
    private static DataStructure sketch = new DataStructure(epsilon, delta, support);

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        Timer timer=new Timer();
        FileUtil.deleteFile("D://HotKeyUpdateTime.txt");


        long startTimeSystemTime = System.currentTimeMillis();
        sketch.setStartTimeSystemTime(startTimeSystemTime);
        long tupleCount = 0L;
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\dataresult\\resultTweets.txt")));
        String str;

        //设置计时器每1000ms计算时间
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                int size = sketch.getHeap().getCurrentSize();
                int length = sketch.getHeap().getSize();
                hotKeyMapSizes.add(new HotKeyMapSize(size,length));
            }
        }, 1,1000);// 设定指定的时间time,此处为1000毫秒

//        StopWords sw = new StopWords();
//        sw.addWords("/stopwords.txt");
        while ((str = br.readLine()) != null) {
//            if (sw.isIn(str))
//                continue;
            sketch.add(str, 1L);
            tupleCount++;
        }

        long endTimeSystemTime = System.currentTimeMillis();
        logger.info("startTime:" + new Timestamp(startTimeSystemTime));
        logger.info("endTime:" + new Timestamp(endTimeSystemTime));
        long timelong = (endTimeSystemTime - startTimeSystemTime) / 1000;
        logger.info("totalTime:" + timelong + " s" + "------or------" + timelong / 60 + " min");
        logger.info("tupleCount: " + tupleCount + " avg: " + tupleCount / timelong);
        timer.cancel();

        sketch.outputKeyUpdateTimesQueue();
        logger.info("ExecutorService run over");

        String sql="insert INTO t_predicthotkey(keysize,tablelength) VALUES(?,?)";
        Connection connection = dataBaseUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        while (!hotKeyMapSizes.isEmpty()){
            HotKeyMapSize poll = hotKeyMapSizes.poll();
            preparedStatement.setInt(1,poll.getKeysize());
            preparedStatement.setInt(2,poll.getTablelength());
            preparedStatement.executeUpdate();
        }
        preparedStatement.close();
        logger.info("DataBaseService run over");

        System.exit(0);
    }

}
