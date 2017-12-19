
package ch.loway.oss.ari4java;

import ch.loway.oss.ari4java.generated.AriBuilder;
import ch.loway.oss.ari4java.generated.ari_0_0_1.AriBuilder_impl_ari_0_0_1;
import ch.loway.oss.ari4java.generated.ari_1_0_0.AriBuilder_impl_ari_1_0_0;
import ch.loway.oss.ari4java.generated.ari_1_10_0.AriBuilder_impl_ari_1_10_0;
import ch.loway.oss.ari4java.generated.ari_1_5_0.AriBuilder_impl_ari_1_5_0;
import ch.loway.oss.ari4java.generated.ari_1_6_0.AriBuilder_impl_ari_1_6_0;
import ch.loway.oss.ari4java.generated.ari_1_7_0.AriBuilder_impl_ari_1_7_0;
import ch.loway.oss.ari4java.generated.ari_1_8_0.AriBuilder_impl_ari_1_8_0;
import ch.loway.oss.ari4java.generated.ari_1_9_0.AriBuilder_impl_ari_1_9_0;
import ch.loway.oss.ari4java.generated.ari_2_0_0.AriBuilder_impl_ari_2_0_0;
import ch.loway.oss.ari4java.generated.ari_3_0_0.AriBuilder_impl_ari_3_0_0;

import ch.loway.oss.ari4java.tools.ARIException;

/**
 * The version of ARI to be used.
 * 
 * @author lenz
 */
public enum AriVersion {


    ARI_0_0_1 ( "0.0.1", new AriBuilder_impl_ari_0_0_1() ),   /** Asterisk 12 beta 1 */
    ARI_1_0_0 ( "1.0.0", new AriBuilder_impl_ari_1_0_0() ),   /** Asterisk 12 */
    ARI_1_5_0 ( "1.5.0", new AriBuilder_impl_ari_1_5_0() ),   /** Asterisk 13.0.0 */
    ARI_1_6_0 ( "1.6.0", new AriBuilder_impl_ari_1_6_0() ),   /** Asterisk 13.1.0 */
    ARI_1_7_0 ( "1.7.0", new AriBuilder_impl_ari_1_7_0() ),   /** Asterisk 13.2.0 */
    ARI_1_8_0 ( "1.8.0", new AriBuilder_impl_ari_1_8_0() ),   /** Asterisk 13.5.0 */
    ARI_1_9_0 ( "1.9.0", new AriBuilder_impl_ari_1_9_0() ),   /** Asterisk 13.7.0 */
    ARI_1_10_0 ( "1.10.0", new AriBuilder_impl_ari_1_10_0() ),   /** Asterisk 14.0.0 */
    ARI_2_0_0 ( "2.0.0", new AriBuilder_impl_ari_2_0_0() ),   /** Asterisk 14.2.1 */
    ARI_3_0_0 ( "3.0.0", new AriBuilder_impl_ari_3_0_0() ),   /** Asterisk 15.1.4 */
    
    
    IM_FEELING_LUCKY ( "", null );

    final AriBuilder builder;
    final String versionString;

    private AriVersion( String myVersion, AriBuilder ab ) {
        versionString = myVersion;
        builder = ab;
    }

    /**
     * You cannot get a builder for IM_FEELING_LUCKY or similar.
     * If you try to do this, it s a logical error and you get an exception.
     *
     * @return the builder object
     * @throws IllegalArgumentException
     */

    public AriBuilder builder() {
        if ( builder == null ) {
            throw new IllegalArgumentException("This version has no builder. Library error for :" + this.name());
        } else {
            return builder;
        }
    }

    /**
     * Return the correct version object from the signature string.
     * 
     * @param version
     * @return the inferred version.
     * @throws ARIException
     */

    public static AriVersion fromVersionString( String version ) throws ARIException {

        for ( AriVersion av: AriVersion.values() ) {
            if ( av.builder != null ) {
                if (av.versionString.equalsIgnoreCase(version) ) {
                    return av;
                }
            }
        }

        throw new ARIException( "Unknown ARI Version object for " + version );
    }


}

