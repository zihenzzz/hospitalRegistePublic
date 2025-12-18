package com.pipilin.web.controller.monitor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pipilin.common.core.domain.AjaxResult;
import com.pipilin.framework.web.domain.Server;

/**
 * 服务器监控
 * 
 * @author  931708230@qq.com
 */
@RestController
@RequestMapping("/monitor/server")
public class ServerController
{
    @GetMapping()
    public AjaxResult getInfo() throws Exception
    {
        Server server = new Server();
        server.copyTo();
        return AjaxResult.success(server);
    }
}
