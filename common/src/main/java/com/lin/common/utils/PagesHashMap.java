package com.lin.common.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PagesHashMap {
    public static <T> Map<String, Object> getPagesHashMap(Page<T> page, String dateName) {
        long total = page.getTotal();//获取全部共n条
        List<T> list = page.getRecords();//获取查询数据
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", total);
        hashMap.put(dateName, list);
        return hashMap;
    }

    public static <T> Map<String, Object> getPagesHashMap(Long total, String dateName, List<T> list) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", total);
        hashMap.put(dateName, list);
        return hashMap;
    }
}
