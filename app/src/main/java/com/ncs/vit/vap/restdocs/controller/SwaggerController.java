package com.ncs.vit.vap.restdocs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncs.vit.vap.restdocs.util.YamlReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping(value = "/apis")
public class SwaggerController
{
    private final String API_UI = "swaggerUI";
    private Logger logger = LogManager.getLogger(SwaggerController.class);

    /**
     * return a html template
     * @return
     */
    @RequestMapping(value = "")
    public String showApi()
    {
        return API_UI;
    }

    /**
     * convert yaml file to a json and return to frontend
     * @param moduleName
     * @return
     * @throws Exception
     */
    @RequestMapping("/{moduleName}")
    @ResponseBody
    public String getApi(@PathVariable("moduleName") String moduleName) throws Exception
    {
        File file = new File("yaml"+File.separator+moduleName+".yaml");
        String result = YamlReader.convert2Json(file);
        return result;
    }

    /**
     * list all modules
     * @return
     */
    @RequestMapping("/getAllModules")
    @ResponseBody
    public String getAllModules()
    {
        String result = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try
        {
            File file = new File("yaml");
            File[] allFiles = file.listFiles();
            List<String> list = new ArrayList<>();
            for (File f : allFiles)
            {
                String fileName = f.getName().substring(0, f.getName().lastIndexOf("."));
                list.add(fileName);
            }
            result = objectMapper.writeValueAsString(list);
        }
        catch (Exception e)
        {
            logger.error("ERROR OCCURRED DURING CONVERT YAML TO JSON,MESSAGE IS: "+e.getMessage());
        }
        return result;
    }

    /**
     * download pdf file by moduleName
     * @param response
     * @param module
     * @return
     */
    @RequestMapping("/download")
    public String download(HttpServletResponse response,String module)
    {
        String fileName = module+".pdf";
        InputStream fis = null;
        OutputStream os = null;
        BufferedInputStream bis = null;
        try
        {
            File file = new File("generated/pdf/"+fileName);
            if (file.exists())
            {
                response.setContentType("application/force-download");
                response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
                byte[] buffer = new byte[1024];
                os = response.getOutputStream();
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                int i = bis.read(buffer);
                while (i != -1)
                {
                    os.write(buffer);
                    i = bis.read(buffer);
                }

            }
        }
        catch (Exception e)
        {
            logger.error("DOWNLOAD ERROR,ERROR MESSAGE IS: ",e);
        }finally
        {
            try
            {
                if(bis!=null)
                {
                    bis.close();
                }
                if(fis!=null)
                {
                    fis.close();
                }
            }
            catch (IOException e)
            {
                logger.error(e);
            }
        }
        return null;
    }

}
