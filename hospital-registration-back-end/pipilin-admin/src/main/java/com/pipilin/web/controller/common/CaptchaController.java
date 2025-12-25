package com.pipilin.web.controller.common;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jakarta.annotation.Resource;
import javax.imageio.ImageIO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.code.kaptcha.Producer;
import com.pipilin.common.config.PipilinConfig;
import com.pipilin.common.constant.CacheConstants;
import com.pipilin.common.constant.Constants;
import com.pipilin.common.core.domain.AjaxResult;
import com.pipilin.common.utils.sign.Base64;
import com.pipilin.common.utils.uuid.IdUtils;
import com.pipilin.system.service.ISysConfigService;

/**
 * 验证码操作处理
 */
@RestController
public class CaptchaController
{
    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    // 验证码缓存（内存存储）
    private static final Map<String, CaptchaEntry> captchaCache = new ConcurrentHashMap<>();
    
    private static class CaptchaEntry {
        String code;
        long expireTime;
        
        CaptchaEntry(String code, long expireTime) {
            this.code = code;
            this.expireTime = expireTime;
        }
    }
    
    @Autowired
    private ISysConfigService configService;
    
    /**
     * 生成验证码
     */
    @GetMapping("/captchaImage")
    public AjaxResult getCode(HttpServletResponse response) throws IOException
    {
        AjaxResult ajax = AjaxResult.success();
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        ajax.put("captchaEnabled", captchaEnabled);
        if (!captchaEnabled)
        {
            return ajax;
        }

        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;

        // 生成验证码
        String captchaType = PipilinConfig.getCaptchaType();
        if ("math".equals(captchaType))
        {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        }
        else if ("char".equals(captchaType))
        {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        // 存储验证码到内存
        long expireTime = System.currentTimeMillis() + Constants.CAPTCHA_EXPIRATION * 60 * 1000;
        captchaCache.put(verifyKey, new CaptchaEntry(code, expireTime));
        
        // 清理过期验证码
        cleanExpiredCaptcha();
        
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try
        {
            ImageIO.write(image, "jpg", os);
        }
        catch (IOException e)
        {
            return AjaxResult.error(e.getMessage());
        }

        ajax.put("uuid", uuid);
        ajax.put("img", Base64.encode(os.toByteArray()));
        return ajax;
    }
    
    /**
     * 验证验证码
     */
    public static String getCaptcha(String key) {
        CaptchaEntry entry = captchaCache.get(key);
        if (entry == null) {
            return null;
        }
        if (System.currentTimeMillis() > entry.expireTime) {
            captchaCache.remove(key);
            return null;
        }
        return entry.code;
    }
    
    /**
     * 删除验证码
     */
    public static void removeCaptcha(String key) {
        captchaCache.remove(key);
    }
    
    private void cleanExpiredCaptcha() {
        long now = System.currentTimeMillis();
        captchaCache.entrySet().removeIf(entry -> now > entry.getValue().expireTime);
    }
}
