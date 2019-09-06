package com.springboot.dao;

import com.springboot.model.product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by wxl on 2019/9/5.
 */
public interface productDao {

    int insertProduct(@Param("product") product mProduct);
}
