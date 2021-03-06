package com.shmingjiang.www.mldaq.bean;

import java.util.LinkedList;
import java.util.List;

/**
 *焊接壁厚。
 * Created by wangzs on 2015/12/21.
 */
public class WeldThick {

    //实时显示数据条数
    public static final int SIZE = 50;
    //插入位置
    public static final int INSERT_POSITION = 0;

    //当前壁厚
    public Float currentValue;

    //最大值
    public Float maxValue;

    //最小值
    public Float minValue;

    //最适值
    public Float bestValue;

    //壁厚列表
    public List<Float> valueList = new LinkedList<Float>();

    //添加值
    public void addValue(Float value){

        valueList.add(INSERT_POSITION,value);

        if(valueList.size() == SIZE){
            valueList.remove(SIZE-1);
        }

    }
}
