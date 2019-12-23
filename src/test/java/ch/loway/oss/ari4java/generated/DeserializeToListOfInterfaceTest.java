package ch.loway.oss.ari4java.generated;

import ch.loway.oss.ari4java.tools.BaseAriAction;
import ch.loway.oss.ari4java.tools.RestException;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test how we can deserialize a Json blob to a List of Interface.
 * To make our life easier, we create a custom interface and a 
 * matching object.
 * 
 * @author lenz
 */
public class DeserializeToListOfInterfaceTest {

    public DeserializeToListOfInterfaceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }



    /**
     * Test if all the machinery is correct.
     *
     */

    @Test
    public void testDeserializeConcrete() throws RestException {
        
        List<? extends Tuple_if> l = BaseAriAction.deserializeJson( STR_TUPLE, 
                new TypeReference<List<Tuple_impl>>() {} );

        assertEquals( "Size", 2, l.size() );
        assertEquals( "El 1 A", "x1", l.get(0).getA() );
        assertEquals( "El 2 B", "y2", l.get(1).getB() );
    }

    /**
     * Now this gets us what we need.
     * 
     * @throws RestException 
     */
    
    @Test
    public void testDeserializeAbstract() throws RestException {
        
        List<Tuple_if> l = BaseAriAction.deserializeJsonAsAbstractList(STR_TUPLE, 
                new TypeReference<List<Tuple_impl>>() {}
                );
        
        assertEquals( "Size", 2, l.size() );
        assertEquals( "El 1 A", "x1", l.get(0).getA() );
        assertEquals( "El 2 B", "y2", l.get(1).getB() );
        System.out.println( l.get(0) );
    }

    
    
    
    
    public static String STR_TUPLE = "[\n" +
    "	{\"a\": \"x1\", \"b\": \"x2\" },\n" +
    "	{\"a\": \"y1\", \"b\": \"y2\" }\n" +
    "]";
    
    /**
     * My interface
     */

    private static interface Tuple_if {
        public String getA();
        public String getB();
        public void setA( String v );
        public void setB( String v );
    }

    /**
     * My physical class
     */
    
    private static class Tuple_impl implements Tuple_if {
        private String a;
        private String b;

        /**
         * @return the a
         */
        @Override
        public String getA() {
            return a;
        }

        /**
         * @param a the a to set
         */
        @Override
        public void setA(String a) {
            this.a = a;
        }

        /**
         * @return the b
         */
        @Override
        public String getB() {
            return b;
        }

        /**
         * @param b the b to set
         */
        @Override
        public void setB(String b) {
            this.b = b;
        }
        
    }

}
