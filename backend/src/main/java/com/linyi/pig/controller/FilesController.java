package com.linyi.pig.controller;

import com.linyi.pig.common.model.PageResult;
import com.linyi.pig.common.model.Result;
import com.linyi.pig.config.MinioComponent;
import com.linyi.pig.config.MinioConfiguration;
import com.linyi.pig.entity.Files;
import com.linyi.pig.entity.vo.files.FilesAddVo;
import com.linyi.pig.entity.vo.files.FilesQueryVo;
import com.linyi.pig.entity.vo.files.FilesUpdateVo;
import com.linyi.pig.service.FilesService;
import io.minio.http.Method;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
* @Author: linyi
* @Date: 2025-02-26 08:42:14
* @ClassName: FilesController
* @Version: 1.0
* @Description: 文件信息 控制层
*/

@Tag(name = "文件信息管理模块")
@Slf4j
@Validated
@RestController
@RequestMapping("/files")
@SuppressWarnings({"unchecked", "rawtypes"})
public class FilesController{

    @Autowired
    private FilesService filesService;

    @Autowired
    private MinioComponent minioComponent;

    /**
     * 一个文件和一个文件名，将文件上传到MinIO
     *
     * @param file 要上传的文件，类型为MultipartFile
     * @return 返回一个Result对象，其中包含上传文件的临时URL
     */
    @Operation(summary = "文件上传")
    @PostMapping("/upload")
    public Result<FilesAddVo> upload(@RequestParam("file") MultipartFile file) {
        return Result.success(filesService.updoadFileAndName(file));
    }


    /**
     * 获取预签名的策略信息
     * 该方法用于生成一个带有策略的URL，允许用户在限定时间内上传文件到MinIO
     *
     * @param fileName 文件名，用于生成策略的唯一性
     * @return 返回包含预签名策略信息的Map对象
     */
    @Operation(summary = "上传文件并生成临时url（限时时间）")
    @GetMapping("/policy")
    public Result<Map> policy(@RequestParam("fileName")String fileName) {
        return Result.success(filesService.policy(fileName));
    }

    /**
     * 获取文件上传的预签名URL
     * <p>
     * 本端点用于生成一个预签名的URL，客户端可以使用该URL直接上传文件到MinIO服务器
     * 这种方法避免了文件通过后端服务器中转，提高了上传效率和性能
     *
     * @param fileName 文件名，用于生成唯一的对象名
     * @return 返回一个包含预签名URL的Result对象如果生成URL成功，Result为成功状态；
     *         否则，为失败状态
     */
    @Operation(summary = "获取文件上传的预签名URL")
    @GetMapping("/uploadUrl")
    public Result<String> uploadUrl(@RequestParam("fileName") String fileName) {

        return Result.success(filesService.getPolicyUrl(fileName));
    }

    /**
     * 获取文件的URL
     * 该方法用于获取指定文件名的文件URL，URL具有一定的过期时间
     *
     * @param fileName 文件名，用于识别和定位存储在MinIO中的文件
     * @return 返回一个Result对象，其中包含文件的URL
     */
    @Operation(summary = "获取文件的URL(7天)")
    @GetMapping("/url")
    public Result<String> getUrl(@RequestParam("fileName")String fileName) {
        // 返回成功结果，包含生成的文件URL
        return Result.success(filesService.getUrl(fileName));
    }


    /**
     * 分页查询文件信息
     *
     * @param filesQueryVo 分页查询实体
     * @return Result<PageResult<Files>> 返回分页数据
     */
    @Operation(summary = "分页查询文件信息")
    @PostMapping("/filesPage")
    public Result<PageResult<Files>> filesPage(@RequestBody FilesQueryVo filesQueryVo) {
        return Result.success(filesService.filesPage(filesQueryVo));
    }

    /**
     * 新增文件信息
     *
     * @param filesAddVo 新增实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "新增文件信息")
    @PostMapping("/filesAdd")
    public Result<Boolean> filesAdd(@RequestBody FilesAddVo filesAddVo) {
        return Result.success(filesService.filesAdd(filesAddVo));
    }

    /**
     * 根据主键ID删除文件信息
     *
     * @param id 主键id
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID删除文件信息")
    @DeleteMapping("filesDelete")
    public Result<Boolean> filesDelete(@RequestParam Serializable id) {
        return Result.success(filesService.removeById(id));
    }

    /**
    * 根据主键ID批量删除文件信息
    *
    * @param ids 主键id集合
    * @return Result<Boolean> 返回结果(true/false)
    */
    @Operation(summary = "根据主键ID批量删除文件信息")
    @DeleteMapping("filesListDelete")
    public Result<Boolean> filesListDelete(@RequestParam List<Serializable> ids) {
        return Result.success(filesService.removeByIds(ids));
        }

    /**
     * 根据主键ID修改文件信息
     *
     * @param filesUpdateVo 修改实体
     * @return Result<Boolean> 返回结果(true/false)
     */
    @Operation(summary = "根据主键ID修改文件信息")
    @PutMapping("filesUpdate")
    public Result<Boolean> filesUpdate(@RequestBody FilesUpdateVo filesUpdateVo) {
        return Result.success(filesService.filesUpdate(filesUpdateVo));
    }

    /**
     * 根据主键ID查询文件信息
     *
     * @param id 主键id
     * @return Result<Files> 返回文件信息
     */
    @Operation(summary = "根据主键ID查询文件信息")
    @GetMapping("/getInfo")
    public Result<Files> filesUpdate(@RequestParam Serializable id) {
        return Result.success(filesService.getById(id));
    }

}
