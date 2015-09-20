
package ch.loway.oss.ari4java.codegen2.writeClass;

import ch.loway.oss.ari4java.codegen2.learn.LearnFreemarker;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author lenz
 */
public class BasicFtl {

    Configuration cfg = null;
    
    
    public Configuration getCfg() throws Exception {
        
        if ( cfg == null ) {
        
        cfg = new Configuration(Configuration.VERSION_2_3_22);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setTemplateLoader( new ClassTemplateLoader( BasicFtl.class, "") );
        }
        
        return cfg;
    }
     
    
    public String processTemplate( String template, Map root ) throws Exception {
    
        getCfg();
        
        Template temp = cfg.getTemplate( template );
        Writer out = new StringWriter();
        
        temp.process(root, out);
        return out.toString();
    }
    
    
}


