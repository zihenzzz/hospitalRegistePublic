package com.pipilin.framework.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import com.pipilin.common.annotation.DataScope;
import com.pipilin.common.core.domain.BaseEntity;
import com.pipilin.common.utils.StringUtils;

/**
 * 数据过滤处理（已禁用Spring Security相关功能）
 *
 * @author  931708230@qq.com
 */
@Aspect
@Component
public class DataScopeAspect
{
    /**
     * 全部数据权限
     */
    public static final String DATA_SCOPE_ALL = "1";

    /**
     * 自定数据权限
     */
    public static final String DATA_SCOPE_CUSTOM = "2";

    /**
     * 部门数据权限
     */
    public static final String DATA_SCOPE_DEPT = "3";

    /**
     * 部门及以下数据权限
     */
    public static final String DATA_SCOPE_DEPT_AND_CHILD = "4";

    /**
     * 仅本人数据权限
     */
    public static final String DATA_SCOPE_SELF = "5";

    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE = "dataScope";

    @Before("@annotation(controllerDataScope)")
    public void doBefore(JoinPoint point, DataScope controllerDataScope) throws Throwable
    {
        clearDataScope(point);
        handleDataScope(point, controllerDataScope);
    }

    protected void handleDataScope(final JoinPoint joinPoint, DataScope controllerDataScope)
    {
        // TODO: 数据权限功能已禁用（需要Spring Security支持）
        // 如果需要数据权限过滤，请重新启用Spring Security
    }

    /**
     * 数据范围过滤（已禁用）
     */
    public static void dataScopeFilter(JoinPoint joinPoint, Object user, String deptAlias, String userAlias, String permission)
    {
        // TODO: 数据权限功能已禁用（需要Spring Security支持）
        // 如果需要数据权限过滤，请重新启用Spring Security
    }

    /**
     * 拼接权限sql前先清空params.dataScope参数防止注入
     */
    private void clearDataScope(final JoinPoint joinPoint)
    {
        Object params = joinPoint.getArgs()[0];
        if (StringUtils.isNotNull(params) && params instanceof BaseEntity)
        {
            BaseEntity baseEntity = (BaseEntity) params;
            baseEntity.getParams().put(DATA_SCOPE, "");
        }
    }
}
