package ch.loway.oss.ari4java.generated;

// ----------------------------------------------------
//      THIS CLASS WAS GENERATED AUTOMATICALLY         
//               PLEASE DO NOT EDIT                    
//    Generated on: --------------------------------
// ----------------------------------------------------

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import ch.loway.oss.ari4java.tools.RestException;
import ch.loway.oss.ari4java.tools.AriCallback;
import ch.loway.oss.ari4java.tools.tags.*;

/**********************************************************
 * Template: Interface.ftl
 *********************************************************/


public interface ${interfaceName} {

<#list methods as met>
   /**
   * ${met.description}
   *
   ${met.paramDescriptions}
   * Available in: ${met.availableVers}
   */  
   ${met.code};

</#list>


}

