package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.List;

/**
 * 根据查询条件分页并排序查询品牌信息
 */
@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> queryBrandsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        //初始化example对象
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();

        //根据name模糊查询，或者根据首字母查询
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("name","%+key+%").orEqualTo("letter",key);
        }
        //添加分页条件
        PageHelper.startPage(page,rows);
        //添加排序条件
        if (StringUtils.isNotBlank(sortBy)){
            example.setOrderByClause(sortBy+" "+(desc? "desc" : "asc"));   //("id desc") desc升序降序取决于desc的值 true升序 false降序
        }



        List<Brand> brands = this.brandMapper.selectByExample(example);
        //包装成pageInfo
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);
        //包装成分页结果集返回,不能设置参数，在common的PageResult里面生产构造方法   无参的  全参的  total、items的
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());

    }

    /**
     * 新增品牌
     * @param brand
     * @param cids
     */
    @Transactional   //加事务
    public void saveBrand(Brand brand, List<Long> cids) {
        //新增brand
        //Boolean flag = this.brandMapper.insertSelective(brand)==1;
        this.brandMapper.insertSelective(brand);
        //由于添加了事务，不用设置flag,要么都完成，要么都不完成
        //再新增中间表
        //if (flag){
            cids.forEach(cid->{
                this.brandMapper.insertCategoryAndBrand(cid,brand.getId());
            });
        //}
    }

    /**
     * 根据分类id查询品牌列表
     * @param cid
     * @return
     */
    public List<Brand> queryBrandsByCid(Long cid) {


       return this.brandMapper.selectBrandsCid(cid);  //自定义SQL语句
    }

    /**
     * 查询品牌名称
     * @param id
     * @return
     */
    public Brand queryBrandsById(Long id) {

        return this.brandMapper.selectByPrimaryKey(id);
    }
}
