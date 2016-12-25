package com.star.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

/**
 * Created by hp on 2016/12/23.
 */

public class RecieveServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        //当文件较大时，不能放在内存中，应该放在一个临时文件夹里面
        String tepdir=getServletContext().getRealPath("/temp");
        //DiskFileItemFactory：将请求消息实体中的每一个项目封装成单独的DiskFIleItem对象
        //第一个参数：上传文件的缓存最大值，当大于这个值的时候就存入临时目录
        //第二个参数：设置缓存文件的目录
        DiskFileItemFactory factory =new DiskFileItemFactory(1024*1024,new File(tepdir));
        //文件上传处理类
        ServletFileUpload upload=new ServletFileUpload(factory);
        try {
            //parseRequest方法解析Request中的文件，并返回FileItem列表
            List<FileItem> list=upload.parseRequest(request);
            //创建一个迭代器，用来迭代多个FileItem对象
            ListIterator<FileItem> iterator=list.listIterator();
            //临时容纳单个fileItem
            FileItem item;
            String fileName;
            int index;
            while (iterator.hasNext()){
                //获得下一个item文件对象
                item=iterator.next();
                //获取文件名
               fileName=item.getName();
System.out.println(fileName);
                //分离文件名后缀，如.jpg
                index=fileName.indexOf(".");
                //substring 方法会返回下标为index之后的字符串
                fileName=fileName.substring(index);

System.out.println(fileName);
                //使用UUID类重新生成一个随机的文件名（防止文件名重复），记住要加上文件后缀
                fileName=UUID.randomUUID().toString()+fileName;
System.out.println(fileName);
                //多级文件存储目录，以日期划分，这样可以分流
                SimpleDateFormat df=new SimpleDateFormat("/yyyy/MM/dd/HH");
                String dir=df.format(new Date());
System.out.println(dir);

                //getServletContext().getRealPath方法会自动获取并在
                // 最前面加上当前项目的根路径
                String path=getServletContext().getRealPath("upload/"+dir);
System.out.println(path);
                //新建文件目录
                File pathFile=new File(path);
                //如果文件夹不存在，则创建文件夹
                Boolean b=null;
                if (!pathFile.exists()){
                    //返回值是bollean类型，true则表示创建目录成功
                    //mkdirs方法或自动创建多级目录，而mkdir则只会创建一层
                    b= pathFile.mkdirs();
                }
System.out.println(b);
                /*
                 * 将文件写入磁盘
                 * @param pathFile 文件存储的路径
                 * @param fileName 存储的文件名
                 */
                item.write(new File(pathFile,fileName));
                response.getWriter().write("恭喜你，文件上传成功！");
            }
            //parseRequest抛出的异常
        } catch (FileUploadException e) {
            e.printStackTrace();
            //方法item.write抛出的异常
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
