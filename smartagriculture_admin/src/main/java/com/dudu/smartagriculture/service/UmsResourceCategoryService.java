package com.dudu.smartagriculture.service;

import com.dudu.smartagriculture.mbg.model.UmsResourceCategory;

import java.util.List;

public interface UmsResourceCategoryService {
    /**
     * 获取资源分类
     * @return
     */
    List<UmsResourceCategory> getListAll();

    /**
     * 增加资源分类
     * @param umsResourceCategory
     * @return
     */
    int create(UmsResourceCategory umsResourceCategory);


    /**
     * 修改资源分类
     */
    int update(Long id, UmsResourceCategory umsResourceCategory);

    /**
     * 删除资源分类
     */
    int delete(Long id);
}
