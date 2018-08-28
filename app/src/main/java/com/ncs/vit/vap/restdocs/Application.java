package com.ncs.vit.vap.restdocs;

import com.ncs.vit.vap.restdocs.util.SwaggerConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ComponentScan(basePackages = {"com.ncs.vit.vap.restdocs"})
public class Application
{
    public static void main(String[] args)
    {

       if (args==null || args.length==0){
           SpringApplication.run(Application.class);
       }else{
            boolean generatePdf = Boolean.valueOf(args[0]);
            if(generatePdf){
                String swaggerInputDir = "classpath:yaml";
                String swaggerOutputDir = "build/install/vap-restdocs-app/generated/swagger";
                String pdfDocOutputDir = "build/install/vap-restdocs-app/generated/pdf";
                String templateDir = "classpath:templates";
                SwaggerConverter.swaggerToDocument(swaggerInputDir, swaggerOutputDir, pdfDocOutputDir, templateDir);
            }
       }

    }
}
