package com.xiao.pro.persistance;

import com.xiao.pro.utils.HikariPoolUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by xiaoliang on 2017/11/26.
 */
public class TestDao {

    private Connection connection = null;
    private PreparedStatement pre = null;

    public void init(){
        try {
            connection = HikariPoolUtils.getConnection();
            connection.setAutoCommit(false);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void batchInsert(Map<String,String> map) throws Exception {
        String sql = "insert into t_test(key_work,key_url) values (?,?) ";
        pre = connection.prepareStatement(sql);
        for(Map.Entry<String,String> en : map.entrySet()){
            pre.setString(1,en.getValue());
            pre.setString(2,en.getKey());
            pre.addBatch();
        }
        pre.executeBatch();
        connection.commit();

        HikariPoolUtils.closeStatement(pre);
    }

}
