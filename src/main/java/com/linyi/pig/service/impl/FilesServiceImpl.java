package com.linyi.pig.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.Files;
import com.linyi.pig.entity.vo.files.FilesAddVo;
import com.linyi.pig.entity.vo.files.FilesQueryVo;
import com.linyi.pig.entity.vo.files.FilesUpdateVo;
import com.linyi.pig.mapper.FilesMapper;
import com.linyi.pig.service.FilesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
* @Author: linyi
* @Date: 2025-02-26 08:42:14
* @ClassName: FilesServiceImpl
* @Version: 1.0
* @Description: 文件信息 服务实现层
*/
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@SuppressWarnings({"unchecked", "rawtypes"})
public class FilesServiceImpl extends ServiceImpl<FilesMapper, Files> implements FilesService {

    @Autowired
    private FilesMapper filesMapper;

    @Override
    public PageResult<Files> filesPage(FilesQueryVo filesQueryVo) {
        LambdaQueryWrapper<Files> queryWrapper = new LambdaQueryWrapper<>();
        //TODO 需要补充条件查询

        //分页数据
        Page<Files> page = new Page<>(filesQueryVo.getPageNum(),filesQueryVo.getPageSize());
        //查询数据
        Page<Files> pageNew = filesMapper.selectPage(page, queryWrapper);
        //返回分页数据
        return new PageResult<>(pageNew.getRecords(), pageNew.getTotal(), pageNew.getPages(), filesQueryVo.getPageNum(), filesQueryVo.getPageSize());
    }

    @Override
    public Boolean filesAdd(FilesAddVo filesAddVo){
        //创建实体对象
        Files files = new Files();
        //复制属性
        BeanUtils.copyProperties(filesAddVo, files);
        //插入数据
        return filesMapper.insert(files) > 0 ? true : false;
    }

    @Override
    public Boolean filesUpdate(FilesUpdateVo filesUpdateVo){
        //根据ID查询数据
        Files byId=this.getById(filesUpdateVo.getId());
        //判断数据是否存在
        if(Optional.ofNullable(byId).isEmpty()){
            log.error("数据不存在");
            return false;
        }
        //复制属性
        BeanUtils.copyProperties(filesUpdateVo, byId);
        //修改数据
        return filesMapper.updateById(byId) > 0 ? true : false;
    }
}
