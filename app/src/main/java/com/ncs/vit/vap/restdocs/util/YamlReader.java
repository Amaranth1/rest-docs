package com.ncs.vit.vap.restdocs.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

public final class YamlReader
{
    private static Logger logger = LogManager.getLogger(YamlReader.class);
    private YamlReader(){}

    /**
     * get a yaml file by filePath and convert to json
     * @param path
     * @return
     */
    public static String convert2Json(String path){
        String result = null;
        ObjectMapper objectMapper = new ObjectMapper();
        Yaml yaml = new Yaml();
        try
        {
            File file = ResourceUtils.getFile(path);
            Map map =(Map)yaml.load(new FileInputStream(file));
            result = objectMapper.writeValueAsString(map);
        }catch(Exception e){
            logger.error("ERROR OCCURRED DURING PARSING YAML,MESSAGE IS: ",e);
        }
        return result;
    }

    /**
     * convert yaml file to json directly
     * @param file
     * @return
     */
    public static String convert2Json(File file){
        String result = null;
        ObjectMapper objectMapper = new ObjectMapper();
        Yaml yaml = new Yaml();
        try
        {
            Map map =(Map)yaml.load(new FileInputStream(file));
            result = objectMapper.writeValueAsString(map);
        }catch(Exception e){
            logger.error("ERROR OCCURRED DURING PARSING YAML,MESSAGE IS: ",e);
        }
        return result;
    }

}
