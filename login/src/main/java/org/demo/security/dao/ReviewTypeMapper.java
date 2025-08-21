package org.demo.security.dao;

import com.alibaba.fastjson2.JSONObject;
import org.demo.security.entity.ReviewType;

import java.util.List;

public interface ReviewTypeMapper {

    List<JSONObject> getReviewTypeBox();

}