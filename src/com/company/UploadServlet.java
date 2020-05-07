package com.company;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // 1.创建磁盘文件项工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // 2.创建上传解析器对象（依赖工厂对象）
            ServletFileUpload fileUpload = new ServletFileUpload(factory);
            // 3.解析Request请求，返回上传项List集合
            List<FileItem> fileItems = fileUpload.parseRequest(request);
            // 4.遍历List
            for (FileItem fileItem : fileItems) {
                // 5.判断是文件项还是普通项
                if (fileItem.isFormField()) {// 普通上传项
                    // 获取name属性名
                    String name = fileItem.getFieldName();
                    // 获取value属性值
                    String value = fileItem.getString();
                    System.out.println("普通上传项：" + name + "=" + value);
                } else {// 文件上传项
                    // a.获取文件名 Regino.txt
                    String fileName = fileItem.getName();
                    // b.获取文件字节输入流
                    InputStream in = fileItem.getInputStream();
                    // c.获取文件扩展名
                    String extName = FileUtil.extName(fileName);
                    // d.生成随机文件名，解决服务器端保存的文件出现文件名重复问题
                    fileName = IdUtil.simpleUUID() + "." + extName; // UUID 全球唯一

                    // 此文件名会保存数据库一份，给用户查询使用...

                    // 获取upload目录在服务器真实路径
                    String realPath = request.getServletContext().getRealPath("/upload/");

                    // 判断此目录是否存在，解决IDEA没有上传空的upload文件夹导致找不到输出目录的问题
                    File dirFile = new File(realPath);
                    if (!dirFile.exists()) {
                        dirFile.mkdirs(); // 如果不存在自己创建
                    }

                    // 设置文件字节输出流
                    FileOutputStream out = new FileOutputStream(realPath + fileName);

                    // 流的拷贝
                    IoUtil.copy(in, out);

                    // 关流
                    out.close();
                    in.close();

                    response.getWriter().write("success");
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
    }
}
