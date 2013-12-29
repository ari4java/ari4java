/*
 * 
 */

package ch.loway.oss.ari4java.sandbox;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * These are a couple of tests written to learn how to use Jackson.
 *
 * @author lenz
 */
public class deserializeJsonTest {

    public deserializeJsonTest() {
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


    public static String JSON_XXX = "";



    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //

    /**
     * Lessons learned:
     * - use public methods
     *
     * @throws IOException
     */

    @Test
    public void testJacksonSimple() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        MyValue mx = (MyValue) mapper.readValue("{\"name\":\"Bob\", \"age\":13}", MyValue.class);

        Assert.assertEquals( "name", "Bob", mx.name );
        Assert.assertEquals( "age", 13, mx.age );

    }

    /**
     * Lessons learned:
     * - Used TypeReference
     * 
     * @throws IOException
     */

    @Test
    public void testJacksonListOfStuff() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        List<MyValue> lVals = mapper.readValue("[ {\"name\":\"Leo\", \"age\":5}, {\"name\":\"Leda Sofia\", \"age\":3} ]",
                new TypeReference<List<MyValue>>() {});

        Assert.assertEquals( "N items", 2, lVals.size() );

        Assert.assertEquals( "name 1", "Leo", lVals.get(0).name );
        Assert.assertEquals( "age 1", 5, lVals.get(0).age );

        Assert.assertEquals( "name 2", "Leda Sofia", lVals.get(1).name );
        Assert.assertEquals( "age 2", 3, lVals.get(1).age );
    }


    /**
     * Lessons learned:
     * - for setters in inner onjects, you set the congrete class
     *   but have a getter for the interface. Our clients will always use
     *   the custom interface.
     * 
     * @throws IOException
     */

    @Test
    public void createHierarchy() throws IOException {
        
        ObjectMapper mapper = new ObjectMapper();

        String json = " { \"name\": \"Leo\", "
                    + "   \"ageObj\": "
                    + "     {  \"age\": 5 } "
                    + " } ";

        InterfaceA obj = mapper.readValue( json, ImplementationA.class );

        Assert.assertEquals( "name", "Leo", obj.getName() );
        Assert.assertEquals( "age", 5, obj.getAgeObj().getAge() );

    }

    /**
     * Simple object
     *
     */

    public static class MyValue {
        public String name= "";
        public int    age = 0;
    }


    /**
     * Multiple objects with multiple interfaces.
     * 
     */

    public static interface InterfaceA {
        public String getName();
        public InterfaceB getAgeObj();
    }

    public static interface InterfaceB {
        public int getAge();
    }

    public static class ImplementationA implements InterfaceA {

        private String name = "";
        private ImplementationB obB = null;

        public InterfaceB getAgeObj() {
            return obB;
        }

        public void setAgeObj( ImplementationB b ) {
            obB = (ImplementationB) b;
        }

        public String getName() {
            return name;
        }

        public void setName( String n ) {
            name = n;
        }

    }

    public static class ImplementationB implements InterfaceB {

        private int age;

        public int getAge() {
            return age;
        }

        public void setAge( int a) {
            age = a;
        }
        
    }


}