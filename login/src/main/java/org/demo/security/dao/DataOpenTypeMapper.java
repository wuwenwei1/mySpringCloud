package org.demo.security.dao;

import com.alibaba.fastjson2.JSONObject;
import org.demo.security.entity.DataOpenType;

import java.util.List;

public interface DataOpenTypeMapper {

    List<JSONObject> getDataOpenTypeBox();

}