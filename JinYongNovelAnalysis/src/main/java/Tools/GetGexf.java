package Tools;

import it.uniroma1.dis.wsngroup.gexf4j.core.*;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.Attribute;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeClass;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeList;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeType;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.GexfImpl;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.StaxGraphWriter;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.data.AttributeListImpl;
import org.junit.Test;


import java.io.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class GetGexf {
    @Test
    public void geph4j() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("last_result.txt"));
        Gexf gexf = new GexfImpl();
        Calendar date = Calendar.getInstance();

        gexf.getMetadata()
                .setLastModified(date.getTime())
                .setCreator("Gephi.org")
                .setDescription("A Web network");
        gexf.setVisualization(true);

        Graph graph = gexf.getGraph();
        graph.setDefaultEdgeType(EdgeType.UNDIRECTED).setMode(Mode.STATIC);

        AttributeList attrList = new AttributeListImpl(AttributeClass.NODE);
        graph.getAttributeLists().add(attrList);

        Attribute attUrl = attrList.createAttribute("class", AttributeType.INTEGER, "Class");
        Attribute attIndegree = attrList.createAttribute("pageranks", AttributeType.DOUBLE, "PageRank");

        String line = "";
        int index = 0;
        int edge_index = 0;
        Map<String, Node> node_map = new HashMap<>();
        while((line = br.readLine())!= null){
            int index_j = line.indexOf("#");
            int index_t = line.indexOf("\t");
            int index_dollar = line.indexOf("$");
            String label = line.substring(0,index_dollar);
            String name = line.substring(index_dollar+1,index_t);
            double PR = Double.parseDouble(line.substring(index_t+1,index_j));
            Node node = node_map.get(name);
            if (node == null){
                index++;
                node = graph.createNode(String.valueOf(index));
                node.setLabel(name)
                        .getAttributeValues()
                        .addValue(attUrl, label)
                        .addValue(attIndegree, String.valueOf(PR));
                node_map.put(name,node);
            }else{
                node.getAttributeValues().addValue(attIndegree,String.valueOf(PR));
            }
            String nameList = line.substring(index_j+1);
            for (String namePair:nameList.split(";")){
                String[] name_value_pair = namePair.split(":");
                Node name_node = node_map.get(name_value_pair[0]);
                if (name_node == null){
                    index++;
                    name_node = graph.createNode(String.valueOf(index));
                    name_node.setLabel(name_value_pair[0])
                            .getAttributeValues()
                            .addValue(attUrl, label);
                    node_map.put(name_value_pair[0],name_node);
                    edge_index++;
                    try {
                        node.connectTo(String.valueOf(edge_index), name_node).setWeight(Float.parseFloat(name_value_pair[1]));
                    }catch (Exception e){
                        System.out.println(edge_index+"----------"+label +name);
                    }
                }else{
                    edge_index++;
                    node.connectTo(String.valueOf(edge_index), name_node).setWeight(Float.parseFloat(name_value_pair[1]));
                }
            }
        }
        StaxGraphWriter graphWriter = new StaxGraphWriter();
        File f = new File("static_graph_sample.gexf");
        Writer out;
        try {
            out =  new FileWriter(f, false);
            graphWriter.writeToStream(gexf, out, "UTF-8");
            System.out.println(f.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
