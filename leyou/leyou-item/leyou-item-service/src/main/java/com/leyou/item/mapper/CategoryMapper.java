package com.leyou.item.mapper;

import com.leyou.item.pojo.Category;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

@SpringBootApplication
@EnableDiscoveryClient
public interface CategoryMapper extends Mapper<Category>, SelectByIdListMapper<Category,Long> {
}
