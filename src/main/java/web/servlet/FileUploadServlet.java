package web.servlet;

import base.UUIDUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@WebServlet("/upload")
public class FileUploadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            //1.创建文件磁盘对象工厂
            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            //2.创建文件上传核心类
            ServletFileUpload servletFileUpload = new ServletFileUpload();
            //2.1设置上传文件编码
            servletFileUpload.setHeaderEncoding("UTF-8");
            //2.配判断表单是否是文件上传表单
            boolean multipartContent = servletFileUpload.isMultipartContent(request);
            //2.3是文件上传表单
            if (multipartContent) {
                //获取表单集合
                List<FileItem> list = servletFileUpload.parseRequest(request);
                if (list != null) {
                    //遍历集合,获取表单项
                    for (FileItem fileItem : list) {
                        //判断当前表单项是否是普通表单
                        boolean formField = fileItem.isFormField();
                        if (formField) {
                            //普通表单项
                            String fieldName = fileItem.getFieldName();
                            fileItem.getString("UTF-8");//设置编码
                            System.out.println("Filename:" + fieldName);
                        } else {
                            //文件上传项
                            //获取文件夹名
                            String name = fileItem.getName();
                            //使用新的文件名,保证UUID不重复
                            String newfilename = UUIDUtils.getUUID() + "_" + name;
                            //获取输入流
                            InputStream inputStream = fileItem.getInputStream();
                            //创建输出流
                            FileOutputStream fileOutputStream = new FileOutputStream("E:/uploadfile/" + newfilename);
                            //完成最终传输
                            IOUtils.copy(inputStream, fileOutputStream);
                            //关闭流
                            fileOutputStream.close();
                            inputStream.close();
                        }
                    }
                } else {

                }
            } else {

            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

}
