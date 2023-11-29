package com.donny.community.controller;


import com.donny.community.annotation.LoginRequired;
import com.donny.community.entity.User;
import com.donny.community.service.FollowService;
import com.donny.community.service.LikeService;
import com.donny.community.service.UserService;
import com.donny.community.util.CommunityConstant;
import com.donny.community.util.CommunityUtil;
import com.donny.community.util.HostHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController implements CommunityConstant {

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${community.path.context-path}")
    private String contextPath;


    @LoginRequired
    @GetMapping("/setting")
    public String getSettingPage() {
        return "site/setting";
    }

    @LoginRequired
    @PostMapping("/upload")
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if(headerImage == null) {
            model.addAttribute("error", "您还没有选择图片");
            return "site/setting";
        }
        String originalFilename = headerImage.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        if(!StringUtils.hasText(suffix)) {
            model.addAttribute("error", "文件格式不正确!");
            return "site/setting";
        }
        // 生成随机文件名
        String fileName = CommunityUtil.generateUUID() + suffix;
        // 确定文件存放的路径
        File destiny = new File(uploadPath + "/" + fileName);
        try {
            // 尝试存储文件
            headerImage.transferTo(destiny);
        } catch (IOException e) {
            log.error("上传文件失败:", e.getMessage());
            throw new RuntimeException("上传文件失败", e);
        }
        // 更新当前用户的HeaderUrl
        // http://localhost:8082/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(), headerUrl);
        return "redirect:/index";
    }

    @GetMapping("/header/{fileName}")
    public void getHeader(HttpServletResponse response, @PathVariable String fileName) throws IOException {
        fileName = uploadPath + "/" + fileName;
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        response.setContentType("image/" + suffix);
        FileInputStream fileInputStream = null;
        try {
            OutputStream outputStream = response.getOutputStream();
            fileInputStream = new FileInputStream(fileName);
            byte[] bytes = new byte[1024];
            int b = 0;
            while((b = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, b);
            }
        } catch (IOException e) {
            log.error("读取头像失败:" + e.getMessage());
        } finally {
            fileInputStream.close();
        }
    }

    /**
     * 个人主页
     */
    @GetMapping("/profile/{userId}")
    @LoginRequired
    public String userProfilePage(@PathVariable Integer userId, Model model) {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在!");
        }

        // 用户
        model.addAttribute("user", user);
        // 点赞数量
        Integer userLikeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("userLikeCount", userLikeCount);
        // 关注数量
        Long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);
        // 粉丝数量
        Long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        // 当前登录用户是否已经关注


        return "site/profile";
    }
}
