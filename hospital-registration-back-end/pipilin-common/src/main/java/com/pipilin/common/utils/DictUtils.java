package com.pipilin.common.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.pipilin.common.core.domain.entity.SysDictData;

/**
 * 字典工具类（内存存储版）
 */
public class DictUtils
{
    // 字典缓存
    private static final Map<String, List<SysDictData>> dictCache = new ConcurrentHashMap<>();

    /**
     * 设置字典缓存
     */
    public static void setDictCache(String key, List<SysDictData> dictDatas)
    {
        dictCache.put(key, dictDatas);
    }

    /**
     * 获取字典缓存
     */
    public static List<SysDictData> getDictCache(String key)
    {
        return dictCache.get(key);
    }

    /**
     * 根据字典类型和字典值获取字典标签
     */
    public static String getDictLabel(String dictType, String dictValue)
    {
        return getDictLabel(dictType, dictValue, ",");
    }

    /**
     * 根据字典类型和字典标签获取字典值
     */
    public static String getDictValue(String dictType, String dictLabel)
    {
        return getDictValue(dictType, dictLabel, ",");
    }

    /**
     * 根据字典类型和字典值获取字典标签
     */
    public static String getDictLabel(String dictType, String dictValue, String separator)
    {
        StringBuilder propertyString = new StringBuilder();
        List<SysDictData> datas = getDictCache(dictType);

        if (StringUtils.isNotNull(datas))
        {
            if (StringUtils.containsAny(separator, dictValue))
            {
                for (SysDictData dict : datas)
                {
                    for (String value : dictValue.split(separator))
                    {
                        if (value.equals(dict.getDictValue()))
                        {
                            propertyString.append(dict.getDictLabel()).append(separator);
                            break;
                        }
                    }
                }
            }
            else
            {
                for (SysDictData dict : datas)
                {
                    if (dictValue.equals(dict.getDictValue()))
                    {
                        return dict.getDictLabel();
                    }
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 根据字典类型和字典标签获取字典值
     */
    public static String getDictValue(String dictType, String dictLabel, String separator)
    {
        StringBuilder propertyString = new StringBuilder();
        List<SysDictData> datas = getDictCache(dictType);

        if (StringUtils.isNotNull(datas))
        {
            if (StringUtils.containsAny(separator, dictLabel))
            {
                for (SysDictData dict : datas)
                {
                    for (String label : dictLabel.split(separator))
                    {
                        if (label.equals(dict.getDictLabel()))
                        {
                            propertyString.append(dict.getDictValue()).append(separator);
                            break;
                        }
                    }
                }
            }
            else
            {
                for (SysDictData dict : datas)
                {
                    if (dictLabel.equals(dict.getDictLabel()))
                    {
                        return dict.getDictValue();
                    }
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 删除指定字典缓存
     */
    public static void removeDictCache(String key)
    {
        dictCache.remove(key);
    }

    /**
     * 清空字典缓存
     */
    public static void clearDictCache()
    {
        dictCache.clear();
    }
}
