package com.ncs.vit.vap.restdocs.util;

import io.github.swagger2markup.Swagger2MarkupConverter;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class SwaggerConverter
{
    private static Logger logger = LogManager.getLogger(SwaggerConverter.class);
    public static  Asciidoctor asciidoctor;

    public static void swaggerToDocument(String swaggerInputDir ,String swaggerOutputDir,String docOutputDir,String templateDir)
    {
        //List all swagger files
        try
        {
           File[] allYamlFiles = ResourceUtils.getFile(swaggerInputDir).listFiles();
            logger.info("total yaml files count: "+allYamlFiles.length);
            if (allYamlFiles == null)
            {
                logger.info("No Swagger yaml fount in: " + swaggerInputDir);
                return;
            }

            for (File file : allYamlFiles)
            {
                String fileName = file.getName().substring(0,file.getName().lastIndexOf("."));
                logger.info(file.getName() + ": Coverting Swagger Document to AsciiDoc");
                Path swaggerOutputPath = Paths.get(swaggerOutputDir,fileName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                Swagger2MarkupConverter.from(reader).build().toFolder(swaggerOutputPath);
                generatePdf(swaggerOutputPath.toString(), fileName, docOutputDir, "pdf",templateDir);
            }
        }catch(Exception e)
        {
            logger.error("swaggerToDocument wrong:",e);
        }

    }

    /**
     * Generate PDF Doc from Ascii Doc
     *
     * @param swaggerOutputDir swagger output path
     * @param docFormat        document format: pdf/html
     * @throws IOException
     */
    private static void generatePdf(String swaggerOutputDir,
                                    String fileName,
                                    String docOutputDir,
                                    String docFormat,
                                    String templateDir)

    {
        try
        {
            File inputTemplate = ResourceUtils.getFile(templateDir+File.separator+"apidoc_pdf.adoc");
            logger.info("get template doc!!!");
            File outputTemplate = Paths.get(swaggerOutputDir,fileName+".adoc").toFile();
            FileUtils.copyFile(inputTemplate, outputTemplate);
            // Document generation
            asciidoctor = Asciidoctor.Factory.create(SwaggerConverter.class.getClassLoader());
            asciidoctor.convertFile(outputTemplate, apiDocLayout(docFormat));

            // Move generated document to destination path
            outputTemplate = Paths.get(swaggerOutputDir,fileName+"."+docFormat).toFile();
            String generatedFilename = Paths.get(docOutputDir,fileName+"."+docFormat).toString();

            File finalDocument = new File(generatedFilename);
            if (finalDocument.exists())
            {
                finalDocument.delete();
            }

            FileUtils.moveFile(outputTemplate, finalDocument);
            System.out.println(fileName + ": Done..");
        }catch(Exception e)
        {
            logger.error("ERROR OCCURRED DURING DOCUMENT CONVERT,MESSAGE IS: ",e);
        }
    }

    /**
     * API Document Layour Settings
     *
     * @param docFormat pdf/html5
     */
    private static OptionsBuilder apiDocLayout(String docFormat)
    {
        Map<String, Object> attr = new HashMap<>();
        attr.put("toc", "left");
        attr.put("toclevels", "3");
        attr.put("numbered", "");
        attr.put("sectlinks", "");
        attr.put("sectanchors", "");
        attr.put("hardbreaks", "");
        attr.put("linkattrs", true);

        OptionsBuilder optionsBuilder = OptionsBuilder.options();
        optionsBuilder.docType("book");
        optionsBuilder.attributes(attr);
        optionsBuilder.safe(SafeMode.SAFE);

        if (docFormat.contains("html"))
        {
            optionsBuilder.backend("html5");
        }
        else
        {
            optionsBuilder.backend(docFormat);
        }

        return optionsBuilder;
    }


}
