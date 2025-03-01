package com.linyi.pig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.entity.Files;
import com.linyi.pig.entity.vo.files.FilesAddVo;
import com.linyi.pig.entity.vo.files.FilesQueryVo;
import com.linyi.pig.entity.vo.files.FilesUpdateVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
* @Author: linyi
* @Date: 2025-02-26 08:42:14
* @ClassName: FilesService
* @Version: 1.0
* @Description: 文件信息 服务层
*/
public interface FilesService extends IService<Files> {
    /**
     * 分页查询
     *
     * @param filesQueryVo 分页查询实体
     * @return PageResult<Files>
     */
    PageResult<Files> filesPage(FilesQueryVo filesQueryVo);

    /**
     * 新增
     *
     * @param filesAddVo 新增实体
     * @return Boolean
     */
    Boolean filesAdd(FilesAddVo filesAddVo);

    /**
     * 修改
     *
     * @param filesUpdateVo 修改实体
     * @return Boolean
     */
    Boolean filesUpdate(FilesUpdateVo filesUpdateVo);

    /**
     * 一个文件和一个文件名，将文件上传到MinIO
     *
     * @param file 要上传的文件，类型为MultipartFile
     * @return 返回一个Result对象，其中包含上传文件的临时URL
     */
    FilesAddVo updoadFileAndName(MultipartFile file);

    Map policy(String fileName);

    String getPolicyUrl(String fileName);

    String getUrl(String fileName);
}
