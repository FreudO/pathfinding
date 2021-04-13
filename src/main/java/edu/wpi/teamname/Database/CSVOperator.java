package edu.wpi.teamname.Database;

import edu.wpi.teamname.Algo.Node;
import javafx.util.converter.IntegerStringConverter;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVOperator {

    public static List<List<String>> readFile(String path) {
        List<List<String>> allElements = new ArrayList<>();
        List<String> elementData;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            boolean skip = true;
            while ((line = br.readLine()) != null) {
                if (skip) {
                    skip = false;
                } else {
                    String[] values = line.split(",");
                    elementData = Arrays.asList(values);
                    allElements.add(elementData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allElements;
    }

    public static void writeNodeCSV(ArrayList<Node> nodes, String nodeCSVName) {
        StringBuilder nodeBuilder = new StringBuilder();

        nodeBuilder.append("nodeId,xcoord,ycoord,level,building,nodetype,type,longname,shortname\n");

        for (Node node : nodes) {

            nodeBuilder.append(node.getNodeID() + ",");
            nodeBuilder.append(node.getX() + ",");
            nodeBuilder.append(node.getY() + ",");
            nodeBuilder.append(node.getFloor() + ",");
            nodeBuilder.append(node.getBuilding() + ",");
            nodeBuilder.append(node.getNodeType() + ",");
            nodeBuilder.append(node.getFullName() + ",");
            nodeBuilder.append(node.getShortName() + "\n");

            //for(Node node: node.g)

        }


        try {
            File node = new File(System.getProperty("user.dir") + "\\newNodes.csv");
            try (FileWriter fr = new FileWriter(System.getProperty("user.dir") + "\\" + nodeCSVName)) {
                fr.write(nodeBuilder.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void writeEdgeCSV(ArrayList<String> edges, String edgeCSVName) {
        StringBuilder edgeBuilder = new StringBuilder();
        boolean isStart = true;
        String startTemp = "beans"; //temporarily store the start edge

        edgeBuilder.append("edgeID,startNode,endNode\n");

        for (String edge : edges) {
            if (isStart) {
                startTemp = edge;
                isStart = false;
            } else {
                edgeBuilder.append(startTemp + "_" + edge + ",");
                edgeBuilder.append(startTemp + ",");
                edgeBuilder.append(edge + "\n");
                isStart = true;
            }
        }
/*
        class Edge {
            String nodeA;
            String nodeB;

            public Edge(String nodeA, String nodeB) {

                this.nodeA = nodeA;
                this.nodeB = nodeB;
                new IntegerStringConverter();
            }
        }
*/
        try {
            File edges = new File(System.getProperty("user.dir") + "\\newEdges.csv");
            try (FileWriter fr = new FileWriter(System.getProperty("user.dir") + "\\" + edgeCSVName)) {
                fr.write(edgeBuilder.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}