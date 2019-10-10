import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import converter.sql2json.Content;
import converter.sql2json.Node;
import converter.sql2json.Property;
import converter.sql2json.Property_;
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DataBaseToJson {

    public static  final String PROJECT_NAME="drupalmigration";

    public static ResultSet RetrieveData() throws Exception {
        //Registering the Driver
        Class.forName("com.mysql.cj.jdbc.Driver");
        //Getting the connection
        String mysqlUrl = "jdbc:mysql://localhost:3307/wordpress";
        Connection con = DriverManager.getConnection(mysqlUrl, "root", "root");
        System.out.println("Connection established......");
        //Creating the Statement
        Statement stmt = con.createStatement();
        //Retrieving the records
        ResultSet rs = stmt.executeQuery("Select * from wp_posts");
        return rs;
    }
    public static void main(String args[]) throws Exception {
        //Creating a JSONObject object
        JSONObject jsonObject = new JSONObject();
        //Creating a json array
        JSONArray array = new JSONArray();
        ResultSet rs = RetrieveData();
        //Inserting ResutlSet data into the json object
        while(rs.next()) {
            JSONObject record = new JSONObject();
            //Inserting key-value pairs into the json object
            if(rs.getString("post_status").equals("publish")) {
                Content content = new Content();
                content.setName(rs.getString("post_name"));
                content.setPrimaryType(PROJECT_NAME + ":contentdocument");
                List mixinTypeslist = new ArrayList<String>();
                mixinTypeslist.add("mix:referenceable");
                content.setMixinTypes(mixinTypeslist);
                content.setProperties(getPropertiesList(rs));
                content.setNodes(getNodesList(rs));
                ObjectMapper mapper = new ObjectMapper();
                String value = mapper.writeValueAsString(content);

                try {
                    FileWriter file = new FileWriter("E:/bloomreach_outputs/"+rs.getString("post_title").replaceAll("\\W", "")+".json");
                    file.write(value);
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //array.add(record);
            }
        }
        jsonObject.put("Posts_Data", array);

        System.out.println("JSON file created......");
    }


    public static  List<Property> getPropertiesList(ResultSet rs) throws SQLException, ParseException {
        List<Property> propertieslist=new ArrayList<Property>();
        //hippo:related___pathreference
        Property pathRefprop=new Property();
        pathRefprop.setName("hippo:related___pathreference");
        pathRefprop.setType("STRING");
        pathRefprop.setMultiple(true);
        propertieslist.add(pathRefprop);


        //hippotranslation:locale
        Property localeProp=new Property();
        localeProp.setName("hippotranslation:locale");
        localeProp.setType("STRING");
        localeProp.setMultiple(false);
        localeProp.setValues(Arrays.asList("en"));
        propertieslist.add(localeProp);

        //publicationdate
        Property datePostedProp=new Property();
        datePostedProp.setName(PROJECT_NAME+":publicationdate");
        datePostedProp.setType("DATE");
        datePostedProp.setMultiple(false);
        //datePostedProp.setValues(Arrays.asList((new DateTime(rs.getString("post_date"))).toString()));

        datePostedProp.setValues(Arrays.asList((new DateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rs.getString("post_date")))).toString()));
        //datePostedProp.setValues(Arrays.asList( (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rs.getString("post_date"))).toString()));
        propertieslist.add(datePostedProp);

        //drupalmigration:introduction
        Property introProp=new Property();
        introProp.setName(PROJECT_NAME+":introduction");
        introProp.setType("STRING");
        introProp.setMultiple(false);
        introProp.setValues(Arrays.asList(rs.getString("post_title")));
        propertieslist.add(introProp);

        //hippo:availability
        Property availabilityProp=new Property();
        availabilityProp.setName("hippo:availability");
        availabilityProp.setType("STRING");
        availabilityProp.setMultiple(true);
        availabilityProp.setValues(Arrays.asList("live"));
        propertieslist.add(availabilityProp);

        //drupalmigration:title
        Property titleProp=new Property();
        titleProp.setName(PROJECT_NAME+":title");
        titleProp.setType("STRING");
        titleProp.setMultiple(false);
        titleProp.setValues(Arrays.asList(rs.getString("post_title")));
        propertieslist.add(titleProp);

        //jcr:path
        Property pathProp=new Property();
        pathProp.setName("jcr:path");
        pathProp.setType("STRING");
        pathProp.setMultiple(false);
        pathProp.setValues(Arrays.asList("/content/documents/"+PROJECT_NAME+"/myfolder/"+rs.getString("post_name")));
        propertieslist.add(pathProp);


        //jcr:localizedName
        Property localNameProp=new Property();
        localNameProp.setName("jcr:localizedName");
        localNameProp.setType("STRING");
        localNameProp.setMultiple(false);
        localNameProp.setValues(Arrays.asList(rs.getString("post_title")));
        propertieslist.add(localNameProp);

        return  propertieslist;
    }



    public static  List<Node> getNodesList(ResultSet rs) throws SQLException {

        List<Node> nodeslist=new ArrayList<Node>();
        Node node=new Node();
        node.setName(PROJECT_NAME+":content");
        node.setPrimaryType("hippostd:html");
        node.setMixinTypes(Arrays.asList());

            Property_  contentProp=new Property_();
            contentProp.setName("hippostd:content");
            contentProp.setType("STRING");
            contentProp.setMultiple(false);
            contentProp.setValues(Arrays.asList(rs.getString("post_content")));

        node.setProperties(Arrays.asList(contentProp));
        node.setNodes(Arrays.asList());
        nodeslist.add(node);

       return nodeslist;
    }
}