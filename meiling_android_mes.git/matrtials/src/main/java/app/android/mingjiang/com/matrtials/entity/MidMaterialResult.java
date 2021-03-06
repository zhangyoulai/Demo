package app.android.mingjiang.com.matrtials.entity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * 备注：中间库物料查询结果。
 * 作者：wangzs on 2016/2/20 09:11
 * 邮箱：wangzhaosen@shmingjiang.org.cn
 */
public class MidMaterialResult {

    public String result_size = ""; //返回记录数

    public List<MidMaterialValue> result = new ArrayList<MidMaterialValue>();//返回查询记录

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
